package com.tqmall.legend.biz.shop;

/**
 * Created by twg on 16/10/26.
 */
public interface ShopPaymentService {

    /**
     * 检测是否已申请开通支付方式
     * @param shopId 门店id
     * @param applyType
     * @return boolen
     */
    Boolean hasApplyPayment(Long shopId, Integer applyType);
}
