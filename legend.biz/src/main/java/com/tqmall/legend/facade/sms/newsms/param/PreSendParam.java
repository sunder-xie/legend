package com.tqmall.legend.facade.sms.newsms.param;

import java.util.Collection;
import java.util.List;

public class PreSendParam {
    private final String smsTemplate;
    private final Long shopId;
    private final List<Long> carIds;
    private int position;

    public PreSendParam(String smsTemplate, Long shopId, List<Long> carIds, int position) {
        this.smsTemplate = smsTemplate;
        this.shopId = shopId;
        this.carIds = carIds;
        this.position = position;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public Long getShopId() {
        return shopId;
    }

    public List<Long> getCarIds() {
        return carIds;
    }

    public void addCarIds(Collection<Long> carIds) {
        this.carIds.addAll(carIds);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
