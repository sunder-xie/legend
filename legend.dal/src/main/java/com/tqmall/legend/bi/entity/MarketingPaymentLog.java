package com.tqmall.legend.bi.entity;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wangjiachao on 15/11/24.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MarketingPaymentLog extends BaseEntity {

    private Long shopId;
    private Date payTime;
    private Long paymentLogId;
    private String paymentLogSn;
    private Long customerId;
    private Long customerCarId;
    private Long orderId;
    private String orderSn;
    private String carLicense;
    private BigDecimal payAmount;

}