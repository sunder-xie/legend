package com.tqmall.legend.enums.serviceInfo;

/**
 * Created by twg on 16/8/26.
 */
public enum ServiceInfoAppPublishStatusEnum {
    NOT_PUBLISH(0, "未发布"),
    PUBLISHED(1, "已发布");

    private final int code;
    private final String name;

    private ServiceInfoAppPublishStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
