package com.tqmall.legend.facade.account.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wanghui on 6/12/16.
 */
@Data
public class MemberGrantVo {
    /**
     * 会员卡号
     */
    private String cardNumber;
    /**
     * 会员卡类型id
     */
    private Long memberCardInfoId;
    /**
     * 服务顾问id
     */
    private Long receiver;
    /**
     * 服务顾问姓名
     */
    private String receiverName;
    /**
     * 账户id
     */
    private Long accountId;
    /**
     * 支付方式id
     */
    private Long paymentId;
    /**
     * 支付方式名称
     */
    private String paymentName;
    /**
     * 支付金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
}
