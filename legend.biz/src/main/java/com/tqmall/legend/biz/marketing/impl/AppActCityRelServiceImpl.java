package com.tqmall.legend.biz.marketing.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.AppActCityRelService;
import com.tqmall.legend.dao.marketing.AppActCityRelDao;
import com.tqmall.legend.entity.marketing.AppActCityRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/4.
 */
@Service
public class AppActCityRelServiceImpl extends BaseServiceImpl implements AppActCityRelService {

    @Autowired
    private AppActCityRelDao actCityRelDao;

    @Override
    public List<AppActCityRel> select(Map param) {
        return actCityRelDao.select(param);
    }
}
