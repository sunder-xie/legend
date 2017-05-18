package com.tqmall.legend.biz.marketing.impl;

import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.marketing.MarketingActivityService;
import com.tqmall.legend.dao.marketing.MarketingCaseDao;
import com.tqmall.legend.dao.marketing.MarketingCaseServiceDao;
import com.tqmall.legend.entity.marketing.MarketingCase;
import com.tqmall.legend.entity.marketing.MarketingCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 */
@Service
public class MarketingActivityServiceImpl extends BaseServiceImpl implements MarketingActivityService {
    @Autowired
    private MarketingCaseDao marketingCaseDao;
    @Autowired
    private MarketingCaseServiceDao marketingCaseServiceDao;

    @Override
    public int updateById(MarketingCase marketingCase) {
        return marketingCaseDao.updateById(marketingCase);
    }

    @Override
    public Integer batchInsert(List<MarketingCase> marketingCases) {
        return marketingCaseDao.batchInsert(marketingCases);
    }

    @Override
    public List<MarketingCaseService> selectCaseService(Map param) {
        return marketingCaseServiceDao.select(param);
    }

    @Override
    public Integer save(MarketingCase marketingCase,UserInfo userInfo) {
        Long shopId = marketingCase.getShopId();
        marketingCase.setCreator(userInfo.getUserId());
        marketingCase.setModifier(userInfo.getUserId());
        Integer num = marketingCaseDao.insert(marketingCase);
        if (num > 0) {
            List<MarketingCaseService> marketingCaseServices = marketingCase.getServiceInfos();
            if (!CollectionUtils.isEmpty(marketingCaseServices)) {
                for (MarketingCaseService marketingCaseService : marketingCaseServices) {
                    marketingCaseService.setCaseId(marketingCase.getId());
                    marketingCaseService.setShopId(shopId);
                    marketingCaseService.setCreator(marketingCase.getCreator());
                    marketingCaseService.setModifier(marketingCase.getModifier());
                }
                marketingCaseServiceDao.batchInsert(marketingCaseServices);
            }
        }

        return num;
    }

    @Override
    public Integer update(MarketingCase marketingCase,UserInfo userInfo) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("caseId", marketingCase.getId());
        param.put("shopId", marketingCase.getShopId());

        marketingCase.setModifier(userInfo.getUserId());

        int num = marketingCaseDao.updateById(marketingCase);

        marketingCaseServiceDao.delete(param);
        List<MarketingCaseService> marketingCaseServices = marketingCase.getServiceInfos();

        if (!CollectionUtils.isEmpty(marketingCaseServices)) {
            for (MarketingCaseService marketingCaseService : marketingCaseServices) {

                if(null != marketingCaseService.getServiceId()){
                    marketingCaseService.setCaseId(marketingCase.getId());
                    marketingCaseService.setShopId(marketingCase.getShopId());
                    marketingCaseService.setCreator(userInfo.getUserId());
                    marketingCaseService.setModifier(userInfo.getUserId());
                }
                if(null != marketingCaseService.getId()){
                    marketingCaseServiceDao.updateById(marketingCaseService);
                }else {
                    marketingCaseServiceDao.insert(marketingCaseService);
                }
            }
        }

        return num;
    }

    @Override
    public List<MarketingCase> select(Map param) {
        return marketingCaseDao.select(param);
    }
}
