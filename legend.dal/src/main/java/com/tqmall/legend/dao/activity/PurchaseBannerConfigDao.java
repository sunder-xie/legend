package com.tqmall.legend.dao.activity;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.activity.PurchaseBannerConfig;

import java.util.List;

@MyBatisRepository
public interface PurchaseBannerConfigDao extends BaseDao<PurchaseBannerConfig> {

    /**
     * 根据参数获取列表
     * @param config
     * @return
     */
    List<PurchaseBannerConfig> queryByParam(PurchaseBannerConfig config);

    /**
     * 根据参数获取数量
     * @param config
     * @return
     */
    Integer selectCountByParam(PurchaseBannerConfig config);

    /**
     * 查询有起始时间的banner列表
     * @param config
     * @return
     */
    List<PurchaseBannerConfig> queryByParamWithStartTime(PurchaseBannerConfig config);

}
