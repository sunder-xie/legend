package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.SuiteCouponRelService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.SuiteCouponRelDao;
import com.tqmall.legend.entity.account.SuiteCouponRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class SuiteCouponRelServiceImpl extends BaseServiceImpl implements SuiteCouponRelService {
    @Autowired
    private SuiteCouponRelDao suiteCouponRelDao;

    @Override
    public Integer insert(SuiteCouponRel suiteCouponRel){
        return suiteCouponRelDao.insert(suiteCouponRel);
    }

    @Override
    public List<SuiteCouponRel> select(Map param){
        return suiteCouponRelDao.select(param);
    }

    @Override
    public List<SuiteCouponRel> findBySuitId(Long shopId, Long suitId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("suitId", suitId);
        return suiteCouponRelDao.select(param);
    }

}
