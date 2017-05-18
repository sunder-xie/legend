package com.tqmall.legend.biz.shop.impl;

import com.tqmall.legend.biz.shop.ShopApplyRecordService;
import com.tqmall.legend.dao.shop.ShopApplyRecordDao;
import com.tqmall.legend.entity.shop.ShopApplyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 16/10/17.
 */
@Service
@Slf4j
public class ShopApplyRecordServiceImpl implements ShopApplyRecordService {

    @Autowired
    private ShopApplyRecordDao shopApplyRecordDao;

    @Override
    public Integer selectCount(Map<String, Object> searchParams) {
        return shopApplyRecordDao.selectCount(searchParams);
    }

    @Override
    public List<ShopApplyRecord> select(Map<String, Object> searchParams) {
        return shopApplyRecordDao.select(searchParams);
    }

    @Override
    public ShopApplyRecord selectById(Long id) {
        return shopApplyRecordDao.selectById(id);
    }

    @Override
    public Integer insert(ShopApplyRecord shopApplyRecord) {
        return shopApplyRecordDao.insert(shopApplyRecord);
    }

    @Override
    public Integer updateById(ShopApplyRecord shopApplyRecord) {
        return shopApplyRecordDao.updateById(shopApplyRecord);
    }
}
