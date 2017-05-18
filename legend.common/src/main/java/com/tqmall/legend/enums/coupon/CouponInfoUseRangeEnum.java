package com.tqmall.legend.enums.coupon;

/**
 * Created by wushuai on 16/07/12.
 * 对应数据库字段legend_coupon_info.use_range
 */
public enum CouponInfoUseRangeEnum {
    QCTY(0, "全场通用"),
    ZXFUGS(1, "全部服务"),
    ZXZDFWXMDZ(2, "只限指定服务项目打折");

    private final Integer value;
    private final String name;

    private CouponInfoUseRangeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    //获值名称
    public static String getNameByValue(Integer value) {
        if (value == null) {
            return "";
        }
        CouponInfoUseRangeEnum[] statusEnums = CouponInfoUseRangeEnum.values();
        for (CouponInfoUseRangeEnum statusEnum : statusEnums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.getName();
            }
        }
        return "";
    }

    public static CouponInfoUseRangeEnum value(int value) {
        for (CouponInfoUseRangeEnum v : values()) {
            if (v.getValue() == value) {
                return v;
            }
        }
        return null;
    }
}
