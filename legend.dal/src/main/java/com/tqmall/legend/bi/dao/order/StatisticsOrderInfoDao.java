package com.tqmall.legend.bi.dao.order;

import com.tqmall.legend.dao.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by zsy on 16/12/9.
 */
@MyBatisRepository
public interface StatisticsOrderInfoDao {
    /**
     * 获取车辆最近一次到店时间
     *
     * @param shopId
     * @param customerCarId
     * @return
     */
    Date getLastOrderDate(@Param(value = "shopId") Long shopId, @Param(value = "customerCarId") Long customerCarId);
}
