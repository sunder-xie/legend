package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderGoods extends BaseEntity {

    private Long shopId;
    private Long orderId;
    private Long goodsId;
    private BigDecimal goodsNumber;
    private BigDecimal goodsPrice;
    private BigDecimal goodsAmount;
    private BigDecimal soldPrice;
    private String orderSn;
    private BigDecimal discount;
    private String goodsSn;
    private String goodsName;
    private String goodsFormat;
    private BigDecimal inventoryPrice;
    private BigDecimal soldAmount;
    private String measureUnit;
    private Long relOrderServiceId;
    private String goodsNote;
    private Long goodsType;

    private BigDecimal outNumber;
    //剩余待出库的数量（总量-已出库数量）
    private BigDecimal remainingNumber;
    private String imgUrl;
    private BigDecimal stock;
    private BigDecimal currentInventoryPrice;
    private String depot;

    private BigDecimal warehouseOutAmount;
    private BigDecimal warehouseInventoryAmount;
    private BigDecimal warehouseOutPrice;
    private String carInfoStr;
    // 页面比较
    int goodsNumberInt;
    int stockInt;

    // 销售员id
    private Long saleId;

    //扩展字段，销售员姓名
    private String saleName;

    // 工单创建时间
    private String orderCreateTimeStr;

    public int getGoodsNumberInt() {
        return goodsNumber != null ? goodsNumber.intValue() : 0;
    }

    public void setGoodsNumberInt(int goodsNumberInt) {
        this.goodsNumberInt = goodsNumberInt;
    }

    public int getStockInt() {
        return stock != null ? stock.intValue() : 0;
    }

    public void setStockInt(int stockInt) {
        this.stockInt = stockInt;
    }

}

