package com.tqmall.legend.object.result.activity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanghao on 16/11/24.
 */
@Data
public class PurchaseActivityConfigDTO implements Serializable{
    private String activityName;//活动名称
    private Integer activityType;//活动类型：0：电商免登陆活动，1：自定义活动
    private String customRedirectUrl;//自定义活动地址
    private Long tqmallBannerId;//电商活动banner_id
    private String optType;//电商提供的免登陆类型


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
}
