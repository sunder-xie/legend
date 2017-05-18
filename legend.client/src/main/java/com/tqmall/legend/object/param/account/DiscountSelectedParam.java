package com.tqmall.legend.object.param.account;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class DiscountSelectedParam implements Serializable{
    /**
     * 使用他人优惠信息时需要输入他人手机号
     */
    private String guestMobile;
    /**
     * 本次结算所使用的会员卡实例信息
     */
    private DiscountSelectedCardParam selectedCard;
    /**
     * 已选中优惠券列表
     */
    private List<DiscountSelectedCouponParam> selectedCouponList;
    /**
     * 已选中计次卡列表
     */
    private List<DiscountSelectedComboParam> selectedComboList;

}
