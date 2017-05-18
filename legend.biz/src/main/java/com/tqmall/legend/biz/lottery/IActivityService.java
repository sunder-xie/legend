package com.tqmall.legend.biz.lottery;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.lottery.Activity;

import java.util.List;

/**
 * Created by dongc on 15/10/27.
 */
public interface IActivityService extends BaseService{

    /**
     * 获取当前有效活动
     * 备注：可能多个
     * 规则：startTime <= SYSDATE <= endTime AND ID ASC
     *
     * @return
     */
    Optional<List<Activity>> getCurrentActivity();

    /**
     * 获取活动
     *
     * @param activityId 活动ID
     * @return Optional<Activity>
     */
    Optional<Activity> getActivity(Long activityId);

    /**
     * 判断活动是否有效
     *
     * @param activityId 活动ID
     * @return defalut false
     */
    boolean checkActivityIsValid(Long activityId);
}
