package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanghui on 6/16/16.
 */
@Data
public class AccountCouponDTO implements Serializable{
    /**
     * 优惠券id
     */
    private Long accountCouponId;
    /**
     * 优惠券券码
     */
    private String accountCouponSn;
    /**
     * 优惠券类型id
     */
    private Long couponInfoId;
    /**
     * 优惠券券名
     */
    private String couponName;
    /**
     * 优惠券类型
     * 0:折扣卷;1:现金券;2:通用券
     */
    private Integer couponType;
    /**
     * 券生效时间
     */
    private Date effectiveDate;
    /**
     * 券失效时间
     */
    private Date expireDate;
    /**
     * 券使用范围
     * 0:全场通用;1.只限服务工时;2.只限指定服务项目打折
     */
    private Integer useRange;

    /**
     * 获取优惠券描述
     * @return
     */
    private String limitDescription;

    /**
     * 现金券抵扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 折扣券折扣
     */
    private BigDecimal discount;
    private Integer compatibleWithCard;//允许与会员卡共同使用.0:不允许;1:允许
    private Integer compatibleWithOtherAccount;//允许他人使用.0:不允许;1:允许
    private Integer compatibleWithOtherCoupon;//允许与其他优惠券共同使用.0:不允许;1:允许
    private Integer singleUse;//一张工单只允许使用一张优惠券.0:不允许;1:允许

    private Long flowId;
}
