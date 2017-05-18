package com.tqmall.legend.biz.order.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.IVirtualOrderService;
import com.tqmall.legend.dao.order.VirtualOrderDao;
import com.tqmall.legend.entity.order.VirtualOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 虚拟工单Service Impl
 * <p/>
 * Created by dongc on 15/9/11.
 */
@Service
public class VirtualOrderServiceImpl extends BaseServiceImpl implements IVirtualOrderService {

    // log
    public static final Logger LOGGER = LoggerFactory.getLogger(VirtualOrderServiceImpl.class);

    @Autowired
    VirtualOrderDao virtualOrderDao;

    @Override
    public int save(VirtualOrder orderInfo) {
        return virtualOrderDao.insert(orderInfo);
    }

    @Override
    public int update(VirtualOrder virtualOrder) {
        return virtualOrderDao.updateById(virtualOrder);
    }

    @Override
    public Optional<VirtualOrder> getOrderById(Long orderId) {
        VirtualOrder virtualOrder = null;
        try {
            virtualOrder = virtualOrderDao.selectById(orderId);
        } catch (Exception e) {
            LOGGER.error("获取虚拟工单失败,工单ID：{}", orderId);
            return Optional.absent();
        }

        return Optional.fromNullable(virtualOrder);
    }

    @Override
    public Long isExistVirtualOrder(Long parentId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("parentId", parentId);
        try {
            List<VirtualOrder> virtualOrderList = virtualOrderDao.select(parameters);
            if (virtualOrderList != null && virtualOrderList.size() > 0) {
                return virtualOrderList.get(0).getId();
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("获取虚拟工单失败,实际工单ID：{}", parentId);
            return null;
        }
    }

    @Override
    public int delete(Long orderId) {
        return virtualOrderDao.deleteById(orderId);
    }
}
