package com.tqmall.legend.dao.config;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.config.ShopNetworkConfig;

import java.util.List;

/**
 * Created by zsy on 16/9/6.
 */
@MyBatisRepository
public interface ShopNetworkConfigDao extends BaseDao<ShopNetworkConfig> {
    List<ShopNetworkConfig> getShopNetworkConfigs(Long shopId);

}
