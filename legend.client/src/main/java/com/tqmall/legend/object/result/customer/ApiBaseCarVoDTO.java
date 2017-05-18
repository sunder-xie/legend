package com.tqmall.legend.object.result.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by dingbao on 16/9/21.
 */
@Data
public class ApiBaseCarVoDTO  implements Serializable{

    /**
     * 车辆id
     */
    private Long carId = Long.valueOf("0");
    /**
     * 客户id
     */
    private Long customerId = Long.valueOf("0");
    /**
     * 车牌信息
     */
    private String license = "";

    /**
     * 车型信息,已经组装好,格式为:品牌 车型 年款 批量
     */
    private String carInfo = "";
    /**
     * 车主信息
     */
    private String customerName = "";
    /**
     * 车主电话
     */
    private String mobile = "" ;
    /**
     * 车型信息
     */
    private Long carModelId = Long.valueOf("0");
    /**
     * 车系
     */
    private Long carSeriesId = Long.valueOf("0");
    /**
     * 行驶里程
     */
    private Long mileage = Long.valueOf("0");
    /**
     * vin码
     */
    private String vin = "";
    /**
     * 车辆图片路径
     */
    private String imgUrl;
    /**
     * 品牌
     */
    private Long carBrandId = Long.valueOf("0");
    /**
     * 排量
     */
    private Long carPowerId = Long.valueOf("0");
    /**
     * 年款
     * */
    private Long carYearId = Long.valueOf("0");
    /**
     * 变速箱
     * */
    private Long carGearBoxId = Long.valueOf("0");

    private String importInfo;
    private String carBrand;
    private String carSeries;
    private String carModel;
    private String carPower;
    private String carYear;
    private String carGearBox;
    private String carCompany;


    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactMobile;
}
