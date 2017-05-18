package com.tqmall.legend.facade.order.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.biz.order.vo.OrderGoodsVo;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.precheck.PrecheckValue;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.order.OrderPrecheckDetailsFacade;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.facade.order.vo.OrderPrecheckDetailsVo;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lixiao on 16/4/15.
 */
@Slf4j
@Service
public class WrapperOrderFacadeImpl implements WrapperOrderFacade {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WarehouseOutService warehouseOutService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private OrderPrecheckDetailsFacade orderPrecheckDetailsFacade;

    /**
     * wrapper model of edited page
     *
     * @param orderId
     * @param model
     * @param shopId
     */

    public OrderInfo wrapperModelOfEditedPage(Long orderId, Model model, Long shopId)
            throws BusinessCheckedException {
        // 主对象为null，直接退出逻辑，前端给公共错误提示
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            throw new BusinessCheckedException("", "工单不存在,编号：" + orderId);
        }

        OrderInfo orderInfo = orderInfoOptional.get();

        Optional<OrderType> orderTypeObj = orderTypeService.getOrderType(orderInfo.getOrderType());
        if (orderTypeObj.isPresent()) {
            orderInfo.setOrderTypeName(orderTypeObj.get().getName());
        }

        //客户车辆信息
        Customer customer = customerService.selectById(orderInfo.getCustomerId());
        model.addAttribute("customer", customer);
        CustomerCar customerCar = customerCarService.selectById(orderInfo.getCustomerCarId());
        //客户车辆信息
        model.addAttribute("customerCar", customerCar);

        orderInfo = wrapperOrderInfo(orderInfo, customerCar, customer);

        // 工单信息
        model.addAttribute("orderInfo", orderInfo);

        List<OrderGoods> realOrderGoodsList =
                orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        if (!CollectionUtils.isEmpty(realOrderGoodsList)) {
            for (OrderGoods orderGoods : realOrderGoodsList) {
                orderGoods.setOutNumber(warehouseOutService.countOutGoods(
                        orderGoods.getGoodsId(),
                        orderGoods.getShopId(),
                        orderGoods.getOrderId(),
                        orderGoods.getId()));
            }
        }

        // 关联销售员
        List<OrderGoodsVo> orderGoodsVoList = orderGoodsListReferSaleName(realOrderGoodsList);
        model.addAttribute("orderGoodsList", orderGoodsVoList);

