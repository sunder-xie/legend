package com.tqmall.legend.facade.warehouse.bo;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class WarehouseInDetailBo extends BaseEntity {
    private Long shopId;
    private Long supplierId;
    private Long goodsId;
    private BigDecimal goodsCount;
    private BigDecimal purchasePrice;
}
