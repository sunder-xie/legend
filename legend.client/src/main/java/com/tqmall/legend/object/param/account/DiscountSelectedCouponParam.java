package com.tqmall.legend.object.param.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class DiscountSelectedCouponParam implements Serializable{
    /**
     * 优惠券id
     */
    private Long accountCouponId;
    /**
     * 优惠券金额
     */
    private BigDecimal discountAmount;
}
