package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zwb on 16/8/18.
 */
@Setter
@Getter
public class InsuranceCarModelVo {
    private String vehicleCode;//车型编码
    private String vehicleName;//车型名称
    private Integer pageTotal;
    private List<InsuranceVehicleVo> vehicleList; //车型查询结果
    private List<InsuranceFamilyVo> familyList;//品牌
    private List<InsuranceBrandVo> brandList;//车系
    private List<String> engineDescList;//发动机
    private List<String> gearboxTypeList;//驱动
}
