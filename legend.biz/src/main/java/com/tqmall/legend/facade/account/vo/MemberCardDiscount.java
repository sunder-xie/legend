package com.tqmall.legend.facade.account.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 会员卡优惠
 */
@Getter
@Setter
public class MemberCardDiscount {
    private Long id;//会员卡id
    private String typeName;//会员卡类型
    private String cardNumber;//会员卡号
    private BigDecimal balance;//卡内金额
    private BigDecimal depositAmount;//累计充值
    private BigDecimal cardPoints;//积分
}