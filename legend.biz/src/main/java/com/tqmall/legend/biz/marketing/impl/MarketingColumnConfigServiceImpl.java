package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingColumnConfigService;
import com.tqmall.legend.dao.marketing.MarketingColumnConfigDao;
import com.tqmall.legend.entity.marketing.MarketingColumnConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
@Service
public class MarketingColumnConfigServiceImpl extends BaseServiceImpl implements MarketingColumnConfigService {

    @Autowired
    private MarketingColumnConfigDao marketingColumnConfigDao;

    @Override
    public List<MarketingColumnConfig> select(Map param) {
        return marketingColumnConfigDao.select(param);
    }
}
