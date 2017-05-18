package com.tqmall.legend.facade.warehouse.vo;

import com.tqmall.legend.entity.shop.SupplierSettlementVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sven on 16/11/24.
 */
@Data
public class SupplierStatistics extends SupplierSettlementVO {
    private List<SupplierSettlementVO> supplierSettlementVOList;
    private BigDecimal amountPayableStatistics;//未付金额统计
    private BigDecimal amountPaidStatistics;//实付金额统计
    private BigDecimal goodsAmountStatistics; //配件金额统计
    private BigDecimal totalAmountStatistics;//应付金额统计
}
