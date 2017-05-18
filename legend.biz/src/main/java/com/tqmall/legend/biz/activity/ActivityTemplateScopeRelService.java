package com.tqmall.legend.biz.activity;

import com.tqmall.legend.entity.activity.ActivityTemplateScopeRel;

import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/5/10.
 */
public interface ActivityTemplateScopeRelService {
    void insert(ActivityTemplateScopeRel activityTemplateScopeRel);
    int selectCount(Map<String, Object> map);

    int batchInsert(List<ActivityTemplateScopeRel> activityTemplateScopeRelList);

    Integer delete(Map<String,Object> param);

    List<ActivityTemplateScopeRel> select(Map<String, Object> param);
}
