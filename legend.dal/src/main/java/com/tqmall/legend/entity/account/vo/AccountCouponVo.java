package com.tqmall.legend.entity.account.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jiachao on 16/6/8.
 */
@Data
public class AccountCouponVo {
    private Long shopId;
    private Long creator;
    private Long couponSuiteId;
    private List<CouponVo> couponVos;
    private Long paymentId;
    private BigDecimal payAmount;
    private String note;
    private Long accountId;
    private String paymentName;
}
