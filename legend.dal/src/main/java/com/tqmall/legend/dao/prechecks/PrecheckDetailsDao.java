package com.tqmall.legend.dao.prechecks;

/**
 * Created by guangxue on 14/11/1.
 */

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.precheck.PrecheckDetails;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PrecheckDetailsDao extends BaseDao<PrecheckDetails> {
    List<PrecheckDetails> getPrecheckDetail(Map<String, Object> params);
    void addPrecheckDetail(PrecheckDetails precheckDetails);
    Long batchInsertDetail(List<PrecheckDetails> precheckDetailsList);
}