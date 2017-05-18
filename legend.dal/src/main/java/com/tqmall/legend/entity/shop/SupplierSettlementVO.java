package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by guangxue on 15/1/30.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SupplierSettlementVO extends Supplier {
    private String supplierId;
    private BigDecimal totalAmount;
    private BigDecimal amountPayable;
    private BigDecimal amountPaid;
    private BigDecimal taxAmount; //税费
    private BigDecimal freightAmount; //运费
    private BigDecimal goodsAmount; //配件总价

}
