package com.tqmall.legend.biz.marketing.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.marketing.MarketingShopRelService;
import com.tqmall.legend.dao.marketing.MarketingShopRelDao;
import com.tqmall.legend.entity.marketing.MarketingShopRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 15/6/10.
 */
@Service
public class MarketingShopRelServiceImpl implements MarketingShopRelService {

    @Autowired
    private MarketingShopRelDao marketingSmsShopRelDao;

    @Override
    public MarketingShopRel selectOne(Map<String, Object> params) {
        List<MarketingShopRel> list = marketingSmsShopRelDao.select(params);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateById(MarketingShopRel marketingShopRel) {
        marketingSmsShopRelDao.updateById(marketingShopRel);
    }

    @Override
    public void updateByShopId(MarketingShopRel marketingShopRel) {
        marketingSmsShopRelDao.updateByShopId(marketingShopRel);
    }

    @Override
    public List<MarketingShopRel> select(Map<String, Object> params) {
        return marketingSmsShopRelDao.select(params);
    }

    @Override
    public boolean insert(MarketingShopRel marketingShopRel) {
        return this.marketingSmsShopRelDao.insert(marketingShopRel) > 0;
    }

    @Override
    public MarketingShopRel selectOneById(Long shopID) {
        Map params = Maps.newHashMap();
        params.put("shopId", shopID);
        List<MarketingShopRel> list = marketingSmsShopRelDao.select(params);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateRemain(Long shopId, Integer usedCount) {
        MarketingShopRel shopRel = selectOneById(shopId);
        Long remainNum = shopRel.getSmsNum();
        shopRel.setSmsNum(remainNum - usedCount);
        updateById(shopRel);
    }

}
