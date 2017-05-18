package com.tqmall.legend.entity.customer;

import lombok.Data;

/**
 * 车辆简要信息
 */
@Data
public class CarConcision {
    Long shopId;
    String customerName;
    String mobilePhone;
    String license;
    public CarConcision(){}
    public CarConcision(Long shopId,
                        String customerName,
                        String mobilePhone,
                        String license) {
        this.shopId = shopId;
        this.customerName = customerName;
        this.mobilePhone = mobilePhone;
        this.license = license;
    }
}
