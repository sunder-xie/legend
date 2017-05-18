package com.tqmall.legend.facade.setting.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.enums.setting.OrderTypeEnum;
import com.tqmall.legend.enums.setting.OrderTypeShowStatusEnum;
import com.tqmall.legend.facade.setting.OrderTypeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/11/7.
 */
@Service
public class OrderTypeFacadeImpl implements OrderTypeFacade {
    @Autowired
    private OrderTypeService orderTypeService;

    @Override
    public List<OrderType> getOrderTypeByShopId(Long shopId) {
        //设置门店工单类型
        Map<String, Object> orderTypeMap = new HashMap<>();
        orderTypeMap.put("shopId", shopId);
        List<OrderType> shopOrderTypeList = orderTypeService.selectNoCache(orderTypeMap);
        return shopOrderTypeList;
    }


    @Override
    public void updateOrderType(OrderType orderType, Long userId) {
        if (orderType.getId() == null) {
            //插入
            List<OrderType> insertList = Lists.newArrayList();
            insertList.add(orderType);
            orderTypeService.batchInsert(insertList);
        } else {
            //更新
            List<Long> ids = Lists.newArrayList();
            ids.add(orderType.getId());
            orderTypeService.updateShowStatusByIds(orderType.getShowStatus(), ids, userId);
        }
    }

    /**
     * 初始化业务类型
     *
     * @param shopId
     */
    @Override
    public void initOrderTypeByShopId(Long shopId) {
        List<OrderType> orderTypeList = getSimpleOrderType();
        for (OrderType orderType : orderTypeList) {
            orderType.setShowStatus(OrderTypeShowStatusEnum.SHOW.getCode());
            orderType.setShopId(shopId);
        }
        orderTypeService.batchInsert(orderTypeList);
    }

    /**
     * 获取业务类型
     *
     * @return
     */
    private List<OrderType> getSimpleOrderType() {
        List<OrderType> simpleList = new ArrayList<>();
        OrderType orderType;
        OrderTypeEnum[] orderTypeEnums = OrderTypeEnum.values();
        for (OrderTypeEnum orderTypeEnum : orderTypeEnums) {
            orderType = new OrderType();
            orderType.setName(orderTypeEnum.getName());
            simpleList.add(orderType);
        }
        return simpleList;
    }

    /**
     * 查询门店已有的业务类型
     *
     * @param shopId
     * @return
     */
    @Override
    public OrderType getOrderTypeNoCacheByShopIdAndName(Long shopId, String name) {
        Map<String, Object> orderTypeMap = new HashMap<>();
        orderTypeMap.put("shopId", shopId);
        orderTypeMap.put("name", name);
        List<OrderType> orderTypeList = orderTypeService.selectNoCache(orderTypeMap);
        if (CollectionUtils.isEmpty(orderTypeList)) {
            return null;
        }
        return orderTypeList.get(0);
    }
}
