package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class DiscountInfoDTO implements Serializable{
    /**
     * 折扣总金额
     */
    private BigDecimal totalDiscountAmount;

    /**
     * 用于账户和关联账户会员卡折扣信息
     */
    private List<DiscountCardDTO> discountCardList;

    /**
     * 他人账户会员卡折扣信息
     */
    private List<DiscountCardDTO> guestDiscountCardList;

    /**
     * 本人及绑定账户下的计次卡折扣信息
     */
    private List<DiscountComboDTO> discountComboList;
    /**
     * 使用他人账户下的计次卡
     */
    private List<DiscountComboDTO> guestDiscountComboList;
    /**
     * 优惠券折扣信息(本人及绑定账户下)
     */
    private List<DiscountCouponDTO> discountCouponList;
    /**
     * 使用他人账户下的优惠券
     */
    private List<DiscountCouponDTO> guestDiscountCouponList;
}
