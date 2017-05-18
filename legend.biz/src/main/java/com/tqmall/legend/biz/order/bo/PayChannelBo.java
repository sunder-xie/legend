package com.tqmall.legend.biz.order.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收款渠道实体
 */
@Data
public class PayChannelBo implements Serializable {

    private static final long serialVersionUID = 7683171170685511109L;

    // 渠道ID
    private Long channelId;
    // 渠道名称
    private String channelName;
    // 支付金额
    private BigDecimal payAmount;
    // 备注
    private String remark;
}
