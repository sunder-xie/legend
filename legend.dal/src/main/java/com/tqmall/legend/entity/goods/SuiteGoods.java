package com.tqmall.legend.entity.goods;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class SuiteGoods extends BaseEntity {

    private String goodsSn;
    private String tqmallGoodsSn;
    private String name;
    private String measureUnit;
    private String origin;
    private String format;
    private Long catId;
    private Long brandId;
    private BigDecimal price;
    private BigDecimal stock;
    private Integer tqmallStatus;
    private Long shopId;
    private Integer goodsStatus;
    private BigDecimal inventoryPrice;
    private String imgUrl;
    private BigDecimal goodsSoldPrice;
    private Long goodsNum;
    private BigDecimal goodsAmount;

}
