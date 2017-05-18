package com.tqmall.yunxiu.web.pub;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.facade.marketing.gather.GatherPlanFacade;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCouponConfigVo;
import com.tqmall.legend.facade.marketing.gather.vo.ReceiveCouponResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * 集客-老客户拉新送券H5后端接口
 * Created by wushuai on 16/12/19.
 */
@Slf4j
@Controller
@RequestMapping("/pub/gather-coupon")
public class PubGatherCouponController {
    @Autowired
    private SmsService smsService;
    @Autowired
    private GatherPlanFacade gatherPlanFacade;


    /**
     * 获取送券信息
     * @param gatherCouponConfigId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCouponInfo")
    public Result<GatherCouponConfigVo> getGatherCouponConfig(@RequestParam(value = "gatherCouponConfigId", required = true) final Long gatherCouponConfigId) {
        return new ApiTemplate<GatherCouponConfigVo>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(gatherCouponConfigId,"参数错误");
            }
            @Override
            protected GatherCouponConfigVo process() throws BizException {
                logger.info("[集客老客户带新]获取优惠券发放配置信息,gatherCouponConfigId:{}", gatherCouponConfigId);
                gatherPlanFacade.accessDeal(gatherCouponConfigId);
                GatherCouponConfigVo gatherCouponConfigVo = gatherPlanFacade.getGatherCouponInfo(gatherCouponConfigId);
                return gatherCouponConfigVo;
            }
        }.execute();
    }

    /**
     * 发送手机验证码
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "send-code", method = RequestMethod.POST)
    public Result<Boolean> sendCode(@RequestParam(value = "mobile") final String mobile,
                                    @RequestParam(value = "gatherCouponConfigId") final Long gatherCouponConfigId) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(gatherCouponConfigId,"优惠券信息为空");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("手机号码有误");
                }
            }
            @Override
            protected Boolean process() throws BizException {
                //.获取优惠券信息
                GatherCouponConfigVo gatherCouponConfigVo = gatherPlanFacade.getGatherCouponInfo(gatherCouponConfigId);
                if(gatherCouponConfigVo==null){
                    throw new BizException("优惠券不存在");
                }
                String shopName = gatherCouponConfigVo.getShopName();//对应的门店名称
                //.生成验证码
                String code = smsService.generateCode();
                Map<String,Object> smsMap = new HashMap<>();
                smsMap.put("code",code);
                smsMap.put("shopName",shopName);
                SmsBase smsBase = new SmsBase(mobile, Constants.LEGEND_COUPON_SMS_TPL, smsMap);
                logger.info("[集客-老客户带新]车主领券发送短信验证码,gatherCouponConfigId:{},mobile:{},code:{}", gatherCouponConfigId, mobile, code);
                boolean success =smsService.sendMsg(smsBase, "[集客-老客户带新]车主领券发送验证码");
                if (!success) {
                    throw new BizException("发送验证码短信失败");
                }
                //将验证码放到redis
                cacheSmsCode(code, mobile);
                return success;
            }
        }.execute();
    }


    /**
     * 领取优惠券
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "receive-coupon", method = RequestMethod.POST)
    public Result<ReceiveCouponResultVo> receiveCoupon(@RequestParam(value = "mobile") final String mobile,
                                                       @RequestParam(value = "smsCode") final String smsCode,
                                                       @RequestParam(value = "gatherCouponConfigId") final Long gatherCouponConfigId) {
        return new ApiTemplate<ReceiveCouponResultVo>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(gatherCouponConfigId,"优惠券信息为空");
                if (!StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("手机号码有误");
                }
                Assert.notNull(smsCode, "验证码不能为空");
            }
            @Override
            protected ReceiveCouponResultVo process() throws BizException {
                //.短信验证码校验
                checkSmsCode(mobile, smsCode);
                ReceiveCouponResultVo receiveCouponResultVo = gatherPlanFacade.grantCoupon(mobile,gatherCouponConfigId);
                logger.info("[集客-老客户带新]车主领取优惠券:mobile:{},gatherCouponConfigId:{},receiveCouponResultVo:{}", mobile, gatherCouponConfigId, LogUtils.objectToString(receiveCouponResultVo));
                return receiveCouponResultVo;
            }
        }.execute();
    }

    /**
     * 校验短信验证码
     * @param mobile
     * @param smsCode
     */
    private void checkSmsCode(String mobile, String smsCode) {
        Jedis slaveJedis = null;
        Jedis masterJedis = null;
        try {
            slaveJedis = JedisPoolUtils.getSlaveJedis();
            String key = Constants.MARKETING_GATHER_COUPON_MOBILE_CODE + mobile;
            String cacheSmsCode = slaveJedis.get(key);
            log.info("[集客-老客户带新]短信验证码校验,从redis获取验证码,key:{},code:{}", key, cacheSmsCode);
            if (!smsCode.equals(cacheSmsCode)) {
                throw new BizException("验证码错误");
            }
            //删除验证码
            masterJedis = JedisPoolUtils.getMasterJedis();
            log.info("[集客-老客户带新]短信验证码校验,删除已经校验的验证码,key:{}", key);
            masterJedis.del(key);
        } finally {
            if (slaveJedis != null) {
                JedisPoolUtils.returnSlaveRes(slaveJedis);
            }
            if (masterJedis != null) {
                JedisPoolUtils.returnMasterRes(masterJedis);
            }
        }
    }

    /**
     * 缓存短信验证码
     * @param code
     * @param mobile
     */
    private void cacheSmsCode(String code, String mobile) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            String key = Constants.MARKETING_GATHER_COUPON_MOBILE_CODE + mobile;
            jedis.set(key, code);
            jedis.expire(key, CacheConstants.MOBILE_CODE_KEY_EXP_TIME);//10分钟后失效
        } catch (Exception e) {
            log.error("[集客-老客户带新]车主领券短信验证码存redis异常", e);
            throw e;
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnMasterRes(jedis);
            }
        }
    }



}
