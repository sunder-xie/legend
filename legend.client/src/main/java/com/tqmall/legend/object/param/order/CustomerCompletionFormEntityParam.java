package com.tqmall.legend.object.param.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/10 14:48.
 * 洗车单完善客户资料
 */
@Data
public class CustomerCompletionFormEntityParam implements Serializable {
    // 工单ID
    private Long orderId;
    // 工单编号
    private String orderSn;
    // 工单生成时间
    private String gmtCreateStr;
    // 工单开单时间
    private String createTimeStr;
    // 工单总金额
    private BigDecimal orderAmount;
    // 工单状态
    private String orderStatus;
    //开票状态 0 - 没开票
    private Long invoiceType;

    // 车牌号
    private String carLicense;
    // 收款金额
    private BigDecimal expense;
    // 洗车工ID
    private Long workerId;
    // 洗车工名称
    private String workerName;
    // 维修工人列表，以逗号，隔开
    private String workerIds;
    // 维修工名称列表, 以逗号，隔开
    private String workerNames;
    // 服务顾问ID
    private Long receiver;
    // 服务顾问名称
    private String receiverName;
    // 备注
    private String postscript;

    // [[车型基本信息
    // 品牌
    private Long carBrandId;
    private String carBrand;
    // 车系
    private Long carSeriesId;
    private String carSeries;
    // 车型
    private Long carModelId;
    private String carModel;

    private Long carPowerId;
    private String carPower;

    private Long carYearId;
    private String carYear;

    private Long carGearBoxId;
    private String carGearBox;
    // 进口与国产
    private String importInfo;
    // 车型组合信息
    private String carInfo;
    private String carCompany;
    // ]]

    // [[ 客户车辆信息
    // 客户车辆ID
    private Long customerCarId;
    // 客户ID
    private Long customerId;
    // 联系人姓名
    private String contactName;
    // 联系人电话
    private String contactMobile;
    // 联系人单位
    private String company;
    // 行驶里程
    private Long mileage;
    // 车架号
    private String vin;
    // 车牌照图片
    private String carLicensePicture;
    // ]]

    //--以下字段用于APP
    //门店ID
    private Long shopId;
    //用户ID
    private Long userId;
    //用户名称
    private String userName;

    // 支付状态
    private Integer payStatus;

    // 结算时间
    private Date payTime;

    // 结算编号
    private String paymentLogSn;


}
