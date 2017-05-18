package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 辉辉大侠 on 9/6/16.
 */
@Data
public class BusinessTrendVo {
    /**
     * 趋势图总金额
     */
    private BigDecimal amount;
    /**
     *日期点
     */
    List<DatePointVo> datePointVoList;
}
