package com.tqmall.legend.entity.warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 配件淘汽采购价
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GoodsTqmallPriceBo implements Serializable {


    private static final long serialVersionUID = -3656655301746014981L;

    // 配件ID
    private Long goodsId;
    // 淘汽配件ID
    private Long tqmallGoodsId;
    // 配件采购价
    private String tqmallPrice;
    // 前三个月月均销量
    private BigDecimal averageNumber;
    // 建议采购数量
    private BigDecimal suggestGoodsNumber;

}
