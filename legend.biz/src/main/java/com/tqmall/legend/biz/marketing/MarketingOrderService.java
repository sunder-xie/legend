package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15/6/10.
 */
public interface MarketingOrderService {

    public Integer create(MarketingOrder marketingOrder);

    public List<MarketingOrder> select(Map map);

    public Integer update(MarketingOrder marketingOrder);
}
