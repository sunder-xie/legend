package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
@Data
public class PaidInStatisticsVo {
    /**
     * 实收金额
     */
    private BigDecimal totalPaidAmount;

    /**
     *  会员卡收款金额
     */
    private BigDecimal memberCardPayAmount;

    /**
     * 不同类型的业务实收
     */
    private List<BusinessPaidInAmountVo> businessPaidInAmountVoList;

}
