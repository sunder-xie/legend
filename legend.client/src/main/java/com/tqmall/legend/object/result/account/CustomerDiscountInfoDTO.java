package com.tqmall.legend.object.result.account;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xin on 2017/3/7.
 */
@Getter
@Setter
public class CustomerDiscountInfoDTO implements Serializable {
    private Long customerId;
    private String customerName;
    private String mobile;
    private List<MemberCardDiscountDTO> memberCardDiscountList; // 会员卡列表
    private List<ComboDiscountDTO> comboDiscountList; // 计次卡列表
    private List<CouponDiscountDTO> couponDiscountList; // 优惠券列表
}
