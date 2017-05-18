package com.tqmall.legend.web.account.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by majian on 16/9/27.
 */
public class CashCouponCreateParam {
    private Long id;
    private String couponName;//券名称
    private BigDecimal discountAmount;//抵扣金额
    private BigDecimal limitAmount = BigDecimal.ZERO;//限制金额
    private Integer useRange;//使用范围:0:全场通用;1.全部服务;2.指定服务项目'
    private List<Long> serviceIds;//指定服务的id,useRange=2时使用
    private boolean periodCustomized = false;//是否自定义有效期
    private Integer effectivePeriodDays = 0;//有效期天数
    private String effectiveDate;//生效日期
    private String expiredDate;//失效日期
    private boolean compatibleWithCard = false;
//    private boolean compatibleWithOtherAccount;
//    private boolean compatibleWithOtherCoupon;
    private boolean singleUse = false;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPeriodCustomized() {
        return periodCustomized;
    }

    public void setPeriodCustomized(boolean periodCustomized) {
        this.periodCustomized = periodCustomized;
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

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Integer getUseRange() {
        return useRange;
    }

    public void setUseRange(Integer useRange) {
        this.useRange = useRange;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public Integer getEffectivePeriodDays() {
        return effectivePeriodDays;
    }

    public void setEffectivePeriodDays(Integer effectivePeriodDays) {
        this.effectivePeriodDays = effectivePeriodDays;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
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
