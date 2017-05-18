package com.tqmall.legend.entity.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工单费用实体
 * <p/>
 * Created by dongc on 15/10/9.
 */
@Data
public class OrderExpenseEntity implements Serializable {

    private static final long serialVersionUID = 3141123399087322314L;

    // 物料总金额
    private BigDecimal goodsAmount;
    // 服务总金额
    private BigDecimal serviceAmount;
    // 工单总金额
    private BigDecimal totalAmount;
    // 工单总折扣
    private BigDecimal discount;
    // 物料折扣
    private BigDecimal goodsDiscount;
    // 服务折扣
    private BigDecimal serviceDiscount;
    // 费用总额
    private BigDecimal feeAmount;
    // 费用折扣
    private BigDecimal feeDiscount;
    // 工单应付金额
    private BigDecimal orderAmount;
    //管理费
    private BigDecimal manageFee;
}
