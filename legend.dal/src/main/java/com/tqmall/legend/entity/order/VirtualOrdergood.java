package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class VirtualOrdergood extends BaseEntity {

    private Long shopId;
    private Long orderId;
    private Long goodsId;
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
    private BigDecimal goodsNumber;
    private Long saleId;

}

