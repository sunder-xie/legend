package com.tqmall.legend.facade.report.vo;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/9/22.
 */
public class MonthCouponVO {
    private String couponName;
    private BigDecimal discountAmount;
    private Integer handOutNum;
    private Integer usedNum;

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

    public Integer getHandOutNum() {
        return handOutNum;
    }

    public void setHandOutNum(Integer handOutNum) {
        this.handOutNum = handOutNum;
    }

    public Integer getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Integer usedNum) {
        this.usedNum = usedNum;
    }
}
