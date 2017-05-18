package com.tqmall.legend.entity.activity;

/**
 * Created by zsy on 2016/03/02.
 * 活动页面跳转
 */
public enum ActJumpUrlEnum {
    PABY(Integer.valueOf(1), "yqx/page/settlement/activity/maintain-act-bill", "yqx/page/settlement/activity/maintain-act-bill-excel"),//平安保养活动收入明细
    PABQ(Integer.valueOf(2), "yqx/page/settlement/activity/paint-act-bill", "yqx/page/settlement/activity/paint-act-bill-excel"),//平安补漆活动收入明细
    TMFW(Integer.valueOf(3), "yqx/page/settlement/activity/tmall-act-bill", "yqx/page/settlement/activity/tmall-act-bill-excel"),//天猫活动收入明细
    TAFW(Integer.valueOf(13), "yqx/page/settlement/activity/tianan-act-bill", "yqx/page/settlement/activity/tmall-act-bill-excel");//天安活动收入明细

    private Integer code;
    private String message;
    private String excel;

    private ActJumpUrlEnum(Integer code, String message, String excel) {
        this.code = code;
        this.message = message;
        this.excel = excel;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getExcel() {
        return this.excel;
    }

    public static String getMesByCode(Integer code) {
        ActJumpUrlEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            ActJumpUrlEnum iEnum = arr[i];
            if (iEnum.getCode().equals(code)) {
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static String getExcelByCode(Integer code) {
        ActJumpUrlEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            ActJumpUrlEnum iEnum = arr[i];
            if (iEnum.getCode().equals(code)) {
                return iEnum.getExcel();
            }
        }
        return null;
    }
}
