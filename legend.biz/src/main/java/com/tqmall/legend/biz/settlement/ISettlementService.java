package com.tqmall.legend.biz.settlement;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;

import java.util.List;

/**
 * 结算接口
 *
 * @see com.tqmall.legend.biz.settlement.impl.SettlementServiceImpl
 */
public interface ISettlementService {

    /**
     * 校验淘汽优惠券
     * @param orderInfo
     * @param taoqiCouponSn
     * @return
     */
    Result checkTaoqiCoupon(OrderInfo orderInfo, String taoqiCouponSn);

    /**
     * 核销淘汽优惠券
     * @param taoqiCouponSn
     * @param orderInfo
     * @param itemIds
     * @return
     */
    Result taoqiCouponPush2CAPP(String taoqiCouponSn, OrderInfo orderInfo, List<String> itemIds);
}
