package com.tqmall.legend.entity.inventory;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class InventoryStock extends BaseEntity {

    private Long shopId;
    private Long recordId;
    private String recordSn;
    private Long goodsId;
    private String goodsName;
    private String goodsFormat;
    private String goodsSn;
    private String measureUnit;
    private BigDecimal currentStock;
    private BigDecimal realStock;
    private BigDecimal diffStock;
    private String reason;
    private BigDecimal inventoryPrice;
    // 配件结存前成本价
    private BigDecimal inventoryPrePrice;

    private String catName;
    private String depot;
    private BigDecimal realInventoryAmount;
    private BigDecimal currentInventoryAmount;
    private BigDecimal diffInventoryAmount;
    private String lastInTimeStr;

}

