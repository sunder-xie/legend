package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by twg on 15/8/7.
 * 门店营销活动模板类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MarketingTemplate extends BaseEntity {
    private String title; //门店活动标题
    private String content;//门店活动内容
    private String templateUrl;//门店模板URL
    private Integer serviceNum;//门店服务数量
    private Integer status; //门店活动状态：0上架1下架
    private MarketingColumnConfig marketingColumnConfig;//模板字段配置信息
}
