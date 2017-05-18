package com.tqmall.legend.entity.customer;


import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerC extends BaseEntity {

    private String openid;
    private String mobile;
    private String remark;

}


