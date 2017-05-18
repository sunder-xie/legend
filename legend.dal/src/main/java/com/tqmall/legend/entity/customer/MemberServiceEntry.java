package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class MemberServiceEntry extends BaseEntity {

    private Long shopId;
    private Long memberId;
    private String serviceName;
    private Integer serviceCount;
    private BigDecimal serviceValue;
    private BigDecimal serviceAvg;
    private Date latestUsed;
    private String serviceType;

    public MemberServiceEntry() {
        serviceCount = 0;
        serviceValue = BigDecimal.ZERO;
    }

    public Integer getServiceCount() {
        if (null == serviceCount)
            return 0;
        else
            return serviceCount;
    }

    public BigDecimal getServiceValue() {
        if (serviceValue == null) {
            return BigDecimal.ZERO;
        } else {
            return serviceValue;
        }
    }
}

