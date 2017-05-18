package com.tqmall.legend.object.enums.warehouse;

/**
 * 仓库配件搜索非零库存
 */
public enum ZeroStockRangeEnum {
    NONZEROSTOCK(Integer.valueOf("1"), "非0库存"),
    ZEROSTOCK(Integer.valueOf("0"), "0库存");

    private final Integer key;
    private final String name;

    ZeroStockRangeEnum(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public static ZeroStockRangeEnum[] getNames() {
        ZeroStockRangeEnum[] arr = values();
        return arr;
    }

    public Integer getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }
}