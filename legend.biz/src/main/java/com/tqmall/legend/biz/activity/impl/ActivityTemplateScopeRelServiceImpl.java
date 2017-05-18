package com.tqmall.legend.biz.activity.impl;

import com.tqmall.legend.biz.activity.ActivityTemplateScopeRelService;
import com.tqmall.legend.dao.activity.ActivityTemplateScopeRelDao;
import com.tqmall.legend.entity.activity.ActivityTemplateScopeRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/5/10.
 */
@Service("activityTemplateScopeRelService")
public class ActivityTemplateScopeRelServiceImpl implements ActivityTemplateScopeRelService {
    @Autowired
    private ActivityTemplateScopeRelDao activityTemplateScopeRelDao;

    @Override
    public int selectCount(Map<String, Object> map) {
        return activityTemplateScopeRelDao.selectCount(map);
    }

    @Override
    public void insert(ActivityTemplateScopeRel activityTemplateScopeRel) {
        activityTemplateScopeRelDao.insert(activityTemplateScopeRel);
    }

    @Override
    public int batchInsert(List<ActivityTemplateScopeRel> activityTemplateScopeRelList) {
        return activityTemplateScopeRelDao.batchInsert(activityTemplateScopeRelList);
    }

    @Override
    public Integer delete(Map<String,Object> param) {
        if(param.isEmpty()) {
            return 0;
        }
        return activityTemplateScopeRelDao.delete(param);
    }

    @Override
    public List<ActivityTemplateScopeRel> select(Map<String, Object> param) {
        return activityTemplateScopeRelDao.select(param);
    }
}
