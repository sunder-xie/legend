package com.tqmall.legend.biz.account.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by majian on 16/6/22.
 */
@Data
public class RechargeSummaryVo {
    private BigDecimal summaryAmount;
    private Integer summaryCount;
}
