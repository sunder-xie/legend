package com.tqmall.legend.enums.shop;

/**
 * Created by zsy on 2016/12/14.
 * 门店标签类别枚举
 */
public enum ShopTagCodeEnum {
    YBD("YBD", "样板店");

    private String tagCode;
    private String tagName;

    ShopTagCodeEnum(String tagCode, String tagName) {
        this.tagCode = tagCode;
        this.tagName = tagName;
    }

    public static ShopTagCodeEnum[] getShopTags() {
        ShopTagCodeEnum[] arr = values();
        return arr;
    }

    public String getTagCode() {
        return tagCode;
    }

    public String getTagName() {
        return tagName;
    }
}
