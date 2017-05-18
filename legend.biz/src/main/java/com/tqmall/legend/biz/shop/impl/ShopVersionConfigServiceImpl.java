package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ShopVersionConfigService;
import com.tqmall.legend.dao.shop.ShopVersionConfigDao;
import com.tqmall.legend.entity.shop.ShopVersionConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by sven on 17/01/10.
 */
@Service
public class ShopVersionConfigServiceImpl extends BaseServiceImpl implements ShopVersionConfigService {
    @Autowired
    private ShopVersionConfigDao shopVersionConfigDao;

    @Override
    public List<ShopVersionConfig> select() {

        List<ShopVersionConfig> shopVersionConfigs = shopVersionConfigDao.select(null);
        if (shopVersionConfigs == null) {
            return Lists.newArrayList();
        }
        return shopVersionConfigs;
    }
}
