package com.tqmall.legend.biz.activity.impl;

import com.tqmall.legend.biz.activity.ActivityTemplateServiceRelService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.activity.ActivityTemplateServiceRelDao;
import com.tqmall.legend.entity.activity.ActivityTemplateServiceRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/3/26.
 */
@Service
public class ActivityTemplateServiceRelServiceImpl extends BaseServiceImpl implements ActivityTemplateServiceRelService {
    @Autowired
    private ActivityTemplateServiceRelDao activityTemplateServiceRelDao;

    @Override
    public List<ActivityTemplateServiceRel> select(Map<String, Object> searchParams) {
        List<ActivityTemplateServiceRel> activityTemplateServiceRelList = activityTemplateServiceRelDao.select(searchParams);
        return activityTemplateServiceRelList;
    }

    @Override
    public Integer delete(Map<String, Object> param) {
        if(param==null|| param.isEmpty()){
            return 0;
        }
        return activityTemplateServiceRelDao.delete(param);
    }

    @Override
    public Integer batchInsert(List<ActivityTemplateServiceRel> activityTemplateServiceRelList) {
        return activityTemplateServiceRelDao.batchInsert(activityTemplateServiceRelList);
    }
}
