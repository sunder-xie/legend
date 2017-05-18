package com.tqmall.legend.enums.config;

/**
 * Created by wushuai on 16/12/08.
 */
public enum ShopWorkTimeConfKeyEnum {

    /**
     * 门店上班时间配置conf_key的枚举
     */
    SIGN_IN("3","上班时间"),
    SIGN_OFF("4","下班时间"),
    NOON_BREAK_START("5","午休开始时间"),
    NOON_BREAK_END("6","午休结束时间");


    // 成员变量
    private final String confKey;

    private final String confKeyDes;//confKey 描述


    ShopWorkTimeConfKeyEnum(String confKey, String confKeyDes) {
        this.confKey = confKey;
        this.confKeyDes = confKeyDes;
    }

    public String getConfKey() {
        return confKey;
    }
}
