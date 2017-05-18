package com.tqmall.legend.facade.report.vo;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/21.
 */
public class GoodsCatSaleTopVo {
    private String catName; //类名
    private BigDecimal saleCount; //销量
    private BigDecimal saleAmount;//销售额

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

    public BigDecimal getSaleAmount() {
        return saleAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }
}
