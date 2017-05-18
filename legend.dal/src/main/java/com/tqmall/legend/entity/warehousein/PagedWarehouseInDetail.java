package com.tqmall.legend.entity.warehousein;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by guangxue on 15/1/14.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PagedWarehouseInDetail extends BaseEntity {
    private Long warehouseId;
    private Long shopId;
    private Long supplierId;
    private String supplierName;
    private String warehouseInSn;
    private Date gmtCreate;
    private String gmtCreateStr;
    private String isDeleted;

    private Long detailId;
    private String goodsSn;
    private Long goodsId;
    private String goodsName;
    private String goodsFormat;
    private BigDecimal goodsCount;
    private BigDecimal purchasePrice;
    private BigDecimal purchaseAmount;
    private BigDecimal goodsRealCount;
    private String measureUnit;
    private String relSn;
    private String status;
    private String carInfo;
}
