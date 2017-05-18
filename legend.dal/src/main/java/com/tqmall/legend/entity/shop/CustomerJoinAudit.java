package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by twg on 15/8/24.
 * app资料
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerJoinAudit{
    private Long id;
    private String isDeleted;
    private Long creator;
    private Long modifier;
    private Long customerId;
    private String contactsMobile;//接单人电话
    private String contactsName;
    private String companyName;
    private Long province;
    private Long city;
    private Long district;
    private Long street;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String streetName;
    private String address;
    private String headCount;
    private String qq;
    private String weixin;
    private String post;
    private String mobilephone;
    private String email;
    private String longitude;
    private String latitude;
    private String companyFormalName;
    private String majorCarBrand;
    private String saName;
    private Long workingTime;


    private String businessTimeBeginStr;//开门时间

    private String businessTimeEndStr;//关门时间

    private String saMobilephone;

    private List<CustomerFilePath> customerFilePathList;
    private String saImg;


}
