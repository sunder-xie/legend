package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
public interface MarketingTemplateService {

    /**
     * 获取门店营销活动模板
     * @param param 入参
     * @return 返回门店营销活动模板列表
     */
    public List<MarketingTemplate> select(Map param);

}
