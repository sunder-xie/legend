package com.tqmall.legend.entity.buy;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by twg on 15/7/21.
 */
@Data
public class LegendOrderGoodsParam {
    private Integer goodsId;        // 商品编号

    private Integer goodsNumber;    // 购买数量

    private BigDecimal price;       // 购买价格
}
