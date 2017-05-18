package com.tqmall.legend.facade.statistics.vo;

import lombok.Data;

/**
 * Created by zsy on 16/8/10.
 * 生态首页统计数据vo对象
 */
@Data
public class StatisticsHomeVo {
    //微信公众号下的待处理预约单数
    private Integer appointCount = 0;
    //待结算工单数
    private Integer orderCount = 0;
    //本月新增粉丝数
    private Integer followerCount = 0;
}
