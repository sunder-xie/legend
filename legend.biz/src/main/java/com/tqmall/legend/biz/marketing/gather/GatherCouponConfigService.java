package com.tqmall.legend.biz.marketing.gather;

import com.tqmall.legend.entity.marketing.gather.GatherCouponConfig;

import java.util.List;

/**
 * Created by wushuai on 16/12/16.
 */
public interface GatherCouponConfigService {
    /**
     * 根据批次号查询
     * @param allotSn
     * @return
     */
    List<GatherCouponConfig> selectByAllotSn(String allotSn);

    GatherCouponConfig selectById(Long gatherCouponConfigId);

    int updateById(GatherCouponConfig gatherCouponConfig);

    int insert(GatherCouponConfig gatherCouponConfig);
}
