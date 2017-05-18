package com.tqmall.legend.entity.warehouse;

/**
 * 盘点记录状态enum
 */
public enum InventoryStatusEnum {

    DRAFT(1, "草稿"),
    FORMAL(2, "正式");

    private final int code;
    private final String message;

    InventoryStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMesByCode(int code) {
        for (InventoryStatusEnum e : InventoryStatusEnum.values()) {
            if (e.getCode() == code) {
                return e.getMessage();
            }
        }
        return null;
    }
}
