package com.tqmall.legend.biz.shop.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ShopServiceInfoRelService;
import com.tqmall.legend.dao.shop.ShopServiceInfoRelDao;
import com.tqmall.legend.entity.shop.ShopServiceInfoRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/8/5.
 */
@Service
public class ShopServiceInfoRelServiceImpl extends BaseServiceImpl implements ShopServiceInfoRelService {
    @Autowired
    ShopServiceInfoRelDao shopServiceInfoRelDao;

    @Override
    public List<ShopServiceInfoRel> select(Map<String, Object> searchParams) {
        return shopServiceInfoRelDao.select(searchParams);
    }

    @Override
    public ShopServiceInfoRel selectById(Long id) {
        return shopServiceInfoRelDao.selectById(id);
    }

}
