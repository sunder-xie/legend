package com.tqmall.legend.facade.statistics;

import com.tqmall.legend.facade.statistics.vo.StatisticsHomeVo;

/**
 * Created by zsy on 16/8/10.
 */
public interface StatisticsHomeFacade {
    /**
     * 获取生态首页统计数据
     * @return
     */
    StatisticsHomeVo getStatisticsHome(Long shopId);
}
