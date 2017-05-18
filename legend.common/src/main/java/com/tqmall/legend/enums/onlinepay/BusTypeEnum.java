package com.tqmall.legend.enums.onlinepay;

/**
 * 在线支付业务类型枚举
 * Created by wushuai on 16/6/14.
 */
public enum BusTypeEnum {
    SMS(1,"客户营销短信充值"),
    WECHAT(2,"微信公众好费用充值");

    private final Integer busType;
    private final String busName;

    private BusTypeEnum(Integer busType,String busName) {
        this.busType = busType;
        this.busName = busName;
    }

    public Integer getBusType() {
        return busType;
    }

    public String getBusName() {
        return busName;
    }
}
