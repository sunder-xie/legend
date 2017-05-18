package com.tqmall.legend.enums.activity;

/**
 * Created by lixiao on 16/2/24.
 */
public enum BannerPositionEnum {
    INDEX(1, "首页banner"),
    BUY(2, "淘汽采购banner"),
    ACTIVITY(3, "活动banner");

    private final int position;
    private final String name;

    private BannerPositionEnum(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public int getposition() {
        return this.position;
    }

    public String getname() {
        return this.name;
    }

    public static String getNameByPosition(int position) {
        for (BannerPositionEnum e : BannerPositionEnum.values()) {
            if (e.getposition() == position) {
                return e.getname();
            }
        }
        return null;
    }
}
