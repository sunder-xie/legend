package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zwb on 16/8/22.
 */
@Setter
@Getter
public class InsuranceBasicVo {
    /**主键ID**/
    private Integer id;

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**创建人ID**/
    private Integer creator;

    /**更新时间**/
    private java.util.Date gmtModified;

    /**更新人ID**/
    private Integer modifier;

    /**是否删除,Y删除,N未删除**/
    private String isDeleted;

    /**保险公司id, 关联insurance_company.id**/
    private Integer insuranceCompanyId;

    /**代理人类型, 1:门店 2:个人**/
    private Integer agentType;

    /**卖保险代理人id**/
    private Integer agentId;

    /**卖保险代理人名称**/
    private String agentName;

    /**车主名称**/
    private String vehicleOwnerName;

    /**车主证件类型**/
    private String vehicleOwnerCertType;

    /**车主证件编码**/
    private String vehicleOwnerCertCode;

    /**车主手机号**/
    private String vehicleOwnerPhone;

    /**投保人名称**/
    private String applicantName;

    /**投保人证件类型**/
    private String applicantCertType;

    /**投保人证件编码**/
    private String applicantCertCode;

    /**投保人地址**/
    private String applicantAddr;

    /**投保人手机号**/
    private String applicantPhone;

    /**被保人名称**/
    private String insuredName;

    /**被保人证件类型**/
    private String insuredCertType;

    /**被保人证件编码**/
    private String insuredCertCode;

    /**被保人地址**/
    private String insuredAddr;

    /**被保人手机号**/
    private String insuredPhone;

    /**投保所在省**/
    private String insuredProvince;

    /**投保所在城市**/
    private String insuredCity;

    /**收保单人姓名**/
    private String receiverName;

    /**收保单人手机号**/
    private String receiverPhone;

    /**收保单人所在省**/
    private String receiverProvince;

    /**收保单人所在市**/
    private String receiverCity;

    /**收保单人所在地区**/
    private String receiverArea;

    /**收保单人所在详细地址**/
    private String receiverAddr;

    /**车牌号码**/
    private String vehicleSn;

    /**新车购置发票号**/
    private String newCarBillSn;

    /**发票开具日期**/
    private java.util.Date newCarBillTime;

    /**新车购置价格**/
    private java.math.BigDecimal newCarPurcharsePrice;

    /**品牌**/
    private String carBrand;

    /**车系**/
    private String carSerial;

    /**发动机**/
    private String carEngine;

    /**驱动形式**/
    private String carDrive;

    /**配置型号**/
    private String carConfigType;

    /**发动机号**/
    private String carEngineSn;

    /**车架号**/
    private String carFrameSn;

    /**车辆登记日期**/
    private java.util.Date carEgisterDate;

    /**是否过户, 0:未过户； 1:一年内有过户**/
    private Integer hasTransfered;

    /**保费总和**/
    private java.math.BigDecimal insuredTotalFee;

    /**保费分成，保险公司给淘汽**/
    private java.math.BigDecimal insuredTotalRoyalty;

    /**奖励金，淘汽给代理卖保险人**/
    private java.math.BigDecimal rewardTotalFee;

    /**跳转app二维码**/
    private String dimensionCode;

    /**投保所在省CODE**/
    private String insuredProvinceCode;

    /**投保所在城市CODE**/
    private String insuredCityCode;

    /**奖励金总额**/
    private BigDecimal rewardAmountFee;
    /**已使用金额**/
    private BigDecimal rewardUserdFee;
    /**奖励金余额**/
    private BigDecimal rewardBalanceFee;
}
