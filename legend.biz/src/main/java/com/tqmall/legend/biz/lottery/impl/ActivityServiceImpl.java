package com.tqmall.legend.biz.lottery.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.base.Optional;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.lottery.IActivityService;
import com.tqmall.legend.dao.lottery.ActivityDao;
import com.tqmall.legend.entity.lottery.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dongc on 15/10/27.
 */
@Service
public class ActivityServiceImpl extends BaseServiceImpl implements IActivityService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    ActivityDao activityDao;

    @Override
    public Optional<List<Activity>> getCurrentActivity() {
        List<Activity> activityList = null;
        try {
            activityList = activityDao.getCurrentActivity();
            if (CollectionUtils.isEmpty(activityList)) {
                return Optional.absent();
            }
        } catch (Exception e) {
            LOGGER.error("获取当前有效活动异常，异常信息:{}", e);
            return Optional.absent();
        }
        return Optional.fromNullable(activityList);
    }

    @Override
    public Optional<Activity> getActivity(Long activityId) {

        Activity activity = null;
        try {
            activity = activityDao.selectById(activityId);
        } catch (Exception e) {
            LOGGER.error("[DB]获取活动异常，活动ID:{} 异常信息:{}", activityId, e);
            return Optional.absent();
        }

        return Optional.fromNullable(activity);
    }

    @Override
    public boolean checkActivityIsValid(Long activityId) {
        Activity activity = activityDao.checkvalid(activityId);
        if (activity != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
