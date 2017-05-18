package com.tqmall.legend.object.enums.appoint;

/**
 * Created by wushuai on 16/4/6.
 */
public enum AppointStatusEnum {


    /**
     * 预约单状态枚举类
     */
    INVALID("无效",-1,""),
    TO_CONFIRM("待确认", 0,""),
    APPOINT_SUCCESS("已确认", 1,""),
    ORDER_CREATE("已开单", 2,""),
    CHEZHU_CANCEL("车主取消", 3, "7"),//Constants.APPOINT_C_CANCEL
    SHOP_CANCEL("门店取消", 4, "8"),//Constants.APPOINT_B_CANCEL
    WECHAT_CANCEL("微信取消", 5,"");


    // 成员变量
    private final String name;

    private final Integer index;

    private final String pushMsgType;//置为当前状态时的消息推送类型

    AppointStatusEnum(String name, Integer index,String pushMsgType) {
        this.name = name;
        this.index = index;
        this.pushMsgType = pushMsgType;
    }

    //获取状态名称
    public static String getNameByStatus(Integer status) {
        if (status == null) {
            return "";
        }
        AppointStatusEnum[] statusEnums = AppointStatusEnum.values();
        for (AppointStatusEnum statusEnum : statusEnums) {
            if (statusEnum.getIndex().equals(status)) {
                return statusEnum.getName();
            }
        }
        return "";
    }

    //获取状态名称
    public static String getPushMsgTypeByStatus(Integer status) {
        if (status == null) {
            return "";
        }
        AppointStatusEnum[] statusEnums = AppointStatusEnum.values();
        for (AppointStatusEnum statusEnum : statusEnums) {
            if (statusEnum.getIndex().equals(status)) {
                return statusEnum.getPushMsgType();
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    public String getPushMsgType() {
        return pushMsgType;
    }

}
