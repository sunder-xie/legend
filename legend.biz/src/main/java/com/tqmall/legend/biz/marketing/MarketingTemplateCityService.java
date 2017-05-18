package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingTemplateCity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
public interface MarketingTemplateCityService {

    /**
     * 获取门店营销活动城市模板
     * @param param 入参
     * @return 返回门店营销活动城市模板列表
     */
    public List<MarketingTemplateCity> select(Map param);

    /**
     * 分页获取
     * @param pageable spring
     * @param searchParams 查询条件
     * @return
     */
    public Page<MarketingTemplateCity> getPage(Pageable pageable, Map<String, Object> searchParams);

}
