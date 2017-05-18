package com.tqmall.legend.biz.account.bo;

import lombok.Data;

import java.util.Date;

@Data
public class CardUpgradeBo {
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 操作员id
     */
    private Long operatorId;

    /**
     * 会员卡id
     */
    private Long cardId;
    /**
     * 账户id
     */
    private Long accountId;
    /**
     * 待升级的会员卡类型
     */
    private Long newCardTypeId;
    /**
     * 会员卡过期时间
     */
    private Date expireTime;
}
