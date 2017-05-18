package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 辉辉大侠 on 9/6/16.
 */
@Data
public class DatePointVo {
    /**
     * 日期
     */
    private String date;
    /**
     * 金额
     */
    private BigDecimal amount;
}
