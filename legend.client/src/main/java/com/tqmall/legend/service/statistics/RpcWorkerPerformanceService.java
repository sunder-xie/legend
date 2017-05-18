package com.tqmall.legend.service.statistics;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.statistics.PerformanceParam;
import com.tqmall.legend.object.result.statistics.PerformanceDTO;

/**
 * Created by xiangDong.qu on 16/4/9.
 */
public interface RpcWorkerPerformanceService {
    /**
     * @param performanceParam
     *
     * @return
     */
    public Result<PerformanceDTO> getWorkerPerfor(PerformanceParam performanceParam);
}
