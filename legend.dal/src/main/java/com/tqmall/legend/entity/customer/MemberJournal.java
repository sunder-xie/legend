package com.tqmall.legend.entity.customer;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class MemberJournal extends BaseEntity {

    private Long shopId;
    private Long paymentLogId;
    private Long memberId;
    private String memberName;
    private Long carId;
    private String plateNumber;
    private String consumeType;
    private String serviceName;
    private Long serviceId;
    private Integer serviceCount;
    private BigDecimal serviceValue;
    private Integer leftCount;
    private BigDecimal leftValue;
    private String comment;
    private String creatorName;

    public String getGmtCreateStr() {
        if (gmtCreate == null) {
            return DateUtil.convertDateToYMD(new Date());
        } else
            return DateUtil.convertDateToYMD(gmtCreate);
    }

    public Integer getServiceCount() {
        if (null == serviceCount)
            return 0;
        else
            return serviceCount;
    }

    public BigDecimal getServiceValue() {
        if (null == serviceValue)
            return BigDecimal.ZERO;
        else
            return serviceValue;
    }

    public Integer getLeftCount() {
        if (null == leftCount)
            return 0;
        else
            return leftCount;
    }

    public BigDecimal getLeftValue() {
        if (null == leftValue)
            return BigDecimal.ZERO;
        else
            return leftValue;
    }

}

