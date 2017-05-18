package com.tqmall.legend.enums.shop;

/**
 * Created by sven on 2017/1/11.
 */
public enum ShopVersionEnum {
    OLD(0, "old", "老版"),
    NEW(1, "new", "新版"),
    SWITCH(2, "switch", "新老版本切换");
    private Integer code;
    private String value;
    private String message;

    ShopVersionEnum(Integer code, String value, String message) {
        this.code = code;
        this.value = value;
        this.message = message;
    }

    public static String getValueByCode(Integer code) {
        if (code == null) {
            return OLD.getValue();
        }
        for (ShopVersionEnum versionEnum : values()) {
            if (versionEnum.getCode() == code) {
                return versionEnum.getValue();
            }
        }
        return OLD.getValue();
    }

    public static String getVersion(Integer code, Integer defaultCode) {
        if (code == NEW.getCode()) {
            return NEW.getValue();
        }
        if (code == OLD.getCode()) {
            return OLD.getValue();
        }
        if (defaultCode == NEW.getCode()) {
            return NEW.getValue();
        }
        return OLD.getValue();
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
