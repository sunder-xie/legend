package com.tqmall.legend.facade.service;

import com.tqmall.legend.biz.shop.bo.ServiceStatisBo;

import java.util.Collection;
import java.util.List;

/**
 * Created by majian on 16/10/17.
 */
public interface ShopServiceStatisAttachProcessor {
    void attach(Collection<Long> serviceIds, List<ServiceStatisBo> serviceStatisBoList);
}
