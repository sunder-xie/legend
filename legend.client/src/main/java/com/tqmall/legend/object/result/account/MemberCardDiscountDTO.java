package com.tqmall.legend.object.result.account;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xin on 2017/3/7.
 */
@Getter
@Setter
public class MemberCardDiscountDTO implements Serializable {
    private Long id;//会员卡id
    private String typeName;//会员卡类型
    private String cardNumber;//会员卡号
    private BigDecimal balance;//卡内金额
    private BigDecimal depositAmount;//累计充值
    private BigDecimal cardPoints;//积分
}
