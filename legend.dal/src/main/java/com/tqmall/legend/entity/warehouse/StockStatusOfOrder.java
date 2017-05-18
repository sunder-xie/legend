package com.tqmall.legend.entity.warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 工单物料出库状态
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class StockStatusOfOrder implements Serializable {

    private static final long serialVersionUID = 2432994388397937848L;

    // 工单ID
    private Long orderId;
    // 物料出库状态
    private int goodsOutFlag;
}
