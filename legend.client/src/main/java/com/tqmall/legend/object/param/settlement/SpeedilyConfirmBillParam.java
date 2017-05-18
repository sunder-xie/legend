package com.tqmall.legend.object.param.settlement;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 快修快保首次账单确认AND收款页面 参数实体
 */
@Data
public class SpeedilyConfirmBillParam extends ConfirmBillParam {

    private static final long serialVersionUID = -7136264462326730730L;

    // 收款方式[[
    // 会员余额支付
    private BigDecimal memberBalancePay;
    // 用于支付的会员卡id
    private Long memberIdForSettle;
    // 收款方式
    private List<PayChannel> payChannelList;
    // ]]

    @Override
    public String toString() {
        return "SpeedilyConfirmBillParam{" +
                "memberBalancePay=" + memberBalancePay +
                ", payChannelList=" + payChannelList +
                "} " + super.toString();
    }
}
