package com.tqmall.legend.entity.activity;

import lombok.Data;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.Date;
import java.util.Set;

/**
 * Created by tanghao on 16/11/23.
 */

@Data
public class PurchaseBannerConfig extends BaseEntity {

    public static final Integer DISPLAYTYPE_ALL = 0;//全部展示
    public static final Integer DISPLAYTYPE_PART_AREA = 1;//部分地区展展示
    public static final Integer DISPLAYTYPE_DESIGNATED_SHOP = 2;//指定门店展示

    public static final Integer SHOPTYPE_ALL = 0;//全部展示
    public static final Integer SHOPTYPE_TQMALL = 1;//云修门店
    public static final Integer SHOPTYPE_SPRAY = 2;//钣喷门店

    public static final Integer ACTIVITYTYPE_TQMALL = 0;//电商免登陆跳转
    public static final Integer ACTIVITYTYPE_CUSTOMIZED = 1;//自定义跳转

    public static final Integer STATUS_ONLINE = 0;//活动上线
    public static final Integer STATUS_OFFLINE = 1;//活动下线

    private String bannerImgUrl;//活动banner图片地址
    private Date effectiveDate;//生效时间
    private Date expireDate;//失效时间,若不填，则长期有效
    private String optType;//电商提供的免登陆类型
    private Integer displayType;//0：全部展示，1：部分城市站展示，2：指定门店展示
    private String displayCityIds;//部分地区展示，以逗号，隔开
    private String displayShopIds;//指定展示门店，以逗号，隔开
    private Integer shopType;//0：全部；1：云修banner，2：钣喷banner
    private Integer activityType;//活动类型：0：电商免登陆活动，1：自定义活动
    private Integer bannerStatus;//0：活动上线，1：活动下线
    private String customRedirectUrl;//自定义活动地址
    private Integer bannerSort;//排序id（从小到大）
    private String activityName;//活动名称

    private Integer offset;
    private Integer limit;


    private Integer onlyDisplayType;
    private String onlyDisplayCityIds;
    private String onlyDisplayShopIds;
    private Integer onlyShopType;

}

