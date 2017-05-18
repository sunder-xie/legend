package com.tqmall.legend.facade.sms.newsms;

/**
 * Created by majian on 16/12/2.
 */
public enum SendPositionEnum {
    UNKOWN(0, "未知"),
    CUSTOMER_ANALYSIS_TYPE(1,"客户营销-客户分析-客户类型分析"),
    CUSTOMER_ANALYSIS_LEVEL(2, "客户营销-客户分析-客户级别分析"),
    CUSTOMER_ANALYSIS_LOST(3, "客户营销-客户分析-客户流失分析"),
    CUSTOMER_MAINTAIN_NOTE(4, "客户营销-客情维护-提醒中心"),
    MARKETING_ACCURATE(5, "客户营销-营销中心-精准营销"),
    WECHAT_COUPON_SMS(6, "客户营销-微信公众号-关注送券-点击群发短信"),
    GATHER_PALN_SMS(7, "集客方案-盘活客户-发送短信");

    private int code;
    private String description;
    SendPositionEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SendPositionEnum valueOf(int code) {
        SendPositionEnum[] values = SendPositionEnum.values();
        for (SendPositionEnum value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("code out of bound");
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
