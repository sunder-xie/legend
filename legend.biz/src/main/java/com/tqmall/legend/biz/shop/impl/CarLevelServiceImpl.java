package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.CarLevelService;
import com.tqmall.legend.dao.shop.CarLevelDao;
import com.tqmall.legend.entity.shop.CarLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2014-11-09 17:10
 */
@Service
public class CarLevelServiceImpl extends BaseServiceImpl implements CarLevelService {

    @Autowired
    CarLevelDao carLevelDao;

    @Override
    public List<CarLevel> selectNoCache(Map<String, Object> searchMap) {
        List<CarLevel> carLevelList = carLevelDao.select(searchMap);
        return carLevelList;
    }

    @Override
    public Long getCarLevelId(@NotNull Long shopId,@NotNull Long userId, @NotNull String carLevelName) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("name",carLevelName);
        List<CarLevel> carLevelList = carLevelDao.select(param);
        if(!CollectionUtils.isEmpty(carLevelList)){
            return carLevelList.get(0).getId();
        }else {
            CarLevel carLevel = new CarLevel();
            carLevel.setShopId(shopId);
            carLevel.setName(carLevelName);
            carLevel.setCreator(userId);
            carLevel.setSort(0L);
            carLevelDao.insert(carLevel);
            return carLevel.getId();
        }
    }

    @Override
    public CarLevel getCarLevelByShopIdAndName(Long shopId, String name) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId",shopId);
        searchMap.put("name", name);
        List<CarLevel> carLevelList = selectNoCache(searchMap);
        CarLevel carLevel;
        if(CollectionUtils.isEmpty(carLevelList)){
            //添加
            carLevel = new CarLevel();
            carLevel.setName(name);
            carLevel.setShopId(shopId);
            carLevelDao.insert(carLevel);
        }else{
            carLevel = carLevelList.get(0);
        }
        return carLevel;
    }

    @Override
    public List<CarLevel> findCarLevelsByNames(Long shopId, String... names) {
        return carLevelDao.findCarLevelsByNames(shopId, names);
    }

    @Override
    public void batchSave(List<CarLevel> carLevels) {
        super.batchInsert(carLevelDao,carLevels,1000);
    }
}
