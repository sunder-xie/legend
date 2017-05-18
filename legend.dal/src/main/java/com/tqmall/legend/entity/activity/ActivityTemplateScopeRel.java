package com.tqmall.legend.entity.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/2/24.
 * 活动模板门店范围
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ActivityTemplateScopeRel extends BaseEntity {

    private Long actTplId;//活动模板id
    private Long scopeId;//范围id，模板活动act_scope=1此字段为shop_id,2为city_id

}

