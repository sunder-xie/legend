package com.tqmall.legend.facade.wechat.bo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 微信公众号抽奖活动设置
 *
 * @author kang.zhao@tqmall.com
 * @version 1.0 2016/10/19
 */
@Getter
@Setter
public class ActivityLotteryBO {
    private Long id;

    /**
     * 门店ID
     */
    private Long shopId;

    /**
     * 抽奖时间设置:1 长期 2 自定义时间
     */
    private Integer lotteryTimeType;

    /**
     * lotteryTimeType=2时生效
     *
     * 活动开始时间
     */
    private Date lotteryStartTime;

    /**
     * lotteryTimeType=2时生效
     *
     * 活动结束时间
     */
    private Date lotteryEndTime;

    /**
     * 抽奖次数设置:1 只抽一次 2 每天一次
     */
    private Integer timesSetting;

    /**
     * 抽奖设置的奖品件数类型 1 6件 2 8件
     */
    private Integer prizeNumType;

    /**
     * 谢谢参与的概率
     */
    private BigDecimal probabilityOfThanks;

    /**
     * 抽奖开关状态:0 关闭 1 开启
     */
    private Integer switchStatus;

    /**
     * 发布状态:0 草稿 1 已发布
     */
    private Integer pubStatus;

    /**
     * 前端样式 0 ,1,2
     */
    private Integer lotteryStyle;

    /**
     * 分享+1开关，0 关闭 1开启
     */
    private Integer shareSwitch;

    /**
     * 编辑后是否可再次参与开关，0不可再抽，1可以再抽
     */
    private Integer joinAgainSwitch;


    /**
     * 工单完成是否增加抽奖次数，0 不增加 ，1增加
     */
    private Integer orderFinishSwitch;


    /**
     * 预约成功是否增加抽奖次数，0 不增加 ，1增加
     */
    private Integer appointSuccessSwitch;



    /**
     * 抽奖活动设置的奖品列表
     */
    private List<ActivityLotteryPrizeBO> activityLotteryPrizeBOList;
}
