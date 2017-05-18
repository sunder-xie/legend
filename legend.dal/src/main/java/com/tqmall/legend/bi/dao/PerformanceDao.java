package com.tqmall.legend.bi.dao;

import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.object.result.statistics.PerformanceDTO;

import java.math.BigDecimal;
import java.util.Map;

@MyBatisRepository
public interface PerformanceDao {

    PerformanceDTO queryWorkerInfo(Map<String, Object> param);

    @Deprecated
    BigDecimal queryWorkerPerformance(Map<String, Object> param);
}