        // 工单基本服务
        List<OrderServices> baseOrderService = orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC);
        // 关联维修工名称
        List<OrderServicesVo> orderServicesVos = orderServiceListReferWorderName(baseOrderService);
        model.addAttribute("basicOrderService", orderServicesVos);

        // 工单附属服务
        List<OrderServices> orderServicesList2 =
                orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.ANCILLARY);
        model.addAttribute("additionalServices", orderServicesList2);

        return orderInfo;
    }

    /**
     * 钣喷中心相关代码：获取预检信息
     * @param orderId
     * @param model
     */
    @Override
    public void wrapperModelOfEditedPageAboutPrecheck(Long orderId, Model model) {
        OrderPrecheckDetailsVo orderPrecheckDetailsVo = orderPrecheckDetailsFacade.getOrderPrecheckDetailsByOrderId(orderId);
        // 工单预检信息
        List<PrecheckValue> precheckValueList = orderPrecheckDetailsVo.getPrecheckValueList();
        model.addAttribute("outlineValues", precheckValueList);
        //设置预检单外观检查详情
        List<OrderPrecheckDetails> orderPrecheckDetailsList = orderPrecheckDetailsVo.getOrderPrecheckDetailsList();
        if(!CollectionUtils.isEmpty(orderPrecheckDetailsList)){
            model.addAttribute("precheckDetailsList", orderPrecheckDetailsList);
            OrderPrecheckDetails orderPrecheckDetails = orderPrecheckDetailsList.get(0);
            Long precheckId = orderPrecheckDetails.getPrecheckId();
            model.addAttribute("precheckId", precheckId);
        }
    }

    /**
     * 批量关联维修工名称
     *
     * @param orderServicesList 从数据库查询出的服务列表
     */
    public List<OrderServicesVo> orderServiceListReferWorderName(List<OrderServices> orderServicesList) {
        List<OrderServicesVo> orderServicesVoList = null;
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            orderServicesVoList = new ArrayList<>(orderServicesList.size());
            Set<Long> workerIdSet = new HashSet();
            for (OrderServices orderServices : orderServicesList) {
                OrderServicesVo orderServicesVo = OrderServicesVo.getVo(orderServices);
                String workerIds = orderServices.getWorkerIds();
                if (StringUtils.isNotBlank(workerIds) && !"0".equals(workerIds)) {
                    // 通过逗号切分
                    String[] workerIdArr = workerIds.split(",");
                    if (ArrayUtils.isNotEmpty(workerIdArr)) {
                        if (workerIdArr.length > Constants.MAX_WORKER_NUMBER) {
                            workerIds = StringUtils.join(workerIdArr, ",", 0, Constants.MAX_WORKER_NUMBER);
                            orderServicesVo.setWorkerIds(workerIds);
                            workerIdArr = ArrayUtils.subarray(workerIdArr, 0, Constants.MAX_WORKER_NUMBER);
                        }
                        for (String workerId : workerIdArr) {
                            workerId = workerId.trim();
                            if (!"0".equals(workerId) || !"".equals(workerId)) {
                                long id = Long.parseLong(workerId);
                                workerIdSet.add(id);
                            }
                        }
                    }
                }
                orderServicesVoList.add(orderServicesVo);
            }
            if (!CollectionUtils.isEmpty(workerIdSet)) {
                Long[] workerIdArray = new Long[workerIdSet.size()];
                workerIdArray = workerIdSet.toArray(workerIdArray);
                //查询多个维修工
                List<ShopManager> shopManagerList = shopManagerService.selectByIdsWithDeleted(workerIdArray);
                Map<Long, String> shopManagerMap = new HashMap(shopManagerList.size());
                for (ShopManager shopManager : shopManagerList) {
                    shopManagerMap.put(shopManager.getId(), shopManager.getName());
                }

                for (OrderServicesVo orderServicesVo : orderServicesVoList) {
                    String workerIds = orderServicesVo.getWorkerIds();
                    if (StringUtils.isNotBlank(workerIds) && !"0".equals(workerIds)) {
                        // 通过逗号切分
                        String[] workerIdArr = workerIds.split(",");
                        if (ArrayUtils.isNotEmpty(workerIdArr)) {
                            StringBuilder workerNames = new StringBuilder();
                            for (String workerId : workerIdArr) {
                                workerId = workerId.trim();
                                if (!"0".equals(workerId) || !"".equals(workerId)) {
                                    long id = Long.parseLong(workerId);
                                    String name = shopManagerMap.get(id);
                                    if (StringUtils.isNotEmpty(name)) {
                                        workerNames.append(name).append(",");
                                    }
                                }
                            }
                            if (workerNames.length() > 0) {
                                orderServicesVo.setWorkerNames(workerNames.substring(0, workerNames.length() - 1));
                            }
                        }
                    }
                }
            }
        }
        return orderServicesVoList;
    }

    /**
     * 批量关联销售员
     *
     * @param orderGoodsList
     * @return
     */
    public List<OrderGoodsVo> orderGoodsListReferSaleName(List<OrderGoods> orderGoodsList) {
        List<OrderGoodsVo> orderGoodsVoList = null;
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            orderGoodsVoList = new ArrayList<>(orderGoodsList.size());
            Set<Long> saleIdSet = new HashSet();
            for (OrderGoods orderGoods : orderGoodsList) {
                saleIdSet.add(orderGoods.getSaleId());

                OrderGoodsVo orderGoodsVo = OrderGoodsVo.getVo(orderGoods);
                orderGoodsVoList.add(orderGoodsVo);
            }

            if (!CollectionUtils.isEmpty(saleIdSet)) {
                Long[] saleIdArray = new Long[saleIdSet.size()];
                saleIdArray = saleIdSet.toArray(saleIdArray);
                //查询多个销售员
                List<ShopManager> shopManagerList = shopManagerService.selectByIdsWithDeleted(saleIdArray);
                Map<Long, String> shopManagerMap = new HashMap(shopManagerList.size());
                for (ShopManager shopManager : shopManagerList) {
                    shopManagerMap.put(shopManager.getId(), shopManager.getName());
                }

                for (OrderGoodsVo orderGoodsVo : orderGoodsVoList) {
                    String saleName = shopManagerMap.get(orderGoodsVo.getSaleId());
                    orderGoodsVo.setSaleName(saleName);
                }
            }
        }
        return orderGoodsVoList;
    }

    /**
     * wrapper customercar
     *
     * @param orderInfo 工单实体
     * @return
     */
    @Override
    public CustomerCar wrapperCustomerCar(OrderInfo orderInfo) {
        CustomerCar customerCar = new CustomerCar();
        // 设置车牌
        customerCar.setLicense(orderInfo.getCarLicense());
        // 设置车型

        if (orderInfo.getCarModelsId() != null) {
            customerCar.setCarBrand(orderInfo.getCarBrand());
            customerCar.setCarBrandId(orderInfo.getCarBrandId());
            customerCar.setCarSeries(orderInfo.getCarSeries());
            customerCar.setCarSeriesId(orderInfo.getCarSeriesId());
            customerCar.setCarModel(orderInfo.getCarModels());
            customerCar.setCarModelId(orderInfo.getCarModelsId());
            customerCar.setImportInfo(orderInfo.getImportInfo());
        }
        if (orderInfo.getCarGearBoxId() != null) {
            customerCar.setCarYear(orderInfo.getCarYear());
            customerCar.setCarYearId(orderInfo.getCarYearId());
            customerCar.setCarPower(orderInfo.getCarPower());
            customerCar.setCarPowerId(orderInfo.getCarPowerId());
            customerCar.setCarGearBox(orderInfo.getCarGearBox());
            customerCar.setCarGearBoxId(orderInfo.getCarGearBoxId());
        }

        // 设置联系人
        customerCar.setContact(orderInfo.getContactName());
        customerCar.setContactMobile(orderInfo.getContactMobile());
        // 设置行驶里程
        if (!StringUtils.isBlank(orderInfo.getMileage())) {
            Long mileage = Long.parseLong(orderInfo.getMileage());
            customerCar.setMileage(mileage);
        }
        // 设置vin
        if(!StringUtils.isBlank(orderInfo.getVin())) {
            customerCar.setVin(orderInfo.getVin());
        }
        // 客户单位
        if(!StringUtils.isBlank(orderInfo.getCompany())) {
            customerCar.setCompany(orderInfo.getCompany());
        }

        // 设置下次保养里程
        String upkeepMileage = orderInfo.getUpkeepMileage();
        if (!StringUtils.isBlank(upkeepMileage)) {
            customerCar.setUpkeepMileage(upkeepMileage);
        }
        // 设置车辆颜色
        String carColor = orderInfo.getCarColor();
        if (StringUtils.isNotBlank(carColor)) {
            customerCar.setColor(carColor);
        }
        //设置保险到期
        String insuranceTimeStr = orderInfo.getInsuranceTimeStr();
        if(StringUtils.isNotBlank(insuranceTimeStr)){
            customerCar.setInsuranceTimeStr(insuranceTimeStr);
        }
        return customerCar;
    }


    /**
     * wrapper orderInfo
     *
     * @param orderInfo
     * @param customerCar
     * @param customer
     * @return
     */
    @Override
    public OrderInfo wrapperOrderInfo(OrderInfo orderInfo, CustomerCar customerCar, Customer customer) {
        if (orderInfo == null) {
            orderInfo = new OrderInfo();
        }
        if (customerCar != null) {
            orderInfo.setCarBrand(customerCar.getCarBrand());
            orderInfo.setCarBrandId(customerCar.getCarBrandId());
            orderInfo.setCarSeries(customerCar.getCarSeries());
            orderInfo.setCarSeriesId(customerCar.getCarSeriesId());
            orderInfo.setCarModels(customerCar.getCarModel());
            orderInfo.setCarModelsId(customerCar.getCarModelId());
            orderInfo.setImportInfo(customerCar.getImportInfo());

            orderInfo.setCarYear(customerCar.getCarYear());
            orderInfo.setCarYearId(customerCar.getCarYearId());
            orderInfo.setCarPower(customerCar.getCarPower());
            orderInfo.setCarPowerId(customerCar.getCarPowerId());
            orderInfo.setCarGearBox(customerCar.getCarGearBox());
            orderInfo.setCarGearBoxId(customerCar.getCarGearBoxId());

            orderInfo.setVin(customerCar.getVin());
            orderInfo.setEngineNo(customerCar.getEngineNo());
            orderInfo.setCarColor(customerCar.getColor());
            orderInfo.setInsuranceCompanyId(customerCar.getInsuranceId());
            orderInfo.setInsuranceCompanyName(customerCar.getInsuranceCompany());
            orderInfo.setBuyTime(customerCar.getBuyTime());
            //如果是新建工单，则数据从车辆取
            if (orderInfo.getId() == null) {
                orderInfo.setUpkeepMileage(customerCar.getUpkeepMileage());
                Long mileage = customerCar.getMileage();
                if (mileage != null && Long.compare(mileage, 0) > 0) {
                    orderInfo.setMileage(mileage.toString());
                }
                orderInfo.setCarLicense(customerCar.getLicense());
            }
            //设置String类型的保险到期日期
            orderInfo.setInsuranceTimeStr(customerCar.getInsuranceTimeStr());
        }
        if (customer != null ) {
            if(orderInfo.getId() == null) {
                orderInfo.setCustomerName(customer.getCustomerName());
                orderInfo.setCustomerMobile(customer.getMobile());
                orderInfo.setCustomerAddress(customer.getCustomerAddr());
                String contactName = customer.getContact();
                if (StringUtils.isBlank(contactName)) {
                    contactName = customer.getCustomerName();
                }
                orderInfo.setContactName(contactName);
                String contactMobile = customer.getContactMobile();
                if (StringUtils.isBlank(contactMobile)) {
                    contactMobile = customer.getMobile();
                }
                orderInfo.setContactMobile(contactMobile);
            }
            orderInfo.setIdentityCard(customer.getIdentityCard());
            orderInfo.setCompany(customer.getCompany());
            orderInfo.setCustomerId(customer.getId());
        }
        return orderInfo;
    }

    @Override
    public List<OrderInfoVo> wrapperOrderInfoVo(Long shopId, List<OrderInfoVo> orderInfoVos) {
        if (Langs.isEmpty(orderInfoVos)) {
            return orderInfoVos;
        }
        List<Long> orderIds = Lists.transform(orderInfoVos, new Function<OrderInfoVo, Long>() {
            @Override
            public Long apply(OrderInfoVo input) {
                return Long.valueOf(input.getId());
            }
        });
        //设置服务信息
        wrapperOrderServices(shopId, orderInfoVos, orderIds);
        //设置配件信息
        wrapperGoodsServices(orderInfoVos, orderIds);
        return orderInfoVos;
    }

    /**
     * 设置服务信息：服务内容、出库成本价
     *
     * @param shopId
     * @param orderInfoVos
     * @param orderIds
     */
    private void wrapperOrderServices(Long shopId, List<OrderInfoVo> orderInfoVos, List<Long> orderIds) {
        // 工单服务名
        List<OrderServices> orderServicesList = orderServicesService.selOrderServicesByOrderIds(shopId, orderIds, 1);

        Map<Long, StringBuilder> orderServiceNameMap = new HashMap<>(orderIds.size());
        if (Langs.isNotEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                StringBuilder orderServiceNameSb = orderServiceNameMap.get(orderServices.getOrderId());
                if (Langs.isNull(orderServiceNameSb)) {
                    orderServiceNameSb = new StringBuilder();
                }
                if (Langs.isNotBlank(orderServices.getServiceName())) {
                    orderServiceNameSb.append(orderServices.getServiceName()).append(";");
                    orderServiceNameMap.put(orderServices.getOrderId(), orderServiceNameSb);
                }
            }
        }

        // 工单配件成本
        Map<Long, BigDecimal> orderId2realInventoryAmountMap = warehouseOutService.mapOrderId2realInventoryAmount(shopId, orderIds);

        // 组装数据
        for (OrderInfoVo orderInfo : orderInfoVos) {
            Long orderId = Long.parseLong(orderInfo.getId());
            StringBuilder orderServiceNameSb = orderServiceNameMap.get(orderId);
            if (Langs.isNotNull(orderServiceNameSb)) {
                orderInfo.setServiceContent(orderServiceNameSb.toString());
            }

            BigDecimal realInventoryAmount = orderId2realInventoryAmountMap.get(orderId);
            realInventoryAmount = realInventoryAmount == null ? BigDecimal.ZERO : realInventoryAmount;
            orderInfo.setRealInventoryAmount(realInventoryAmount);
        }
    }

    /**
     * 设置配件信息：配件内容
     *
     * @param orderInfoVos
     * @param orderIds
     */
    private void wrapperGoodsServices(List<OrderInfoVo> orderInfoVos, List<Long> orderIds) {
        // 工单服务名
        Long[] orderIdsArr = orderIds.toArray(new Long[orderIds.size()]);
        List<OrderGoods> orderGoodsList = orderGoodsService.selectByOrderIds(orderIdsArr);

        Map<Long, StringBuilder> orderGoodsNameMap = new HashMap<>(orderIds.size());
        if (Langs.isNotEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                StringBuilder orderGoodsNameSb = orderGoodsNameMap.get(orderGoods.getOrderId());
                if (Langs.isNull(orderGoodsNameSb)) {
                    orderGoodsNameSb = new StringBuilder();
                }
                String goodsName = orderGoods.getGoodsName();
                if (Langs.isNotBlank(goodsName)) {
                    //零件号，配件名称，数量，单位
                    String format = orderGoods.getGoodsFormat();
                    if(Langs.isBlank(format)){
                        format = "";
                    }
                    BigDecimal goodsNumber = orderGoods.getGoodsNumber();
                    String goodsNumberStr = "";
                    if(goodsNumber != null) {
                        goodsNumberStr = goodsNumber.toString();
                        goodsNumberStr = goodsNumberStr.substring(0, goodsNumberStr.lastIndexOf("."));
                    }
                    orderGoodsNameSb
                            .append(format).append(",")
                            .append(goodsName).append(",")
                            .append(goodsNumberStr).append(",")
                            .append(orderGoods.getMeasureUnit()).append(";");
                    orderGoodsNameMap.put(orderGoods.getOrderId(), orderGoodsNameSb);
                }
            }
        }

        // 组装数据
        for (OrderInfoVo orderInfo : orderInfoVos) {
            Long orderId = Long.parseLong(orderInfo.getId());
            StringBuilder orderGoodsNameSb = orderGoodsNameMap.get(orderId);
            if (Langs.isNotNull(orderGoodsNameSb)) {
                orderInfo.setGoodsContent(orderGoodsNameSb.toString());
            }
        }
    }

}
