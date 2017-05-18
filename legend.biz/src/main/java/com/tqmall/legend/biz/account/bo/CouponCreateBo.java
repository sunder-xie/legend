package com.tqmall.legend.biz.account.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by majian on 16/9/27.
 */
public class CouponCreateBo {
    private Long id;
    private Integer couponType;//优惠劵类型,0:折扣卷;1:现金券;2:通用券
    private String couponName;//券名称
    private BigDecimal discount = BigDecimal.ONE;//折扣
    private BigDecimal discountAmount;//抵扣金额
    private BigDecimal limitAmount = BigDecimal.ZERO;//限制金额
    private Integer useRange = 0;//使用范围:0:全场通用;1.全部服务;2.指定服务项目'
    private List<Long> serviceIds;//指定服务的id,useRange=2时使用
    private boolean periodCustomized;//是否自定义有效期
    private Integer effectivePeriodDays = 0;//有效期天数
    private Date expiredDate;//失效日期
    private Date effectiveDate;//生效日期
    private boolean compatibleWithCard = false;
    private boolean compatibleWithOtherAccount = true;
    private boolean compatibleWithOtherCoupon = true;
    private boolean singleUse = false;
    private String remark;
    private Long shopId;
    private Long userId;
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCompatibleWithCard() {
        return compatibleWithCard;
    }

    public void setCompatibleWithCard(boolean compatibleWithCard) {
        this.compatibleWithCard = compatibleWithCard;
    }

    public boolean isCompatibleWithOtherAccount() {
        return compatibleWithOtherAccount;
    }

    public void setCompatibleWithOtherAccount(boolean compatibleWithOtherAccount) {
        this.compatibleWithOtherAccount = compatibleWithOtherAccount;
    }

    public boolean isCompatibleWithOtherCoupon() {
        return compatibleWithOtherCoupon;
    }

    public void setCompatibleWithOtherCoupon(boolean compatibleWithOtherCoupon) {
        this.compatibleWithOtherCoupon = compatibleWithOtherCoupon;
    }

    public boolean isSingleUse() {
        return singleUse;
    }

    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }

    public Boolean isPeriodCustomized() {
        return periodCustomized;
    }

    public void setPeriodCustomized(boolean periodCustomized) {
        this.periodCustomized = periodCustomized;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
