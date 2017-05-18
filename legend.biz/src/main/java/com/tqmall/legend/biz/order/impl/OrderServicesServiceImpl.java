package com.tqmall.legend.biz.order.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.dao.order.OrderServicesDao;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServiceTypeEnum;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lixiao on 14-11-5.
 */
@Service
public class OrderServicesServiceImpl extends BaseServiceImpl implements OrderServicesService {

    Logger logger = LoggerFactory.getLogger(OrderServicesServiceImpl.class);

    @Autowired
    private OrderServicesDao orderServicesDao;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private OrderInfoService orderInfoService;

    @Override
    public List<OrderServices> select(Map<String, Object> map) {
        return orderServicesDao.select(map);
    }

    @Override
    public List<OrderServices> queryOrderServiceList(Long orderId, Long shopId, OrderServiceTypeEnum serviceTypeEnum, Map paramsMap) {
        paramsMap.put("orderId", orderId);
        paramsMap.put("shopId", shopId);
        paramsMap.put("type", serviceTypeEnum.getCode());

        return this.select(paramsMap);
    }

    @Override
    public List<OrderServices> queryOrderServiceList(Long orderId, Long shopId, OrderServiceTypeEnum serviceTypeEnum) {
        Map<String, Object> paramsMap = new HashMap<String, Object>(3);
        paramsMap.put("orderId", orderId);
        paramsMap.put("shopId", shopId);
        paramsMap.put("type", serviceTypeEnum.getCode());
        List<OrderServices> orderServices =null;
        try {
            orderServices =this.select(paramsMap);
        } catch (Exception e) {
            logger.error("获取工单的服务项目异常,异常信息:{}",e);
            orderServices =new ArrayList<OrderServices>();
        }

        if(orderServices ==null || orderServices.size() ==0){
            orderServices =new ArrayList<OrderServices>();
        }

        return orderServices;
    }

    @Override
    public List<OrderServices> queryOrderServiceList(Long orderId, Long shopId) {
        Map<String, Object> paramsMap = new HashMap<String, Object>(2);
        paramsMap.put("orderId", orderId);
        paramsMap.put("shopId", shopId);

        return this.select(paramsMap);
    }

    @Override
    public int update(OrderServices newOrderServices) {
        return orderServicesDao.updateById(newOrderServices);
    }

