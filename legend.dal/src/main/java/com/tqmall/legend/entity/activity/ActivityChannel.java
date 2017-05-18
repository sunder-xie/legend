package com.tqmall.legend.entity.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/2/25.
 * 活动渠道
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ActivityChannel extends BaseEntity {

    private String channelName;//活动渠道名称
    private Long sort;//排序，默认降序
    private Integer channelSource;//渠道中活动模版来源:0 legendm,1 sam

}

