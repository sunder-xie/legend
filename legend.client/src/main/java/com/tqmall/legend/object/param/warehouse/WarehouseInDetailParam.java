package com.tqmall.legend.object.param.warehouse;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by sven on 16/11/22.
 */
@Getter
@Setter
public class WarehouseInDetailParam implements Serializable{
    private static final long serialVersionUID = 4956053087461696274L;
    private Long shopId; //门店id
    private Long goodsId;//商品id
    private BigDecimal purchasePrice; //入库单价
    private BigDecimal goodsCount;//入库数量
    private Long supplierId;//供应商id
    private Long userId; //用户id
}
