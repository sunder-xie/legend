package com.tqmall.legend.biz.customer;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2015/6/8.
 */

public interface CustomerSourceService {
    /**
     * 获取店铺对应的客户来源 可选信息
     * @param param 主要参数是 shopId，例如 key:shopId  value:1
     * @return 客户来源列表，或者空字符串
     * */
    public List<CustomerSource> getCustomerSourceList(Map<String,Object> param);

    /**
     * 添加客户来源信息
     * @param customerSource
     * @return 成功或失败
     * */
    public Result addCustomerSource(CustomerSource customerSource);

    /**
     * 获取客户来源信息
     * @param shopId 店铺id
     * @param source 客户来源
     * @return
     * */
    public CustomerSource getCustomerSource(String source, long shopId);
}
