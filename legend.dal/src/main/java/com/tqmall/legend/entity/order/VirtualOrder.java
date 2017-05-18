package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class VirtualOrder extends BaseEntity {

    private Date payTime;
    private Long shopId;
    private Long orderType;
    private Long parentId;
    private String orderSn;
    private Long customerId;
    private Long customerCarId;
    private String postscript;
    private String orderStatus;
    private String carLicense;
    private Long carBrandId;
    private Long carSeriesId;
    private Long carPowerId;
    private Long carYearId;
    private Long carModelsId;
    private String carBrand;
    private String carSeries;
    private String carPower;
    private String carYear;
    private String carModels;
    private String carCompany;
    private String importInfo;
    private String customerName;
    private String customerMobile;
    private String vin;
    private String engineNo;
    private Long receiver;
    private String receiverName;
    private String operatorName;
    private Integer payStatus;
    private Date finishTime;
    private String carAlias;
    private Long otherInsuranceCompanyId;
    private String otherInsuranceCompanyName;
    private String contactName;
    private String contactMobile;
    private Long insuranceCompanyId;
    private String insuranceCompanyName;
    private String mileage;
    private String carColor;
    private Date buyTime;
    private String customerAddress;
    private String oilMeter;
    private BigDecimal goodsAmount;
    private BigDecimal serviceAmount;
    private BigDecimal orderAmount;
    private BigDecimal goodsDiscount;
    private BigDecimal serviceDiscount;
    private BigDecimal feeAmount;
    private BigDecimal feeDiscount;
    private BigDecimal taxAmount;
    private Date expectedTime;
    private String identityCard;
    private String company;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;//创建时间
   
    //子单打印时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date printTime;

    //扩展字段
    String OrderTypeName;

    public String getCarInfo(){
        StringBuffer sb = new StringBuffer();
        if(getCarBrand() != null){
            sb.append(getCarBrand());
        }
        if(StringUtils.isNotBlank(getImportInfo())){
            sb.append('(').append(getImportInfo()).append(')');
        }
        if(StringUtils.isBlank(getCarModels())){
            sb.append(' ').append(getCarSeries());
        } else {
            sb.append(' ').append(getCarModels());
        }
        return sb.toString();
    }
}

