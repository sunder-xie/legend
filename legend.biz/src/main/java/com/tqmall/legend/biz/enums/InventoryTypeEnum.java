package com.tqmall.legend.biz.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by litan on 14-11-5.
 */
public enum InventoryTypeEnum {

    /**
     * 结算类型枚举
     */
    INIT("初始化", "CSH"),

    /**
     * 出库相关状态
     */
    OUT_WAREHOUSE("蓝字出库", "LZCK"),
    OUT_WAREHOUSE_RED("红字出库", "HZCK"),
    RED_INVALID("红字作废", "CK_HZZF"),
    BLUE_INVALID("蓝字作废", "CK_LZZF"),

    /**
     * 入库相关状态
     */
    IN_WAREHOUSE("蓝字入库", "LZRK"),
    IN_WAREHOUSE_RED("红字入库", "HZRK"),
    RED_IN_ABOLISH("红字入库作废", "HZZF"),
    BLUE_IN_ABOLISH("蓝字字入库作废", "LZZF");

    // 成员变量
    private String name;

    private String index;

    // 构造方法
    private InventoryTypeEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(String index) {
        for (InventoryTypeEnum c : InventoryTypeEnum.values()) {
            if (StringUtils.equals(c.getIndex(), index)) {
                return c.name;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
}
