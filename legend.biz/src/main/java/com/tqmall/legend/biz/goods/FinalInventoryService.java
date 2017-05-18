package com.tqmall.legend.biz.goods;

import com.tqmall.legend.entity.goods.FinalInventory;

import java.util.List;

/**
 * Created by sven on 16/8/2.
 */
public interface FinalInventoryService {
    void batchInsert(List<FinalInventory> finalInventoryList);
}
