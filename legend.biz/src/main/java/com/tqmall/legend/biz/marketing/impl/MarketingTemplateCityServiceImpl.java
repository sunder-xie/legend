package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingTemplateCityService;
import com.tqmall.legend.dao.marketing.MarketingTemplateCityDao;
import com.tqmall.legend.entity.marketing.MarketingTemplateCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
@Service
public class MarketingTemplateCityServiceImpl extends BaseServiceImpl implements MarketingTemplateCityService {
    @Autowired
    private MarketingTemplateCityDao marketingTemplateCityDao;

    @Override
    public List<MarketingTemplateCity> select(Map param) {
        return marketingTemplateCityDao.select(param);
    }

    public Page<MarketingTemplateCity> getPage(Pageable pageable, Map<String, Object> searchParams) {
        Page<MarketingTemplateCity> page = super.getPage(marketingTemplateCityDao, pageable, searchParams);
        return page;
    }
}
