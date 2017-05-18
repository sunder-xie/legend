package com.tqmall.legend.facade.insurance.vo;

import lombok.Data;

/**
 * Created by zhouheng on 17/3/11.
 */
@Data
public class InsuranceShopCashCouponDetailVo {

    /**
     * 门店ID
     **/
    private Integer shopId;

    /**
     * 现金券编号,以cx开头
     **/
    private String cashCouponSn;

    /**
     * 现金券面额
     **/
    private java.math.BigDecimal faceAmount;

    /**
     * 现金券生效日期
     **/
    private java.util.Date gmtValidate;

    /**
     * 现金券过期日期
     **/
    private java.util.Date gmtExpired;

    /**
     * 现金券使用日期
     **/
    private java.util.Date gmtConsumed;

    /**
     * 现金券结算日期
     **/
    private java.util.Date gmtSettled;

    /**
     * 淘气内部订单号
     **/
    private String insuranceOrderSn;

    /**
     * 现金券状态 0 未用  1 已使用  2 已过期 3 已结算
     **/
    private int cashCouponStatus;

}
