package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingOrderService;
import com.tqmall.legend.dao.marketing.MarketingOrderDao;
import com.tqmall.legend.entity.marketing.MarketingOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15/6/10.
 */
@Service
public class MarketingOrderServiceImpl extends BaseServiceImpl implements MarketingOrderService {

    @Autowired
    private MarketingOrderDao marketingOrderDao;

    @Override
    public Integer create(MarketingOrder marketingOrder) {
        return marketingOrderDao.insert(marketingOrder);
    }

    @Override
    public List<MarketingOrder> select(Map map) {
        return marketingOrderDao.select(map);
    }

    @Override
    public Integer update(MarketingOrder marketingOrder) {
        return marketingOrderDao.updateById(marketingOrder);
    }
}
