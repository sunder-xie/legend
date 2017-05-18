package com.tqmall.legend.facade.report.vo;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/9/22.
 */
public class MonthComboStripVO {
    private BigDecimal reciveAmount;//收款金额
    private BigDecimal discountAmount;//抵扣金额

    public BigDecimal getReciveAmount() {
        return reciveAmount;
    }

    public void setReciveAmount(BigDecimal reciveAmount) {
        this.reciveAmount = reciveAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
}
