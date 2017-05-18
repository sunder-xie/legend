package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by twg on 15/8/7.
 * 门店营销活动城市模板类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MarketingTemplateCity extends BaseEntity {
    private Long templateId; //门店活动模板id
    private Long city;//门店活动城市
}
