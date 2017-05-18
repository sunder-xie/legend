package com.tqmall.legend.biz.marketing.impl;


import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.AppActivityService;
import com.tqmall.legend.dao.marketing.AppActCityRelDao;
import com.tqmall.legend.dao.marketing.AppActivityDao;
import com.tqmall.legend.entity.marketing.AppActCityRel;
import com.tqmall.legend.entity.marketing.AppActivity;
import com.tqmall.legend.entity.marketing.AppActivityCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppActivityServiceImpl extends BaseServiceImpl implements AppActivityService{

    @Autowired
    private AppActivityDao appActivityDao;

    @Autowired
    private AppActCityRelDao actCityRelDao;

    public List<AppActivity> select(Map param) {
        return appActivityDao.select(param);
    }

    /**
     * create by jason 2015-11-04
     * 组装活动信息
     */
    public AppActivity warpActivity(Map param) {
        //获得活动基本基本
        List<AppActivity> activityList = appActivityDao.select(param);

        if (!CollectionUtils.isEmpty(activityList)) {
            AppActivity appActivity = activityList.get(0);

            Long actId = appActivity.getId();
            Map map = new HashMap(2);
            map.put("appActId", actId);
            //获取活动对应的城市信息
            List<AppActCityRel> actCityList = actCityRelDao.select(map);
            if (!CollectionUtils.isEmpty(actCityList)) {
                List<AppActivityCity> list = new ArrayList<>();
                //组装城市信息
                for (AppActCityRel actCityRel : actCityList) {
                    AppActivityCity actCity = new AppActivityCity();
                    actCity.setCityId(actCityRel.getCityId());
                    actCity.setCityName(actCityRel.getCityName());
                    list.add(actCity);
                }
                appActivity.setActCityList(list);
            }

            return appActivity;
        } else {
            return null;
        }
    }

    public AppActivity getById(Long id) {
        return super.getById(appActivityDao, id);
    }

}
