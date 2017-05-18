package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;


/**
 * 洗车单客户信息完善实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerPerfectOfCarWashEntity extends BaseEntity {

    private static final long serialVersionUID = 7203010305912482467L;
    //车牌号
    private String license;
    // 联系人姓名
    String contactName;
    // 联系人电话
    String contactMobile;
    // 联系人单位
    String company;
    // 车牌图片地址
    String carLicensePictureUrl;
    //车架号
    String vin;
    //行驶里程
    Long mileage;
    // 车辆ID
    private Long customerCarId;
    //门店id
    private Long shopId;
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

    private String carCompany;
    // 进口与国产
    private String importInfo;

    // carInfo
    private String carInfo;

    public String getCarInfo() {
        StringBuffer sb = new StringBuffer();
        if (getCarBrand() != null) {
            sb.append(getCarBrand());
        }
        if (StringUtils.isNotBlank(getImportInfo())) {
            sb.append('(').append(getImportInfo()).append(')');
        }
        if (StringUtils.isNotBlank(getCarModel())) {
            sb.append(' ').append(getCarModel());
        } else if (StringUtils.isNotBlank(getCarSeries())) {
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }


    @Override
    public String toString() {
        return "CustomerPerfectOfCarWashEntity{" +
                "contactName='" + contactName + '\'' +
                ", contactMobile='" + contactMobile + '\'' +
                ", carLicensePictureUrl='" + carLicensePictureUrl + '\'' +
                ", vin='" + vin + '\'' +
                ", mileage=" + mileage +
                ", customerCarId=" + customerCarId +
                ", shopId=" + shopId +
                ", carBrandId=" + carBrandId +
                ", carBrand='" + carBrand + '\'' +
                ", carSeriesId=" + carSeriesId +
                ", carSeries='" + carSeries + '\'' +
                ", carModelId=" + carModelId +
                ", carModel='" + carModel + '\'' +
                ", importInfo='" + importInfo + '\'' +
                ", carInfo='" + carInfo + '\'' +
                "} ";
    }
}
