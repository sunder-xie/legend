package com.tqmall.legend.biz.account.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.ComboInfoServiceRelService;
import com.tqmall.legend.dao.account.ComboInfoServiceRelDao;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;

/**
 * Created by majian on 16/6/20.
 */
@Service
@Slf4j
public class ComboInfoServiceRelServiceImpl implements ComboInfoServiceRelService {
    @Autowired
    private ComboInfoServiceRelDao comboInfoServiceRelDao;

    @Override
    public List<ComboInfoServiceRel> findByComboInfoId(Long shopId, Long comboInfoId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        if (comboInfoId != null) {
            param.put("comboInfoId", comboInfoId);
        }
        return comboInfoServiceRelDao.select(param);
    }

    @Override
    public List<ComboInfoServiceRel> findByComboInfoIds(Long shopId, List<Long> comboInfoIds) {
        return comboInfoServiceRelDao.findByComboInfoIds(shopId, comboInfoIds);
    }

    @Override
    public List<ComboInfoServiceRel> findByServiceNames(Long shopId, List<String> serviceNames) {
        return comboInfoServiceRelDao.findByServiceNames(shopId, serviceNames);
    }

    @Override
    public List<ComboInfoServiceRel> findByServiceId(long serviceId) {
        Map<String,Object> params = new HashMap<>();
        params.put("serviceId",serviceId);
        List<ComboInfoServiceRel> comboInfoServiceRelList = comboInfoServiceRelDao.select(params);
        return comboInfoServiceRelList;
    }
}
