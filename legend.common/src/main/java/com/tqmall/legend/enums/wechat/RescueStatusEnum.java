package com.tqmall.legend.enums.wechat;

/**
 * Created by pituo on 16/8/17.
 */
public enum RescueStatusEnum {
    UNTREATED(0,"未处理"),
    CONFIRM(1,"已确认"),
    CANCEL(2,"已取消");

    private final Integer value;
    private final String statusName;

    private RescueStatusEnum(Integer value, String statusName) {
        this.value = value;
        this.statusName = statusName;
    }

    public Integer getValue() {
        return value;
    }

    public String getStatusName() {
        return statusName;
    }
}
