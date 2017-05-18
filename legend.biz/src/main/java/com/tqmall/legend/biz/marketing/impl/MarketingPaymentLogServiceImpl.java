package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.bi.dao.MarketingPaymentLogDao;
import com.tqmall.legend.bi.entity.MarketingPaymentLog;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingPaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wjc on 15/11/24
 */
@Service
public class MarketingPaymentLogServiceImpl extends BaseServiceImpl implements MarketingPaymentLogService{
    @Autowired
    private MarketingPaymentLogDao dao;

    @Override
    public List<MarketingPaymentLog> select(Map<String, Object> searchParams){
        return dao.select(searchParams);
    }

}
