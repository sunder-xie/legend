package com.tqmall.legend.object.param.customer;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

/**
 * 领取优惠券入参
 */
@Data
public class ReceiveCouponParam extends BaseRpcParam {
    private static final long serialVersionUID = -5301105753877268645L;
    private Integer couponSource;   //领券来源:2.门店微信公众号
    private Long userGlobalId;      //全局门店id
    private String mobile;          //手机号
    private String license;         //车牌号
    private Long carBrandId;        //车品牌ID
    private String carBrand;        //车品牌名称
    private Long carSeriesId;       //车系列ID
    private String carSeries;       //车系列名称
    private Long carModelId;        //车型ID
    private String carModel;        //车型名称
    private String byName;          //车型参数/别名
    private String customerName;    //客户姓名
    private Long couponInfoId;      //优惠券类型id
}