package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.AppActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/10/28.
 */
public interface AppActivityService {

    /**
     * 获取车主活动信息List
     *
     * @param param 入参
     * @return
     */
     List<AppActivity> select(Map<String, Object> param);

    /**
     * 获取车主活动信息
     *
     * @param param 入参
     * @return AppActivity
     */
     AppActivity warpActivity(Map<String, Object> param);



}
