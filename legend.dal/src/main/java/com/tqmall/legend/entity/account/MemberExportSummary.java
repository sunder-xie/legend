package com.tqmall.legend.entity.account;

import java.math.BigDecimal;

/**
 * Created by majian on 17/1/6.
 */
public class MemberExportSummary {
    private Integer numberSummary;//有效会员卡张数
    private BigDecimal balanceSummary;//余额总计
    private BigDecimal depositSummary;//充值金额总计

    public Integer getNumberSummary() {
        return numberSummary;
    }

    public void setNumberSummary(Integer numberSummary) {
        this.numberSummary = numberSummary;
    }

    public BigDecimal getBalanceSummary() {
        return balanceSummary;
    }

    public void setBalanceSummary(BigDecimal balanceSummary) {
        this.balanceSummary = balanceSummary;
    }

    public BigDecimal getDepositSummary() {
        return depositSummary;
    }

    public void setDepositSummary(BigDecimal depositSummary) {
        this.depositSummary = depositSummary;
    }
}
