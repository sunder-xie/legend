package com.tqmall.legend.biz.enums;

/**
 * 生产线类型枚举
 * Created by yanxinyin on 16/7/8.
 */
public enum ProductionLineTypeEnum {
    KXX(1, "快修线"),
    SGX(2, "事故线"),
    KPX(3, "快喷线"),
    XSGX(4,"小钣金事故线");


    private final Integer type;
    private final String name;

    ProductionLineTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static String getNameByType(Integer code) {
        for (ProductionLineTypeEnum e : ProductionLineTypeEnum.values()) {
            if (code.equals(e.getType())) {
                return e.getName();
            }
        }
        return null;
    }
}
