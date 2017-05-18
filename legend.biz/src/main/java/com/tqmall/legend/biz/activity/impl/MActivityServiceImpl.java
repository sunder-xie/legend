package com.tqmall.legend.biz.activity.impl;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.activity.MActivityService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.lottery.ActivityDao;
import com.tqmall.legend.entity.lottery.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@Service
@Slf4j
public class MActivityServiceImpl extends BaseServiceImpl implements MActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Override
    public Activity getActivityWithDrawTime() {
        Map<String, Object> param = new HashMap<>();
        param.put("currentWithdrawTime", DateUtil.convertDateToYMDHMS(new Date()));
        param.put("sorts", new String[] { "withdraw_start_time asc" });
        List<Activity> activityList = activityDao.select(param);
        if (!CollectionUtils.isEmpty(activityList)) {
            return activityList.get(0);
        }
        return null;
    }
}
