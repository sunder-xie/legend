package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.bi.dao.order.StatisticsOrderInfoDao;
import com.tqmall.legend.biz.order.StatisticsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zsy on 16/12/9.
 */
@Service
public class StatisticsOrderServiceImpl implements StatisticsOrderService {
    @Autowired
    private StatisticsOrderInfoDao statisticsOrderInfoDao;

    @Override
    public Date getLastOrderDate(Long shopId, Long customerCarId) {
        return statisticsOrderInfoDao.getLastOrderDate(shopId, customerCarId);
    }
}
