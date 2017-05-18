package com.tqmall.legend.enums.warehouse;

/**
 * Created by sven on 16/8/24.
 */
public enum WarehouseOutStatusEnum {

    LZCK(0L, "蓝字入库"),
    HZCK(1L, "红字入库"),
    CK_HZZF(2L, "红字作废"),
    CK_LZZF(3L, "蓝字作废");
    private final Long code;
    private final String message;

    WarehouseOutStatusEnum(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessageByName(String name) {
        for (WarehouseOutStatusEnum status : WarehouseOutStatusEnum.values()) {
            if (status.name().equals(name)) {
                return status.getMessage();
            }
        }
        return null;
    }


}
