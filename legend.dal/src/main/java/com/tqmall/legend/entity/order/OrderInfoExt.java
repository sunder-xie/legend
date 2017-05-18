package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/15.
 * 工单扩展表
 */
@Getter
@Setter
public class OrderInfoExt extends BaseEntity {

    private Long shopId;//门店id
    private Long userId;//归属员工id
    private Long orderId;//工单id

}

