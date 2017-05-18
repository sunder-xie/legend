package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/11/8.
 */
@Data
public class GoodsCateTotalVO {
    // 期初金额总计
    private BigDecimal totalBeginGoodsAmount = BigDecimal.ZERO;
    // 期末金额总计
    private BigDecimal totalEndGoodsAmount = BigDecimal.ZERO;
    // 借进金额总计
    private BigDecimal totalBorrowGoodsAmount = BigDecimal.ZERO;
    // 贷出金额总计
    private BigDecimal totalLendGoodsAmount = BigDecimal.ZERO;
    // 周转率
    private String rotationRate = "0%";
}
