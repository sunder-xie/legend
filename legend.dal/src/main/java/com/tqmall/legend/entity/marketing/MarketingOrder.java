package com.tqmall.legend.entity.marketing;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class MarketingOrder extends BaseEntity {

    private Long shopId;
    private String orderSn;
    private BigDecimal orderAmount;
    private Integer payStatus;
    private Integer type;

}


