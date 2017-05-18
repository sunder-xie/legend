package com.tqmall.legend.biz.settlement.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xin on 16/6/8.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderDebitVo implements Serializable {
    private static final long serialVersionUID = 2753068776966623633L;

    private Long orderId;

    private Long billId;

    // 会员卡支付金额
    private MemberCardVo memberCard;

    // 支付流水金额
    private List<DebitBillFlowVo> flowList;

    private String remark;
}
