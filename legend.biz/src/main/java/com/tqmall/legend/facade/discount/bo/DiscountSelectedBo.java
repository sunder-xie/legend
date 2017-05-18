package com.tqmall.legend.facade.discount.bo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @Author 辉辉大侠
 * @Date:10:32 AM 02/03/2017
 */
@Data
public class DiscountSelectedBo {

    public DiscountSelectedBo() {
        selectedComboList = Lists.newArrayList();
        selectedCouponList = Lists.newArrayList();
    }

    /**
     * 使用他人优惠信息时需要输入他人手机号
     */
    private String guestMobile;
    /**
     * 本次结算所使用的会员卡实例信息
     */
    private SelectedCardBo selectedCard;
    /**
     * 本次结算所使用的计次卡服务信息
     */
    private List<SelectedComboBo> selectedComboList;
    /**
     * 本次结算所使用的优惠券信息
     */
    private List<SelectedCouponBo> selectedCouponList;
}
