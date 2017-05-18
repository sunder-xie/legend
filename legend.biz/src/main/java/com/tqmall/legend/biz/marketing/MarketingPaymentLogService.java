package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.bi.entity.MarketingPaymentLog;
import com.tqmall.legend.biz.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by wjc on 2015/11/24.
 */
public interface MarketingPaymentLogService extends BaseService{
    public List<MarketingPaymentLog> select(Map<String, Object> searchParams);
}
