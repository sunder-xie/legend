package com.tqmall.legend.entity.account;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by majian on 16/6/2.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AccountComboFlowDetail extends BaseEntity {

    public static final int RECHARGE = 1;
    public static final int CONSUME = 3;
    public static final int REVERSE_CONSUME = 4;

    private Long shopId;//门店id
    private Long comboId;//计次卡id
    private Long serviceId;//服务项目id
    private String serviceName;//服务项目名
    private Integer changeCount;//计次卡服务项目变化数
    private Integer consumeType;//消费类型1:充值3：消费
    private Long accountTradeFlowId;//账户交易流水id
    private String comboName;//套餐名称

    public enum ConsumeTypeEnum {
        CHARGE(1, "充值"),
        CHARGE_REVERT(2, "充值撤销"),
        CONSUME(3, "消费"),
        CONSUME_REVERT(4, "消费撤销"),
        IMPORT(5,"导入");

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
    }

}