package com.tqmall.legend.facade.order.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.customer.vo.CarDetailVo;
import com.tqmall.legend.facade.order.CommonOrderFacade;
import com.tqmall.legend.facade.order.vo.OrderServicesVo;
import com.tqmall.legend.facade.order.vo.OrderToProxyVo;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.object.result.proxyServices.ProxyServicesDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class CommonOrderFacadeImpl implements CommonOrderFacade {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private CustomerCarFacade customerCarFacade;

    /**
     * 工单转委托单，获取工单详细信息
     *
     * @param orderId
     * @param shopId
     * @return
     */
    @Override
    public Result<OrderToProxyVo> getProxyOrderInfo(Long orderId, Long shopId) {
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        OrderInfo orderInfo;
        if (orderInfoOptional.isPresent()) {
            orderInfo = orderInfoOptional.get();
        } else {
            return Result.wrapErrorResult(LegendErrorCode.ORDER_NOT_FIND_EX.getCode(), LegendErrorCode.ORDER_NOT_FIND_EX.getErrorMessage());
        }
        //获取工单服务数据
        List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId);
        if (CollectionUtils.isEmpty(orderServicesList)) {
            return Result.wrapErrorResult(LegendErrorCode.ORDER_SERVICE_NULL_EX.getCode(), LegendErrorCode.ORDER_SERVICE_NULL_EX.getErrorMessage());
        }
        OrderToProxyVo orderToProxyVo = new OrderToProxyVo();
        try {
            orderToProxyVo.setOrderId(orderId);
            BeanUtils.copyProperties(orderInfo, orderToProxyVo);
            //设置其它车辆消息
            Long customerCarId = orderInfo.getCustomerCarId();
            CarDetailVo carDetailVo = customerCarFacade.getSimpleCar(customerCarId, shopId);
            if (carDetailVo != null) {
                CustomerCar customerCar = carDetailVo.getCustomerCar();
                if (customerCar != null) {
                    buildCarInfo(orderToProxyVo, customerCar);
                }
            }
        } catch (BeansException e) {
            log.error("【工单转委托单】：工单转vo对象copy异常", e);
            return Result.wrapErrorResult(LegendErrorCode.ORDER_COPY_EX.getCode(), "系统繁忙，请稍后再试");
        }
        //组装服务数据
        List<OrderServicesVo> orderServicesVoList = Lists.newArrayList();
        List<Long> serviceIdsList = Lists.newArrayList();
        Map<Long, OrderServicesVo> orderServicesVoMap = Maps.newHashMap();
        Map<Long, Object> proxyServicesVoMap = Maps.newHashMap();
        try {
            //过滤已委托的服务
            //工单是委托状态才查询委托单
            if (orderInfo.getProxyType().equals(1)) {
                ProxyParam proxyParam = new ProxyParam();
                proxyParam.setSource(Constants.CUST_SOURCE);
                proxyParam.setOrderId(orderId);//委托单工单id
                com.tqmall.core.common.entity.Result<List<ProxyDTO>> proxyResult = rpcProxyService.showProxyList(proxyParam);
                if (proxyResult.isSuccess()) {
                    List<ProxyDTO> proxyDTOList = proxyResult.getData();
                    List<ProxyServicesDTO> proxyServicesDTODeleteList = Lists.newArrayList();
                    for (ProxyDTO proxyDTO : proxyDTOList) {
                        String proxyStatus = proxyDTO.getProxyStatus();
                        Long id = proxyDTO.getId();
                        if (!ProxyStatusEnum.YQX.getCode().equals(proxyStatus)) {
                            com.tqmall.core.common.entity.Result<List<ProxyServicesDTO>> proxyServicesDTOResult = rpcProxyService.getProxyServicesByOrderId(id);
                            if (proxyServicesDTOResult.isSuccess()) {
                                List<ProxyServicesDTO> proxyServicesDTOList = proxyServicesDTOResult.getData();
                                proxyServicesDTODeleteList.addAll(proxyServicesDTOList);
                            }
                        }
                    }
                    for (ProxyServicesDTO proxyServicesDTO : proxyServicesDTODeleteList) {
                        Long serviceId = proxyServicesDTO.getServiceId();
                        proxyServicesVoMap.put(serviceId, null);
                    }
                }
            }
            for (OrderServices orderServices : orderServicesList) {
                Long serviceId = orderServices.getServiceId();
                //过滤已经委托了的服务
                if (!proxyServicesVoMap.containsKey(serviceId)) {
                    serviceIdsList.add(orderServices.getServiceId());
                }
            }
        } catch (BeansException e) {
            log.error("【工单转委托单】：工单服务转vo对象copy异常", e);
            return Result.wrapErrorResult(LegendErrorCode.ORDER_COPY_EX.getCode(), "系统繁忙，请稍后再试");
        } catch (Exception e) {
            log.error("【工单转委托单】：出现异常", e);
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_PRC_EX.getCode(), "系统繁忙，请稍后再试");
        }
        //组装服务
        if (CollectionUtils.isEmpty(serviceIdsList)) {
            return Result.wrapErrorResult(LegendErrorCode.NO_SERVICE_CAN_PROXY_EX.getCode(), "没有服务可以委托");
        }
        //任何服务都能够委托，不做限制
        Map<String, Object> serviceMap = Maps.newHashMap();
        serviceMap.put("ids", serviceIdsList);
        serviceMap.put("shopId", shopId);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(serviceMap);
        if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
            Map<Long, ShopServiceInfo> exServiceMap = Maps.newHashMap();
            for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                exServiceMap.put(shopServiceInfo.getId(), shopServiceInfo);
            }
            for (OrderServices orderServices : orderServicesList) {
                Long serviceId = orderServices.getServiceId();
                //过滤已经委托了的服务
                if (!proxyServicesVoMap.containsKey(serviceId) && exServiceMap.containsKey(serviceId)) {
                    OrderServicesVo orderServicesVo = new OrderServicesVo();
                    BeanUtils.copyProperties(orderServices, orderServicesVo);
                    orderServicesVoList.add(orderServicesVo);
                    orderServicesVoMap.put(orderServicesVo.getServiceId(), orderServicesVo);
                }
            }

            Map<String, List<ShopServiceInfo>> shopServiceInfoMap = Maps.newHashMap();
            List<Long> parentIds = Lists.newArrayList();
            for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                Long serviceId = shopServiceInfo.getId();
                Long parentId = shopServiceInfo.getParentId();
                Long categoryId = shopServiceInfo.getCategoryId();
                if (orderServicesVoMap.containsKey(serviceId)) {
                    OrderServicesVo orderServicesVo = orderServicesVoMap.get(serviceId);
                    orderServicesVo.setFlags(shopServiceInfo.getFlags());
                    orderServicesVo.setSharePrice(shopServiceInfo.getSharePrice());
                    orderServicesVo.setSurfaceNum(shopServiceInfo.getSurfaceNum());
                }
                if (Long.compare(parentId, 0l) > 0) {
                    parentIds.add(parentId);
                    shopServiceInfoMap.put(createServiceMapKey(parentId, categoryId), null);
                }
            }
            //查询同类别的服务
            if (!CollectionUtils.isEmpty(parentIds)) {
                Map<String, Object> serviceSearchMap = Maps.newHashMap();
                serviceSearchMap.put("parentIds", parentIds);
                serviceSearchMap.put("shopId", shopId);
                List<ShopServiceInfo> parentServiceInfoList = shopServiceInfoService.select(serviceSearchMap);
                if (!CollectionUtils.isEmpty(parentServiceInfoList)) {
                    for (ShopServiceInfo shopServiceInfo : parentServiceInfoList) {
                        Long parentId = shopServiceInfo.getParentId();
                        Long categoryId = shopServiceInfo.getCategoryId();
                        String key = createServiceMapKey(parentId, categoryId);
                        if (shopServiceInfoMap.containsKey(key)) {
                            List<ShopServiceInfo> tempList = shopServiceInfoMap.get(key);
                            if (tempList == null) {
                                tempList = Lists.newArrayList();
                            }
                            tempList.add(shopServiceInfo);
                            shopServiceInfoMap.put(key, tempList);
                        }
                    }
                }
            }
            //设置同类别服务
            for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                Long serviceId = shopServiceInfo.getId();
                Long parentId = shopServiceInfo.getParentId();
                Long categoryId = shopServiceInfo.getCategoryId();
                if (orderServicesVoMap.containsKey(serviceId)) {
                    OrderServicesVo orderServicesVo = orderServicesVoMap.get(serviceId);
                    String key = createServiceMapKey(parentId, categoryId);
                    if (shopServiceInfoMap.containsKey(key)) {
                        List<ShopServiceInfo> tempList = shopServiceInfoMap.get(key);
                        orderServicesVo.setShopServiceInfoList(tempList);
                    } else {
                        //门店自定义的钣喷服务
                        List<ShopServiceInfo> tempList = Lists.newArrayList();
                        tempList.add(shopServiceInfo);
                        orderServicesVo.setShopServiceInfoList(tempList);
                    }
                }
            }
        }
        orderToProxyVo.setOrderServicesList(orderServicesVoList);
        return Result.wrapSuccessfulResult(orderToProxyVo);
    }

    /**
     * 设置车辆消息
     *
     * @param orderToProxyVo
     * @param customerCar
     */
    private void buildCarInfo(OrderToProxyVo orderToProxyVo, CustomerCar customerCar) {
        orderToProxyVo.setCustomerCarId(customerCar.getId());
        orderToProxyVo.setCustomerId(customerCar.getCustomerId());
        orderToProxyVo.setCarLicense(customerCar.getLicense());
        orderToProxyVo.setCarColor(customerCar.getColor());
        orderToProxyVo.setVin(customerCar.getVin());
        orderToProxyVo.setCarBrandId(customerCar.getCarBrandId());
        orderToProxyVo.setCarSeriesId(customerCar.getCarSeriesId());
        orderToProxyVo.setCarModelsId(customerCar.getCarModelId());
        orderToProxyVo.setCarBrand(customerCar.getCarBrand());
        orderToProxyVo.setCarSeries(customerCar.getCarSeries());
        orderToProxyVo.setCarModels(customerCar.getCarModel());
        orderToProxyVo.setImportInfo(customerCar.getImportInfo());
        orderToProxyVo.setCarPowerId(customerCar.getCarPowerId());
        orderToProxyVo.setCarPower(customerCar.getCarPower());
        orderToProxyVo.setCarYearId(customerCar.getCarYearId());
        orderToProxyVo.setCarYear(customerCar.getCarYear());
        orderToProxyVo.setCarGearBoxId(customerCar.getCarGearBoxId());
        orderToProxyVo.setCarGearBox(customerCar.getCarGearBox());
    }

    @Override
    public Result updateOrderInfoProxyType(Long orderId, Integer proxyType) {
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        OrderInfo orderInfo;
        if (orderInfoOptional.isPresent()) {
            orderInfo = orderInfoOptional.get();
        } else {
            return Result.wrapErrorResult(LegendErrorCode.ORDER_NOT_FIND_EX.getCode(), LegendErrorCode.ORDER_NOT_FIND_EX.getErrorMessage());
        }
        if (proxyType != null && orderInfo.getProxyType().equals(0)) {
            log.info("【委托单回写工单委托类型】：委托类型为：{}", proxyType);
            orderInfo.setProxyType(proxyType);
            try {
                orderService.updateOrder(orderInfo);
                return Result.wrapSuccessfulResult(true);
            } catch (Exception e) {
                log.error("【委托单回写工单委托类型】：出现异常", e);
            }
        }
        return Result.wrapSuccessfulResult(false);
    }

    /**
     * 获取工单信息
     *
     * @param orderId
     * @param shopId
     * @return
     */
    @Override
    public OrderInfo getOrderInfoById(Long orderId, Long shopId) {
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (orderInfoOptional.isPresent()) {
            return orderInfoOptional.get();
        }
        return null;
    }

    /**
     * 生成同类服务map的key
     *
     * @param parentId
     * @param categoryId
     * @return
     */
    private String createServiceMapKey(Long parentId, Long categoryId) {
        return parentId + "&" + categoryId;
    }
}
