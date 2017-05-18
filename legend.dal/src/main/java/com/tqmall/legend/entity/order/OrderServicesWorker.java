package com.tqmall.legend.entity.order;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.legend.entity.base.BaseEntity;

@Getter
@Setter
public class OrderServicesWorker extends BaseEntity {

    private Long shopId;//'门店
    private Long orderId;//工单id
    private Long orderServicesId;//工单服务id
    private Long workerId;//维修工id
    private String workerName;//维修工名称

}

