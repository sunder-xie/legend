package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.AppActCityRel;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/4.
 */
public interface AppActCityRelService {

    /**
     * 车主活动商品与车型对应表
     * @param param 入参
     * @return
     */
    public List<AppActCityRel> select(Map param);

}
