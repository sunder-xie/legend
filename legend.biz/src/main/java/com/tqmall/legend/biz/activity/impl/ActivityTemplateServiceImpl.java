package com.tqmall.legend.biz.activity.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.activity.ActivityTemplateScopeRelService;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.activity.ActivityTemplateServiceRelService;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.biz.activity.IShopActivityServiceRelService;
import com.tqmall.legend.dao.activity.ActivityTemplateDao;
import com.tqmall.legend.dao.activity.ActivityTemplateScopeRelDao;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.activity.ActivityTemplateScopeRel;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.entity.shop.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lixiao on 16/2/24.
 */
@Service
public class ActivityTemplateServiceImpl implements ActivityTemplateService {

    @Autowired
    private ActivityTemplateDao activityTemplateDao;
    @Autowired
    private ActivityTemplateScopeRelDao activityTemplateScopeRelDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ActivityTemplateScopeRelService activityTemplateScopeRelService;
    @Autowired
    private ActivityTemplateServiceRelService activityTemplateServiceRelService;
    @Autowired
    private IShopActivityService shopActivityService;
    @Autowired
    private IShopActivityServiceRelService shopActivityServiceRelService;

    @Override
    public List<ActivityTemplate> getValidActivityList(Long shopId) {
        List<ActivityTemplate> activityTemplateList = activityTemplateDao.getValidActivityList(null);
        List<ActivityTemplate> returnList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(activityTemplateList)) {
            filterActTpl(shopId, activityTemplateList, returnList);
        }
        return returnList;
    }

    @Override
    public List<ActivityTemplate> getActivityAllList(Long shopId) {
        List<ActivityTemplate> activityTemplateList = activityTemplateDao.select(null);
        List<ActivityTemplate> returnList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(activityTemplateList)) {
            filterActTpl(shopId, activityTemplateList, returnList);
        }
        return returnList;
    }

    @Override
    public ActivityTemplate getById(Long actTplId) {
        return activityTemplateDao.selectById(actTplId);
    }

    @Override
    public List<ActivityTemplate> select(Map<String, Object> map) {
        return activityTemplateDao.select(map);
    }

    /**
     * 过滤门店对应的模板服务
     *
     * @param shopId
     * @param activityTemplateList
     */
    private void filterActTpl(Long shopId, List<ActivityTemplate> activityTemplateList, List<ActivityTemplate> returnList) {
        for (ActivityTemplate activityTemplate : activityTemplateList) {
            if (activityTemplate.getActScope().equals(0)) {
                //针对所有门店
                returnList.add(activityTemplate);
            } else if (activityTemplate.getActScope().equals(1)) {
                //针对指定门店
                Map searchMap = Maps.newHashMap();
                searchMap.put("actTplId", activityTemplate.getId());
                searchMap.put("scopeId", shopId);
                List<ActivityTemplateScopeRel> relList = activityTemplateScopeRelDao.select(searchMap);
                if (!CollectionUtils.isEmpty(relList)) {
                    returnList.add(activityTemplate);
                }
            } else if (activityTemplate.getActScope().equals(2)) {
                //针对指定市
                Shop shop = shopDao.selectById(shopId);
                Map searchMap = Maps.newHashMap();
                searchMap.put("actTplId", activityTemplate.getId());
                searchMap.put("scopeId", shop.getCity());
                List<ActivityTemplateScopeRel> relList = activityTemplateScopeRelDao.select(searchMap);
                if (!CollectionUtils.isEmpty(relList)) {
                    returnList.add(activityTemplate);
                }
            }
        }
    }

    @Override
    public Integer selectCount(Map<String, Object> searchMap) {
        return activityTemplateDao.selectCount(searchMap);
    }

    @Override
    public int deleteById(Long activityTemplateId) {
        if(activityTemplateId==null){
            return 0;
        }
        int effCount = activityTemplateDao.deleteById(activityTemplateId);
        if(effCount==0){
            return effCount;
        }
        //.删除模版推送范围
        Map<String,Object> delScopeRelMap = new HashMap<>();
        delScopeRelMap.put("actTplId",activityTemplateId);
        activityTemplateScopeRelService.delete(delScopeRelMap);

        //.删除模版关联的服务
        Map<String,Object> delSvRelMap = new HashMap<>();
        delSvRelMap.put("actTplId",activityTemplateId);
        activityTemplateServiceRelService.delete(delSvRelMap);

        //.删除模版派生的活动实体
        Map<String,Object> searchShopActMap = new HashMap<>();
        searchShopActMap.put("actTplId",activityTemplateId);
        shopActivityService.delete(searchShopActMap);
        List<ShopActivity> shopActivityList = shopActivityService.select(searchShopActMap);
        Set<Long> shopActivityIds = new HashSet<>();
        for (ShopActivity shopActivity : shopActivityList) {
            shopActivityIds.add(shopActivity.getId());
        }
        //.删除活动实体和服务关系
        if (CollectionUtils.isEmpty(shopActivityIds)) {
            Map<String, Object> delShopActivitySvRelMap = new HashMap<>();
            delShopActivitySvRelMap.put("shopActIds", shopActivityIds);
            shopActivityServiceRelService.delete(delShopActivitySvRelMap);
        }
        return effCount;
    }

    @Override
    public Integer update(ActivityTemplate activityTemplate) {
        return activityTemplateDao.updateById(activityTemplate);
    }

    @Override
    public Integer add(ActivityTemplate activityTemplate) {
        return activityTemplateDao.insert(activityTemplate);
    }
}
