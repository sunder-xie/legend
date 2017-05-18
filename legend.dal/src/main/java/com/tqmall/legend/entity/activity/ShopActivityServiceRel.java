package com.tqmall.legend.entity.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/2/24.
 * 活动服务关系
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopActivityServiceRel extends BaseEntity {

    private Long shopActId;//门店活动id
    private Long serviceId;//门店服务id

}

