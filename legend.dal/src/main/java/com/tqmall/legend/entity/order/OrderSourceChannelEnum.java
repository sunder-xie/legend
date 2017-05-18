package com.tqmall.legend.entity.order;

/**
 * 工单渠道来源
 * 0:web,1:android,2:ios
 * <p/>
 * Created by dongc on 15/7/20.
 */
public enum OrderSourceChannelEnum {
    WEB(0, "web渠道"),
    IOS(2, "ios渠道"),
    ANDROID(1, "andriod渠道");

    private final int code;
    private final String sName;

    private OrderSourceChannelEnum(int code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public int getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(int code) {
        for (OrderSourceChannelEnum e : OrderSourceChannelEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }
}
