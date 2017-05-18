package com.tqmall.legend.facade.account.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by xin on 2017/2/28.
 *
 * 客户优惠信息
 */
@Getter
@Setter
public class CustomerDiscountInfo {
    private Long customerId;
    private String customerName;
    private String mobile;
    private List<MemberCardDiscount> memberCardDiscountList; // 会员卡优惠
    private Long sumComboCouponNum; // 卡券数
    private List<ComboCouponDiscount> comboCouponDiscountList; // 计次卡和优惠券优惠
}
