package com.tqmall.legend.biz.subsidy;

import com.tqmall.legend.entity.subsidy.SubsidyGoods;

import java.util.List;

/**
 * Created by xiangDong.qu on 16/2/25.
 */
public interface SubsidyGoodsNumService {
    /**
     * 获取补贴包内的商品的总的补贴数
     *
     * @param subsidyGoodsList 补贴包商品列表
     * @param subsidyActId     补贴包id
     * @param actId            活动id
     */
    public Long getSubsidyGoodsTotalNum(List<SubsidyGoods> subsidyGoodsList, Long subsidyActId, Long actId);
}
