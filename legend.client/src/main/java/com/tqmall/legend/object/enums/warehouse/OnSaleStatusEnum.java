package com.tqmall.legend.object.enums.warehouse;

/**
 * 仓库配件搜索上架状态
 */
public enum OnSaleStatusEnum {
    ONSALE(1, "上架"),
    OFFSALE(0, "下架");

    private final Integer key;
    private final String name;

    OnSaleStatusEnum(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static OnSaleStatusEnum[] getNames() {
        OnSaleStatusEnum[] arr = values();
        return arr;
    }

    public Integer getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }
}