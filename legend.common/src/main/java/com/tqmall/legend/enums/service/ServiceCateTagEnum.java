package com.tqmall.legend.enums.service;

/**
 * Created by zsy on 16/09/28.
 * 服务标准类目：1保养2洗车3美容4检修5钣喷6救援7其他
 */
public enum ServiceCateTagEnum {
    BY(1, "保养"),
    XC(2, "洗车"),
    MR(3, "美容"),
    JX(4, "检修"),
    BP(5, "钣喷"),
    JY(6, "救援"),
    QT(7, "其他");

    private Integer code;
    private String name;

    private ServiceCateTagEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
