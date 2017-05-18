package com.tqmall.legend.bi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class StatisticsDaily extends BaseEntity {
    private Long shopId;
    private Date orderDate;
    private String orderYear;
    private String orderMonth;
    private String orderDay;
    private BigDecimal orderProfit;
    private Long orderCount;
    private Long precheckOrderCount;

}


