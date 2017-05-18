package com.tqmall.legend.dao.prechecks;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.precheck.PrecheckRequest;

import java.util.List;

@MyBatisRepository
public interface PrecheckRequestDao extends BaseDao<PrecheckRequest> {
    Long batchInsertRequest(List<PrecheckRequest> precheckRequestList);
}
