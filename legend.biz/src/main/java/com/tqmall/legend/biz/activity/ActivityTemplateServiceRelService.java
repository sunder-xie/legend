package com.tqmall.legend.biz.activity;

import com.tqmall.legend.entity.activity.ActivityTemplateServiceRel;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/3/26.
 */
public interface ActivityTemplateServiceRelService {

    public List<ActivityTemplateServiceRel> select(Map<String,Object> searchParams);

    Integer delete(Map<String, Object> param);

    Integer batchInsert(List<ActivityTemplateServiceRel> activityTemplateServiceRelList);
}
