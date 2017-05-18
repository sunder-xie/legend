package com.tqmall.legend.facade.customer.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/21.
 */
@Getter
@Setter
public class CustomAllotBo extends AllotBo {
    private Integer size;
    private boolean allot;
    private String sTime;
    private String eTime;
    private String minAmount;
    private String maxAmount;
    private String minAverage;
    private String maxAverage;
    private String numberSign;
    private String number;
    private String daySign;
    private String day;
    private String carLevelTag;
    private String minMileage;
    private String maxMileage;
    private String carLicense;
    private String carType;
    private String mobile;
    private String customerCompany;
    private String tag;
    private String customerTag;
}
