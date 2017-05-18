package com.tqmall.legend.facade.setting;

import com.tqmall.legend.entity.settlement.Payment;

import java.util.List;

/**
 * Created by zsy on 16/11/7.
 */
public interface PaymentFacade {
    /**
     * 根据shopId查询门店已有的支付方式（含公共的支付方式）
     *
     * @param shopId
     * @return
     */
    List<Payment> getPaymentByShopId(Long shopId);

    /**
     * 根据id获取支付方式
     * @param id
     * @return
     */
    Payment getCachePaymentById(Long id);

    /**
     * 更新支付方式
     * @param payment
     */
    void updatePaymentById(Payment payment);
}
