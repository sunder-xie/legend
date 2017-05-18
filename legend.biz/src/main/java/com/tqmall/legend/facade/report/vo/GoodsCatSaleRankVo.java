package com.tqmall.legend.facade.report.vo;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/21.
 */
public class GoodsCatSaleRankVo {
    private Integer rank = 0; //排名
    private Long catId; //配件类型id
    private String catName; //配件类名
    private BigDecimal saleCount = BigDecimal.ZERO; //销量
    private BigDecimal cost = BigDecimal.ZERO; //成本
    private BigDecimal income = BigDecimal.ZERO; //销售额
    private BigDecimal profit = BigDecimal.ZERO; //利润
    private BigDecimal profitRate = BigDecimal.ZERO; //利率

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public BigDecimal getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(BigDecimal saleCount) {
        this.saleCount = saleCount;
    }

    public BigDecimal getCost() {
        return cost.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getIncome() {
        return income.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getProfit() {
         profit = income.add(cost.negate());
        return profit.setScale(2,BigDecimal.ROUND_HALF_UP);
    }


    public BigDecimal getProfitRate() {
        if (BigDecimal.ZERO.compareTo(income) == 0) {
            profitRate = BigDecimal.ZERO;
        } else {
            profitRate = getProfit().divide(income,4,BigDecimal.ROUND_HALF_UP);
        }
        profitRate = profitRate.multiply(BigDecimal.valueOf(100));
        return profitRate.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

}
