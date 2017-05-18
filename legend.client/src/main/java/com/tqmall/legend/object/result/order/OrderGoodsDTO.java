package com.tqmall.legend.object.result.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dingbao on 16/3/16.
 */
@Data
@ToString
@EqualsAndHashCode
public class OrderGoodsDTO implements Serializable {
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

    // 页面比较
    int goodsNumberInt;
    int stockInt;
    private Long saleId;
}
