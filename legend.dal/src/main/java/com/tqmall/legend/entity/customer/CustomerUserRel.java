package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/15.
 * 客户归属
 */
@Getter
@Setter
public class CustomerUserRel extends BaseEntity {

    private Long shopId;//门店id
    private Long userId;//员工id
    private Long customerCarId;//车辆id
    private Long customerId;//客户id   注：目前员工帮客户指的是车辆，customerId是冗余字段
    private String allotSn;//分配批次号，uuid

}

