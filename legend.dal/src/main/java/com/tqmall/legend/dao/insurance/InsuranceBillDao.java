package com.tqmall.legend.dao.insurance;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.insurance.InsuranceBill;

import java.util.Map;

/**
 * Created by zsy on 16/1/7.
 */
@MyBatisRepository
public interface InsuranceBillDao extends BaseDao<InsuranceBill> {

    /**
     * 批量确认对账
     * @param param
     * @return
     */
    Integer updateBills(Map<String, Object> param);

}
