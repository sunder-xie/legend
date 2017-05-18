package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
@Data
public class PaidOutStatisticsVo {

    /**
     * 实付金额
     */
    private BigDecimal totalPaidAmount;
    /**
     * 实付金额组成部分
     */
    private List<BusinessPaidOutAmountVo> businessPaidOutAmountVoList;

}
