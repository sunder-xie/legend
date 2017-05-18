package com.tqmall.legend.biz.activity.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.activity.BannerConfigService;
import com.tqmall.legend.dao.activity.BannerConfigDao;
import com.tqmall.legend.entity.activity.BannerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/2/24.
 */
@Service
public class BannerConfigServiceImpl implements BannerConfigService {

    @Autowired
    private BannerConfigDao bannerConfigDao;

    public List<BannerConfig> getListByPostion(Integer position) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("position", position);
        List<String> sorts = new ArrayList<>();
        sorts.add(" sort desc ");
        searchMap.put("sorts", sorts);
        return bannerConfigDao.select(searchMap);
    }
}
