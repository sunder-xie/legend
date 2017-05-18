package com.tqmall.legend.enums.warehouse;

/**
 * Created by sven on 16/8/26.
 */
public enum WarehouseOutTypeEnum {
    NBLY(1, "内部领用"),
    WBXS(2, "外部销售"),
    GDCK(3, "工单出库");
    private final Integer code;
    private final String message;

    WarehouseOutTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessageByName(String name) {
        for (WarehouseOutTypeEnum typeEnum : WarehouseOutTypeEnum.values()) {
            if (typeEnum.name().equals(name)) {
                return typeEnum.getMessage();
            }
        }
        return null;
    }


}
