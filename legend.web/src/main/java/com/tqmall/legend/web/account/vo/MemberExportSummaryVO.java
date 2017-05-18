package com.tqmall.legend.web.account.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by majian on 17/1/5.
 */
public class MemberExportSummaryVO {
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
        return balanceSummary.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBalanceSummary(BigDecimal balanceSummary) {
        this.balanceSummary = balanceSummary;
    }

    public BigDecimal getDepositSummary() {
        return depositSummary.setScale(2, RoundingMode.HALF_UP);
    }

    public void setDepositSummary(BigDecimal depositSummary) {
        this.depositSummary = depositSummary;
    }
}
