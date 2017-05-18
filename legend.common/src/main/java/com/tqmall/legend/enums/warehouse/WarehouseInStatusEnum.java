package com.tqmall.legend.enums.warehouse;

/**
 * Created by sven on 16/6/14.
 */
public enum WarehouseInStatusEnum {
    LZRK(0L, "蓝字入库"),
    HZRK(1L, "红字入库"),
    HZZF(2L, "红字作废"),
    LZZF(3L, "蓝字作废"),
    DRAFT(4L,"草稿");
    private final Long code;
    private final String message;

    WarehouseInStatusEnum(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public static String getMessageByName(String name){
        for(WarehouseInStatusEnum warehouseInStatusEnum:WarehouseInStatusEnum.values()){
            if(warehouseInStatusEnum.name().equals(name)){
                return warehouseInStatusEnum.getMessage();
            }
        }
        return null;
    }
}
