package com.tqmall.legend.object.result.news;

/**
 * Created by litan on 17/4/13.
 */
public enum NewClassifyEnum {
    REPORT(1,"简报"),
    NEWS(2,"咨询");

    private final int code;
    private final String alias;

    NewClassifyEnum(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public int getCode() {
        return this.code;
    }

    public String getAlias() {
        return this.alias;
    }

    public static String getAliasByCode(int code) {
        for (NewClassifyEnum e : NewClassifyEnum.values()) {
            if (e.getCode() == code) {
                return e.getAlias();
            }
        }
        return null;
    }
}
