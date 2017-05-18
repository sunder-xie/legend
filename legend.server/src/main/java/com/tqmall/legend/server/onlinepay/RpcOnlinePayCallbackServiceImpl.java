package com.tqmall.legend.server.onlinepay;

import com.google.common.collect.Maps;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.marketing.MarketingShopRelService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.shop.SmsPayLogDao;
import com.tqmall.legend.entity.marketing.MarketingShopRel;
import com.tqmall.legend.entity.shop.SmsPayLog;
import com.tqmall.legend.enums.onlinepay.BusTypeEnum;
import com.tqmall.legend.facade.wechat.WechatFacade;
import com.tqmall.legend.log.MarketSmsLog;
import com.tqmall.legend.object.param.onlinepay.CallbackParam;
import com.tqmall.legend.service.onlinepay.RpcOnlinePayCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wanghui on 3/16/16.
 */
@Slf4j
@Service("rpcOnlinePayCallbackService")
public class RpcOnlinePayCallbackServiceImpl implements RpcOnlinePayCallbackService {

    @Autowired
    private SmsPayLogDao smsPayLogDao;
    @Autowired
    private MarketingShopRelService marketingShopRelService;
    @Autowired
    private WechatFacade wechatFacade;

    private static final String lockKey = "smsLockWithShopId";
    private static final long timeout = 30000l;

    @Override
    public Result<String> callback(CallbackParam param) {
        if (log.isInfoEnabled()) {
            log.info("支付接口回调信息:{}", LogUtils.objectToString(param));
        }

        /**
         * 获取之前的订单信息
         */
        if (param != null) {
            if (param.getOrderSn() != null) {
                Long shopId = Long.parseLong(param.getOrderSn().split("-")[1]);
                HashMap<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("shopId", shopId);
                paramMap.put("orderSn", param.getOrderSn());
                List<SmsPayLog> smsPayLogs = smsPayLogDao.select(paramMap);
                if (smsPayLogs.size() == 1) {
                    SmsPayLog smsPayLog = smsPayLogs.get(0);
                    if (smsPayLog.getStatus() != 0) {
                        if (Integer.valueOf(1).equals(smsPayLog.getStatus())) {
                            if (log.isInfoEnabled()) {
                                log.info("订单已支付成功,本次[{}]属于二次回调,结果[{}],不做任何处理.", param.getOrderSn(), param.getPaySuccess());
                            }
                            return Result.wrapSuccessfulResult("订单已经支付成功,本次属于二次回调,不做任何处理.");
                        } else {
                            if (log.isInfoEnabled()) {
                                log.info("订单已经支付失败,本次[{}]属于二次回调,结果[{}].不做任何处理", param.getOrderSn(), param.getPaySuccess());
                            }
                            return Result.wrapSuccessfulResult("订单已经支付失败,本次属于二次回调,不做任何处理.");
                        }
                    }
                    SmsPayLog payLog = new SmsPayLog();
                    payLog.setId(smsPayLog.getId());
                    payLog.setBackTime(new Date());
                    payLog.setPaySn(param.getPayNo());
                    if (StringUtils.isNoneBlank(param.getRemark())) {
                        payLog.setRemark(smsPayLog.getRemark() != null?smsPayLog.getRemark():"" + ",支付回调备注信息:" + param.getRemark());
                    }
                    if (param.getPaySuccess()) {//支付成功的情况
                        payLog.setStatus(1);
                        /**
                         * 支付成功的情况下需要将短信充值进去
                         */
                        paramMap.clear();
                        paramMap.put("shopId", shopId);
                        /**
                         * 使用redis实现分布式锁
                         */
                        Jedis jedis = JedisPoolUtils.getMasterJedis();
                        while(true) {
                            /**
                             * 对锁进行setnx
                             */
                            String key = lockKey + shopId;
                            Long setnx = jedis.setnx(key, Long.toString(System.currentTimeMillis()));
                            if(setnx==1l) {
                                /**
                                 * 成功,没有上锁,继续执行
                                 */
                                rechargeSms(shopId, paramMap, smsPayLog);
                                jedis.del(key);
                                break;
                            } else {
                                /**
                                 * 失败,判断锁是否超时
                                 */
                                String value = jedis.get(key);
                                if(StringUtil.isNotStringEmpty(value)){
                                    Long valuel = Long.valueOf(value);
                                    long timeOutTime = System.currentTimeMillis() - valuel;
                                    if(timeOutTime > timeout){
                                        String value2 = jedis.getSet(key, Long.toString(System.currentTimeMillis()));
                                        if(value.equals(value2)){
                                            /**
                                             * 去除超时死锁,继续执行计划
                                             */
                                            rechargeSms(shopId, paramMap, smsPayLog);
                                            jedis.del(key);
                                            break;
                                        }
                                    }
                                }
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                log.error("等待锁时出错",e);
                            }
                        }
                    } else {
                        payLog.setStatus(2);
                        log.error("门店充值短信失败.{}", LogUtils.objectToString(payLog));
                    }
                    this.smsPayLogDao.updateById(payLog);

                } else {
                    log.error("存在多张错误支付单,请检查.{}", smsPayLogs);
                }

            } else {
                log.error("从finance返回原始订单号为空.");
            }
        } else {
            log.error("从finance回调支付信息失败,返回对象为空.");
        }

        return Result.wrapSuccessfulResult("回调成功");
    }

    private void rechargeSms(Long shopId, HashMap<String, Object> paramMap, SmsPayLog smsPayLog) {
        MarketingShopRel marketingShopRel = marketingShopRelService.selectOne(paramMap);
        Integer smsNum = smsPayLog.getSmsNum();
        smsNum = smsNum == null ? 0 : smsNum;
        if (marketingShopRel == null) {
            if (log.isInfoEnabled()) {
                log.info("由于之前门店未做过充值,新增门店短信记录.");
            }
            MarketingShopRel msr = new MarketingShopRel();
            msr.setShopId(shopId);
            msr.setSmsNum(smsNum.longValue());
            this.marketingShopRelService.insert(msr);
        } else {
            MarketingShopRel msr = new MarketingShopRel();
            msr.setId(marketingShopRel.getId());
            msr.setSmsNum(marketingShopRel.getSmsNum() + smsNum);
            this.marketingShopRelService.updateById(msr);
        }

        // 记录log
        log.info(MarketSmsLog.rechargeSmsLog(shopId, smsNum));

        if (log.isInfoEnabled()) {
            log.info("成功充值{}条短信,shopId:{}", smsNum, shopId);
        }
    }

    @Override
    public Result<String> callbackCommon(CallbackParam param) {
        if(param==null){
            log.error("[dubbo]从finance回调支付信息失败,返回对象为空.");
            return Result.wrapErrorResult("","回调入参为空");
        }
        if (param.getBusType() == null) {
            log.error("支付结果回调,busType为空,入参{}", LogUtils.objectToString(param));
            return Result.wrapErrorResult("", "业务类型busType为空");
        }
        int busType = param.getBusType();
        if (BusTypeEnum.SMS.getBusType().intValue() == busType) {
            return callback(param);
        } else if (BusTypeEnum.WECHAT.getBusType().intValue() == busType) {
            return wechatFacade.callBackPayFlow(param);
        } else {
            log.error("支付结果回调,未定义的busType,入参{}", LogUtils.objectToString(param));
            return Result.wrapErrorResult("", "未定义的busType");
        }
    }
}
