package com.tqmall.legend.biz.marketing.gather;

import com.tqmall.legend.entity.marketing.gather.GatherCouponFlowDetail;

/**
 * Created by wushuai on 16/12/16.
 */
public interface GatherCouponFlowDetailService {

    /**
     * 查询客户最新一次领券流水
     * @param shopId 门店id
     * @param customerMobile 领券客户手机号
     * @param days 从多少天之前开始算起(为空则不限制)
     * @return
     */
    GatherCouponFlowDetail getLatelyFlow(long shopId, String customerMobile, Integer days);

    /**
     * @param mobile
     * @param gatherConfigId
     * @return
     */
    int selectCount(String mobile, Long gatherConfigId);

    int add(GatherCouponFlowDetail gatherCouponFlowDetail);
}
