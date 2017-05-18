package com.tqmall.legend.entity.warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 盘点配件列表
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class InventoryGoodsBo implements Serializable {

    private static final long serialVersionUID = 5930960632062590827L;


    // 主键ID
    private Long stockId;
    // 配件编号
    private Long goodsId;
    // 实盘库存
    private BigDecimal realStock;
    // 盘点前成本价
    private BigDecimal inventoryPrePrice;
    // 盘点后成本价
    private BigDecimal inventoryPrice;
    // 原因
    private String reason;
}
