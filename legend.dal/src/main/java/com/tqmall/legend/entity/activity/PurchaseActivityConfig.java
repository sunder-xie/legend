package com.tqmall.legend.entity.activity;

import lombok.Data;

import com.tqmall.legend.entity.base.BaseEntity;

@Data
public class PurchaseActivityConfig extends BaseEntity {

    private String activityName;//活动名称
    private Integer activityType;//活动类型：0：电商免登陆活动，1：自定义活动
    private String customRedirectUrl;//自定义活动地址
    private Long tqmallBannerId;//电商活动banner_id
    private String optType;//电商提供的免登陆类型

    private Integer offset;
    private Integer limit;

}

