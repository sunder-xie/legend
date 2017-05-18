package com.tqmall.legend.biz.subsidy.impl;

import com.tqmall.legend.biz.subsidy.SubsidyGoodsNumService;
import com.tqmall.legend.entity.subsidy.SubsidyGoods;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiangDong.qu on 16/2/25.
 */
@Service
public class SubsidyGoodsNumServiceImpl implements SubsidyGoodsNumService {

    @Override
    public Long getSubsidyGoodsTotalNum(List<SubsidyGoods> subsidyGoodsList, Long subsidyActId, Long actId) {

        return null;
    }

    /**
     * 获取rediskey
     */
    private String getSubsidyActGoodsRedisKey(Long subsidyActId, Long actId) {
        return "";
    }
}
