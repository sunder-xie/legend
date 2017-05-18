package com.tqmall.legend.biz.shop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.entity.shop.ShopWxpayConfig;
import com.tqmall.legend.object.param.shop.ShopWxpayConfigSaveParam;

import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 16/10/17.
 */
public interface ShopWxpayConfigService {

    public List<ShopWxpayConfig> select(Map<String, Object> searchParams);

    public Integer insert(ShopWxpayConfig shopWxpayConfig);

    public Integer updateById(ShopWxpayConfig shopWxpayConfig);

    /**
     * 保存门店在线支付配置
     * @param shopWxpayConfigSaveParam
     * @param ShopId
     * @param applyRecordId 申请记录id
     * @return
     */
    public Result<String> saveWxpayConfig(ShopWxpayConfigSaveParam shopWxpayConfigSaveParam, Long ShopId, Long applyRecordId);
}
