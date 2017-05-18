package com.tqmall.legend.biz.insurance;

import com.tqmall.legend.entity.insurance.InsuranceCompany;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15-1-15.
 */
public interface InsuranceCompanyService {
    public List<InsuranceCompany> select(Map map);
    void batchSave(List<InsuranceCompany> insuranceCompanies);
}
