package com.tqmall.legend.biz.activity.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.activity.PurchaseBannerConfigService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.activity.PurchaseBannerConfigDao;
import com.tqmall.legend.entity.activity.PurchaseBannerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by tanghao on 16/11/24.
 */
@Service
@Slf4j
public class PurchaseBannerConfigServiceImpl implements PurchaseBannerConfigService {

    @Autowired
    private PurchaseBannerConfigDao purchaseBannerConfigDao;
    @Override
    public Integer insert(PurchaseBannerConfig config) {
        Assert.notNull(config,"插入的活动对象不能为空.");
        return purchaseBannerConfigDao.insert(config);
    }

    @Override
    public Integer update(PurchaseBannerConfig config) {
        Assert.notNull(config,"插入的活动对象不能为空.");
        Assert.notNull(config.getId(),"更新的对象id不能为空.");
        return purchaseBannerConfigDao.updateById(config);
    }

    @Override
    public Integer deleteById(Long id) {
        Assert.notNull(id,"要删除的活动id不能为空.");
        return purchaseBannerConfigDao.deleteById(id);
    }

    @Override
    public List<PurchaseBannerConfig> query(PurchaseBannerConfig config) {
        return purchaseBannerConfigDao.queryByParam(config);
    }

    @Override
    public Integer selectCount(PurchaseBannerConfig config) {
        return purchaseBannerConfigDao.selectCountByParam(config);
    }

    @Override
    public PurchaseBannerConfig queryById(Long id) {
        Assert.notNull(id,"id不能为空.");
        return purchaseBannerConfigDao.selectById(id);
    }

    public List<PurchaseBannerConfig> queryBannerList(Long cityId,Long shopId, Integer shopType) {
        Assert.notNull(cityId,"城市id不能为空.");
        Assert.notNull(shopType,"门店类型不能为空.");
        PurchaseBannerConfig param = new PurchaseBannerConfig();
        //设置门店类型查询参数
        param.setShopType(shopType);
        //查询上线活动
        param.setBannerStatus(PurchaseBannerConfig.STATUS_ONLINE);

//        param.setDisplayType(PurchaseBannerConfig.DISPLAYTYPE_ALL);

        List<PurchaseBannerConfig> list = purchaseBannerConfigDao.queryByParamWithStartTime(param);

        List<PurchaseBannerConfig> resultList = Lists.newArrayList();
        for(PurchaseBannerConfig config : list){
            Set<Long> cityIds = Sets.newHashSet();
            if(config.getDisplayType().equals(PurchaseBannerConfig.DISPLAYTYPE_PART_AREA)){
                String cityStr = config.getDisplayCityIds();
                checkDisplayType(cityId, resultList, config, cityIds, cityStr);
            }else if(config.getDisplayType().equals(PurchaseBannerConfig.DISPLAYTYPE_DESIGNATED_SHOP)){
                Set<Long> shopIds = Sets.newHashSet();
                if(PurchaseBannerConfig.DISPLAYTYPE_DESIGNATED_SHOP.equals(config.getDisplayType())){
                    String shopStr = config.getDisplayShopIds();
                    checkDisplayType(shopId, resultList, config, shopIds, shopStr);
                }
            }else {
                resultList.add(config);
            }
        }
        Iterator iterator =  resultList.iterator();
        while (iterator.hasNext()){
            PurchaseBannerConfig config = (PurchaseBannerConfig) iterator.next();
            if(null == config){
                iterator.remove();
                continue;
            }
            if(config.getExpireDate().before(new Date())){
                iterator.remove();
            }
        }
        return resultList;
    }

    private void checkDisplayType(Long shopId, List<PurchaseBannerConfig> resultList, PurchaseBannerConfig config, Set<Long> shopIds, String shopStr) {
        if(!StringUtil.isStringEmpty(shopStr)){
            String[] shops = shopStr.split(",");
            for(String str : shops){
                if(!StringUtil.isStringEmpty(str.trim())){
                    Long id = Long.parseLong(str.trim());
                    shopIds.add(id);
                }
            }
            if(shopId != null){
                if(shopIds.contains(shopId)){
                    resultList.add(config);
                }
            }
        }
    }
}
