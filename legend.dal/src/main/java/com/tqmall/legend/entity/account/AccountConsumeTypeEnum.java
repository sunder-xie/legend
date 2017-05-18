package com.tqmall.legend.entity.account;

import com.tqmall.legend.entity.settlement.ConsumeTypeEnum;

/**
 */
public enum AccountConsumeTypeEnum {
    CHARGE(1, "充值"),
    CHARGE_REVERT(2, "充值撤销"),
    CONSUME(3, "消费"),
    CONSUME_REVERT(4, "消费撤销"),
    HANDLE_CARD(5, "办卡"),
    CANCEL_CARD(6, "退卡"),
    CONSUME_MODIFY(7, "积分修改"),
    INIT(8,"导入"),
    GRANT_REVERSE(9,"办卡撤销"),
    UPGRADE_CARD(10,"会员卡升级");

    private final int code;
    private final String alias;

    AccountConsumeTypeEnum(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public int getCode() {
        return this.code;
    }

    public String getAlias() {
        return this.alias;
    }

    public static String getAliasByCode(int code) {
        for (ConsumeTypeEnum e : ConsumeTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getAlias();
            }
        }
        return null;
    }
}
