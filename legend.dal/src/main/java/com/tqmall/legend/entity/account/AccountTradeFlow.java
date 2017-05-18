package com.tqmall.legend.entity.account;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class AccountTradeFlow extends BaseEntity {
    public static final Integer CONSUMETYPE_RECHARGE=1;
    public static final Integer CONSUMETYPE_RECHARGE_REVERSE=2;
    public static final Integer CONSUMETYPE_CONSUME=3;
    public static final Integer CONSUMETYPE_CONSUME_REVERSE=4;
    public static final Integer CONSUMETYPE_CREATE_CARD=5;
    public static final Integer CONSUMETYPE_CREATE_CARD_REVERSE=6;

    //1:充值 2：充值撤销 3：消费 4：消费撤销 5：办卡 6：退卡
    private Long shopId;//门店id
    private Long orderId;//工单id
    private BigDecimal amount;//金额
    private BigDecimal payAmount;//实付金额
    private String paymentName;//支付方式名
    private Long paymentId;//支付方式
    private String flowSn;//流水号
    private Long reverseId;//反充对应流水id
    private Integer isReversed;//'是否撤销
    private Integer consumeType;//'消费类型
    private Long accountId;//账户id
    private String remark;//备注
    private Integer tradeType;//交易类型：1:优惠券2:计次卡3:会员卡4:多种类型
    private String customerName;//客户姓名
    private String mobile;//手机号
    private BigDecimal cardBalance;//卡内余额
    private String couponExplain;//优惠券变更说明
    private String serviceExplain;//服务项目变更说明
    private String cardExplain;//会员卡变更说明

    private String consumeTypeName;//消费类型名
    private String tradeTypeName;//交易类型名

    private Long receiver;//服务顾问
    private String receiverName;//服务顾问名字
    private String operatorName;//操作人名字
    private String orderSn;

    //卡、券充值报表
    private AccountInfo accountInfo;//账户信息
    private MemberCard memberCard;//会员卡
    private AccountCombo accountCombo;//计次卡
    private AccountCoupon accountCoupon;//优惠券

    private String importFlag;//导入标记

    private String cardTypeName; // 会员卡类型名称

    public String getGmtCreateStr(){
        if(gmtCreate != null){
            return DateUtil.convertDateToYMD(gmtCreate);
        }
        return DateUtil.convertDateToYMD(new Date());
    }
}

