package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.shop.ShopTagService;
import com.tqmall.legend.dao.shop.ShopTagDao;
import com.tqmall.legend.entity.shop.ShopTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/12/14.
 */
@Service
public class ShopTagServiceImpl implements ShopTagService {
    @Autowired
    private ShopTagDao shopTagDao;

    @Override
    public ShopTag selectByTagCode(String tagCode) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("tagCode", tagCode);
        List<ShopTag> shopTagList = shopTagDao.select(searchMap);
        if(CollectionUtils.isEmpty(shopTagList)){
            return null;
        }
        return shopTagList.get(0);
    }
}
