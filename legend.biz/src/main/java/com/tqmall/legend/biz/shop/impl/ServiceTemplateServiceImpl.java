package com.tqmall.legend.biz.shop.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.dao.shop.ServiceTemplateDao;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/09/17.
 */
@Service
public class ServiceTemplateServiceImpl extends BaseServiceImpl implements ServiceTemplateService {
    @Autowired
    private ServiceTemplateDao serviceTemplateDao;


    Logger logger = LoggerFactory.getLogger(ServiceTemplateServiceImpl.class);

    @Override
    public List<ServiceTemplate> select(Map<String, Object> searchParams) {
        List<ServiceTemplate> serviceTemplateList = serviceTemplateDao.select(searchParams);
        return serviceTemplateList;
    }

    @Override
    public ServiceTemplate selectById(Long id) {
        ServiceTemplate serviceTemplate = serviceTemplateDao.selectById(id);
        return serviceTemplate;
    }

    @Override
    public Integer selectCount(Map<String, Object> searchMap) {
        return serviceTemplateDao.selectCount(searchMap);
    }
}
