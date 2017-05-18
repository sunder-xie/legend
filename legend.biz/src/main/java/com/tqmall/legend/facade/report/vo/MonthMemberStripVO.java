package com.tqmall.legend.facade.report.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/9/22.
 */
@Getter
@Setter
public class MonthMemberStripVO {
    private BigDecimal rechargeAmount;//充值实冲金额
    private BigDecimal payAmount;//充值支付金额
    private BigDecimal cardPayAmount;//会员卡支付金额
    private BigDecimal discountAmount;//会员卡特权抵扣金额
    private BigDecimal retreatCardAmount;// 会员卡退卡金额
}
