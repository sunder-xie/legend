package com.tqmall.legend.entity.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class AccountCouponFlowDetail extends BaseEntity {

    private Long shopId;//门店id
    private Long couponId;//优惠劵id
    private String couponCode;//优惠劵码
    private Integer changeCount;//优惠劵变化数量
    private Integer consumeType;//消费类型1:充值3：消费
    private String couponName;//优惠券名
    private Long accountTradeFlowId;//账户交易流水id

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

