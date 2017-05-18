package com.tqmall.legend.object.param.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xin on 2017/3/7.
 */
@Data
public class DiscountSelectedCardParam implements Serializable {
    /**
     * 本次结算使用的会员卡id
     */
    private Long cardId;
    /**
     * 会员卡折扣抵扣金额（操作人员可将系统计算值自行修改）
     */
    private BigDecimal cardDiscountAmount;
}
