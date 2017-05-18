package com.tqmall.legend.facade.discount.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SelectedCardBo {
    /**
     * 本次结算使用的会员卡id
     */
    private Long cardId;
    /**
     * 会员卡折扣抵扣金额（操作人员可将系统计算值自行修改）
     */
    private BigDecimal cardDiscountAmount;
}
