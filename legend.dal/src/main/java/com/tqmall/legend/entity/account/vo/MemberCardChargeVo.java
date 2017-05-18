package com.tqmall.legend.entity.account.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by twg on 16/6/18.
 */
@Data
public class MemberCardChargeVo {
    private Long cardId;
    private BigDecimal amount;//会员卡充值金额
    private BigDecimal payAmount;//会员卡实付金额
    private Long paymentId;//支付方式
    private String paymentName;//支付方式名
    private String remark;//备注
}
