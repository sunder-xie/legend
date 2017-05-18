package com.tqmall.legend.biz.order.bo;

import com.tqmall.legend.entity.order.OrderDiscountFlow;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

/**
 * Created by zsy on 16/6/2.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderDiscountFlowBo extends OrderDiscountFlow {
    private Long couponId;//优惠券id
    private Long comboServiceId;//计次卡优惠的服务id
    private Integer useCount;//计次卡使用次数
}
