package com.tqmall.legend.object.param.settlement;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收款渠道 实体
 */
@Data
public class PayChannel implements Serializable {
    private static final long serialVersionUID = -2359786064430994672L;

    // 渠道ID
    private Long channelId;
    // 渠道名称
    private String channelName;
    // 支付金额
    private BigDecimal payAmount;
}
