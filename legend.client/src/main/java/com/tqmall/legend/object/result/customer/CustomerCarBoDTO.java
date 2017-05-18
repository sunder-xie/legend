package com.tqmall.legend.object.result.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by dingbao on 16/9/22.
 */
@Data
public class CustomerCarBoDTO implements Serializable {
    private Long id;

    private String license;

    private Long shopId;

    private Long customerId;

    private Integer repairCount;

    private String carBrand;

    private String carSeries;

    private String carCompany;

    private String importInfo;

    private String customerName;

    private String mobile;
}
