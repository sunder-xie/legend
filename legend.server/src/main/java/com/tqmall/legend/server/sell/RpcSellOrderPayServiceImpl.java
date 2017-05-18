package com.tqmall.legend.server.sell;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.sell.SellOrderPayService;
import com.tqmall.legend.biz.sell.SellOrderService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.entity.sell.SellOrderPay;
import com.tqmall.legend.entity.sell.SellOrderPayLog;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.sell.SellOrderHandleStatusEnum;
import com.tqmall.legend.enums.sell.SellOrderPayStatusEnum;
import com.tqmall.legend.facade.sell.SellOrderPayFaced;
import com.tqmall.legend.facade.sell.SellOrderPayTslService;
import com.tqmall.legend.object.param.sell.RpcSellNoticeParam;
import com.tqmall.legend.service.sell.RpcSellOrderPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by xiangdong.qu on 17/2/23 09:42.
 */
@Service("rpcSellOrderPayService")
@Slf4j
public class RpcSellOrderPayServiceImpl implements RpcSellOrderPayService {

    @Autowired
    private SellOrderService sellOrderService;

    @Autowired
    private SellOrderPayService sellOrderPayService;

    @Autowired
    private SellOrderPayTslService sellOrderPayTslService;

    @Autowired
    private SellOrderPayFaced sellOrderPayFaced;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private ShopService shopService;

    /**
     * finance 支付成功通知
     *
     * @param rpcSellNoticeParam
     *
     * @return
     */
    @Override
    public Result<Boolean> sellOrderPaySuccessNotice(RpcSellNoticeParam rpcSellNoticeParam) {
        log.info("[云修系统在线销售支付] 账务系统支付结果回调.rpcSellNoticeParam:{}", JSONUtil.object2Json(rpcSellNoticeParam));
        if (null == rpcSellNoticeParam) {
            return Result.wrapErrorResult("", "参数为空");
        } else if (rpcSellNoticeParam.getPayAmount() == null || rpcSellNoticeParam.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.error("[云修系统在线销售支付] 账务系统支付结果回调,支付金额错误. payAmount:{}", rpcSellNoticeParam.getPayAmount());
            return Result.wrapErrorResult("", "支付金额错误");
        } else if (rpcSellNoticeParam.getPayDate() == null) {
            log.error("[云修系统在线销售支付] 账务系统支付结果回调,支付日期为空. payDate:{}", rpcSellNoticeParam.getPayDate());
            return Result.wrapErrorResult("", "支付日期错误");
        } else if (rpcSellNoticeParam.getPayId() == null || rpcSellNoticeParam.getPayId() < 1) {
            log.error("[云修系统在线销售支付] 账务系统支付结果回调,支付Id错误. payId:{}", rpcSellNoticeParam.getPayId());
            return Result.wrapErrorResult("", "支付Id错误");
        } else if (StringUtils.isBlank(rpcSellNoticeParam.getPayNo())) {
            log.error("[云修系统在线销售支付] 账务系统支付结果回调,支付流水号错误. payNo:{}", rpcSellNoticeParam.getPayNo());
            return Result.wrapErrorResult("", "支付流水号错误");
        } else if (StringUtils.isBlank(rpcSellNoticeParam.getSellOrderSn())) {
            log.error("[云修系统在线销售支付] 账务系统支付结果回调,订单商品号为空. sellOrderSn:{}", rpcSellNoticeParam.getSellOrderSn());
            return Result.wrapErrorResult("", "订单商品号为空");
        }
        try {
            //获取商品订单
            String sellOrderSn = rpcSellNoticeParam.getSellOrderSn();
            SellOrder sellOrder = sellOrderService.getSellOrderBySn(sellOrderSn);

            BigDecimal payAmount = rpcSellNoticeParam.getPayAmount();
            if (payAmount.compareTo(sellOrder.getSellAmount()) != 0) {
                log.error("[云修系统在线销售支付] 账务系统支付结果回调,支付金额与售卖金额不符.sellOrderSn:{},payAmount:{},sellAmount:{}",
                        rpcSellNoticeParam.getSellOrderSn(), payAmount, sellOrder.getSellAmount());
                return Result.wrapErrorResult("", "支付金额与售卖金额不符");
            }
            //获取支付单信息
            SellOrderPay sellOrderPay = sellOrderPayService.getSellOrderPayBySellOrderId(sellOrder.getId());

            //商品订单数据
            SellOrder sellOrderTemp = new SellOrder();
            sellOrderTemp.setId(sellOrder.getId());
            sellOrderTemp.setPayStatus(SellOrderPayStatusEnum.PAY_STATUS_SUCCESS.getPayStatus());

            //支付记录数据
            SellOrderPay sellOrderPayTemp = new SellOrderPay();
            sellOrderPayTemp.setId(sellOrderPay.getId());
            sellOrderPayTemp.setPayAmount(rpcSellNoticeParam.getPayAmount());
            sellOrderPayTemp.setPayNo(rpcSellNoticeParam.getPayNo());
            sellOrderPayTemp.setPayResult(SellOrderPayStatusEnum.PAY_STATUS_SUCCESS.getPayStatus());
            sellOrderPayTemp.setPayTime(rpcSellNoticeParam.getPayDate());
            sellOrderPayTemp.setPayId(Long.valueOf(rpcSellNoticeParam.getPayId()));

            //支付状态变更记录
            SellOrderPayLog sellOrderPayLog = new SellOrderPayLog();
            sellOrderPayLog.setPayResult(SellOrderPayStatusEnum.PAY_STATUS_SUCCESS.getPayStatus());
            sellOrderPayLog.setPayTime(rpcSellNoticeParam.getPayDate());
            sellOrderPayLog.setPayNo(rpcSellNoticeParam.getPayNo());
            sellOrderPayLog.setPayAmount(rpcSellNoticeParam.getPayAmount());
            sellOrderPayLog.setSellOrderPayId(sellOrderPay.getId());
            sellOrderPayLog.setPayId(Long.valueOf(rpcSellNoticeParam.getPayId()));
            sellOrderPayLog.setMsgSource(1);
            sellOrderPayLog.setPayOrderSn(sellOrderPay.getPayOrderSn());
            try {
                log.info("[云修系统售卖在线支付] 更新支付状态信息. sellOrderTemp:{},sellOrderPayTemp:{},sellOrderPayLog:{}",
                        sellOrderTemp, sellOrderPayTemp, sellOrderPayLog);
                sellOrderPayTslService.updateSellOrderPayStatus(sellOrderTemp, sellOrderPayTemp, sellOrderPayLog);
            } catch (RuntimeException e) {
                log.error("[云修系统售卖在线支付] 更新支付状态失败. sellOrderSn:" + sellOrderSn, e);
                return Result.wrapErrorResult("", "支付状态更新失败");
            }
            //开通门店
            if (sellOrder.getHandleStatus() == null ||
                    !sellOrder.getHandleStatus().equals(SellOrderHandleStatusEnum.HANDLE_STATUS_SUCCESS.getHandleStatus())) {
                //是否正在开通门店
                //获取开通门店锁
                if (jedisClient.acquireLock("SELL_ORDER_LOCK:" + sellOrder.getBuyMobile(), 300)) {
                    openShop(sellOrder);
                    jedisClient.delete("SELL_ORDER_LOCK:" + sellOrder.getBuyMobile());
                } else {
                    log.info("[云修系统售卖在线支付] 当前电话号码正在开通门店. mobile:{}", sellOrder.getBuyMobile());
                }
            }
        } catch (IllegalArgumentException ill) {
            return Result.wrapErrorResult("", ill.getMessage());
        } catch (BizException biz) {
            return Result.wrapErrorResult("", biz.getMessage());
        } catch (Exception e) {
            return Result.wrapErrorResult("", "支付状态更新失败");
        }
        return Result.wrapSuccessfulResult(Boolean.TRUE);
    }

