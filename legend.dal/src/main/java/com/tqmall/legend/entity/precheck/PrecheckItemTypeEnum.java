package com.tqmall.legend.entity.precheck;

/**
 * Created by sven.zhang on 2016/4/6.
 */
public enum PrecheckItemTypeEnum {
    GOODSINCARCODE(0L, "随车物品"),
    OUTLINECODE(1L, "外观检测"),
    TYRECODE(2L, "轮胎"),
    OTHERCODE(3L, "其他"),
    CARFACILITY(4L, "车内设施"),
    METERCODE(5L, "仪表"),
    ENGINECODE(6L, "发动机"),
    CHASSISCODE(7L, "底盘"),
    OILCODE(8L, "油表");


    private Long index;
    private String name;

    // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
    PrecheckItemTypeEnum(Long index, String name) {
        this.index = index;
        this.name = name;
    }

    public Long getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
