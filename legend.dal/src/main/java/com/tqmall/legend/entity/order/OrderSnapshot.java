package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderSnapshot extends BaseEntity {

    private String orderInfo;
    private String orderGoods;
    private String orderServices;
    private Long shopId;
    private String orderSn;
    private String customerInfo;
    private String carInfo;

    @Override
    public String toString() {
        return "OrderSnapshot{" +
                "orderInfo='" + orderInfo + '\'' +
                ", orderGoods='" + orderGoods + '\'' +
                ", orderServices='" + orderServices + '\'' +
                ", shopId=" + shopId +
                ", orderSn='" + orderSn + '\'' +
                ", customerInfo='" + customerInfo + '\'' +
                ", carInfo='" + carInfo + '\'' +
                '}';
    }
}

