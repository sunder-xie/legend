package com.tqmall.legend.biz.shop.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.dao.shop.ServiceGoodsSuiteDao;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : Freeway <jiajie.qian@tqmall.com>
 * @Created : 2014-11-09 22:26
 */

@Service
public class ServiceGoodsSuiteServiceImpl extends BaseServiceImpl implements ServiceGoodsSuiteService {

    @Autowired
    ServiceGoodsSuiteDao serviceGoodsSuiteDao;

    @Override
    public List<ServiceGoodsSuite> select(Map<String, Object> searchMap) {
        return serviceGoodsSuiteDao.select(searchMap);
    }

    @Override
    public List<ServiceGoodsSuite> selectByServiceIds(Long[] ServiceIds){
        if(ServiceIds==null || ServiceIds.length==0){
            return new ArrayList<>();
        }
        return  serviceGoodsSuiteDao.selectByServiceIds(ServiceIds);
    }

    @Override
    public ServiceGoodsSuite selectByServiceId(Long id) {
        return serviceGoodsSuiteDao.selectByServiceId(id);
    }

    @Override
    public ServiceGoodsSuite selectByServiceId(Long id,Long shopId) {
        return serviceGoodsSuiteDao.selectByServiceIdAndShopId(id, shopId);
    }

    @Override
    public Integer add(ServiceGoodsSuite serviceGoodsSuite) {
        return serviceGoodsSuiteDao.insert(serviceGoodsSuite);
    }

    @Override
    public Integer deleteByServiceIdAndShopId(Long serviceId, Long shopId) {
        return serviceGoodsSuiteDao.deleteByServiceIdAndShopId(serviceId, shopId);
    }
}
