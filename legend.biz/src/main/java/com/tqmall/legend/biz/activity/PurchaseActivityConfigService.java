package com.tqmall.legend.biz.activity;

import com.tqmall.legend.entity.activity.PurchaseActivityConfig;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
public interface PurchaseActivityConfigService {

    /**
     * 插入活动配置
     * @param config
     * @return
     */
    Integer insert(PurchaseActivityConfig config);

    /**
     * 修改活动配置
     * @param config
     * @return
     */
    Integer update(PurchaseActivityConfig config);

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
    List<PurchaseActivityConfig> query(PurchaseActivityConfig config);

    /**
     * 获取活动数量
     * @param config
     * @return
     */
    Integer selectCount(PurchaseActivityConfig config);

    /**
     * 根据id获取数据
     * @param id
     * @return
     */
    PurchaseActivityConfig queryById(Long id);

    /**
     * 查询所有的活动配置
     * @return
     */
    List<PurchaseActivityConfig> getAllActivity();
}
