package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zwb on 16/8/24.
 */
@Setter
@Getter
public class InsuranceVehicleVo {

    private String vehicleCode;//车型编码
    private String vehicleName;//车型名称
    private String purchasePrice;//新车购置价
    private String purchasePriceTax;//新车购置价（含税)
    private Integer seat;//额定座位数
    private String yearPattern;//年款
    private String gearboxType;//驱动型式
    private String engineDesc;//发动机描述
    private String configName;//配置名称
    private String displacement;//排量
    private String vehicleImport;//国产进口标志
    private String marketDate;//上市年月:格式yyyymm
    private String kindredPrice;//类比价格
    private String kindredPriceTax;//类比价格含税、
    private String brandName;//品牌名称
    private String familyName;//车系名称

}
