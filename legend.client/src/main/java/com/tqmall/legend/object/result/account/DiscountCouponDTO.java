package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class DiscountCouponDTO implements Serializable{
    /**
     * 优惠券id
     */
    private Long accountCouponId;
    /**
     * 优惠券sn
     */
    private String couponSn;
    /**
     * 优惠券名
     */
    private String couponName;
    /**
     * 优惠券类型
     */
    private String couponTypeName;
    /**
     * 优惠劵类型 1:现金券;2:通用券
     */
    private Integer couponType;

    /**
     * 现金券代扣金额
     */
    private BigDecimal couponAmount;


    private BigDecimal discountAmount;
    /**
     * 是否使用
     */
    private boolean selected;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 过期时间
     */
    private Date expireDate;

    private boolean compatibleWithCard;//允许与会员卡共同使用.0:不允许;1:允许
    private boolean singleUse;//一张工单只允许使用一张优惠券.0:不允许;1:允许
}
