package com.tqmall.legend.biz.order.impl;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderInfoExtService;
import com.tqmall.legend.dao.order.OrderInfoExtDao;
import com.tqmall.legend.entity.order.OrderInfoExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/12/15.
 */
@Slf4j
@Service
public class OrderInfoExtServiceImpl extends BaseServiceImpl implements OrderInfoExtService{

    @Autowired
    private OrderInfoExtDao orderInfoExtDao;


    @Override
    public int save(OrderInfoExt orderInfoExt , UserInfo userInfo) {
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("orderId", orderInfoExt.getOrderId());
        List<OrderInfoExt> orderInfoExtList = orderInfoExtDao.select(searchMap);
        if(!CollectionUtils.isEmpty(orderInfoExtList)){
            OrderInfoExt orderInfoExtSrc = orderInfoExtList.get(0);
            orderInfoExt.setId(orderInfoExtSrc.getId());
            orderInfoExt.setModifier(userInfo.getUserId());
            return orderInfoExtDao.updateById(orderInfoExt);
        }else{
            orderInfoExt.setCreator(userInfo.getUserId());
            return orderInfoExtDao.insert(orderInfoExt);
        }
    }
}
