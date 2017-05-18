package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class VirtualOrderservice extends BaseEntity {

    private Long shopId;
    private Long orderId;
    private Long serviceId;
    private BigDecimal soldPrice;
    private BigDecimal serviceHour;
    private String orderSn;
    private BigDecimal servicePrice;
    private BigDecimal serviceAmount;
    private BigDecimal discount;
    private Integer type;
    private BigDecimal soldAmount;
    private String serviceName;
    private String serviceSn;
    private Long workerId;
    private String workerName;
    private String serviceCatName;
    private Long serviceCatId;
    private String flags;
    private String serviceNote;

    //管理费比率
    private BigDecimal manageRate;

}

