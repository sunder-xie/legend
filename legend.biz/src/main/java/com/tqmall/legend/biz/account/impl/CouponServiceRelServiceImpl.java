package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.CouponServiceRelService;
import com.tqmall.legend.dao.account.CouponServiceRelDao;
import com.tqmall.legend.entity.account.CouponServiceRel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/9/30.
 */
@Service
public class CouponServiceRelServiceImpl implements CouponServiceRelService {
    @Autowired
    private CouponServiceRelDao serviceRelDao;

    @Override
    public List<CouponServiceRel> list(Long couponInfoId, Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("couponInfoId", couponInfoId);
        return serviceRelDao.select(param);
    }

    @Override
    public List<CouponServiceRel> list(Long shopId, Collection<Long> couponInfoIds) {
        List<CouponServiceRel> couponServiceRelList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(couponInfoIds)) {
            return couponServiceRelList;
        }
        return serviceRelDao.selectByCouponInfoIds(shopId, couponInfoIds);
    }

    @Override
    public void save(List<CouponServiceRel> serviceRels) {
        serviceRelDao.batchInsert(serviceRels);
    }

    @Override
    public void deleteByCouponInfoId(Long shopId, Long couponInfoId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("couponInfoId", couponInfoId);
        serviceRelDao.delete(param);
    }

    @Override
    public List<CouponServiceRel> selectByServiceId(long serviceId) {
        Map<String,Object> params = new HashMap<>();
        params.put("serviceId",serviceId);
        return serviceRelDao.select(params);
    }
}
