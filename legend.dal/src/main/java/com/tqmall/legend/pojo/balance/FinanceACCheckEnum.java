package com.tqmall.legend.pojo.balance;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiangDong.qu on 16/5/16.
 */
public enum FinanceACCheckEnum {
    DSH(0, "待审核"),
    SHCG(1, "审核成功"),
    SHSB(2, "审核失败");
    private Integer code;
    private String name;

    private FinanceACCheckEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code) {
        if (code != null) {
            for (FinanceACCheckEnum op : FinanceACCheckEnum.values()) {
                if (code.compareTo(op.getCode()) == 0) {
                    return op.getName();
                }
            }
            return null;
        }
        return null;
    }
}
