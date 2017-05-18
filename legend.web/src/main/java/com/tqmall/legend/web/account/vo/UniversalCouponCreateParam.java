package com.tqmall.legend.web.account.vo;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/27.
 */
public class UniversalCouponCreateParam {
    private Long id;
    private String couponName;//券名称
    private BigDecimal discountAmount;//抵扣金额
    private Integer effectivePeriodDays;//有效期天数
    private boolean compatibleWithCard;//能否与优惠券共同使用
//    private Boolean compatibleWithOtherAccount;
//    private Boolean compatibleWithOtherCoupon;
    private boolean singleUse;//单次消费只能使用一张
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getEffectivePeriodDays() {
        return effectivePeriodDays;
    }

    public void setEffectivePeriodDays(Integer effectivePeriodDays) {
        this.effectivePeriodDays = effectivePeriodDays;
    }

    public boolean isCompatibleWithCard() {
        return compatibleWithCard;
    }

    public void setCompatibleWithCard(boolean compatibleWithCard) {
        this.compatibleWithCard = compatibleWithCard;
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
