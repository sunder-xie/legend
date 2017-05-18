package com.tqmall.legend.biz.activity.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.activity.PurchaseActivityConfigService;
import com.tqmall.legend.dao.activity.PurchaseActivityConfigDao;
import com.tqmall.legend.entity.activity.PurchaseActivityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by tanghao on 16/11/24.
 */
@Service
@Slf4j
public class PurchaseActivityConfigServiceImpl implements PurchaseActivityConfigService {
    @Autowired
    private PurchaseActivityConfigDao purchaseActivityConfigDao;
    @Override
    public Integer insert(PurchaseActivityConfig config) {
        Assert.notNull(config,"插入的活动对象不能为空.");
        return purchaseActivityConfigDao.insert(config);
    }

    @Override
    public Integer update(PurchaseActivityConfig config) {
        Assert.notNull(config,"更新的活动对象不能为空.");
        Assert.notNull(config.getId(),"更新的对象id不能为空.");
        return purchaseActivityConfigDao.updateById(config);
    }

    @Override
    public Integer deleteById(Long id) {
        Assert.notNull(id,"要删除的活动id不能为空.");
        return purchaseActivityConfigDao.deleteById(id);
    }

    @Override
    public List<PurchaseActivityConfig> query(PurchaseActivityConfig config) {
        return purchaseActivityConfigDao.queryByParam(config.getActivityName(), config.getActivityType(), config.getOptType(),config.getLimit(), config.getOffset());
    }

    @Override
    public Integer selectCount(PurchaseActivityConfig config) {
        return purchaseActivityConfigDao.selectCountByParam(config.getActivityName(), config.getActivityType(), config.getOptType());
    }

    @Override
    public PurchaseActivityConfig queryById(Long id) {
        Assert.notNull(id,"id不能为空.");
        return purchaseActivityConfigDao.selectById(id);
    }

    /**
     * 查询所有的活动配置
     *
     * @return
     */
    @Override
    public List<PurchaseActivityConfig> getAllActivity() {
        Map<String, Object> searchParam = Maps.newHashMap();
        return purchaseActivityConfigDao.select(searchParam);
    }
}
