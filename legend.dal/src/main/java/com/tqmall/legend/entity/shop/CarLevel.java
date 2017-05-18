package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class CarLevel extends BaseEntity {

    private String name;
    private Long shopId;
    private Long sort;
}

