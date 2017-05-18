package com.tqmall.legend.facade.smart;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.smart.SmartSearchParam;
import com.tqmall.insurance.domain.result.smart.SmartThirdInsuredInfoDTO;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import com.tqmall.legend.facade.smart.result.SmartInsureCategoryDO;

import java.util.List;

/**
 * Created by zwb on 16/12/21.
 */
public interface BihuSmartSearchFacade {
    /**
     * 壁虎查询接口 对象内参数均为必传
     * @param param
     * @return
     */
    Result<SmartInsureCategoryDO> bihuSearch(SmartSearchParam param);
}
