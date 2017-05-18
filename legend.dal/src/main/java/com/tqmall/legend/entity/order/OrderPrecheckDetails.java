package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/7/9.
 * 工单预检信息
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderPrecheckDetails extends BaseEntity {

    private Long shopId;//店铺id
    private Long orderId;//工单id
    private Long precheckId;//预检单id
    private Long precheckItemId;//预检项目id
    private Long precheckItemType;//每个检测项目的取值的类型:
    private String precheckItemName;//每个检测项目的类型:
    private Long precheckValueId;//预检项目值id
    private String precheckValueType;//取值的类型分类,
    private String precheckValue;//预检项目值

    /**
     * 临时变量
     */
    private String ftlId;//预检项目标识
}