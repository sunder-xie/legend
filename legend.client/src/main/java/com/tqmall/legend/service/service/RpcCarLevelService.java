package com.tqmall.legend.service.service;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.service.CarLevelDTO;

import java.util.List;

/**
 * Created by zsy on 16/11/22.
 * 服务车辆级别
 */
public interface RpcCarLevelService {
    /**
     * 根据门店id获取车辆级别
     *
     * @param shopId
     * @param nameLike 车辆级别名称模糊查询，可不传
     * @return
     */
    Result<List<CarLevelDTO>> getCarLevelList(Long shopId, String nameLike);
}
