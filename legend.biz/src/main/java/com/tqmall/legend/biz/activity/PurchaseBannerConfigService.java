package com.tqmall.legend.biz.activity;


import com.tqmall.legend.entity.activity.PurchaseBannerConfig;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
public interface PurchaseBannerConfigService {
    /**
     * 插入活动配置
     * @param config
     * @return
     */
    Integer insert(PurchaseBannerConfig config);

    /**
     * 修改活动配置
     * @param config
     * @return
     */
    Integer update(PurchaseBannerConfig config);

    /**
     * 根据id删除配置活动
     * @param id
     * @return
     */
    Integer deleteById(Long id);

    /**
     * 查询活动配置
     * @param config
     * @return
     */
    List<PurchaseBannerConfig> query(PurchaseBannerConfig config);

    /**
     * 获取banner数量
     * @param config
     * @return
     */
    Integer selectCount(PurchaseBannerConfig config);

    /**
     * 根据id获取数据
     * @param id
     * @return
     */
    PurchaseBannerConfig queryById(Long id);

    /**
     * 获取门店展示bannerlist
     * @param cityId
     * @param shopId
     * @param shopType
     * @return
     */
    List<PurchaseBannerConfig> queryBannerList(Long cityId,Long shopId, Integer shopType);
}