    /**
     * 开通门店
     *
     * @param sellOrder
     */
    private void openShop(SellOrder sellOrder) {
        //开通门店
        SellOrder sellOrderTempOne = new SellOrder();
        sellOrderTempOne.setId(sellOrder.getId());
        try {
            Long userGlobalId = sellOrderPayFaced.createShop(sellOrder.getId());
            sellOrderTempOne.setUserGlobalId(userGlobalId);
            sellOrderTempOne.setHandleStatus(SellOrderHandleStatusEnum.HANDLE_STATUS_SUCCESS.getHandleStatus());
            try {
                Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
                if (null != shop) {
                    sellOrderTempOne.setShopOpenTime(shop.getGmtCreate());
                    sellOrderTempOne.setShopEndTime(shop.getExpireTime());
                }
            } catch (Exception e) {
                log.error("[云修系统售卖在线支付] 回写sellOrder店铺开通时间失败. sellOrderId:" + sellOrder.getId(), e);
            }
        } catch (IllegalArgumentException ill) {
            log.error("[云修系统售卖在线支付] 门店开通失败. sellOrderId:{},msg:{}", sellOrder.getId(), ill.getMessage());
            sellOrderTempOne.setHandleStatus(SellOrderHandleStatusEnum.HANDLE_STATUS_FALSE.getHandleStatus());
        } catch (BizException biz) {
            log.error("[云修系统售卖在线支付] 门店开通失败. sellOrderId:{},,msg:{}", sellOrder.getId(), biz.getMessage());
            sellOrderTempOne.setHandleStatus(SellOrderHandleStatusEnum.HANDLE_STATUS_FALSE.getHandleStatus());
        } catch (Exception e) {
            log.error("[云修系统售卖在线支付] 门店开通失败. sellOrderId:" + sellOrder.getId(), e);
            sellOrderTempOne.setHandleStatus(SellOrderHandleStatusEnum.HANDLE_STATUS_FALSE.getHandleStatus());
        }
        sellOrderService.updateSellOrderById(sellOrderTempOne);
    }
}
