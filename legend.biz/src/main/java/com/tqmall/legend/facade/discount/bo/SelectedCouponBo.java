package com.tqmall.legend.facade.discount.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SelectedCouponBo {
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 优惠券抵扣的金额(操作员可手工修改)
     */
    private BigDecimal couponDiscountAmount;
}
