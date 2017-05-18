package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.ShopTag;

import java.util.List;

/**
 * Created by zsy on 16/12/14.
 */
public interface ShopTagService {

    /**
     * 根据标签类别获取标签列表
     *
     * @param tagCode 标签标识
     * @return
     */
    ShopTag selectByTagCode(String tagCode);
}