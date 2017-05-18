package com.tqmall.legend.facade.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.offline.InsuranceOffLineTempInfoParam;
import com.tqmall.insurance.domain.param.insurance.offline.SearchTempInfoListParam;
import com.tqmall.insurance.domain.result.offline.InsuranceOfflineTempInfoDTO;

import java.util.Map;

/**
 * Created by zwb on 17/3/24.
 * 线下录单
 */
public interface InsuranceOfflineFacade {

   /**
    * 获取线下保单审核状态map用于展示
    * @return
    */
   Map <Integer, String> getOfflineInsuranceStatus();

   /**
    * 线下保单的保存
    * @return
    */
   Result saveOfflineInsurance(InsuranceOffLineTempInfoParam param);

   PagingResult<InsuranceOfflineTempInfoDTO> offlineInsuranceList(SearchTempInfoListParam param);




}
