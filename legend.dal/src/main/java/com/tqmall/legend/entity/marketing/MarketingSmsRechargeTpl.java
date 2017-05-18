package com.tqmall.legend.entity.marketing;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class MarketingSmsRechargeTpl extends BaseEntity {

    private BigDecimal rechargeMoney;
    private Long buySmsNum;
    private Long freeSmsNum;
    private String flags;

}

