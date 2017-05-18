package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.CarLevel;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2014-11-09 17:09
 */
public interface CarLevelService {

    public List<CarLevel> selectNoCache(Map<String,Object> searchMap);

    /**
     * 通过门店id和车辆级别名，获取id
     * @param shopId 门店Id
     * @param userId 用户Id
     * @param carLevelName 车辆级别名
     * @return
     */
    Long getCarLevelId(@NotNull Long shopId,@NotNull Long userId,@NotNull String carLevelName);

    /**
     * 获取门店的服务车辆级别，没有则新增
     */
    CarLevel getCarLevelByShopIdAndName(Long shopId,String name);

    /**
     * 根据门店id、车辆级别名，获取信息
     * @param shopId 门店id
     * @param names 车辆级别名
     * @return
     */
    List<CarLevel> findCarLevelsByNames(Long shopId,String... names);

    void batchSave(List<CarLevel> carLevels);
}
