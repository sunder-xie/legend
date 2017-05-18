package com.tqmall.legend.facade.account.vo;

import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.CouponInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wanghui on 6/6/16.
 */
@Data
public class DiscountCouponVo implements Comparable<DiscountCouponVo>{
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 优惠券信息
     */
    private AccountCoupon coupon;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 是否使用
     */
    private boolean used;
    /**
     * 优惠券类型
     */
    private CouponInfo couponInfo;
    /**
     * 抵扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 备注信息
     */
    private String message;

    public Long getCouponInfoId() {
        if (couponInfo != null) {
            return couponInfo.getId();
        } else {
            return null;
        }
    }

    public Integer getCouponType() {
        if (couponInfo != null) {
            return couponInfo.getCouponType();
        } else {
            return null;
        }
    }
    
    @Override
    public int compareTo(DiscountCouponVo o) {
        if (o == null) {
            return 1;
        } else {
            if (getCoupon().getExpireDate() == null && o.getCoupon().getExpireDate() == null) {
                return 0;
            } else if(getCoupon().getExpireDate() == null) {
                return 1;
            } else if(o.getCoupon().getExpireDate() == null) {
                return -1;
            } else {
                return getCoupon().getExpireDate().compareTo(o.getCoupon().getExpireDate());
            }
        }
    }

}
