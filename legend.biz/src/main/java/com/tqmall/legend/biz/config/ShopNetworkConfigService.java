package com.tqmall.legend.biz.config;

import com.tqmall.legend.entity.config.ShopNetworkConfig;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by twg on 16/9/6.
 */
public interface ShopNetworkConfigService {
    /**
     * 获取指定门店的网络设置信息
     * @param shopId
     * @return
     */
    List<ShopNetworkConfig> getShopNetworkConfigs(@NotNull Long shopId);

    /**
     * 获取门店网络配置
     * @param shopId
     * @param macAddress
     * @return
     */
    ShopNetworkConfig getShopNetworkConfig(Long shopId , String macAddress);

    /**
     * 插入门店网络配置记录
     * @param shopNetworkConfig
     * @return
     */
    void insertNetConfig(ShopNetworkConfig shopNetworkConfig);

    /**
     *  删除门店网络配置
     * @param configId
     * @param shopId
     * @return
     */
    void deleteNetConfig(Long configId,Long shopId);
}
