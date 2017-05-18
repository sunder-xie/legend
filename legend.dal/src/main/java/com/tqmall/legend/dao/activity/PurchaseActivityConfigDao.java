package com.tqmall.legend.dao.activity;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.activity.PurchaseActivityConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
@MyBatisRepository
public interface PurchaseActivityConfigDao extends BaseDao<PurchaseActivityConfig> {

    /**
     * 根据对象参数查询
     * @return
     */
    List<PurchaseActivityConfig> queryByParam(@Param("activityName") String activityName, @Param("activityType") Integer activityType, @Param("optType") String optType, @Param("limit") Integer limit, @Param("offset") Integer offset);

    /**
     * 获取活动数量
     * @return
     */
    Integer selectCountByParam(@Param("activityName") String activityName, @Param("activityType") Integer activityType, @Param("optType") String optType);
}
