package com.tqmall.legend.object.result.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by dingbao on 16/9/22.
 */
@Data
public class ApiCustomerSearchVoDTO implements Serializable {
    /**
     * 车辆id
     */
    private Long carId;
    /**
     * 车主姓名
     */
    private String customerName;

    /**
     * 车主电话
     */
    private String mobile;

    /**
     * 车牌号码
     */
    private String license;

    /**
     * 车型
     */
    private String  carInfo;

    /**
     * 维修次数
     */
    private Integer repairCount;
}
