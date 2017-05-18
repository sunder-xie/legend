package com.tqmall.legend.dao.insurance;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.insurance.InsuranceCompany;

import java.util.List;

/**
 * Created by zsy on 2015/1/15.
 */
@MyBatisRepository
public interface InsuranceCompanyDao extends BaseDao<InsuranceCompany> {
    /*
     * 批量插入
     */
    public Integer batchInsert(List<InsuranceCompany> insuranceCompanyList);
}
