package com.tqmall.legend.facade.activity.vo;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.dandelion.wechat.client.dto.wechat.GameCouponStatisticsDTO;

/**
 * Created by wushuai on 16/8/27.
 */
@Setter
@Getter
public class GameCouponStatisticsVo extends GameCouponStatisticsDTO {
    private Integer durationDay;//活动进行天数
}
