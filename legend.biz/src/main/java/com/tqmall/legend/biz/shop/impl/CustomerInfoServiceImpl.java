package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.shop.CustomerInfoService;
import com.tqmall.legend.dao.marketing.ng.CustomerInfoDao;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/3/22.
 */
@Service
public class CustomerInfoServiceImpl implements CustomerInfoService {
    @Autowired
    private CustomerInfoDao customerInfoDao;
    @Override
    public List<CustomerInfo> getCustomerInfoList(Map param) {
        return customerInfoDao.select(param);
    }

    @Override
    public CustomerInfo getCustomerInfo(Long shopId, Long customerCarId) {
        Map param  = Maps.newConcurrentMap();
        param.put("shopId",shopId);
        param.put("customerCarId",customerCarId);
        List<CustomerInfo> customerInfoList = customerInfoDao.select(param);
        if(!CollectionUtils.isEmpty(customerInfoList)){
            return customerInfoList.get(0);
        }
        return null;
    }

    @Override
    public List<CustomerInfo> selectCustomerInfoIsNote(Map param){
       return customerInfoDao.selectCustomerInfoIsNote(param);
    }

    @Override
    public List<CustomerInfo> listByCarIds(Long shopId, Collection<Long> carIds) {
        if (carIds.isEmpty()) {
            return Lists.newArrayList();
        }
        return customerInfoDao.listByCarIds(shopId, carIds);
    }

}
