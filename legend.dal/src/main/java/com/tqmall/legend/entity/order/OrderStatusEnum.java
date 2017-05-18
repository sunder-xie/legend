package com.tqmall.legend.entity.order;

/**
 * Created by lixiao on 14-11-4.
 */
public enum OrderStatusEnum {
    CJDD("CJDD", "创建订单", 1, "待报价"),
    DDBJ("DDBJ", "订单报价", 2, "已报价"),
    FPDD("FPDD", "分配订单", 3, "已派工"),
    DDSG("DDSG", "订单施工", 4, "修理中"),
    DDWC("DDWC", "订单完成", 5, "已完工"),
    DDYFK("DDYFK", "订单已付款", 6, "已结算"),
    WXDD("WXDD", "订单无效", -1, "无效");


    private String key;
    private final String value;
    private final int orderStatusInt;
    private final String orderStatusClientName;

    private OrderStatusEnum(String key, String value, int orderStatusInt, String orderStatusClientName) {
        this.key = key;
        this.value = value;
        this.orderStatusInt = orderStatusInt;
        this.orderStatusClientName = orderStatusClientName;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int getorderStatusInt() {
        return orderStatusInt;
    }


    public String getorderStatusClientName() {
        return orderStatusClientName;
    }

    public static String getorderStatusClientNameByKey(String key) {
        if (key != null) {
            for (OrderStatusEnum op : OrderStatusEnum.values()) {
                if (op.getKey().equals(key)) {
                    return op.getorderStatusClientName();
                }
            }
            return null;
        }
        return null;
    }


    public static Integer getorderStatusIntByKey(String key) {
        if (key != null) {
            for (OrderStatusEnum op : OrderStatusEnum.values()) {
                if (op.getKey().equals(key)) {
                    return op.getorderStatusInt();
                }
            }
        }
        return null;
    }

    /**
     * get enum by key
     *
     * @param key
     * @return OrderStatusEnum
     */
    public static OrderStatusEnum getOrderStatusEnum(String key) {
        if (key != null) {
            for (OrderStatusEnum e : OrderStatusEnum.values()) {
                if (e.getKey().equals(key)) {
                    return e;
                }
            }
        }
        return null;
    }


}
