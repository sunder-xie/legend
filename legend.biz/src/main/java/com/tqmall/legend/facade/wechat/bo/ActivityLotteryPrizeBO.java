package com.tqmall.legend.facade.wechat.bo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author kang.zhao@tqmall.com
 * @version 1.0 2016/10/19
 */
@Getter
@Setter
public class ActivityLotteryPrizeBO {
    private Long id;

    /**
     * 抽奖设置ID
     * wechat_activity_lottery表ID
     */
    private Long lotteryId;

    /**
     * 奖品名称
     */
    private String prizeName;

    /**
     * 奖品类型:0 谢谢参与 1 云修优惠券 2保险优惠券
     */
    private Integer prizeType;

    /**
     * 优惠券ID，当奖品类型为优惠券时使用
     */
    private Long couponId;

    /**
     * 抽奖设置的奖品数量
     */
    private Integer prizeNum;

    /**
     * 已领取的奖品数量
     */
    private Integer receivedPrizeNum;

    /**
     * 奖品中奖概率
     */
    private BigDecimal probabilityOfPrize;

    /**
     * 奖品在用状态:1 生效 0 失效
     */
    private Integer prizeStatus;

    /**
     * 奖品排序
     */
    private Integer prizeOrderIndex;
}
