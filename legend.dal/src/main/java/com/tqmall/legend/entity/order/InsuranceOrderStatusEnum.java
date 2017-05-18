package com.tqmall.legend.entity.order;

/**
 * 保险工单状态值
 * 审核中、审核成功、审核失败、已打款
 * <p/>
 * OrderGoodType Enum
 */
public enum InsuranceOrderStatusEnum {
    SAVED(0, "已保存"),
    AUDITING(1, "审核中"),
    PASS(2, "审核成功"),
    NOTPASS(3, "审核失败"),
    paid(4, "已打款");

    private final int code;
    private final String sName;

    private InsuranceOrderStatusEnum(int code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public int getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(int code) {
        for (InsuranceOrderStatusEnum e : InsuranceOrderStatusEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }

    public static InsuranceOrderStatusEnum[] getMessages() {
        InsuranceOrderStatusEnum[] arr = values();
        return arr;
    }
}
