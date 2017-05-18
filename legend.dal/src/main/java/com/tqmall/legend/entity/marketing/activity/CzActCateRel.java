package com.tqmall.legend.entity.marketing.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class CzActCateRel extends BaseEntity {

    private Long actId;//活动ID
    private Long cateId;//车主服务一级类别ID
    private String cateName;//车主服务一级类别名称

}

