package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
@Data
public class BusinessPaidInAmountVo {
    /**
     * 业务类型id
     */
    private Integer bussinessTagId;
    /**
     * 业务类型名称
     */
    private String businessTagName;
    /**
     * 实收金额
     */
    private BigDecimal paidAmount;
}
