package com.tqmall.legend.enums.sell;

/**
 * Created by xiangdong.qu on 17/2/23 17:29.
 */
public enum SellOrderHandleStatusEnum {
    HANDLE_STATUS_NO(0, "待处理"),
    HANDLE_STATUS_SUCCESS(1, "处理成功"),
    HANDLE_STATUS_FALSE(2, "处理失败");

    private Integer handleStatus;
    private String handleStatusName;

    private SellOrderHandleStatusEnum(Integer handleStatus, String handleStatusName) {
        this.handleStatus = handleStatus;
        this.handleStatusName = handleStatusName;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public String getHandleStatusName() {
        return handleStatusName;
    }
}
