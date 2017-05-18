package com.tqmall.legend.entity.account;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class
AccountSuite extends BaseEntity {

    private Long shopId;//门店id
    private Integer couponSource;//来源：0充值1赠送
    private Long accountId;//账户id
    private Long couponSuiteId;//优惠劵套餐id
    private String couponSuiteName;//优惠劵套餐名称
    private BigDecimal amount;//收款金额
    private Long flowId;//充值流水id
    private String flowSn;//流水号
    private String customerName;// 车主姓名
    private String mobile;// 车主电话
    private String operatorName;
    private List<AccountCoupon> coupons = Lists.newArrayList();

    public enum SourceEnum {
        CHARGE(0, "充值"),
        GIFT(1,"赠送"),
        CHARGE_REVERESE(2,"充值撤销");

        private final int code;
        private final String alias;

        SourceEnum(int code, String alias) {
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

    public String getGmtCreateStr() {
        if (gmtCreate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(gmtCreate);
        }
        return null;
    }

}

