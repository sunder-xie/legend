package com.tqmall.legend.object.result.warehouseshare;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xin on 2016/11/23.
 */
@Data
public class WarehouseShareDTO implements Serializable {
    private Long id;
    private Long shopId;
    private Long goodsId;
    private String goodsName;//配件名称
    private String shopName;//门店名称
    private BigDecimal goodsStock;//库存
    private String measureUnit;//最小单位
    private BigDecimal inventoryPrice;//结存价格
    private BigDecimal saleNumber;//销售数量
    private BigDecimal goodsPrice;//销售价格
    private Integer goodsStatus;//0：待出售，1：出售中，9：审核未通过
    private String goodsRemark;//审核情况
}
