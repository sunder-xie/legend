package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingSmsLogServie;
import com.tqmall.legend.dao.marketing.MarketingSmsLogDao;
import com.tqmall.legend.entity.marketing.MarketingSmsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dingbao on 15/6/12.
 */
@Service
public class MarketingSmsLogServiceImpl extends BaseServiceImpl implements MarketingSmsLogServie {

    @Autowired
    private MarketingSmsLogDao marketingSmsLogDao;

    @Override
    public void insert(MarketingSmsLog marketingSmsLog) {
        marketingSmsLogDao.insert(marketingSmsLog);
    }

    @Override
    public Page<MarketingSmsLog> getPage(Pageable pageable, Map<String, Object> searchParams) {
        Page<MarketingSmsLog> page = super.getPage(marketingSmsLogDao, pageable, searchParams);
        if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
            //写业务逻辑

        }
        return page;
    }

    @Override
    public Long logConsume(Long shopId, Long userId, String operator, Integer usedCount, Integer position, String template) {
        MarketingSmsLog log = new MarketingSmsLog();
        log.setCreator(userId);
        log.setModifier(userId);
        log.setOperator(operator);
        log.setShopId(shopId);
        log.setSmsNum(Long.valueOf(usedCount));
        log.setType(2);
        log.setTemplate(template);
        log.setPosition(position);
        insert(log);
        return log.getId();
    }

    @Override
    public void updateSmsNum(Long smsLogId, int count, Long shopId) {
        MarketingSmsLog smsLog = findById(smsLogId, shopId);
        smsLog.setSmsNum(Long.valueOf(count));
        marketingSmsLogDao.updateById(smsLog);
    }

    @Override
    public void deleteById(Long shopId, Long smsLogId) {
        marketingSmsLogDao.deleteById(smsLogId);
    }

    private MarketingSmsLog findById(Long smsLogId, Long shopId) {
        return marketingSmsLogDao.selectById(smsLogId);
    }
}
