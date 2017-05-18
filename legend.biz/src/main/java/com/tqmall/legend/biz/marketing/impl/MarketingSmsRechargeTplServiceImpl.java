package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingSmsRechargeTplService;
import com.tqmall.legend.dao.marketing.MarketingSmsRechargeTplDao;
import com.tqmall.legend.dao.shop.SmsPayLogDao;
import com.tqmall.legend.entity.marketing.MarketingSmsRechargeTpl;
import com.tqmall.legend.entity.shop.SmsPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15/6/10.
 */
@Service
public class MarketingSmsRechargeTplServiceImpl extends BaseServiceImpl implements MarketingSmsRechargeTplService{

    @Autowired
    private MarketingSmsRechargeTplDao marketingSmsRechargeTplDao;
    @Autowired
    private SmsPayLogDao smsPayLogDao;

    public List<MarketingSmsRechargeTpl> select(Map map){
        return marketingSmsRechargeTplDao.select(map);
    }

    @Override
    public MarketingSmsRechargeTpl selectById(Long id) {
        return marketingSmsRechargeTplDao.selectById(id);
    }

    @Override
    public Page<SmsPayLog> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(smsPayLogDao, pageable, searchParams);
    }
}
