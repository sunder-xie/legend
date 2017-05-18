package com.tqmall.legend.dao.prechecks;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.precheck.PrecheckValue;

import java.util.List;

@MyBatisRepository
public interface PrecheckValueDao extends BaseDao<PrecheckValue> {
    List<PrecheckValue> getValuesByType(Long valueType);
}
