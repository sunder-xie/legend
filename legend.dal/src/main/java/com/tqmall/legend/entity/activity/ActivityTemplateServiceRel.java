package com.tqmall.legend.entity.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/2/24.
 * 活动模板服务关系
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ActivityTemplateServiceRel extends BaseEntity {

    private Long actTplId;//活动模板id
    private Long serviceTplId;//服务模板id

}

