package com.tqmall.legend.biz.marketing.gather.impl;

import com.tqmall.legend.biz.marketing.gather.GatherCouponConfigService;
import com.tqmall.legend.dao.marketing.gather.GatherCouponConfigDao;
import com.tqmall.legend.entity.marketing.gather.GatherCouponConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wushuai on 16/12/16.
 */
@Service
public class GatherCouponConfigServiceImpl implements GatherCouponConfigService {

    @Autowired
    private GatherCouponConfigDao gatherCouponConfigDao;

    @Override
    public List<GatherCouponConfig> selectByAllotSn(String allotSn) {
        if (StringUtils.isBlank(allotSn)) {
            return new ArrayList<>();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("allotSn", allotSn);
        return gatherCouponConfigDao.select(param);
    }

    @Override
    public GatherCouponConfig selectById(Long gatherCouponConfigId) {
        if(gatherCouponConfigId==null){
            return null;
        }
        return gatherCouponConfigDao.selectById(gatherCouponConfigId);
    }

    @Override
    public int updateById(GatherCouponConfig gatherCouponConfig) {
        Assert.notNull(gatherCouponConfig);
        Assert.notNull(gatherCouponConfig.getId());
        return gatherCouponConfigDao.updateById(gatherCouponConfig);
    }

    @Override
    public int insert(GatherCouponConfig gatherCouponConfig) {
        Assert.notNull(gatherCouponConfig);
        return gatherCouponConfigDao.insert(gatherCouponConfig);
    }
}
