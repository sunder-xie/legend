package com.tqmall.legend.biz.customer.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerUserRelService;
import com.tqmall.legend.dao.customer.CustomerUserRelDao;
import com.tqmall.legend.entity.customer.CustomerUserRel;
import com.tqmall.legend.facade.customer.bo.UnbindingBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/12/15.
 */
@Slf4j
@Service
public class CustomerUserRelServiceImpl extends BaseServiceImpl implements CustomerUserRelService {

    @Autowired
    private CustomerUserRelDao customerUserRelDao;

    @Override
    public List<CustomerUserRel> selectByUserId(Long userId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("userId", userId);
        return customerUserRelDao.select(searchMap);
    }

    @Override
    public List<CustomerUserRel> selectByUserIds(List<Long> userIds) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("userIds", userIds);
        return customerUserRelDao.select(searchMap);
    }

    @Override
    public List<CustomerUserRel> selectByShopIdAndCustomerCarIds(Long shopId, List<Long> customerCarIds) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("customerCarIds", customerCarIds);
        return customerUserRelDao.select(searchMap);
    }

    @Override
    public CustomerUserRel selectByCustomerCarId(Long customerCarId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("customerCarId", customerCarId);
        List<CustomerUserRel> customerUserRelList = customerUserRelDao.select(searchMap);
        if (!CollectionUtils.isEmpty(customerUserRelList)) {
            return customerUserRelList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer selectCount(Long shopId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        return customerUserRelDao.selectCount(searchMap);
    }

    @Override
    public Integer selectCount(Long shopId, Long userId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("userId", userId);
        return customerUserRelDao.selectCount(searchMap);
    }

    @Override
    public Integer batchInsert(List<CustomerUserRel> customerUserRelList) {
        Integer totalSize = super.batchInsert(customerUserRelDao, customerUserRelList, 300);
        return totalSize;
    }

    @Override
    public Integer batchDelete(UnbindingBo unbindingBo) {
        Map<String, Object> deleteMap = Maps.newHashMap();
        deleteMap.put("shopId", unbindingBo.getShopId());
        Long userId = unbindingBo.getUserId();
        deleteMap.put("userId", userId);
        deleteMap.put("modifier", unbindingBo.getOperatorId());
        List<Long> customerCarIds = unbindingBo.getCustomerCarIds();
        if (!CollectionUtils.isEmpty(customerCarIds)) {
            //全部解绑
            deleteMap.put("customerCarIds", customerCarIds);
        }
        return customerUserRelDao.batchDelete(deleteMap);
    }

    @Override
    public List<Long> selectAllotUserIds(Long shopId) {
        return customerUserRelDao.selectAllotUserIds(shopId);
    }

    @Override
    public int insert(CustomerUserRel customerUserRel) {
        return customerUserRelDao.insert(customerUserRel);
    }

    @Override
    public Map<Long, Integer> selectAllotUserNumMapByUserIds(Long shopId, Long[] userIds) {
        Map<Long, Integer> resultMap = Maps.newHashMap();
        if (shopId == null || userIds == null) {
            return resultMap;
        }
        for (Long userId : userIds) {
            Integer count = selectCount(shopId, userId);
            resultMap.put(userId, count);
        }
        return resultMap;
    }
}
