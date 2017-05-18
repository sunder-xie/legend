package com.tqmall.legend.entity.settlement;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by QXD on 2014/12/25.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Coupon extends BaseEntity {

    private Long shopId;
    private String isUsed;
    private Long customerId;
    private Long customerCarId;
    private String carLicense;
    private String couponSn;
    private BigDecimal couponValue;

}


