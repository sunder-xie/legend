package com.tqmall.legend.biz.goods.impl;

import com.tqmall.legend.biz.goods.FinalInventoryService;
import com.tqmall.legend.dao.goods.FinalInventoryDao;
import com.tqmall.legend.entity.goods.FinalInventory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sven on 16/8/2.
 */
@Service
public class FinalInventoryServiceImpl implements FinalInventoryService {
    @Resource
    private FinalInventoryDao finalInventoryDao;
    @Override
    public void batchInsert(List<FinalInventory> finalInventoryList) {
        finalInventoryDao.batchInsert(finalInventoryList);
    }
}
