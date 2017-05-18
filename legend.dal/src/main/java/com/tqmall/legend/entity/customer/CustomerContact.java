package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerContact extends BaseEntity {

    private Long shopId;
    private String contact;
    private String contactMobile;
    private Long customerId;
    private Long customerCarId;
    /**
     * 版本号
     */
    private String ver;
    /**
     * 数据来源 0:web,1:android,2:ios
     */
    private String refer = "0";

}

