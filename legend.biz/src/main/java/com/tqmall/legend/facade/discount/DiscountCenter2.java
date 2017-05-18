package com.tqmall.legend.facade.discount;

import com.tqmall.legend.facade.discount.bo.DiscountInfoBo;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;

import java.math.BigDecimal;

/**
 * @Author 辉辉大侠
 * @Date:9:47 AM 02/03/2017
 */
public interface DiscountCenter2 {
    /**
     * 计算工单折扣信息
     *
     * @param shopId
     * @param orderId
     * @param selectedItem
     * @return
     */
    DiscountInfoBo discountOrder(Long shopId, Long orderId, DiscountSelectedBo selectedItem);

    /**
     * 洗车单折扣
     *
     * @param shopId
     * @param carLicense
     * @param carWashServiceId
     * @param amount
     * @param selectedItem
     * @return
     */
    DiscountInfoBo discountCarWashOrder(Long shopId, String carLicense, Long carWashServiceId, BigDecimal amount, DiscountSelectedBo selectedItem);
}