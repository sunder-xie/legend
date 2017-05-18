package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lixiao on 15-2-10.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class RealWarehouseGoodsVo {

    private BigDecimal totalGoodsAmount;
    private BigDecimal totalWarehouseOutAmount;
    private BigDecimal totalWarehouseInventoryAmount;

    private List<OrderGoods> orderGoodsList;

    private boolean code = true;


}
