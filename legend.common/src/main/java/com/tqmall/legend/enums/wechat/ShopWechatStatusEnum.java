package com.tqmall.legend.enums.wechat;

/**
 * Created by wushuai on 16/6/17.
 */
public enum ShopWechatStatusEnum {
    UNPAID(0,"待支付"),
    PAID(1,"已支付"),
    SUBMITTED(2,"已提交"),
    REGISTERED(3,"已注册"),
    EXPIRED(4,"已到期"),
    TO_GRANT(5,"待授权"),
    GRANTLOCK(6,"授权缺失"),//没有获得所有必须权限
    DATA_INIT(7," 数据初始化中");

    private final Integer value;
    private final String statusName;

    private ShopWechatStatusEnum(Integer value, String statusName) {
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
