package com.tqmall.legend.biz.customer.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.bo.AccountAndCarBO;
import com.tqmall.legend.biz.customer.CustomerCarRelService;
import com.tqmall.legend.dao.customer.CustomerCarRelDao;
import com.tqmall.legend.entity.customer.CustomerCarRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by xin on 2017/2/28.
 */
@Service
public class CustomerCarRelServiceImpl implements CustomerCarRelService {

    @Autowired
    private CustomerCarRelDao customerCarRelDao;

    @Override
    public List<CustomerCarRel> listByCarId(Long shopId, Long carId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("customerCarId", carId);
        return customerCarRelDao.select(param);
    }

    @Override
    public CustomerCarRel findByCustomerIdAndCarId(AccountAndCarBO accountAndCarBO) {
        Map param = Maps.newHashMap();
        param.put("shopId", accountAndCarBO.getShopId());
        param.put("accountId", accountAndCarBO.getAccountId());
        param.put("customerId", accountAndCarBO.getCustomerId());
        param.put("customerCarId", accountAndCarBO.getCustomerCarId());
        List<CustomerCarRel> customerCarRels = customerCarRelDao.select(param);
        if (CollectionUtils.isEmpty(customerCarRels)) {
            return null;
        }
        return customerCarRels.get(0);
    }

    @Override
    public void save(CustomerCarRel customerCarRel) {
        customerCarRelDao.insert(customerCarRel);
    }

    @Override
    public void delete(AccountAndCarBO accountAndCarBO) {
        Map param = Maps.newHashMap();
        param.put("shopId", accountAndCarBO.getShopId());
        param.put("accountId", accountAndCarBO.getAccountId());
        param.put("customerId", accountAndCarBO.getCustomerId());
        param.put("customerCarId", accountAndCarBO.getCustomerCarId());
        customerCarRelDao.delete(param);
    }

    @Override
    public List<CustomerCarRel> selectByCustomerId(Long shopId, Long customerId) {
        Assert.notNull(shopId,"shopId不能为空.");
        Assert.notNull(customerId,"customerId不能为空.");
        Map<String, Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("customerId", customerId);
        return customerCarRelDao.select(param);
    }

    @Override
    public List<CustomerCarRel> findCustomerCarRelByAccountId(Long shopId, Long accountId) {
        Assert.notNull(shopId,"shopId不能为空.");
        Assert.notNull(accountId,"accountId不能为空.");
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        return customerCarRelDao.select(param);
    }
}
