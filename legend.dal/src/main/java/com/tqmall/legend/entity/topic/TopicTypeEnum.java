package com.tqmall.legend.entity.topic;

/**
 * Created by zsy on 2015/4/28.
 */
public enum TopicTypeEnum {
    REPAIRCASE(Integer.valueOf(1), "维修案例"),
    BASICDATA(Integer.valueOf(2), "基础资料");

    private Integer code;
    private String message;

    private TopicTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMesByCode(Integer code) {
        TopicTypeEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            TopicTypeEnum iEnum = arr[i];
            if (iEnum.getCode().equals(code)) {
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static TopicTypeEnum[] getMessages() {
        TopicTypeEnum[] arr = values();
        return arr;
    }

    /**
     * get enum by key
     *
     * @param key
     * @return OrderStatusEnum
     */
    public static TopicTypeEnum getTopicTypeEnum(Integer code) {
        if (code != null) {
            for (TopicTypeEnum e : TopicTypeEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
        }
        return null;
    }
}
