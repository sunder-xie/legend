package com.tqmall.legend.entity.warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 计算盘点记录盈亏总金额
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CalculateInventoryAmountBo implements Serializable {

    private static final long serialVersionUID = 349465813887708277L;

    // 盘点记录ID
    private Long recordId;

    // 盘亏总数
    private BigDecimal kuiTotal;
    // 盘亏总金额
    private BigDecimal kuiTotalAmount;
    // 盘盈总数
    private BigDecimal yinTotal;
    // 盘盈总金额
    private BigDecimal yinTotalAmount;


}
