package com.tqmall.legend.biz.marketing.gather.impl;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.marketing.gather.GatherCouponFlowDetailService;
import com.tqmall.legend.dao.marketing.gather.GatherCouponFlowDetailDao;
import com.tqmall.legend.entity.marketing.gather.GatherCouponFlowDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wushuai on 16/12/16.
 */
@Service
public class GatherCouponFlowDetailServiceImpl implements GatherCouponFlowDetailService {
    @Autowired
    private GatherCouponFlowDetailDao gatherCouponFlowDetailDao;

    @Override
    public GatherCouponFlowDetail getLatelyFlow(long shopId, String customerMobile, Integer days) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("customerMobile", customerMobile);
        //只需要最新一条
        param.put("sorts", new String[]{"gmt_modified desc"});
        param.put("offset", 0);
        param.put("limit", 1);
        if (days != null) {
            Date startDate = DateUtil.addDate(new Date(), -days);
            param.put("gmtModifiedGT", startDate);
        }
        List<GatherCouponFlowDetail> gatherCouponFlowDetailList = gatherCouponFlowDetailDao.select(param);
        if (CollectionUtils.isEmpty(gatherCouponFlowDetailList)) {
            return null;
        }
        return gatherCouponFlowDetailList.get(0);
    }

    @Override
    public int selectCount(String mobile, Long gatherConfigId) {
        if (StringUtils.isBlank(mobile) || gatherConfigId==null) {
            return 0;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("customerMobile",mobile);
        param.put("gatherConfigId",gatherConfigId);
        return gatherCouponFlowDetailDao.selectCount(param);
    }

    @Override
    public int add(GatherCouponFlowDetail gatherCouponFlowDetail) {
        if (gatherCouponFlowDetail == null) {
            return 0;
        }
        return gatherCouponFlowDetailDao.insert(gatherCouponFlowDetail);
    }
}
