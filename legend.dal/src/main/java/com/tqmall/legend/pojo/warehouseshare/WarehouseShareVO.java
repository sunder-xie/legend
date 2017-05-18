package com.tqmall.legend.pojo.warehouseshare;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/11/11.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class WarehouseShareVO {
    private Long id;
    private Long goodsId;//关联库存表物料id
    private String goodsName;//商品名称
    private BigDecimal saleNumber;//销售数量
    private String measureUnit;//最小单位
    private BigDecimal goodsPrice;//销售价格
    private Long shopId;
    private String cityName;
}
