package com.tqmall.legend.biz.shop.impl;

/**
 * Created by jason on 15/8/24.
 */

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ServiceTemplateCateRelService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.shop.ServiceTemplateCateRelDao;
import com.tqmall.legend.entity.shop.ServiceTemplateCateRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceTemplateCateRelServiceImpl extends BaseServiceImpl implements ServiceTemplateCateRelService {

    @Autowired
    private ServiceTemplateCateRelDao serviceTemplateCateRelDao;


    public List<ServiceTemplateCateRel> select(Map<String, Object> searchMap) {

        return serviceTemplateCateRelDao.select(searchMap);
    }

    @Override
    public Result add(ServiceTemplateCateRel serviceTemplateCateRel) {
        return null;
    }

    @Override
    public Integer update(ServiceTemplateCateRel serviceTemplateCateRel) {
        return null;
    }

    @Override
    public Integer deleteById(Long id) {
        return null;
    }

    @Override
    public ServiceTemplateCateRel selectById(Long id) {
        return null;
    }

    @Override
    public Page<ServiceTemplateCateRel> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return null;
    }

    @Override
    public Map<Long, List<ServiceTemplateCateRel>> getServiceTemplateCateRel(Map<String, Object> searchMap) {
        List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelDao.select(searchMap);
        Map<Long, List<ServiceTemplateCateRel>> returnMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
            for (ServiceTemplateCateRel serviceTemplateCateRel : serviceTemplateCateRelList) {
                Long templateId = serviceTemplateCateRel.getTemplateId();
                if (returnMap.containsKey(templateId)) {
                    List<ServiceTemplateCateRel> tempList = returnMap.get(templateId);
                    tempList.add(serviceTemplateCateRel);
                } else {
                    List<ServiceTemplateCateRel> tempList = new ArrayList<>();
                    tempList.add(serviceTemplateCateRel);
                    returnMap.put(templateId, tempList);
                }
            }
        }
        return returnMap;
    }
}

