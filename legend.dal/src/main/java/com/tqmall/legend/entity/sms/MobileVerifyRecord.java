package com.tqmall.legend.entity.sms;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class MobileVerifyRecord extends BaseEntity {

    private String mobile;
    private String code;

}


