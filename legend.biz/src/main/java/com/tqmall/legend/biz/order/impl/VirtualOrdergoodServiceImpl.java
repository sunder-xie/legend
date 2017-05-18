package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.IVirtualOrdergoodService;
import com.tqmall.legend.dao.order.VirtualOrdergoodDao;
import com.tqmall.legend.entity.order.VirtualOrdergood;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 虚拟工单物料Service Impl
 * <p/>
 * Created by dongc on 15/9/11.
 */
@Service
public class VirtualOrdergoodServiceImpl extends BaseServiceImpl implements IVirtualOrdergoodService {

    public static final Logger LOGGER = LoggerFactory.getLogger(VirtualOrdergoodServiceImpl.class);

    @Autowired
    VirtualOrdergoodDao virtualOrdergoodDao;


    @Override
    public int save(VirtualOrdergood orderGoods) {
        return virtualOrdergoodDao.insert(orderGoods);
    }

    @Override
    public int update(VirtualOrdergood virtualOrdergood) {
        return virtualOrdergoodDao.updateById(virtualOrdergood);
    }

    @Override
    public List<VirtualOrdergood> queryOrderGoods(Long orderId, Long shopId, long goodsType) {
        Map<String, Object> orderGoodsMap = new HashMap<String, Object>(3);
        orderGoodsMap.put("orderId", orderId);
        orderGoodsMap.put("shopId", shopId);
        orderGoodsMap.put("goodsType", goodsType);
        return virtualOrdergoodDao.select(orderGoodsMap);
    }

    @Override
    public List<VirtualOrdergood> queryOrderGoods(Long orderId, Long shopId) {
        Map<String, Object> orderGoodsMap = new HashMap<String, Object>(2);
        orderGoodsMap.put("orderId", orderId);
        orderGoodsMap.put("shopId", shopId);
        return virtualOrdergoodDao.select(orderGoodsMap);
    }

    @Override
    public int deleteByIds(Set<Long> orderIdSet) {
        return virtualOrdergoodDao.deleteByIds(orderIdSet.toArray());
    }
}
