package com.tqmall.legend.entity.shop;

/**
 * Created by feilong.li on 16/10/17.
 */
public enum ShopApplyStatusEnum {

    SQZ(0, "申请中"),
    DJZ(1, "对接中"),
    ZD(2, "中断"),
    YKT(3, "已开通"),
    CSTG(4, "测试通过");

    private Integer code;
    private String name;

    private ShopApplyStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getMesByCode(Integer code){
        ShopApplyStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            ShopApplyStatusEnum statusEnum = arr[i];
            if(statusEnum.getCode().equals(code)){
                return statusEnum.getName();
            }
        }
        return null;
    }

}
