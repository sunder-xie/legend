package com.tqmall.legend.entity.tqcheck;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lifeilong on 2016/4/12.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TqCheckLog extends BaseEntity{
    private Long shopId;    //店铺id
    private String checkSn; //检测编号
    private Long customerCarId;     //客户车辆id
    private Long customerId;    //客户id
    private String contactMobile;   //联系人电话
    private String contactName;   //联系人名称
    private String carLicense;  //车牌号',
    private Long carBrandId;    //车品牌id
    private Long carSeriesId;   //车系列ID
    private Long carPowerId;    //车排量ID
    private Long carYearId;     //车年款ID
    private Long carModelsId;   //车款式ID
    private String carBrand;    //车品牌
    private String carSeries;   //车系列
    private String carPower;    //车排量
    private String carYear;     //车年款
    private String carModels;   //车款式
    private String carCompany;  //车厂家
    private String importInfo;  //进出口
    private String imgUrl;      //车牌图片URL
    private String suggestion;  //检测建议

    public String getCarInfo(){
        StringBuffer sb = new StringBuffer();
        if(getCarBrand() != null){
            sb.append(getCarBrand());
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(getImportInfo())){
            sb.append('(').append(getImportInfo()).append(')');
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(getCarModels())){
            sb.append(' ').append(getCarModels());
        } else if(org.apache.commons.lang3.StringUtils.isNotBlank(getCarSeries())){
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }
}
