package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.AppActServiceRel;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/10/28.
 */
public interface AppActServiceRelService {

    /**
     * 获取车主活动与服务信息
     *
     * @param param 入参
     * @return
     */
    List<AppActServiceRel> select(Map<String, Object> param);

}
