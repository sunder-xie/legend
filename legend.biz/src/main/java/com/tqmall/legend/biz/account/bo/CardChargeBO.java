package com.tqmall.legend.biz.account.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 辉辉大侠 on 9/26/16.
 */
@Data
public class CardChargeBO {
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 充值账户id
     */
    private Long accountId;
    /**
     * 会员卡充值金额
     */
    private BigDecimal amount;
    /**
     * 会员卡实付金额
     */
    private BigDecimal payAmount;
    /**
     * 支付方式id
     */
    private Long paymentId;
    /**
     * 支付方式名称
     */
    private String paymentName;
    /**
     * 支付备注
     */
    private String remark;

}
