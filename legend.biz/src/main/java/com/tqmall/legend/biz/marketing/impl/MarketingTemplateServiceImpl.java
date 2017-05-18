package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingTemplateService;
import com.tqmall.legend.dao.marketing.MarketingTemplateDao;
import com.tqmall.legend.entity.marketing.MarketingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
@Service
public class MarketingTemplateServiceImpl extends BaseServiceImpl implements MarketingTemplateService {

    @Autowired
    private MarketingTemplateDao marketingTemplateDao;

    @Override
    public List<MarketingTemplate> select(Map param) {
        return marketingTemplateDao.select(param);
    }

}
