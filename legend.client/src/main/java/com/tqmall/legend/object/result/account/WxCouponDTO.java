package com.tqmall.legend.object.result.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 微信优惠券信息
 * Created by 辉辉大侠 on 9/12/16.
 */
public class WxCouponDTO implements Serializable {
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 优惠券券码
     */
    private String couponSn;
    /**
     * 生效时间
     */
    private Date effectiveDate;
    /**
     * 失效时间
     */
    private Date expireDate;

    /**
     * 是否使用过
     */
    private boolean used;

    /**
     * 优惠券类型id
     */
    private Long couponTypeId;

    /**
     * 是否通用券
     */
    private boolean universal;

    /**
     * 金额限制
     */
    private BigDecimal limitAmount;

    /**
     * 抵扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 适用范围
     */
    private String useRange;
    /**
     * 适用范围详细描述(若优惠券设置了指定服务可用列出相应服务名称,形如:仅限洗车,大保健可用)
     */
    private String useRangeDetail;

    public String getUseRangeDetail() {
        return useRangeDetail;
    }

    public void setUseRangeDetail(String useRangeDetail) {
        this.useRangeDetail = useRangeDetail;
    }

    /**
     * 备注信息
     */
    private String remark;
    /**
     * 一张工单只允许使用一张优惠券.0:不允许;1:允许
     */
    private Integer singleUse;
    /**
     * 允许与会员卡共同使用.0:不允许;1:允许
     */
    private Integer compatibleWithCard;

    private List<String> licenseList;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Long getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(Long couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public boolean isUniversal() {
        return universal;
    }

    public void setUniversal(boolean universal) {
        this.universal = universal;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getUseRange() {
        return useRange;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSingleUse() {
        return singleUse;
    }

    public void setSingleUse(Integer singleUse) {
        this.singleUse = singleUse;
    }

    public Integer getCompatibleWithCard() {
        return compatibleWithCard;
    }

    public void setCompatibleWithCard(Integer compatibleWithCard) {
        this.compatibleWithCard = compatibleWithCard;
    }

    public List<String> getLicenseList() {
        return licenseList;
    }

    public void setLicenseList(List<String> licenseList) {
        this.licenseList = licenseList;
    }

    public void setUseRange(Integer code) {
        this.useRange = UseRangeEnum.valueOf(code).getMsg();
    }

    enum UseRangeEnum {
        UNIVERSAL(0, "全场通用"),
        SERVICE_LABOR(1,"只限服务工时"),
        SERVICE_ITEM_DISCOUNT(2,"只限指定服务项目打折");

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        private int code;
        private String msg;

        UseRangeEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static UseRangeEnum valueOf(int code) {
            for (UseRangeEnum useRangeEnum : values()) {
                if (useRangeEnum.code == code) {
                    return useRangeEnum;
                }
            }
            throw new IllegalArgumentException("no such enum");
        }

    }
}
