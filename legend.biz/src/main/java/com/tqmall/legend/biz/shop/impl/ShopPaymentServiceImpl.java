package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.shop.ShopApplyRecordService;
import com.tqmall.legend.biz.shop.ShopPaymentService;
import com.tqmall.legend.entity.shop.ShopApplyRecord;
import com.tqmall.legend.entity.shop.ShopApplyStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/10/26.
 */
@Slf4j
@Service
public class ShopPaymentServiceImpl implements ShopPaymentService {
    @Autowired
    private ShopApplyRecordService shopApplyRecordService;

    @Override
    public Boolean hasApplyPayment(Long shopId, Integer applyType) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("applyType",applyType);
        param.put("applyStatus", ShopApplyStatusEnum.CSTG.getCode());
        List<ShopApplyRecord> shopApplyRecords = shopApplyRecordService.select(param);
        if(CollectionUtils.isEmpty(shopApplyRecords)){
            return false;
        }
        return true;
    }
}
