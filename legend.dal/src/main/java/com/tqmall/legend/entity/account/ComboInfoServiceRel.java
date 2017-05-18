package com.tqmall.legend.entity.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class ComboInfoServiceRel extends BaseEntity {

    private Long shopId;//门店id
    private Long comboInfoId;//计次卡类型id
    private Long serviceId;//服务项目id
    private String serviceName;//服务项目名
    private Integer serviceCount;//服务项目数量

}

