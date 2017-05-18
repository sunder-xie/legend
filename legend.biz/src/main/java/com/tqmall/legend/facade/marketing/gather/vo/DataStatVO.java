package com.tqmall.legend.facade.marketing.gather.vo;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/12/24.
 */
public class DataStatVO {
    private BigDecimal value;
    private BigDecimal percentage;

    public DataStatVO() {
        this(BigDecimal.ZERO, BigDecimal.ONE);
    }

    public DataStatVO(BigDecimal value, BigDecimal percentage) {
        this.value = value;
        this.percentage = percentage;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
