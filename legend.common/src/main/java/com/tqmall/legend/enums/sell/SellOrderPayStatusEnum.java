package com.tqmall.legend.enums.sell;

/**
 * Created by xiangdong.qu on 17/2/23 10:26.
 */
public enum SellOrderPayStatusEnum {
    PAY_STATUS_NO(0, "待支付"),
    PAY_STATUS_SUCCESS(2, "支付成功"),
    PAY_STATUS_FALSE(1, "支付失败");

    private Integer payStatus;
    private String payStatusName;

    private SellOrderPayStatusEnum(Integer payStatus, String payStatusName) {
        this.payStatus = payStatus;
        this.payStatusName = payStatusName;
    }

    public Integer getPayStatus(){
        return payStatus;
    }

    public String getPayStatusName(){
        return payStatusName;
    }
}
