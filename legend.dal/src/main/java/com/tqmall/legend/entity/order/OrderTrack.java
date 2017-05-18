package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderTrack extends BaseEntity {

    private Long shopId;
    private Long orderId;
    private String orderStatus;
    private String preOrderStatus;
    private Long preManager;
    private String log;
    private String attributes;
    private String orderSn;
    private String operatorName;

}

