package com.tqmall.legend.entity.account;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by majian on 16/9/7.
 */
@Data
public class MemberCardUsedSummay {
    Integer usedCount;//使用会员卡消费次数
    BigDecimal usedAmount;//使用会员卡消费金额
}
