package com.tqmall.legend.biz.order.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收款单实体
 */
@Data
public class SpeedilyBillBo extends ConfirmBillBo {

    // 收款方式[[
    // 会员余额支付
    private BigDecimal memberBalancePay;
    // 用于支付的会员卡id
    private Long memberIdForSettle;

    // 收款方式
    private List<PayChannelBo> payChannelBoList;

    // ]]
}
