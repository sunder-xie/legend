package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.ShopTagRel;
import com.tqmall.legend.facade.magic.vo.BPShopTagRelVo;

import java.util.List;

/**
 * Created by zsy on 16/12/14.
 */
public interface ShopTagRelService {

    /**
     * 根据shopId,获取门店标签关系列表
     *
     * @param shopId
     * @return
     */
    List<ShopTagRel> selectByShopIdAndTagId(Long shopId, Long tagId);

    /**
     * 根据shopId,tagId,cityId，查询满足条件的店铺，返回数据集中缺少tagName字段信息
     *
     * @param shopId
     * @param tagId
     * @param cityId
     * @return
     */
    List<BPShopTagRelVo> searchShopTagRel(Long shopId, Long tagId, Long cityId);

    /**
     * 根据门店名称查询钣喷中心店
     *
     * @param shopName 为空时，返回全部
     * @return
     */
    List<BPShopTagRelVo> searchBPShopTagRelByName(String shopName);
}