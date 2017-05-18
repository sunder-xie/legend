package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/9/6.
 */
@Data
public class SeriesRankVO {
    private Integer rank = Integer.valueOf(0);
    private String carSeries = "";
    private String carBrand = "";
    private Integer receptionNumber = Integer.valueOf(0);
    private BigDecimal income;
    private BigDecimal cost;
    private BigDecimal profit;
    private BigDecimal profitRate;

}
