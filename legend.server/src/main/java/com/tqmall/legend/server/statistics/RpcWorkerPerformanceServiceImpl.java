package com.tqmall.legend.server.statistics;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.bi.dao.PerformanceDao;
import com.tqmall.legend.object.param.statistics.PerformanceParam;
import com.tqmall.legend.object.result.statistics.PerformanceDTO;
import com.tqmall.legend.service.statistics.RpcWorkerPerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiangDong.qu on 16/4/9.
 */
@Slf4j
@Service ("rpcWorkerPerformanceService")
public class RpcWorkerPerformanceServiceImpl implements RpcWorkerPerformanceService {
    @Autowired
    private PerformanceDao performanceDao;

    @Override
    public Result<PerformanceDTO> getWorkerPerfor(PerformanceParam performanceParam) {
        Long shopId = performanceParam.getShopId();
        Long worker = performanceParam.getWorker();
        Date startTime = performanceParam.getStartTime();
        Date endTime = performanceParam.getEndTime();
        if (shopId == null || worker == null) {
            log.error("获取维修工绩效dubbo接口错误,shopId或workerId为空,shopId:{},workerId:{}", shopId, worker);
            return Result.wrapErrorResult("", "获取shopId或workerId失败");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("worker", worker);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        PerformanceDTO performanceDTO = performanceDao.queryWorkerInfo(param);
        performanceDTO.setOrderAmount(BigDecimal.ZERO);
        return Result.wrapSuccessfulResult(performanceDTO);
    }
}

