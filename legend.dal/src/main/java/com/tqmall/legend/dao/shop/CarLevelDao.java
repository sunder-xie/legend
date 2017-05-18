package com.tqmall.legend.dao.shop;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.shop.CarLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface CarLevelDao extends BaseDao<CarLevel> {
    /**
     * 根据门店id、车辆级别名，获取信息
     * @param shopId 门店id
     * @param names 车辆级别名
     * @return
     */
    List<CarLevel> findCarLevelsByNames(@Param("shopId")Long shopId,@Param("names")String... names);
}
