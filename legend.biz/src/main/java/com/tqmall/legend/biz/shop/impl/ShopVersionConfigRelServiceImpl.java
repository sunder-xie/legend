package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.shop.ShopVersionConfigRelService;
import com.tqmall.legend.dao.shop.ShopVersionConfigRelDao;
import com.tqmall.legend.entity.shop.ShopVersionConfigRel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 2017/1/10.
 */
@Service
public class ShopVersionConfigRelServiceImpl implements ShopVersionConfigRelService {
    @Resource
    private ShopVersionConfigRelDao shopVersionConfigRelDao;

    @Override
    public List<ShopVersionConfigRel> selectByShopIdAndIds(Long shopId, List<Long> configIds) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("configIds", configIds);
        List<ShopVersionConfigRel> relList = shopVersionConfigRelDao.select(param);
        if (relList == null) {
            return Lists.newArrayList();
        }
        return relList;
    }
}
