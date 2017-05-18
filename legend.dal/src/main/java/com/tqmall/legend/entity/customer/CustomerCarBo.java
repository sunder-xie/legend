package com.tqmall.legend.entity.customer;

import lombok.Data;

/**
 * Created by dingbao on 15/9/21.
 */
@Data
public class CustomerCarBo {
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
