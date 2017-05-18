package com.tqmall.legend.facade.discount.bo;

import lombok.Data;

import java.util.List;

/**
 * @Author 辉辉大侠
 * @Date:10:07 AM 02/03/2017
 */
@Data
public class AccountDiscountBo {

    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户联系手机号码
     */
    private String customerMobile;
    /**
     * 账户id
     */
    private Long accountId;
    /**
     * 账户下的会员卡折扣信息
     */
    private List<AccountCardDiscountBo> cardDiscountList;
    /**
     * 账户下的计次卡折扣信息
     */
    private List<AccountComboDiscountBo> comboDiscountList;
    /**
     * 账户下的优惠券折扣信息
     */
    private List<AccountCouponDiscountBo> couponDiscountList;
}
