package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.entity.shop.Shop;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by twg on 15/8/7.
 * 门店营销活动
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MarketingCase extends BaseEntity {
    private String title; //门店活动标题
    private String content;//门店活动内容
    private String templateUrl;//门店模板URL
    private String oldTemplateUrl;//门店模板URL
    private Integer serviceNum = Integer.valueOf("0");//门店服务数量
    private Integer status = Integer.valueOf("0"); //门店活动状态0未激活1已激活2已发布
    private Long templateId = Long.valueOf("0");//门店模板id
    private Long shopId = Long.valueOf("0");//门店id
    private Shop shop;//门店活动中门店基本信息
    private List<MarketingCaseService> serviceInfos;//门店活动中服务列表
    private MarketingColumnConfig marketingColumnConfig;//模板字段配置信息
    private String imgUrl;//二维码路径
    private String page;//活动页面
    private Long visitCount = Long.valueOf("0");//活动访问量
}
