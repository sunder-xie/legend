package com.tqmall.legend.object.param.order.speedily;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 2017/2/9.
 */
@Getter
@Setter
public class GoodsParam implements Serializable {
    private static final long serialVersionUID = -7494201983672089920L;

    private Long goodsId;
    private BigDecimal discount;
    private Long goodsNumber;
    private BigDecimal goodsAmount;
    private String goodsName;
    private String goodsFormat;
    private Long saleId;
    private BigDecimal goodsPrice;
    private BigDecimal soldPrice;
    private BigDecimal soldAmount;
    private Long goodsType;
    private String measureUnit;
    private String goodsSn;
}
