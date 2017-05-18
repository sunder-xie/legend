package com.tqmall.legend.facade.report.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author 辉辉大侠
 * @Date 2017-04-13 6:14 PM
 * @Motto 一生伏首拜阳明
 */
@Data
public class GrossProfitsSummaryBo {
    /**
     * 收入合计
     */
    private BigDecimal totalAmount;
    /**
     * 配件总成本
     */
    private BigDecimal totalInventoryAmount;
    /**
     * 工单总数
     */
    private Integer totalOrderCount;
    /**
     * 毛利
     */
    private BigDecimal grossProfits;
}
