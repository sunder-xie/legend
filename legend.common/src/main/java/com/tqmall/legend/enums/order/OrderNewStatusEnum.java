package com.tqmall.legend.enums.order;

/**
 * Created by sven on 16/4/22.
 */
public enum OrderNewStatusEnum {
    DBJ("CJDD", 0, "待报价"),
    YBJ("DDBJ", 0, "已报价"),
    DBJANDYBJ("CJDD,DDBJ", 0, "报价工单"),
    YPG("FPDD,DDSG", 0, "已派工"),
    YWG("DDWC", 0, "已完工"),
    YGZ("DDYFK", 1, "已挂账"),
    DDYFK("DDYFK", 2, "已结清"),
    WXDD("WXDD", 0, "无效");


    private final String orderStatus;
    private Integer payStatus;
    private final String orderStatusName;

    OrderNewStatusEnum(String orderStatus, Integer payStatus, String orderStatusName) {
        this.orderStatus = orderStatus;
        this.payStatus = payStatus;
        this.orderStatusName = orderStatusName;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public static Integer getPayStatusByOrderStatus(String orderStatus) {

        for (OrderNewStatusEnum OrderNewStatus : OrderNewStatusEnum.values()) {
            if (OrderNewStatus.getOrderStatus().equals(orderStatus)) {
                return OrderNewStatus.getPayStatus();
            }
        }
        return null;
    }

    public static String getOrderStatusByName(String name) {
        for (OrderNewStatusEnum OrderNewStatus : OrderNewStatusEnum.values()) {
            if (OrderNewStatus.name().equals(name)) {
                return OrderNewStatus.getOrderStatus();
            }
        }
        return null;
    }

    public static Integer getPayStatusByName(String name) {
        for (OrderNewStatusEnum OrderNewStatus : OrderNewStatusEnum.values()) {
            if (OrderNewStatus.name().equals(name)) {
                return OrderNewStatus.getPayStatus();
            }
        }
        return null;
    }

    public static String getOrderStatusNameByName(String name) {
        for (OrderNewStatusEnum OrderNewStatus : OrderNewStatusEnum.values()) {
            if (OrderNewStatus.name().equals(name)) {
                return OrderNewStatus.getOrderStatusName();
            }
        }
        return null;
    }

    public static String getOrderStatusName(String orderStatus, Integer payStatus) {
        for (OrderNewStatusEnum OrderNewStatus : OrderNewStatusEnum.values()) {
            if (OrderNewStatus.getOrderStatus().contains(orderStatus) && OrderNewStatus.getPayStatus().equals(payStatus)) {
                return OrderNewStatus.getOrderStatusName();
            }
        }
        return null;
    }
}