    @Override
    public List<String> getTaoqiFirstCateIds(Long orderId) {

        Map searchMap = new HashMap();
        searchMap.put("orderId", orderId);
        List<OrderServices> orderServiceses = orderServicesDao.select(searchMap);
        if (CollectionUtils.isEmpty(orderServiceses)) {
            logger.info("根据工单ID获取工单服务列表为空，工单ID为：" + orderId);
            return null;
        }
        List<Long> serviceIds = new ArrayList<>();
        for (OrderServices orderServices : orderServiceses) {
            if (orderServices.getParentServiceId() != null && orderServices.getParentServiceId() > 0) {
                serviceIds.add(orderServices.getParentServiceId());
            }
            serviceIds.add(orderServices.getServiceId());
        }
        if (CollectionUtils.isEmpty(serviceIds)) {
            logger.info("工单服务为空，工单ID为：" + orderId);
            return null;
        }
        List<ShopServiceInfo> shopServiceInfos = shopServiceInfoService.selectByIds(serviceIds);
        List<Long> secondAppCateIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(shopServiceInfos)) {
            logger.info("根据服务IDS获取服务信息为空，服务IDS为：" + serviceIds);
            return null;
        }
        for (ShopServiceInfo shopServiceInfo : shopServiceInfos) {
            if (shopServiceInfo.getAppCateId() != null && shopServiceInfo.getAppCateId() > 0) {
                secondAppCateIds.add((long) shopServiceInfo.getAppCateId());
            }
        }
        if (CollectionUtils.isEmpty(secondAppCateIds)) {
            logger.info("根据服务获取二级分类为空，服务列表为：" + shopServiceInfos);
            return null;
        }
        List<ShopServiceCate> shopServiceCates = shopServiceCateService.selectByIds(secondAppCateIds);
        if (CollectionUtils.isEmpty(shopServiceCates)) {
            logger.info("根据二级分类获取服务套餐列表为空，二级分类ID：" + secondAppCateIds);
            return null;
        }
        List<String> firstCateIds = new ArrayList<>();
        for (ShopServiceCate shopServiceCate : shopServiceCates) {
            firstCateIds.add(shopServiceCate.getParentId() + "");
        }
        if (CollectionUtils.isEmpty(firstCateIds)) {
            logger.info("获取淘汽一级分类为空");
            return null;
        }
        return firstCateIds;
    }

    @Override
    public int batchSave(List<OrderServices> orderServicesList) {
        return super.batchInsert(orderServicesDao, orderServicesList,300);
    }

    @Override
    public int batchDel(Object[] ids) {
        return orderServicesDao.deleteByIds(ids);
    }

    /**
     * 批量查询工单服务
     *
     * @param shopId
     * @param orderIds
     * @return key为orderId, value为serviceName,用顿号隔开
     */
    @Override
    public Map<Long, String> getServiceNamesByOrderIds(Long shopId, List<Long> orderIds) {
        Map<Long, String> serviceNamesMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(orderIds)) {
            return serviceNamesMap;
        }
        List<OrderServices> orderServicesList = orderServicesDao.getServiceNamesByOrderIds(shopId, orderIds);
        if (CollectionUtils.isEmpty(orderServicesList)) {
            return serviceNamesMap;
        }
        for (OrderServices orderServices : orderServicesList) {
            Long orderId = orderServices.getOrderId();
            String serviceName = orderServices.getServiceName();
            if (serviceNamesMap.containsKey(orderId)) {
                String serviceNames = serviceNamesMap.get(orderId);
                serviceNames = serviceNames.concat("、").concat(serviceName);
                serviceNamesMap.put(orderId, serviceNames);
            } else {
                serviceNamesMap.put(orderId, serviceName);
            }
        }
        return serviceNamesMap;
    }

    @Override
    public Map<Long, Integer> getServiceOrderUsageMap(Collection<Long> serviceIds) {
        if (CollectionUtils.isEmpty(serviceIds)) {
            return Maps.newHashMap();
        }
        List<OrderServices> orderServiceList = orderServicesDao.selectByServiceIds(serviceIds);
        Map<Long, Integer> serviceIdUsageCountMap = Maps.newHashMap();
        for (OrderServices orderServices : orderServiceList) {
            Long serviceId = orderServices.getServiceId();
            int beforCount = 0;
            if (serviceIdUsageCountMap.containsKey(serviceId)) {
                beforCount = serviceIdUsageCountMap.get(serviceId);
            }
            int afterCount = beforCount + 1;
            serviceIdUsageCountMap.put(serviceId, afterCount);
        }
        return serviceIdUsageCountMap;
    }

    @Override
    public Map<Long, Integer> getServiceConfirmedOrderMap(Collection<Long> serviceIds) {
        if (CollectionUtils.isEmpty(serviceIds)) {
            return Maps.newHashMap();
        }

        Map<Long, Integer> serviceIdUsageCountMap = Maps.newHashMap();
        List<OrderServices> orderServicesList = orderServicesDao.selectByServiceIds(serviceIds);
        List<Long> orderIdList = Lists.newArrayList(Lists.transform(orderServicesList, new Function<OrderServices, Long>() {
            @Override
            public Long apply(OrderServices input) {
                return input.getOrderId();
            }
        }));
        Set<Long> orderIdSet = Sets.newHashSet(orderIdList);
        Map<Long, OrderInfo> orderInfoMap = orderInfoService.selectMapByIds(orderIdSet);

        for (OrderServices orderServices : orderServicesList) {
            Long serviceId = orderServices.getServiceId();
            Long orderId = orderServices.getOrderId();
            int beforCount = 0;
            int afterCount = 0;
            if (serviceIdUsageCountMap.containsKey(serviceId)) {
                beforCount = serviceIdUsageCountMap.get(serviceId);
            }

            if (isOrderConfirmed(orderId, orderInfoMap)) {
                afterCount = beforCount + 1;
            }
            if (afterCount != 0) {
                serviceIdUsageCountMap.put(serviceId, afterCount);
            }

        }
        return serviceIdUsageCountMap;
    }

    @Override
    public List<OrderServices> selOrderServicesByOrderIds(Long shopId, List<Long> orderIds, int serviceType) {
        return orderServicesDao.selOrderServicesByOrderIds(shopId, orderIds, serviceType);
    }

    boolean isOrderConfirmed(Long orderId, Map<Long, OrderInfo> orderInfoMap) {
        if (orderInfoMap.containsKey(orderId)) {
            Date confirmTime = orderInfoMap.get(orderId).getConfirmTime();
            if (confirmTime != null) {
                return true;
            }
        }
        return false;
    }
}
