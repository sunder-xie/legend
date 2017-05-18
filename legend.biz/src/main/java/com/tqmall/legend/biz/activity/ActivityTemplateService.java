package com.tqmall.legend.biz.activity;

import com.tqmall.legend.entity.activity.ActivityTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/2/24.
 */
public interface ActivityTemplateService {

    /**
     * 获取有效的活动列表
     *
     * @param shopId
     * @return
     */
    List<ActivityTemplate> getValidActivityList(Long shopId);

    /**
     * 获取所有需要报名的活动列表
     *
     * @param shopId
     * @return
     */
    List<ActivityTemplate> getActivityAllList(Long shopId);

    /**
     * 根据ID获取活动模板
     *
     * @param actTplId
     * @return
     */
    ActivityTemplate getById(Long actTplId);

    /**
     * 查询所有模板
     *
     * @param map
     * @return
     */

    List<ActivityTemplate> select(Map<String, Object> map);

    /**
     * 计数查询
     * @param searchMap
     * @return
     */
    Integer selectCount(Map<String, Object> searchMap);

    /**
     *  根据Id删除
     * @param activityTemplateId
     * @return
     */
    int deleteById(Long activityTemplateId);

    /**
     * 更新
     * @param activityTemplate
     * @return
     */
    Integer update(ActivityTemplate activityTemplate);

    /**
     * 新增
     * @param activityTemplate
     * @return
     */
    Integer add(ActivityTemplate activityTemplate);
}
