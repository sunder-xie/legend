package com.tqmall.legend.biz.config.impl;

import com.tqmall.legend.biz.config.ShopNetworkConfigService;
import com.tqmall.legend.dao.config.ShopNetworkConfigDao;
import com.tqmall.legend.entity.config.ShopNetworkConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/9/6.
 */
@Service
@Slf4j
public class ShopNetworkConfigServiceImpl implements ShopNetworkConfigService {
    @Autowired
    private ShopNetworkConfigDao shopNetworkConfigDao;

    @Override
    public List<ShopNetworkConfig> getShopNetworkConfigs(@NotNull Long shopId) {
        return shopNetworkConfigDao.getShopNetworkConfigs(shopId);
    }

    /**
     * 获取门店网络配置
     *
     * @param shopId
     * @param macAddress
     * @return
     */
    @Override
    public ShopNetworkConfig getShopNetworkConfig(Long shopId, String macAddress) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("macAddress", macAddress);
        List<ShopNetworkConfig> configList = shopNetworkConfigDao.select(param);
        if (CollectionUtils.isEmpty(configList)) {
            return null;
        }
        return configList.get(0);
    }

    /**
     * 插入门店网络配置记录
     *
     * @param shopNetworkConfig
     * @return
     */
    @Override
    public void insertNetConfig(ShopNetworkConfig shopNetworkConfig) {
        shopNetworkConfigDao.insert(shopNetworkConfig);
    }

    /**
     * 删除门店网络配置
     *
     * @param configId
     * @param shopId
     * @return
     */
    @Override
    public void deleteNetConfig(Long configId, Long shopId) {
        log.info("[删除门店网络配置]configId:"+configId);
        shopNetworkConfigDao.deleteById(configId);
    }
}
