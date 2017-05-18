package com.tqmall.legend.facade.smart.result;

import com.tqmall.insurance.domain.result.InsuranceBasicDTO;
import com.tqmall.legend.facade.insurance.vo.InsuranceCategoryVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by jinju.zeng on 2016/12/23.
 */
@Getter
@Setter
public class SmartInsureCategoryDO {

    private List<InsuranceCategoryVo> insuranceCategoryVoList;
    private InsuranceBasicDTO insuranceBasicDTO;
}
