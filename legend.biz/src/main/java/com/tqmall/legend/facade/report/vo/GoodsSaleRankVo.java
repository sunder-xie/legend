package com.tqmall.legend.facade.report.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by majian on 16/9/21.
 * 配件销售排行
 */
public class GoodsSaleRankVo {
    private Integer rank = 0;           //排名
    private Long goodsId ;           //配件id
    private String goodsName;       //配件名称
    private BigDecimal saleCount = BigDecimal.ZERO;      //销量
    private String unit;            //单位
    private BigDecimal avgCost = BigDecimal.ZERO;     //平均成本
    private BigDecimal cost = BigDecimal.ZERO;        //成本
    private BigDecimal income = BigDecimal.ZERO;      //销售额
    private BigDecimal profit = BigDecimal.ZERO;      //利润
    private BigDecimal profitRate = BigDecimal.ZERO;  //利率

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(BigDecimal saleCount) {
        this.saleCount = saleCount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getAvgCost() {
        if (BigDecimal.ZERO.compareTo(saleCount) == 0) {
            avgCost = BigDecimal.ZERO;
        } else {
            avgCost = cost.divide(saleCount, 2, BigDecimal.ROUND_HALF_UP);
        }
        return avgCost.setScale(2);
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
        return profit.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public BigDecimal getProfitRate() {
        if (BigDecimal.ZERO.compareTo(income) == 0) {
            profitRate = BigDecimal.ZERO;
        } else {
            profitRate = getProfit().divide(income,4,BigDecimal.ROUND_HALF_UP);
        }
        profitRate = profitRate.multiply(BigDecimal.valueOf(100));
        return profitRate.setScale(2);
    }

}
