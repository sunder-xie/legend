package com.tqmall.legend.facade.warehouse.vo;

import java.math.BigDecimal;

/**
 * 超过2个月未周转库存展示对象
 * Created by tanghao on 16/11/10.
 */
public class Over2MonthTurnoverVO {

    private Integer goodsKindsNum;

    private BigDecimal inventoryPrice = BigDecimal.ZERO;

    private BigDecimal lossAmount = BigDecimal.ZERO;


    public Integer getGoodsKindsNum() {
        return goodsKindsNum;
    }

    public void setGoodsKindsNum(Integer goodsKindsNum) {
        this.goodsKindsNum = goodsKindsNum;
    }

    public BigDecimal getInventoryPrice() {
        return inventoryPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setInventoryPrice(BigDecimal inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }

    public BigDecimal getLossAmount() {
        return lossAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setLossAmount(BigDecimal lossAmount) {
        this.lossAmount = lossAmount;
    }
}
