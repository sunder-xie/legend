package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.ServiceGoodsSuite;

import java.util.List;
import java.util.Map;

/**
 * @Author : Freeway <jiajie.qian@tqmall.com>
 * @Created : 2014/11/9 22:23
 */
public interface ServiceGoodsSuiteService {
    /**
     * 获取列表
     *
     * @param searchMap
     * @return
     */
    public List<ServiceGoodsSuite> select(Map<String, Object> searchMap) ;


    public List<ServiceGoodsSuite> selectByServiceIds(Long[] id);

    public ServiceGoodsSuite selectByServiceId(Long id);

    public ServiceGoodsSuite selectByServiceId(Long id, Long shopId);

    public Integer add(ServiceGoodsSuite serviceGoodsSuite);

    public Integer deleteByServiceIdAndShopId(Long serviceId, Long shopId);

}
