package com.tqmall.legend.object.param.tqcheck;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lifeilong on 2016/4/13.
 */
@Data
public class TqCheckAddParam  extends BaseRpcParam implements Serializable{
    private static final long serialVersionUID = -2641476059286408684L;
    private Long shopId;    //店铺id
    private Long userId;    //用户id
    private Long customerCarId; //车辆id
    private String carLicense;  //车牌号
    private String contactMobile;   //联系人电话
    private String contactName;     //联系人名称
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
    private String imgUrl;  //车牌图片URL

    private Long carGearBoxId;//app 获取车型信息用
    private List<TqCheckDetailParam> tqCheckDetailList;
}
