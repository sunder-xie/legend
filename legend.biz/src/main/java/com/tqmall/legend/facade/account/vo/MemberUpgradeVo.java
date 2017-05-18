package com.tqmall.legend.facade.account.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wanghui on 04/01/2017.
 */
@Data
public class MemberUpgradeVo {
    /**
     * 会员卡姓名
     */
    private String customerName;
    /**
     * 客户手机号
     */
    private String mobile;
    /**
     * 客户账户id
     */
    private Long accountId;
    /**
     * 卡内余额
     */
    private BigDecimal cardBalance;
    /**
     * 服务顾问
     */
    private Long receiver;
    /**
     * 服务顾问姓名
     */
    private String receiverName;
    /**
     * 原会员卡类型名称
     */
    private String oldCardTypeName;
    /**
     * 升级后的会员卡类型名称
     */
    private String newCardTypeName;
}
