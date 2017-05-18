package com.tqmall.legend.object.param.activity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanghao on 16/11/24.
 */
@Data
public class PurchaseBannerConfigParam extends PageParam implements Serializable {
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

    protected Long id;

    /**
     * isDeleted字段是否删除，标记为Y，已删除
     */
    public static final String IS_DELETE_ENABLE = "Y";

    /**
     * isDeleted字段是否删除，标记为N，没删除
     */
    public static final String IS_DELETE_UNENABLE = "N";

    protected String isDeleted;
    protected Date gmtCreate;
    protected Long creator;
    protected Date gmtModified;
    protected Long modifier;

    public void setDefaultBizValue() {
        if (id == null) {
            // 创建
            if (gmtCreate == null) {
                gmtCreate = new Date();
            }
            if (creator == null) {
                creator = 1L;
            }
            if (isDeleted == null) {
                isDeleted = IS_DELETE_UNENABLE;
            }
        }
        // 修改
        if (modifier == null) {
            modifier = 1L;
        }
        gmtModified = new Date();

    }


    private Integer onlyDisplayType;
    private String onlyDisplayCityIds;
    private String onlyDisplayShopIds;
    private Integer onlyShopType;
}
