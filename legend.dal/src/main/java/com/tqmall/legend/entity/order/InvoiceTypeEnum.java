package com.tqmall.legend.entity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 14-11-11.
 */

public enum InvoiceTypeEnum {
    BKP(0, "不开票"),
    PTFP(1, "普通发票"),
    ZZFP(2, "增值发票"),
    QTFP(3, "其它发票");

    private Integer type;
    private String value;

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    InvoiceTypeEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Map<Integer, String> getInvoiceTypeMap() {
        Map<Integer, String> itMap = new HashMap<>();
        for (InvoiceTypeEnum it : InvoiceTypeEnum.values()) {
            itMap.put(it.getType(), it.getValue());
        }
        return itMap;
    }

    public static Integer getTypeByValue(String value) {
        for (InvoiceTypeEnum it : values()) {
            if (it.getValue().equals(value)) {
                return it.getType();
            }
        }
        return InvoiceTypeEnum.BKP.getType();
    }

    public static String getValueByType(Integer type) {
        for (InvoiceTypeEnum it : values()) {
            if (it.getType().intValue() == type) {
                return it.getValue();
            }
        }
        return InvoiceTypeEnum.BKP.getValue();
    }

}
