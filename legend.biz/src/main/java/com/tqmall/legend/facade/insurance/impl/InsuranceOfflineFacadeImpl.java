package com.tqmall.legend.facade.insurance.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.offline.InsuranceOffLineTempInfoParam;
import com.tqmall.insurance.domain.param.insurance.offline.SearchTempInfoListParam;
import com.tqmall.insurance.domain.result.offline.InsuranceOfflineTempInfoDTO;
import com.tqmall.insurance.service.insurance.offline.RpcInsuranceOffLineService;
import com.tqmall.legend.facade.insurance.InsuranceOfflineFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zwb on 17/3/24.
 */
@Slf4j
@Service
public class InsuranceOfflineFacadeImpl implements InsuranceOfflineFacade {
    @Autowired
    RpcInsuranceOffLineService offLineService;


    @Override
    public Map<Integer, String> getOfflineInsuranceStatus() {
        Result<Map<Integer, String>> auditStatusMap = offLineService.getAuditStatusMap();
        if (!auditStatusMap.isSuccess()) {
            throw new BizException("调用接口获取投保单状态失败");
        }
        Map<Integer, String> map = new HashMap<>();
        if (auditStatusMap.getData() != null) {
            map = auditStatusMap.getData();
        }
        return map;
    }


    @Override
    public Result saveOfflineInsurance(InsuranceOffLineTempInfoParam param) {
        Result<Void> result = offLineService.createOffLineTempInfo(param);
        if (result == null || !result.isSuccess()) {
            log.error("【DUBBO】调用线下录入保单出错,错误原因:{}", result.getMessage());
            return Result.wrapErrorResult("", "调用线下录入保单失败," + result.getMessage());
        }
        return Result.wrapSuccessfulResult(null);
    }

    @Override
    public PagingResult<InsuranceOfflineTempInfoDTO> offlineInsuranceList(SearchTempInfoListParam param) {
        log.info("[dubbo-->insurance]调用获取门店线下录单列表接口，param:{}", LogUtils.objectToString(param));
        if (param == null) {
            throw new BizException("调用获取门店线下录单列表接口参数为空");
        }
        PagingResult<InsuranceOfflineTempInfoDTO> pageResult = offLineService.getTempInfoPageList(param);
        if (!pageResult.isSuccess()) {
            log.error("调用获取门店线下录单列表接口失败", LogUtils.objectToString(pageResult));
            throw new BizException(pageResult.getMessage());
        }
        return pageResult;
    }


}
