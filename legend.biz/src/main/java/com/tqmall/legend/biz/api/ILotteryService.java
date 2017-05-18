package com.tqmall.legend.biz.api;

import com.tqmall.legend.common.Result;

/**
 * Lottery service interface
 * <p/>
 * Created by dongc on 15/10/27.
 */
public interface ILotteryService {

    /**
     * 获取当前有效活动
     * 规则：startTime <= SYSDATE <= endTime AND ID DESC
     *
     * @return 返回JSON
     */
    Result getCurrentOnlineActivity();

    /**
     * 获取活动
     *
     * @param activityId 活动ID
     * @return 活动JSON
     */
    Result getActivity(Long activityId);

}
