package com.tqmall.legend.facade.report.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/9/22.
 */
@Getter
@Setter
public class MonthMemberVO {
    private String cardName;//会员卡名

    private Integer handOutNum;//发放数量

    private BigDecimal payAmount;//实收金额

    private BigDecimal amount;//实冲金额

    // 会员卡退卡金额
    private BigDecimal retreatCardAmount;

    // 会员卡退卡数量
    private Integer retreatCardNums;
}
