package com.tqmall.legend.facade.precheck;

import com.tqmall.legend.entity.precheck.PrecheckDetails;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/7/9.
 */
public interface PrechecksFacade {
    /**
     * 预检单新增/编辑共用代码
     *
     * @param appearance
     * @param precheckOtherDetail
     * @param goodsInCar
     * @param customerRequest
     * @param shopId
     * @return
     */
    Map<String, Object> addAndUpdateShare(String appearance, String precheckOtherDetail, String goodsInCar, String customerRequest, Long shopId);

    /**
     * 获取预检信息的值
     */
    Map<Long, Map<String, String>> getPrecheckItemValuesMap();

    /**
     * 获取预检信息的key
     * @return
     */
    Map<Long, Map<String, String>> getPrecheckItemsMap();

    /**
     * 获取预检详情
     *
     * @param tmpItemsAppearance
     * @param tmpItemsOtherDetail
     * @param goodsInCarMap
     * @param shopId
     * @return
     */
    List<PrecheckDetails> getItemList(Map<String, String> tmpItemsAppearance, Map<String, String> tmpItemsOtherDetail, Map<String, Boolean> goodsInCarMap, Long shopId);

    /**
     * 清理缓存
     *
     * @return
     */
    boolean clearPrecheckRedis();
}
