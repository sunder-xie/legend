package com.tqmall.legend.enums.activity;

/**
 * Created by wushuai on 16/4/6.
 * 对应legend.legend_activity_channel.channel_source字段
 */
public enum ActivityChannelSourceEnum {

    LEGENDM(0,"legendm"),
    MEGA(1,"mega");

    private final Integer value;
    private final String name;

    private ActivityChannelSourceEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
