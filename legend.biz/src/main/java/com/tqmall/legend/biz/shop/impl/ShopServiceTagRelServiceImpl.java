package com.tqmall.legend.biz.shop.impl;

/**
 * Created by jason on 15/8/24.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tqmall.legend.biz.shop.ShopServiceTagRelService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.shop.ShopServiceTagDao;
import com.tqmall.legend.entity.shop.ShopServiceTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.shop.ShopServiceTagRelDao;
import com.tqmall.legend.entity.shop.ShopServiceTagRel;
import org.springframework.util.CollectionUtils;

@Service
public class ShopServiceTagRelServiceImpl extends BaseServiceImpl implements ShopServiceTagRelService{

    Logger logger = LoggerFactory.getLogger(ShopServiceTagRelServiceImpl.class);

    @Autowired
    private ShopServiceTagRelDao shopServiceTagRelDao;

    @Autowired
    private ShopServiceTagDao shopServiceTagDao;

    @Override
    public List<ShopServiceTagRel> select(Map<String, Object> searchMap) {

        return shopServiceTagRelDao.select(searchMap);
    }

    /**
     * create by jason 2015-08-25
     * 组装服务tag
     */
    @Override
    public Map<Integer,List<ShopServiceTag>> getServiceFlag(Map<String, Object> searchMap) {

        //根据服务ID 获得服务标识
        List<ShopServiceTagRel> serviceTagRelList = shopServiceTagRelDao.select(searchMap);
        if (!CollectionUtils.isEmpty(serviceTagRelList)) {

            //服务tagId List
            List<Long> tagIdList = new ArrayList<>();
            for (ShopServiceTagRel tagRel : serviceTagRelList) {
                tagIdList.add(Long.valueOf(tagRel.getTagId()));
            }
            if (!CollectionUtils.isEmpty(tagIdList)) {

                Long[] tagIds = (Long[])tagIdList.toArray(new Long[tagIdList.size()]);
                //根据tagId 获得服务tag对象List
                List<ShopServiceTag> serviceTagList = shopServiceTagDao.selectByIds(tagIds);
                if (!CollectionUtils.isEmpty(serviceTagList)) {

                    Map<Integer,List<ShopServiceTag>> map = new HashMap<>();
                    List<ShopServiceTag> marketingFlagList = new ArrayList<>();
                    List<ShopServiceTag> qualityFlagList = new ArrayList<>();
                    for (ShopServiceTag serviceTag : serviceTagList) {

                        //0品质标,1营销标
                        Integer type = serviceTag.getType();
                        if (0 == type) {
                            qualityFlagList.add(serviceTag);
                        } else if (1 == type) {
                            marketingFlagList.add(serviceTag);
                        }
                    }
                    map.put(0,qualityFlagList);
                    map.put(1,marketingFlagList);
                    return map;
                }
            }
        }
        return null;
    }




    @Override
    public Result add(ShopServiceTagRel shopServiceTagRel) {
        return null;
    }

    @Override
    public Integer update(ShopServiceTagRel shopServiceTagRel) {
        return null;
    }

    @Override
    public Integer deleteById(Long id) {
        return null;
    }

    @Override
    public ShopServiceTagRel selectById(Long id) {
        return null;
    }

    @Override
    public Page<ShopServiceTagRel> getPage(Pageable pageable, Map<String, Object> searchParams) {
        return null;
    }
}

