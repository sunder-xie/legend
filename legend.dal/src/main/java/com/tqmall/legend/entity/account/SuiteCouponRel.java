package com.tqmall.legend.entity.account;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class SuiteCouponRel extends BaseEntity {

    private Long shopId;//门店id
    private Integer couponCount;//优惠劵张数
    private Long suiteId;//充值包id
    private Integer couponType;//优惠劵类型,0:折扣卷;1:现金券
    private Long couponInfoId;//优惠劵id
    private String couponName;//优惠劵名称

}

