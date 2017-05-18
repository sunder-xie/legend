package com.tqmall.legend.entity.goods;

/**
 * 数据库记录状态 ENUM
 */
public enum DBStatusEnum {

    DELETED("Y", "已删除"),
    NODELETED("N", "未删除");

    private final String code;
    private final String sName;

    private DBStatusEnum(String code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public String getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(String code) {
        for (DBStatusEnum e : DBStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getsName();
            }
        }
        return null;
    }

    public static DBStatusEnum[] getMessages() {
        DBStatusEnum[] arr = values();
        return arr;
    }
}
