package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.ShopApplyRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 16/10/17.
 */
public interface ShopApplyRecordService {

    public Integer selectCount(Map<String, Object> searchParams);

    public List<ShopApplyRecord> select(Map<String, Object> searchParams);

    public ShopApplyRecord selectById(Long id);

    public Integer insert(ShopApplyRecord shopApplyRecord);

    public Integer updateById(ShopApplyRecord shopApplyRecord);
}
