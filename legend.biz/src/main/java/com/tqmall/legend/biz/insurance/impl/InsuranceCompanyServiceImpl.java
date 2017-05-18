package com.tqmall.legend.biz.insurance.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.insurance.InsuranceCompanyService;
import com.tqmall.legend.dao.insurance.InsuranceCompanyDao;
import com.tqmall.legend.entity.insurance.InsuranceCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15-1-15.
 */
@Service
public class InsuranceCompanyServiceImpl extends BaseServiceImpl implements InsuranceCompanyService {
    @Autowired
    private InsuranceCompanyDao insuranceCompanyDao;

    @Autowired
    private CacheComponent<List<InsuranceCompany>> cacheComponent;

    public List<InsuranceCompany> select(Map map) {
        /**
         * 保险公司列表目前没有条件过滤
         */
        return cacheComponent.getCache(CacheKeyConstant.INSURANCE_COMPANY);
    }

    @Override
    public void batchSave(List<InsuranceCompany> insuranceCompanies) {
        if (super.batchInsert(insuranceCompanyDao, insuranceCompanies, 1000) > 0) {
            cacheComponent.reload(CacheKeyConstant.INSURANCE_COMPANY);
        }
    }
}
