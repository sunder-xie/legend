package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingColumnConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
public interface MarketingColumnConfigService {

    /**
     * 获取门店营销活动模板字段配置
     * @param param 入参
     * @return 返回门店营销活动模板字段配置列表
     */
    public List<MarketingColumnConfig> select(Map param);

}
