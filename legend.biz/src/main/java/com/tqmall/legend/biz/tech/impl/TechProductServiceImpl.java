package com.tqmall.legend.biz.tech.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.tech.TechProductService;
import com.tqmall.legend.dao.tech.TechProductDao;
import com.tqmall.legend.entity.tech.TechProduct;
import com.tqmall.legend.entity.tech.TechProductPositionEnum;
import com.tqmall.legend.entity.tech.TechProductStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/4/30.
 */
@Slf4j
@Service
public class TechProductServiceImpl extends BaseServiceImpl implements TechProductService {
    @Autowired
    TechProductDao techProductDao;

    @Override
    public TechProduct selectById(Long id) {
        return techProductDao.selectById(id);
    }

    @Override
    public List<TechProduct> select(Map<String, Object> searchParams) {
        return techProductDao.select(searchParams);
    }

    /**
     * 获取登录后技术中心推荐产品
     *
     * @return
     */
    @Override
    public List<TechProduct> getTechProductForTech() {
        Map<String, Object> techProductMap = this.getMap(TechProductPositionEnum.TECH.getCode());
        List<TechProduct> techProductList = techProductDao.select(techProductMap);
        return techProductList;
    }

    /**
     * 获取首页技术中心推荐产品
     *
     * @return
     */
    @Override
    public List<TechProduct> getTechProductForIndex() {
        Map<String, Object> techProductMap = this.getMap(TechProductPositionEnum.INDEX.getCode());
        List<TechProduct> techProductList = null;
        try {
            techProductList = techProductDao.select(techProductMap);
        } catch (Exception e) {
            log.error("技术中心-获取推荐产品 失败, 原因:{}", e);
            techProductList = new ArrayList<TechProduct>();
        }
        return techProductList;
    }

    /**
     * 获取条件查询map
     */
    public Map<String, Object> getMap(Integer type) {
        Map<String, Object> techProductMap = new HashMap<>();
        techProductMap.put("status", TechProductStatusEnum.SHOW.getCode());
        if (TechProductPositionEnum.TECH.getCode().equals(type)) {
            techProductMap.put("position", TechProductPositionEnum.TECH.getCode());
        } else {
            techProductMap.put("position", TechProductPositionEnum.INDEX.getCode());
        }
        List<String> sorts = new ArrayList<>();
        sorts.add("sort asc");
        sorts.add("gmt_create desc");
        techProductMap.put("sorts", sorts);
        return techProductMap;
    }
}