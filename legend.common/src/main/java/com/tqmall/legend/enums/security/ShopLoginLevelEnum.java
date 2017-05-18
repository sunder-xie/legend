package com.tqmall.legend.enums.security;

/**
 * Created by zsy on 2015/7/16.
 * 门店安全登录等级
 */
public enum ShopLoginLevelEnum {
    LEVEL_ONE("1", "等级一"),
    LEVEL_TWO("2", "等级二"),
    LEVEL_THREE("3", "等级三");

    private String level;
    private String remark;

    ShopLoginLevelEnum(String level, String remark) {
        this.level = level;
        this.remark = remark;
    }

    public String getLevel() {
        return this.level;
    }

    public String getRemark() {
        return this.remark;
    }

    public static String getLevelByName(String name) {
        for (ShopLoginLevelEnum shopLoginLevelEnum : ShopLoginLevelEnum.values()) {
            if (shopLoginLevelEnum.name().equals(name)) {
                return shopLoginLevelEnum.getLevel();
            }
        }
        return null;
    }
}
