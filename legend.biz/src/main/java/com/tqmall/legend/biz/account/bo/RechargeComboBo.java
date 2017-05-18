package com.tqmall.legend.biz.account.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by majian on 16/6/2.
 */
@Data
public class RechargeComboBo {
    private Long accountId;//账户id
    private Long comboInfoId;//计次卡类型id
    private Long recieverId;//服务顾问id
    private String recieverName;//服务顾问名称
    private BigDecimal amount = BigDecimal.ZERO;//应收金额
    private BigDecimal payAmount = BigDecimal.ZERO;//实收金额
    private Long paymentId;//付款方式id
    private String paymentName;//付款方式

}
