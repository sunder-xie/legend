package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.IVirtualOrderserviceService;
import com.tqmall.legend.dao.order.VirtualOrderserviceDao;
import com.tqmall.legend.entity.order.VirtualOrderservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 虚拟工单服务Service Impl
 * <p/>
 * Created by dongc on 15/9/11.
 */
@Service
public class VirtualOrderserviceServiceImpl extends BaseServiceImpl implements IVirtualOrderserviceService {

    // log
    public static final Logger LOGGER = LoggerFactory.getLogger(VirtualOrderserviceServiceImpl.class);

    @Autowired
    VirtualOrderserviceDao virtualOrderserviceDao;


    @Override
    public int save(VirtualOrderservice orderServices) {
        return virtualOrderserviceDao.insert(orderServices);
    }

    @Override
    public int update(VirtualOrderservice virtualOrderservice) {
        return virtualOrderserviceDao.updateById(virtualOrderservice);
    }

    @Override
    public List<VirtualOrderservice> queryOrderServices(Long orderId, int serviceType, Long shopId) {
        Map<String, Object> paramsMap = new HashMap<String, Object>(3);
        paramsMap.put("orderId", orderId);
        paramsMap.put("type", serviceType);
        paramsMap.put("shopId", shopId);
        return virtualOrderserviceDao.select(paramsMap);
    }

    @Override
    public List<VirtualOrderservice> queryOrderServices(Long orderId, Long shopId) {
        Map<String, Object> paramsMap = new HashMap<String, Object>(2);
        paramsMap.put("orderId", orderId);
        paramsMap.put("shopId", shopId);
        return virtualOrderserviceDao.select(paramsMap);
    }

    @Override
    public int deleteByIds(Set<Long> serviceIdSet) {
        return virtualOrderserviceDao.deleteByIds(serviceIdSet.toArray());
    }
}
