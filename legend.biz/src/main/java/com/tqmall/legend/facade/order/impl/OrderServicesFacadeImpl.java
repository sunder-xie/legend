package com.tqmall.legend.facade.order.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.api.entity.OrderCountSearchVO;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.DebitAndRedBillDTO;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.billcenter.client.enums.DebitTypeEnum;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.base.CountOrderEntity;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.facade.order.OrderServicesFacade;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.facade.order.vo.OrderWorkerVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.common.result.Result;
import com.tqmall.search.dubbo.client.PageableResponseExtend;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import com.tqmall.search.dubbo.client.legend.order.result.LegendOrderInfoDTO;
import com.tqmall.search.dubbo.client.legend.order.service.LegendOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsy on 16/4/14.
 * 车间
 */
@Service
@Slf4j
public class OrderServicesFacadeImpl implements OrderServicesFacade {
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private RpcDebitBillService rpcDebitBillService;
    @Autowired
    private CustomerService customerService;
    @Resource
    private LegendOrderService legendOrderService;
    @Autowired
    private AppointService appointService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private IOrderService iOrderService;


    @Override
    public List<OrderWorkerVo> getOrderWorkerList(Long shopId, Integer size) {
        //查询待报价、已报价、修理中工单信息
        Map<String, Object> orderSearchMap = Maps.newHashMap();
//        orderSearchMap.put("offset", 0);
//        orderSearchMap.put("limit", size);
        orderSearchMap.put("shopId", shopId);
        orderSearchMap.put("orderStatuss", new String[]{"CJDD", "DDBJ", "FPDD", "DDSG"});
        orderSearchMap.put("sorts", new String[]{"id desc"});
        List<OrderInfo> orderInfoList = orderInfoService.select(orderSearchMap);
        if (CollectionUtils.isEmpty(orderInfoList)) {
            return null;
        }
        //返回数据处理
        List<OrderWorkerVo> orderWorkerVoList = Lists.newArrayList();
        List<Long> orderIds = Lists.newArrayList();
        for (OrderInfo orderInfo : orderInfoList) {
            Long id = orderInfo.getId();
            orderIds.add(id);
            OrderWorkerVo orderWorkerVo = new OrderWorkerVo();
            BeanUtils.copyProperties(orderInfo, orderWorkerVo);
            orderWorkerVoList.add(orderWorkerVo);
        }
        //查询服务
        Map<String, Object> serviceSearchMap = Maps.newHashMap();
        serviceSearchMap.put("shopId", shopId);
        serviceSearchMap.put("orderIds", orderIds);
        List<OrderServices> orderServicesList = orderServicesService.select(serviceSearchMap);
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            //获取员工信息
            Map<String, Object> shopManagerSearchMap = Maps.newHashMap();
            shopManagerSearchMap.put("shopId", shopId);
            List<ShopManager> shopManagerList = shopManagerService.select(shopManagerSearchMap);
            Map<Long, String> shopManagerNameMap = Maps.newHashMap();
            for (ShopManager shopManager : shopManagerList) {
                Long id = shopManager.getId();
                String name = shopManager.getName();
                shopManagerNameMap.put(id, name);
            }
            //获取工单id和对应的去重的维修工ids
            Map<Long, Map<Long, Object>> workIdsMap = Maps.newHashMap();
            //获取工单id和对应去重后的维修工names
            Map<Long, StringBuffer> workNamesMap = Maps.newHashMap();
            for (OrderServices orderServices : orderServicesList) {
                Long orderId = orderServices.getOrderId();
                String workerIds = orderServices.getWorkerIds();
                if (StringUtils.isNotBlank(workerIds) && !"0".equals(workerIds)) {
                    String[] workerIdStr = workerIds.split(",");
                    Map<Long, Object> workNameMap;
                    StringBuffer workNameSb;
                    if (workIdsMap.containsKey(orderId)) {
                        workNameMap = workIdsMap.get(orderId);
                        workNameSb = workNamesMap.get(orderId);
                    } else {
                        workNameMap = new HashMap<>();
                        workNameSb = new StringBuffer();
                    }
                    for (String workId : workerIdStr) {
                        Long workIdLong = Long.parseLong(workId);
                        if (shopManagerNameMap.containsKey(workIdLong) && !workNameMap.containsKey(workIdLong)) {
                            workNameSb.append(shopManagerNameMap.get(workIdLong));
                            workNameSb.append("、");
                        }
                        workNameMap.put(workIdLong, null);
                    }
                    workIdsMap.put(orderId, workNameMap);
                    workNamesMap.put(orderId, workNameSb);
                }
            }
            //设置工单维修工
            for (OrderWorkerVo orderWorkerVo : orderWorkerVoList) {
                Long id = orderWorkerVo.getId();
                if (workNamesMap.containsKey(id)) {
                    StringBuffer workerNameSb = workNamesMap.get(id);
                    if (!StringUtils.isEmpty(workerNameSb)) {
                        String workNames = workerNameSb.substring(0, workerNameSb.length() - 1);
                        orderWorkerVo.setWorkerNames(workNames);
                    }
                }
            }
        }
        return orderWorkerVoList;
    }


    @Override
    public Map<String, Long> getOrderCountFromSearch(CountOrderEntity countOrderEntity) {
        List<LegendOrderRequest> orderRequests = Lists.newArrayList();
        LegendOrderRequest orderRequest = new LegendOrderRequest();
        orderRequest.setShopId(countOrderEntity.getShopId().toString());
        orderRequest.setOrderStatus(countOrderEntity.getOrderStatus());
        List<Integer> payStatus = CollectionUtils.arrayToList(countOrderEntity.getPayStatus());
        orderRequest.setPayStatus(payStatus);
        if (countOrderEntity.getSymbol() != null) {
            orderRequest.setSymbol(countOrderEntity.getSymbol().toString());
        }
        orderRequest.setStartTime(countOrderEntity.getStartTime());
        orderRequest.setEndTime(countOrderEntity.getEndTime());
        orderRequest.setPayStartTime(countOrderEntity.getPayStartTime());
        orderRequest.setPayEndTime(countOrderEntity.getPayEndTime());
        orderRequests.add(orderRequest);
        log.info("调用搜索dubbo接口queryOrderCount获取工单数，参数信息：{}", LogUtils.objectToString(orderRequests));
        Result<Map<String, Long>> mapResult = legendOrderService.queryOrderCount(orderRequests.toArray(new LegendOrderRequest[]{}));
        if (!mapResult.isSuccess()) {
            return Maps.newHashMap();
        }
        return mapResult.getData();
    }

    @Override
    public Map<String, Long> getOrderCountFromSearchToApp(List<OrderCountSearchVO> countOrderEntity) {
        List<LegendOrderRequest> orderRequests = Lists.newArrayList();

        for (OrderCountSearchVO orderCountSearchVO : countOrderEntity) {
            LegendOrderRequest orderRequest = new LegendOrderRequest();

            orderRequest.setShopId(orderCountSearchVO.getShopId().toString());
            orderRequest.setOrderStatus(orderCountSearchVO.getOrderStatus());
            if (orderCountSearchVO.getPayStatus() != null && orderCountSearchVO.getPayStatus().length > 0) {
                List<Integer> payStatus = Lists.newArrayList(orderCountSearchVO.getPayStatus());
                orderRequest.setPayStatus(payStatus);
            }
            if (orderCountSearchVO.getSymbol() != null) {
                orderRequest.setSymbol(orderCountSearchVO.getSymbol().toString());
            }
            orderRequest.setStartTime(orderCountSearchVO.getStartTime());
            orderRequest.setEndTime(orderCountSearchVO.getEndTime());
            orderRequest.setPayStartTime(orderCountSearchVO.getPayStartTime());
            orderRequest.setPayEndTime(orderCountSearchVO.getPayEndTime());
            orderRequest.setIsVisit(orderCountSearchVO.getIsVisit());
            orderRequest.setContactLikeKeyWords(orderCountSearchVO.getContactLikeKeyWords());
            orderRequest.setLikeKeyWords(orderCountSearchVO.getLikeKeyWords());
            orderRequest.setCarLicenseLike(orderCountSearchVO.getCarLicenseLike());
            orderRequest.setCompany(orderCountSearchVO.getCompany());
            if (orderCountSearchVO.getProxyType() != null && orderCountSearchVO.getProxyType().length > 0) {
                List<Integer> proxyTypes = Lists.newArrayList(orderCountSearchVO.getProxyType());
                orderRequest.setProxyType(proxyTypes);
            }
            if (orderCountSearchVO.getOrderTag() != null && orderCountSearchVO.getOrderTag().length > 0) {
                List<Integer> orderTag = Lists.newArrayList(orderCountSearchVO.getOrderTag());
                orderRequest.setOrderTag(orderTag);
            }
            if (StringUtils.isNotBlank(orderCountSearchVO.getCustomerCarId())) {
                List<Integer> customerCarIds = Lists.newArrayList();
                String[] customerCarId = orderCountSearchVO.getCustomerCarId().split(",");
                for (String s : customerCarId) {
                    customerCarIds.add(Integer.parseInt(s));
                }
                orderRequest.setCustomerCarId(customerCarIds);
            }
            orderRequests.add(orderRequest);
        }
        log.info("调用搜索dubbo接口queryOrderCount获取工单数，参数信息：{}", LogUtils.objectToString(orderRequests));
        Result<Map<String, Long>> mapResult = legendOrderService.queryOrderCount(orderRequests.toArray(new LegendOrderRequest[]{}));
        if (!mapResult.isSuccess()) {
            return Maps.newHashMap();
        }
        return mapResult.getData();
    }


    /**
     *
     * @param pageableRequest
     * @param orderRequest
     * @return
     */
    @Override
    public DefaultPage<OrderInfoVo> getOrderListFromSearch(PageableRequest pageableRequest, LegendOrderRequest orderRequest) {
        DefaultPage<OrderInfoVo> page = getOrderInfoPage(pageableRequest, orderRequest);
        List<OrderInfoVo> orderInfoVos = page.getContent();
        Long shopId = Long.parseLong(orderRequest.getShopId());
        setOrderTypeName(orderRequest, orderInfoVos);
        setOrderPayAmountAndSignAmount(orderInfoVos, shopId);
        setCompany(orderInfoVos);
        return page;

    }

    @Override
    public DefaultPage<OrderInfoVo> getOrderInfoPage(final PageableRequest pageableRequest, final LegendOrderRequest orderRequest) {
        return new BizTemplate<DefaultPage<OrderInfoVo>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected DefaultPage<OrderInfoVo> process() throws BizException {
                List<OrderInfoVo> orderInfoVos = Lists.newArrayList();
                Result<PageableResponseExtend<LegendOrderInfoDTO>> orderResult = null;
                try {
                    orderResult = legendOrderService.queryLegendOrderListWithAggs(orderRequest, pageableRequest);
                } catch (Exception e) {
                    throw new BizException("通过从搜索获取工单列表信息失败", e);
                }
                if (orderResult== null || !orderResult.isSuccess()) {
                    log.error("通过从搜索获取工单列表信息失败:{}", LogUtils.objectToString(orderRequest));
                    return new DefaultPage<>(orderInfoVos, new PageRequest(1, 10), 0L);
                }
                if(orderResult.getData() == null || CollectionUtils.isEmpty(orderResult.getData().getContent())) {
                    return new DefaultPage<>(orderInfoVos, new PageRequest(1, 10), 0L);
                }
                PageRequest pageRequest = new PageRequest(orderResult.getData().getNumber() < 1 ? 0 : orderResult.getData().getNumber(), orderResult.getData().getSize());
                List<LegendOrderInfoDTO> orderInfoDTOs = orderResult.getData().getContent();
                orderInfoVos = BdUtil.do2bo4List(orderInfoDTOs, OrderInfoVo.class);
                DefaultPage<OrderInfoVo> defaultPage = new DefaultPage(orderInfoVos, pageRequest, orderResult.getData().getTotalElements());
                defaultPage.setOtherData(orderResult.getData().getExtend());
                return defaultPage;
            }
        }.execute();
    }

    @Override
    public Long createSpeedilyOrderForApp(OrderFormEntityBo orderFormEntityBo) {
        // IF 预约单存在 THEN 判断预约单是否创建过工单
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        Long appointId = orderInfo.getAppointId();
        if (appointId != null && appointId.longValue() > 0l) {
            Appoint appoint = appointService.selectById(appointId);
            if (appoint != null) {
                Long status = appoint.getStatus();
                if ((status != null && status.equals(AppointStatusEnum.ORDER_CREATE.getIndex()))) {
                    throw new BizException("该预约单,已经生成过工单", "10003", null);
                }
                //存在预付定金
                BigDecimal downPayment = appoint.getDownPayment();
                if (downPayment != null && downPayment.compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal orderDownPayment = orderInfo.getDownPayment();
                    if (orderDownPayment == null || downPayment.compareTo(orderDownPayment) != 0) {
                        throw new BizException("预付定金与预约单的金额有误，无法创建工单");
                    }
                }
            }
        }
        // 车牌号码
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            throw new BizException("车牌不能为空！");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(orderFormEntityBo.getShopId());
        userInfo.setUserId(orderFormEntityBo.getUserId());
        userInfo.setName(orderFormEntityBo.getUserName());
        // 设置服务顾问(当前操作人)
        orderInfo.setReceiver(orderFormEntityBo.getUserId());
        orderInfo.setReceiverName(userInfo.getName());
        orderInfo.setOrderType(0l);
        // 标记工单来源
        orderInfo.setRefer(orderFormEntityBo.getRefer());
        //版本号
        orderInfo.setVer(orderFormEntityBo.getVer());

        // 设置工单类别"3：快修快保单"
        orderInfo.setOrderTag(OrderCategoryEnum.SPEEDILY.getCode());

        // 工单关联车牌ID
        CustomerCar customerCar = new CustomerCar();
        // 设置车牌
        customerCar.setLicense(orderInfo.getCarLicense());
        //设置车型
        customerCar.setCarBrand(orderInfo.getCarBrand());
        customerCar.setCarBrandId(orderInfo.getCarBrandId());
        customerCar.setCarSeries(orderInfo.getCarSeries());
        customerCar.setCarSeriesId(orderInfo.getCarSeriesId());
        customerCar.setCarModel(orderInfo.getCarModels());
        customerCar.setCarModelId(orderInfo.getCarModelsId());
        customerCar.setImportInfo(orderInfo.getImportInfo());
        customerCar.setCarCompany(orderInfo.getCarCompany());
        //设置联系人
        customerCar.setContact(orderInfo.getContactName());
        customerCar.setContactMobile(orderInfo.getContactMobile());
        //设置行驶里程
        if (!StringUtils.isBlank(orderInfo.getMileage())) {
            Long mileage = Long.parseLong(orderInfo.getMileage());
            customerCar.setMileage(mileage);
        }
        //设置vin
        customerCar.setVin(orderInfo.getVin());
        //设置来源
        customerCar.setVer(orderFormEntityBo.getVer());
        customerCar.setRefer(orderFormEntityBo.getRefer() + "");
        customerCar.setCarPictureUrl(orderFormEntityBo.getImgUrl());
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (Exception e) {
            log.error("[快修快保单]创建快修快保工单失败,[原因] 保存客户车辆信息异常", e);
            throw new BizException("创建快修快保工单失败!");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        //设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());
        orderInfo.setVin(customerCar.getVin());
        orderInfo.setMileage(customerCar.getMileage() == null ? "0" : customerCar.getMileage().toString());
        com.tqmall.legend.common.Result result;
        try {
            result = iOrderService.save(orderFormEntityBo, userInfo);
        } catch (Exception e) {
            log.error("[快修快保单]保存快修快保单异常", e);
            throw new BizException("保存快修快保单异常", "10002",null);
        }
        if (!result.isSuccess()) {
            throw new BizException(result.getErrorMsg());
        }
        return (Long) result.getData();
    }

    @Override
    public Long updateSpeedilyOrderForApp(OrderFormEntityBo orderFormEntityBo) {
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            throw new BizException("车牌不能为空！");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(orderFormEntityBo.getShopId());
        Long userId = orderFormEntityBo.getUserId();
        userInfo.setUserId(userId);
        userInfo.setName(orderFormEntityBo.getUserName());

        // 包装车辆实体
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        customerCar.setCarPictureUrl(orderFormEntityBo.getImgUrl());
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("更新快修快保工单失败,[原因] 获取客户车辆信息异常，异常信息", e);
            throw new BizException("更新快修快保工单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        String company = orderInfo.getCompany();
        orderInfo.setCompany((company == null) ? " " : company);
        // 保存工单信息
        com.tqmall.legend.common.Result result;
        try {
            result = iOrderService.update(orderFormEntityBo, userInfo, false);
            Object orderId = (result != null) ? result.getData() : "";
            log.info("工单状态流转:{} 更新快修快保工单,工单号为:{} 操作人:{}", carLicense, orderId, userId);
        } catch (BusinessCheckedException e1) {
            log.error("更新快修快保工单失败", e1);
            throw new BizException("更新快修快保工单失败!<br/>");
        } catch (RuntimeException e2) {
            log.error("更新快修快保工单异常", e2);
            throw new BizException("更新快修快保工单失败!");
        }
        if (!result.isSuccess()) {
            throw new BizException(result.getErrorMsg());
        }
        return (Long) result.getData();
    }


    //set 客户单位
    private void setCompany(List<OrderInfoVo> orderInfoVos) {
        Set<Long> customerIds = Sets.newHashSet();
        for (OrderInfoVo orderInfo : orderInfoVos) {
            if (orderInfo.getCustomerId() != null) {
                customerIds.add(Long.parseLong(orderInfo.getCustomerId().toString()));
            }
        }
        if (CollectionUtils.isEmpty(customerIds)) {
            return;
        }
        List<Customer> customers = customerService.selectByIds(customerIds.toArray(new Long[]{}));
        Map<Integer, Customer> customerMap = Maps.uniqueIndex(customers, new Function<Customer, Integer>() {
            @Override
            public Integer apply(Customer input) {
                return input.getId().intValue();
            }
        });
        for (OrderInfoVo orderInfo : orderInfoVos) {
            if (customerMap.containsKey(orderInfo.getCustomerId())) {
                orderInfo.setCompany(customerMap.get(orderInfo.getCustomerId()).getCompany());
            }
        }
    }

    //set 工单的应收金额和挂账金额
    private void setOrderPayAmountAndSignAmount(List<OrderInfoVo> orderInfoVos, Long shopId) {
        Set<Long> orderIdSet = Sets.newHashSet();
        for (OrderInfoVo orderInfo : orderInfoVos) {
            if (orderInfo.getOrderStatus().equals(OrderStatusEnum.DDYFK.getKey())) {
                orderIdSet.add(Long.parseLong(orderInfo.getId()));
            }
        }
        if (CollectionUtils.isEmpty(orderIdSet)) {
            return;
        }
        com.tqmall.core.common.entity.Result<DebitAndRedBillDTO> result = rpcDebitBillService.findBillListByRelIds(shopId, DebitTypeEnum.ORDER.getId(), orderIdSet, false);
        if (result.isSuccess() && result.getData() != null) {
            List<DebitBillDTO> debitBillDTOList = result.getData().getDebitBillDTOList();
            if(CollectionUtils.isEmpty(debitBillDTOList)){
                return;
            }
            Map<Long, DebitBillDTO> debitBillMap = Maps.uniqueIndex(debitBillDTOList, new Function<DebitBillDTO, Long>() {
                @Override
                public Long apply(DebitBillDTO input) {
                    return input.getRelId();
                }
            });
            for (OrderInfoVo orderInfo : orderInfoVos) {
                if (debitBillMap.containsKey(Long.parseLong(orderInfo.getId()))) {
                    DebitBillDTO debitBillDTO = debitBillMap.get(Long.parseLong(orderInfo.getId()));
                    //应收金额
                    orderInfo.setPayAmount(debitBillDTO.getReceivableAmount().doubleValue());
                    //实收金额
                    orderInfo.setPayedAmount(debitBillDTO.getPaidAmount());
                    //挂账金额
                    orderInfo.setSignAmount(debitBillDTO.getSignAmount().doubleValue());
                }
            }
        }
    }

    //set 工单业务类型名
    private void setOrderTypeName(LegendOrderRequest orderRequest, List<OrderInfoVo> orderInfoVos) {
        Map map = Maps.newHashMap();
        map.put("shopId", orderRequest.getShopId());
        List<OrderType> orderTypes = orderTypeService.selectNoCache(map);
        Map<Integer, OrderType> orderTypeMap = Maps.uniqueIndex(orderTypes, new Function<OrderType, Integer>() {
            @Override
            public Integer apply(OrderType input) {
                return input.getId().intValue();
            }
        });
        for (OrderInfoVo orderInfoVo : orderInfoVos) {
            if (orderInfoVo.getOrderType() != null && !CollectionUtils.isEmpty(orderTypeMap) &&
                    orderTypeMap.containsKey(orderInfoVo.getOrderType())) {
                orderInfoVo.setOrderTypeName(orderTypeMap.get(orderInfoVo.getOrderType()).getName());
            }
        }
    }

    /**
     * wrapper customercar
     *
     * @param orderInfo 工单实体
     * @return
     */
    private CustomerCar wrapperCustomerCar(OrderInfo orderInfo) {
        CustomerCar customerCar = new CustomerCar();
        // 设置车牌
        customerCar.setLicense(orderInfo.getCarLicense());
        // 设置车型
        customerCar.setCarBrand(orderInfo.getCarBrand());
        customerCar.setCarBrandId(orderInfo.getCarBrandId());
        customerCar.setCarSeries(orderInfo.getCarSeries());
        customerCar.setCarSeriesId(orderInfo.getCarSeriesId());
        customerCar.setCarModel(orderInfo.getCarModels());
        customerCar.setCarModelId(orderInfo.getCarModelsId());
        customerCar.setImportInfo(orderInfo.getImportInfo());
        customerCar.setCarYear(orderInfo.getCarYear());
        customerCar.setCarYearId(orderInfo.getCarYearId());
        customerCar.setCarPower(orderInfo.getCarPower());
        customerCar.setCarPowerId(orderInfo.getCarPowerId());
        customerCar.setCarGearBox(orderInfo.getCarGearBox());
        customerCar.setCarGearBoxId(orderInfo.getCarGearBoxId());

        // 设置联系人
        customerCar.setContact(orderInfo.getContactName());
        customerCar.setContactMobile(orderInfo.getContactMobile());
        // 设置行驶里程
        if (!StringUtils.isBlank(orderInfo.getMileage())) {
            Long mileage = Long.parseLong(orderInfo.getMileage());
            customerCar.setMileage(mileage);
        }
        // 设置vin
        customerCar.setVin(orderInfo.getVin());
        // 客户单位
        customerCar.setCompany(orderInfo.getCompany());

        return customerCar;
    }
}
