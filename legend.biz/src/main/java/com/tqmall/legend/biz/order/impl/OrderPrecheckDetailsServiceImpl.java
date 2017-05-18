package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderPrecheckDetailsService;
import com.tqmall.legend.dao.order.OrderPrecheckDetailsDao;
import com.tqmall.legend.entity.order.OrderPrecheckDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/7/9.
 */
@Slf4j
@Service
public class OrderPrecheckDetailsServiceImpl extends BaseServiceImpl implements OrderPrecheckDetailsService {
    @Autowired
    private OrderPrecheckDetailsDao orderPrecheckDetailsDao;

    /**
     * 批量删除
     * @param orderPrecheckDetailsList
     * @return
     */
    @Override
    public int batchInsert(List<OrderPrecheckDetails> orderPrecheckDetailsList) {
        int totalSize = super.batchInsert(orderPrecheckDetailsDao, orderPrecheckDetailsList, 300);
        return totalSize;
    }

    @Override
    public int deleteByIds(Object[] ids) {
        int deleteSize = orderPrecheckDetailsDao.deleteByIds(ids);
        return deleteSize;
    }

    @Override
    public List<OrderPrecheckDetails> select(Map<String, Object> searchParams) {
        List<OrderPrecheckDetails> orderPrecheckDetailsList = orderPrecheckDetailsDao.select(searchParams);
        return orderPrecheckDetailsList;
    }

    @Override
    public int updateById(OrderPrecheckDetails orderPrecheckDetails) {
        return orderPrecheckDetailsDao.updateById(orderPrecheckDetails);
    }


}
