package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by majian on 16/9/6.
 */
@Data
public class MemberCardFlowDTO implements Serializable {

    private Integer consumeType;//消费类型 1.充值 2.充值撤销 3.消费 4.消费撤销
    private String orderSn;//工单编号
    private BigDecimal consumeAmount = BigDecimal.ZERO;//消费金额
    private String payment;//支付方式
    private BigDecimal rechargeAmount = BigDecimal.ZERO;//充值金额
    private Date gmtCreate;//时间

    public enum ConsumeType {
        RECHARGE ,//充值
        RECHARGE_REVERSE,//充值撤销
        CONSUME,//消费
        CONSUME_REVERSE//消费撤销
    }
}
