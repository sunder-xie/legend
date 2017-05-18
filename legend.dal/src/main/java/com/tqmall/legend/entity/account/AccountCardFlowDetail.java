package com.tqmall.legend.entity.account;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class AccountCardFlowDetail extends BaseEntity {

    public enum ConsumeTypeEnum {
        CHARGE(1, "充值"),
        CHARGE_REVERT(2, "充值撤销"),
        CONSUME(3, "消费"),
        CONSUME_REVERT(4, "消费撤销"),
        HANDLE_CARD(5, "办卡"),
        INIT(6, "导入"),
        GRANT_REVERSE(7,"办卡撤销"),
        BACK(8,"退卡"),
        UPGRADE(9,"会员卡升级");

        private final int code;
        private final String alias;

        ConsumeTypeEnum(int code, String alias) {
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

    private Long shopId;//门店id
    private Long cardId;//会员卡id
    private BigDecimal changeAmount;//会员卡金额变化量
    private BigDecimal cardBalance;//会员卡余额
    private Integer consumeType;//消费类型1:充值2：充值撤销3：消费4：消费撤销
    private Long accountTradeFlowId;//账户交易流水id

}

