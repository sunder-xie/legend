package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingSmsService;
import com.tqmall.legend.dao.marketing.MarketingSmsDao;
import com.tqmall.legend.entity.marketing.MarketingSms;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15/6/8.
 */
@Service
public class MarketingSmsServiceImpl extends BaseServiceImpl implements MarketingSmsService {
    @Autowired
    private MarketingSmsDao marketingSmsDao;

    @Override
    public Page<MarketingSms> getPage(Pageable pageable, Map<String, Object> searchParams) {
        Page<MarketingSms> page = super.getPage(marketingSmsDao, pageable, searchParams);
        if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
            //写业务逻辑

        }
        return page;
    }

    @Override
    public Integer insert(MarketingSms marketingSms) {
        return marketingSmsDao.insert(marketingSms);
    }

    @Override
    public List<MarketingSms> select(Map<String, Object> params) {
        return marketingSmsDao.select(params);
    }

    @Override
    public MarketingSms selectById(Long id) {
        return marketingSmsDao.selectById(id);
    }

    @Override
    public void updateById(MarketingSms marketingSms) {
        marketingSmsDao.updateById(marketingSms);
    }

    @Override
    public void batchInsert(List<MarketingSms> smsList) {
        if (CollectionUtils.isNotEmpty(smsList)) {
            marketingSmsDao.batchInsert(smsList);
        }
    }

    @Override
    public List<MarketingSms> listByLogId(Long shopId, Long smsLogId) {
        Assert.notNull(smsLogId);
        Assert.notNull(shopId);
        return marketingSmsDao.listByLogId(shopId, smsLogId , null);
    }

    @Override
    public List<MarketingSms> listSucessByLogId(Long shopId, Long smsLogId) {
        Assert.notNull(smsLogId);
        Assert.notNull(shopId);
        return marketingSmsDao.listByLogId(shopId, smsLogId, MarketingSms.SEND_SUCCESS);
    }
}
