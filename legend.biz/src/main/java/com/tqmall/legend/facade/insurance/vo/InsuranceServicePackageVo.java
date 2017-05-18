package com.tqmall.legend.facade.insurance.vo;

import com.tqmall.insurance.domain.result.InsuranceServicePackageDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zsy on 16/11/1.
 */
@Getter
@Setter
public class InsuranceServicePackageVo {
    private String suitablePrice;
    private List<InsuranceServicePackageDTO> insuranceServicePackageDTOList;
}
