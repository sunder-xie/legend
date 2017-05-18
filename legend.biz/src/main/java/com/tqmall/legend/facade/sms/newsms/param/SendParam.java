package com.tqmall.legend.facade.sms.newsms.param;

public class SendParam {
    private final Long shopId;
    private final Long userId;
    private final String operator;
    private final String UUID;

    public SendParam(Long shopId, Long userId, String operator, String UUID) {
        this.shopId = shopId;
        this.userId = userId;
        this.operator = operator;
        this.UUID = UUID;
    }

    public Long getShopId() {
        return shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getOperator() {
        return operator;
    }

    public String getUUID() {
        return UUID;
    }
}
