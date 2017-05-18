package com.tqmall.legend.entity.precheck;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by guangxue on 14/10/28.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Prechecks extends BaseEntity {
    private Long shopId;
    private Long customerId;
    private Long customerCarId;
    private String plateNumber;
    private String carModel;
    private String nextTime;
    private String comments;
    private String precheckSn;
    private String gmtCreateStr;
    private String mobile;
    private String mileage;
    private String mileageUnit;
    private String customerName;
    private String insurance;
    private Long insuranceId;
    private Date dueDate;
    private String dueDateStr;
    private String color;
    private String manHour;
    private BigDecimal expFee;
    private String upkeepMileage;
    private String refer;
    private String ver;

}

