package com.tqmall.legend.pojo.warehouseOut;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by litan on 15-2-10.
 */
@Data
public class WarehouseOutDetailVO {

    /**
     * 某条物料出库成本单价
     */
    private BigDecimal inventoryPrice;

    /**
     * 某条物料出库成本总价
     */
    private BigDecimal inventoryAmount;

    /**
     * 某条物料出库售卖单价
     */
    private BigDecimal salePrice;

    /**
     * 某条物料出库售卖总价
     */
    private BigDecimal saleAmount;

    /**
     * 某条物料出库数量
     */
    private BigDecimal goodsCount;
}
