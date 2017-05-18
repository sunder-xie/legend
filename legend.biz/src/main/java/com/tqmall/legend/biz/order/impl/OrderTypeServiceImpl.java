package com.tqmall.legend.biz.order.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.order.OrderTypeDao;
import com.tqmall.legend.entity.order.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * OrderTypeService implement
 */
@Service
public class OrderTypeServiceImpl extends BaseServiceImpl implements OrderTypeService {


    public static final Logger LOGGER = LoggerFactory.getLogger(OrderTypeServiceImpl.class);
    @Autowired
    private OrderTypeDao orderTypeDao;
    @Autowired
    private CacheComponent<List<OrderType>> cacheComponent;

    @Override
    public List<OrderType> select(Map map) {
        List<OrderType> orderTypeList = cacheComponent.getCache(CacheKeyConstant.ORDER_TYPE);
        Object shopId = map.get("shopId");
        List<OrderType> orderTypes = new LinkedList<>();
        if (shopId != null) {
            for (OrderType orderType : orderTypeList) {
                if (orderType.getShopId().equals(shopId)) {
                    orderTypes.add(orderType);
                }
            }
        }
        return orderTypes;
    }

    @Override
    public List<OrderType> selectNoCache(Map map) {
        List<OrderType> orderTypeList = orderTypeDao.select(map);
        return orderTypeList;
    }

    @Override
    public OrderType selectById(Long id) {
        return orderTypeDao.selectById(id);
    }

    @Override
    @Transactional
    public Result batchInsert(List<OrderType> orderTypeList) {
        Integer insertSize = orderTypeDao.batchInsert(orderTypeList);
        return Result.wrapSuccessfulResult(insertSize);
    }


    @Override
    public Optional<OrderType> getOrderType(Long id) {
        OrderType orderType = null;
        try {
            orderType = orderTypeDao.selectById(id);
        } catch (Exception e) {
            LOGGER.error("获取工单类型异常，工单类型ID:{}", id);
            return Optional.absent();
        }
        return Optional.fromNullable(orderType);
    }

    @Override
    public int updateShowStatusByIds(Integer showStatus, List<Long> ids, Long userId) {
        return orderTypeDao.updateShowStatusByIds(showStatus, ids, userId);
    }

    @Override
    public OrderType selectByIdAndShopId(Long shopId, Long id) {
        List<OrderType> orderTypeList = cacheComponent.getCache(CacheKeyConstant.ORDER_TYPE);
        if (shopId != null) {
            for (OrderType orderType : orderTypeList) {
                if (id.equals(orderType.getId()) && orderType.getShopId().equals(shopId)) {
                    return orderType;
                }
            }
        }
        return null;
    }

    @Override
    public List<OrderType> selectByShopId(Long shopId) {
        List<OrderType> orderTypes = Lists.newArrayList();
        List<OrderType> orderTypeList = cacheComponent.getCache(CacheKeyConstant.ORDER_TYPE);
        if (shopId != null) {
            for (OrderType orderType : orderTypeList) {
                if (orderType.getShopId().equals(shopId)) {
                    orderTypes.add(orderType);
                }
            }
        }
        return orderTypes;
    }
}
