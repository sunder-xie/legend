package com.tqmall.legend.entity.insurance;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zsy on 2015/1/15.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class InsuranceCompany extends BaseEntity {

    private String name;

}

