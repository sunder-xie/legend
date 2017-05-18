package com.tqmall.legend.facade.settlement.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xin on 16/6/18.
 */
@Data
public class DebitBillFlowBo {
    private Long paymentId;
    private String paymentName;
    private BigDecimal payAmount;
    private String payAccount;
}
