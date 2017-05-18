package com.tqmall.legend.entity.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class CouponServiceRel extends BaseEntity {

    private Long shopId;//门店id
    private Long serviceId;//服务项目id
    private String serviceName;//服务项目名
    private Long couponInfoId;//关联优惠劵的id

}

