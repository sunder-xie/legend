package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 营业月报营业金额汇总
 * Created by 辉辉大侠 on 9/2/16.
 */
@Data
public class BusinessOverviewVo {
    // 排名
    private int ranking;

    // 营业额
    private BigDecimal sumOfBusiness;

    // 单车产值 = 营业额 / 确认账单数
    private BigDecimal outputValueByCar;

    // 采购金额
    private BigDecimal warehouseInTotalAmount;

    // 毛利
    private BigDecimal grossAmount;
}
