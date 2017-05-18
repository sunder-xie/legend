package com.tqmall.legend.facade.account.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xin on 2017/3/9.
 */
@Getter
@Setter
public class CustomerDiscountInfoForApp {
    private Long customerId;
    private String customerName;
    private String mobile;
    private List<MemberCardDiscount> memberCardDiscountList; // 会员卡
    private List<ComboDiscount> comboDiscountList; // 计次卡
    private List<CouponDiscount> couponDiscountList; // 优惠券
}
