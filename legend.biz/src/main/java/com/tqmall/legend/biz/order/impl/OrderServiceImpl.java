package com.tqmall.legend.biz.order.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.cube.shop.RpcBusinessDailyService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.ICustomerContactService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.insurance.InsuranceCompanyService;
import com.tqmall.legend.biz.manager.thread.OrderInfoExtSaveThread;
import com.tqmall.legend.biz.manager.thread.OrderInfoExtUpdateThread;
import com.tqmall.legend.biz.manager.thread.ThreadManager;
import com.tqmall.legend.biz.order.*;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.JsonUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.order.OrderInfoDao;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.insurance.InsuranceCompany;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.precheck.PrecheckDetails;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.precheck.PrechecksFacade;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * orderInfo service
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl implements IOrderService {


    public static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    public static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    CustomerCarService customerCarService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ShopServiceInfoService shopServiceInfoService;
    @Autowired
    OrderGoodsService orderGoodsService;
    @Autowired
    OrderServicesService orderServicesService;
    @Autowired
    CustomerService customerService;
    @Autowired
    IOrderSnapshotService orderSnapshotService;
    @Autowired
    ICustomerContactService customerContactService;
    @Autowired
    IVirtualOrderService virtualOrderService;
    @Autowired
    IVirtualOrdergoodService virtualOrdergoodService;
    @Autowired
    IVirtualOrderserviceService virtualOrderserviceService;
    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private SnFactory snFactory;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ThreadManager threadManager;
    @Autowired
    private OrderInfoExtSaveThread orderInfoExtSaveThread;
    @Autowired
    private OrderInfoExtUpdateThread orderInfoExtUpdateThread;
    @Autowired
    private InsuranceCompanyService insuranceCompanyService;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private PrechecksFacade prechecksFacade;
    @Autowired
    private OrderPrecheckDetailsService orderPrecheckDetailsService;
    @Autowired
    private RpcBusinessDailyService rpcBusinessDailyService;

    /**
     * calculate virtual order amount
     *
     * @param orderGoodsMapList
     * @param orderServicesMapList
     * @return
     */
    private OrderInfo doVirtualCalcPrice(List<Map<String, Object>> orderGoodsMapList,
                                         List<Map<String, Object>> orderServicesMapList) {
        BigDecimal serviceAmount = BigDecimal.ZERO;
        BigDecimal goodsAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal goodsDiscount = BigDecimal.ZERO;
        BigDecimal serviceDiscount = BigDecimal.ZERO;
        BigDecimal feeDiscount = BigDecimal.ZERO;
        BigDecimal feeAmount = BigDecimal.ZERO;
        BigDecimal manageFee = BigDecimal.ZERO;

        if (!CollectionUtils.isEmpty(orderGoodsMapList)) {

            for (int i = 0; i < orderGoodsMapList.size(); i++) {
                String orderGoodsStr = JsonUtils.mapToJsonStr(orderGoodsMapList.get(i));
                mapper = new ObjectMapper()
                        .setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
                mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
                OrderGoods orderGoods = new OrderGoods();
                try {

                    orderGoods = mapper.readValue(orderGoodsStr, OrderGoods.class);
                } catch (Exception e) {
                    LOGGER.error("json字符串转orderGoods对象失败，ExceptionInfo:{}",e);
                }

                //  判断有效物料
                goodsAmount = goodsAmount.add(orderGoods.getGoodsPrice().multiply(
                        orderGoods.getGoodsNumber()));
                totalAmount = totalAmount.add(orderGoods.getGoodsPrice().multiply(
                        orderGoods.getGoodsNumber()));
                discount = discount.add(orderGoods.getDiscount());
                goodsDiscount = goodsDiscount.add(orderGoods.getDiscount());
            }
        }

        manageFee = goodsAmount.subtract(goodsDiscount);

        if (!CollectionUtils.isEmpty(orderServicesMapList)) {
            for (int i = 0; i < orderServicesMapList.size(); i++) {
                String orderServicesStr = JsonUtils.mapToJsonStr(orderServicesMapList.get(i));
                mapper = new ObjectMapper()
                        .setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
                mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
                OrderServices orderServices = new OrderServices();
                try {

                    orderServices =
                            mapper.readValue(orderServicesStr, OrderServices.class);
                } catch (Exception e) {
                    LOGGER.error("json字符串转orderService对象失败,ExceptionInfo:{}",e);
                }

                // 判断有效服务
                if (orderServices.getType() == 1) {
                    serviceDiscount = serviceDiscount.add(orderServices.getDiscount());
                    serviceAmount = serviceAmount.add(orderServices.getServiceAmount());
                }

                if (orderServices.getType() == 2) {
                    if (orderServices.getFlags() != null && orderServices.getFlags().equals(Constants.GLF)) {
                        feeDiscount = feeDiscount.add(orderServices.getDiscount());
                        manageFee = manageFee.multiply(orderServices.getManageRate());
                        feeAmount = feeAmount.add(manageFee);
                    } else {
                        feeDiscount = feeDiscount.add(orderServices.getDiscount());
                        feeAmount = feeAmount.add(orderServices.getServiceAmount());
                    }

                }
            }
        }

        totalAmount = goodsAmount.add(serviceAmount).add(feeAmount);
        discount = goodsDiscount.add(serviceDiscount).add(feeDiscount);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoodsAmount(goodsAmount);
        orderInfo.setServiceAmount(serviceAmount);
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setDiscount(discount);
        orderInfo.setGoodsDiscount(goodsDiscount);
        orderInfo.setServiceDiscount(serviceDiscount);
        orderInfo.setFeeAmount(feeAmount);
        orderInfo.setFeeDiscount(feeDiscount);
        orderInfo.setOrderAmount(totalAmount.subtract(discount));
        orderInfo.setManageFee(manageFee);

        return orderInfo;
    }

    /**
     * 保存工单使用地方：
     * app：快修快保
     * pc：快修快保、综合维修、销售单
     *
     * @param orderFormEntityBo 工单表单实体
     * @param userInfo          当前登录用户
     * @return
     * @throws BusinessCheckedException
     */
    @Override
    public Result save(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo)
            throws BusinessCheckedException {
        // order's basic info
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        // check customerCar is exsit
        Optional<CustomerCar> customerCarOptional =
                customerCarService.getCustomerCar(orderInfo.getCustomerCarId());
        if (!customerCarOptional.isPresent()) {
            // TODO 整理 可查的错误信息{错误码：错误信息}
            throw new BusinessCheckedException("1", "车牌信息不存在!");
        }
        String carLicense = orderInfo.getCarLicense();
        if (!StringUtils.isBlank(carLicense)) {
            carLicense = carLicense.toUpperCase();
            orderInfo.setCarLicense(carLicense);
        }
        //设置车牌图片
        orderInfo.setImgUrl(orderFormEntityBo.getImgUrl());
        // copy customerCar's property to order（TODO 冗余字段）
        CustomerCar customerCar = customerCarOptional.get();
        //回写下次保养时间
        CustomerCar tempCar = orderFormEntityBo.getCustomerCar();
        if (tempCar != null) {
            String keepupTimeStr = tempCar.getKeepupTimeStr();
            if (!StringUtils.isBlank(keepupTimeStr)) {
                Date keepupTime = DateUtil.convertStringToDateYMD(keepupTimeStr);
                customerCar.setKeepupTime(keepupTime);
            }
        }
        //回写下次保养里程
        String upkeepMileage = orderInfo.getUpkeepMileage();
        if (!StringUtils.isBlank(upkeepMileage)) {
            customerCar.setUpkeepMileage(upkeepMileage);
        }

        // 开单时间(yyyy-MM-dd HH:mm)
        String createTimeStr = orderInfo.getCreateTimeStr();
        Date createTime = null;
        if (StringUtils.isEmpty(createTimeStr)) {
            createTime = new Date();
        } else {
            createTime = DateUtil.convertStringToDate(createTimeStr, "yyyy-MM-dd HH:mm");
        }
        orderInfo.setCreateTime(createTime);

        orderInfo.setCustomerId(customerCar.getCustomerId());
        orderInfo.setCarBrand(customerCar.getCarBrand());
        orderInfo.setCarBrandId(customerCar.getCarBrandId());
        orderInfo.setCarSeries(customerCar.getCarSeries());
        orderInfo.setCarSeriesId(customerCar.getCarSeriesId());

        /**
         *  add by zsy
         *  设置冗余字段start
         *  TODO 页面上未展示，导致数据没set进工单冗余字段，以后页面加的话可能需要去除这些设置,
         *  customerAddress页面没展示，字段在customer里，所以这里不设置了，后续有需要需要查一次customer
         */
        orderInfo.setCarPowerId(customerCar.getCarPowerId());
        orderInfo.setCarPower(customerCar.getCarPower());
        orderInfo.setCarYearId(customerCar.getCarYearId());
        orderInfo.setCarYear(customerCar.getCarYear());
        orderInfo.setEngineNo(customerCar.getEngineNo());
        orderInfo.setCarColor(customerCar.getColor());
        /**
         * 设置冗余字段end
         */
        orderInfo.setCarCompany(customerCar.getCarCompany());
        orderInfo.setImportInfo(customerCar.getImportInfo());
        orderInfo.setCarAlias(customerCar.getByName());
        //add by twg 添加车型
        orderInfo.setCarModelsId(customerCar.getCarModelId());
        orderInfo.setCarModels(customerCar.getCarModel());
        // 关联联系人信息
        Optional<Customer> customerOptional = customerService.getCustomer(customerCar.getCustomerId());
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            orderInfo.setCustomerName(customer.getCustomerName());
            orderInfo.setCustomerMobile(customer.getMobile());
            orderInfo.setCustomerAddress(customer.getCustomerAddr());
        }

        // ordered goods
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        // valid ordered goods
        List<OrderGoods> validGoodsList = new ArrayList<OrderGoods>();
        // ordered services
        List<OrderServices> orderServiceList = new ArrayList<OrderServices>();
        // valid ordered services
        List<OrderServices> validServiceList = new ArrayList<OrderServices>();

        // transfer json to list
        Gson gson = new Gson();
        String orderGoodsJson = orderFormEntityBo.getOrderGoodJson();
        String orderServiceJson = orderFormEntityBo.getOrderServiceJson();
        try {
            orderGoodsList = gson.fromJson(orderGoodsJson,
                    new TypeToken<List<OrderGoods>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购物料JSON：{}", orderGoodsJson);
            throw new BizException("选购物料JSON转对象异常");
        }

        try {
            orderServiceList = gson.fromJson(orderServiceJson,
                    new TypeToken<List<OrderServices>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购服务JSON：{}", orderServiceJson);
            throw new BizException("选购服务JSON转对象异常");
        }

        // ordered goods‘s total number
        Long orderedGoodsTotalNumber = 0l;
        //  filter valid goods and calculate goods's amount
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            // valid goods's ids
            Set<Long> goodsIdSet = new HashSet<Long>();
            for (OrderGoods goods : orderGoodsList) {
                Long goodsId = goods.getGoodsId();
                if (goodsId != null) {
                    goodsIdSet.add(goodsId);

                    // calculator goods’s amount
                    BigDecimal goodsPrice = goods.getGoodsPrice();
                    // goodsPrice default 0
                    goodsPrice = (goodsPrice == null) ? BigDecimal.ZERO : goodsPrice;
                    BigDecimal goodsNumber = goods.getGoodsNumber();
                    // goodsNumber default 1
                    goodsNumber = (goodsNumber == null ||
                            goodsNumber.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE : goodsNumber;
                    goods.setGoodsNumber(goodsNumber);
                    BigDecimal goodsAmountTemp = goodsPrice.multiply(goodsNumber);
                    goods.setGoodsAmount(goodsAmountTemp);
                    BigDecimal discount = goods.getDiscount();
                    // discount default 0
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal soldAmountTemp = goodsAmountTemp.subtract(discount);
                    goods.setSoldAmount(soldAmountTemp);
                    goods.setSoldPrice(soldAmountTemp.divide(goodsNumber, 8, BigDecimal.ROUND_HALF_UP));

                    validGoodsList.add(goods);
                }
            }

            // check goods is exsit
            int goodsIdSize = goodsIdSet.size();
            if (goodsIdSize > 0) {
                List<Goods> goodsLatestData =
                        goodsService.selectByIds(goodsIdSet.toArray(new Long[goodsIdSize]));
                int goodsLatestDataSize = goodsLatestData.size();
                // Map<goods.id,goods>
                Map<Long, Goods> goodsLatestDataMap = new HashMap<Long, Goods>(goodsLatestDataSize);
                for (Goods good : goodsLatestData) {
                    goodsLatestDataMap.put(good.getId(), good);
                }
                if (goodsIdSize != goodsLatestDataSize) {
                    StringBuffer goodsErrorMsgSB = new StringBuffer("选择的配件不存在,配件编号如下：");
                    Iterator<Long> goodsIdSetIT = goodsIdSet.iterator();
                    while (goodsIdSetIT.hasNext()) {
                        Long goodsId = goodsIdSetIT.next();
                        if (!goodsLatestDataMap.containsKey(goodsId)) {
                            goodsErrorMsgSB.append("<br/>");
                            goodsErrorMsgSB.append(goodsId);
                        }
                    }

                    throw new BusinessCheckedException("2", goodsErrorMsgSB.toString());
                }
            }

            orderedGoodsTotalNumber = Long.valueOf(validGoodsList.size() + "");
        }
        // goods's total number
        orderInfo.setGoodsCount(orderedGoodsTotalNumber);

        // ordered service total number
        Long orderedServiceNumber = 0L;
        if (!CollectionUtils.isEmpty(orderServiceList)) {
            Set<Long> serviceIdSet = new HashSet<Long>();
            Set<Long> workerIdSet = new HashSet<Long>();
            // filter valid service and calculate servie's amount
            for (OrderServices service : orderServiceList) {
                Long serviceId = service.getServiceId();
                if (serviceId != null) {
                    serviceIdSet.add(serviceId);
                    String workerIds = service.getWorkerIds();

                    if (StringUtils.isNotBlank(workerIds) && !"0".equals(workerIds)) {
                        // 通过逗号切分
                        String[] workerIdArr = workerIds.split(",");
                        if (ArrayUtils.isNotEmpty(workerIdArr)) {
                            if (workerIdArr.length > Constants.MAX_WORKER_NUMBER) {
                                workerIds = StringUtils.join(workerIdArr, ",", 0, Constants.MAX_WORKER_NUMBER);
                                service.setWorkerIds(workerIds);
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
                    BigDecimal servicePrice = service.getServicePrice();
                    // servicePrice default 0
                    servicePrice = (servicePrice == null) ? BigDecimal.ZERO : servicePrice;
                    // serviceHour default 0
                    BigDecimal serviceHour = service.getServiceHour();
                    serviceHour = (serviceHour == null) ? BigDecimal.ZERO : serviceHour;
                    // discount default 0
                    BigDecimal discount = service.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal serviceAmountTemp = servicePrice.multiply(serviceHour);
                    service.setServiceAmount(serviceAmountTemp);
                    BigDecimal soldAmountTemp = serviceAmountTemp.subtract(discount);
                    service.setSoldAmount(soldAmountTemp);
                    if (serviceHour.compareTo(BigDecimal.ZERO) <= 0) {
                        service.setSoldPrice(BigDecimal.ZERO);
                    } else {
                        service.setSoldPrice(soldAmountTemp.divide(serviceHour, 8, BigDecimal.ROUND_HALF_UP));
                    }
                    validServiceList.add(service);
                }
            }

            // check service is exsit
            int serviceIdSetSize = serviceIdSet.size();
            if (serviceIdSetSize > 0) {
                List<ShopServiceInfo> serviceLatestData =
                        shopServiceInfoService.selectByIds(serviceIdSet.toArray(new Long[serviceIdSetSize]));
                int serviceLatestDataSize = serviceLatestData.size();
                // Map<service.id,service>
                Map<Long, ShopServiceInfo> orderServiceListDBMap = new HashMap<Long, ShopServiceInfo>(serviceLatestDataSize);
                for (ShopServiceInfo serviceInfo : serviceLatestData) {
                    orderServiceListDBMap.put(serviceInfo.getId(), serviceInfo);
                }
                if (serviceIdSetSize != serviceLatestDataSize) {
                    StringBuffer serviceErrorMsgSB = new StringBuffer("选择的服务不存在,服务编号如下：");
                    Iterator<Long> serviceIdSetIT = serviceIdSet.iterator();
                    while (serviceIdSetIT.hasNext()) {
                        Long serviceId = serviceIdSetIT.next();
                        if (!orderServiceListDBMap.containsKey(serviceId)) {
                            serviceErrorMsgSB.append("<br/>");
                            serviceErrorMsgSB.append(serviceId);
                        }
                    }

                    throw new BusinessCheckedException("3", serviceErrorMsgSB.toString());
                }
            }

            // check worker is exsit
            int workerIdSetSize = workerIdSet.size();
            if (workerIdSetSize > 0) {
                List<ShopManager> shopManagerLatestData =
                        shopManagerService.selectByIds(workerIdSet.toArray(new Long[workerIdSetSize]));
                int managerLatestDataSize = shopManagerLatestData.size();
                // Map<shopManager.id,shopManager>
                Map<Long, ShopManager> shopManagerListDBMap = new HashMap<Long, ShopManager>(managerLatestDataSize);
                for (ShopManager shopManager : shopManagerLatestData) {
                    shopManagerListDBMap.put(shopManager.getId(), shopManager);
                }
                if (workerIdSetSize != managerLatestDataSize) {
                    StringBuffer workerErrorMsgSB = new StringBuffer("指派的维修工不存在,维修工名称如下：");
                    Iterator<Long> workerIdSetIT = workerIdSet.iterator();
                    while (workerIdSetIT.hasNext()) {
                        Long workerId = workerIdSetIT.next();
                        if (!shopManagerListDBMap.containsKey(workerId)) {
                            workerErrorMsgSB.append("<br/>");
                            ShopManager shopManager = shopManagerListDBMap.get(workerId);
                            if(shopManager == null){
                                throw new BusinessCheckedException("5", "您选择了已删除的维修工，请重新选择维修工");
                            }
                            workerErrorMsgSB.append(shopManager.getName());
                        }
                    }

                    throw new BusinessCheckedException("4", workerErrorMsgSB.toString());
                }

            }

            orderedServiceNumber = Long.valueOf(validServiceList.size() + "");
        }
        orderInfo.setServiceCount(orderedServiceNumber);

        // calculate order expense
        calculateOrderExpense(orderInfo, validGoodsList, validServiceList);

        // 检验工单金额是否超过限度
        checkOrderAmountIsValid(orderInfo);

        // TODO 设置公共信息(创建者、创建时间、更新者、更新时间)
        orderInfo.setCreator(userInfo.getUserId());
        orderInfo.setModifier(userInfo.getUserId());
        orderInfo.setOperatorName(userInfo.getName());
        orderInfo.setShopId(userInfo.getShopId());

        // set order's status
        orderInfo.setOrderStatus(OrderStatusEnum.CJDD.getKey());
        // set pay's status
        orderInfo.setPayStatus(PayStatusEnum.UNPAY.getCode());
        // set expectedTime
        orderInfo.setExpectedTime(DateUtil.convertStringToDate(
                orderInfo.getExpectedTimeYMD(), DateUtil.DATE_FORMAT_YMDHM));
        // set buyTime
        String buyTimeYMD = orderInfo.getBuyTimeYMD();
        if(StringUtils.isNotBlank(buyTimeYMD)){
            orderInfo.setBuyTime(DateUtil.convertStringToDate(orderInfo.getBuyTimeYMD(),
                    DateUtil.DATE_FORMAT_YMD));
        }else{
            orderInfo.setBuyTime(customerCar.getBuyTime());
        }
        //set orderStatus
        String orderStatus = orderInfo.getOrderStatus();
        //set finishTime
        if (orderStatus != null && orderStatus.equals(OrderStatusEnum.DDWC.getKey())) {
            orderInfo.setFinishTime(new Date());
        }
        //set postscript
        String postscript = orderInfo.getPostscript();
        orderInfo.setPostscript(postscript == null ? "" : postscript);
        //set vin,若vin为null,则从customerCar取
        String vin = orderInfo.getVin();
        orderInfo.setVin(vin == null ? customerCar.getVin() : vin);
        //set insuranceCompanyName
        Long insuranceCompanyId = orderInfo.getInsuranceCompanyId();
        Long otherInsuranceCompanyId = orderInfo.getOtherInsuranceCompanyId();
        if ((insuranceCompanyId != null && insuranceCompanyId > 0) || (otherInsuranceCompanyId != null && otherInsuranceCompanyId > 0)) {
            List<InsuranceCompany> insuranceCompanyList = insuranceCompanyService.select(null);
            for (InsuranceCompany insuranceCompany : insuranceCompanyList) {
                if (insuranceCompany.getId().equals(insuranceCompanyId)) {
                    orderInfo.setInsuranceCompanyName(insuranceCompany.getName());
                }
                if (insuranceCompany.getId().equals(otherInsuranceCompanyId)) {
                    orderInfo.setOtherInsuranceCompanyName(insuranceCompany.getName());
                }
            }
        }
        //设置预检信息
        List<OrderPrecheckDetails> orderPrecheckDetailsList = setOrderPrecheckDetails(orderFormEntityBo, userInfo.getShopId(), orderFormEntityBo.getPrecheckId());
        // insert DB
        insertOrder(orderInfo, validGoodsList, validServiceList, orderPrecheckDetailsList, userInfo);
        //添加工单详情日志start
        Optional<OrderInfo> orderInfoOptional = getOrder(orderInfo.getId(), orderInfo.getShopId());
        if(orderInfoOptional.isPresent()){
            OrderInfo tempOrder = orderInfoOptional.get();
            StringBuffer orderLog = new StringBuffer();
            orderLog.append("工单创建成功: 工单号为:");
            orderLog.append(tempOrder.getId());
            orderLog.append(" 操作人:");
            orderLog.append(userInfo.getUserId());
            String operationLog = OrderOperationLog.getOperationLog(tempOrder, orderLog);
            LOGGER.info(operationLog);
        }
        //添加工单详情日志end
        // 捕获无关紧要信息保存异常，确保工单主数据保存成功。
        try {
            Long proxyId = orderFormEntityBo.getProxyId();
            orderInfoExtSaveThread.init(customerCar, orderInfo, userInfo, orderGoodsJson,
                    orderServiceJson, proxyId);
            threadManager.execute(orderInfoExtSaveThread);
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }

        return Result.wrapSuccessfulResult(orderInfo.getId());
    }

    /**
     * 引流活动工单保存方法
     *
     * @param orderInfo         工单基本信息
     * @param orderServicesList 工单服务列表
     * @param userInfo
     * @return
     */
    @Override
    public OrderInfo save(OrderInfo orderInfo, List<OrderServices> orderServicesList, UserInfo userInfo) {
        // currrent car's license
        String carLicense = orderInfo.getCarLicense();
        // current record's operator and shop
        Long optUserId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // generate Order'Sn
        String newOrderSn = snFactory.generateOrderSn(shopId);
        orderInfo.setOrderSn(newOrderSn);
        // TODO Aspect record log
        LOGGER.info("工单操作流水：{} 工单编号生成,编号为：{} 操作人：{}", carLicense, newOrderSn, optUserId);

        try {
            orderInfoDao.insert(orderInfo);
        } catch (Exception e) {
            LOGGER.error("DB插入工单数据异常，工单实体:{}", orderInfo.toString());
            throw new RuntimeException("DB数据库保存工单数据异常");
        }
        // new orderId
        Long newOrderId = orderInfo.getId();
        LOGGER.info("工单操作流水：{} 保存工单基本信息 操作人:{}", carLicense, optUserId);

        // batch insert new serviceList
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                // refer new order
                orderServices.setOrderId(newOrderId);
                orderServices.setOrderSn(newOrderSn);
                // set operate message
                orderServices.setCreator(optUserId);
                orderServices.setModifier(optUserId);
                orderServices.setShopId(shopId);
            }

            // batch save
            try {
                orderServicesService.batchSave(orderServicesList);
            } catch (Exception e) {
                LOGGER.error("插入服务数据异常，服务实体:{}", new Gson().toJson(orderServicesList));
                throw new RuntimeException("数据库保存服务数据异常");
            }
            LOGGER.info("工单操作流水：{} 保存工单服务信息 操作人:{}", carLicense, optUserId);
        }

        Optional<OrderInfo> orderInfoOptional = this.getOrder(newOrderId);
        if (!orderInfoOptional.isPresent()) {
            LOGGER.error("DB未成功保存工单数据");
            throw new RuntimeException("数据库保存服务数据异常");
        }

        return orderInfoOptional.get();
    }


    /**
     * 保存工单基本信息、选购商品、选购服务
     *
     * @param orderInfo
     * @param orderGoodsList
     * @param orderServicesList
     * @param userInfo
     */
    // TODO 声明式事务，不起作用
    @Transactional
    public void insertOrder(OrderInfo orderInfo,
                            List<OrderGoods> orderGoodsList,
                            List<OrderServices> orderServicesList,
                            List<OrderPrecheckDetails> orderPrecheckDetailsList,
                            UserInfo userInfo) {
        // currrent car's license
        String carLicense = orderInfo.getCarLicense();
        // current record's operator and shop
        Long optUserId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // generate Order'Sn
        String newOrderSn = snFactory.generateOrderSn(shopId);
        orderInfo.setOrderSn(newOrderSn);
        // TODO Aspect record log
        LOGGER.info("工单操作流水：{} 工单编号生成,编号为：{} 操作人：{}", carLicense, newOrderSn, optUserId);

        // insert orderInfo
        orderInfoDao.insert(orderInfo);
        // new orderId
        Long newOrderId = orderInfo.getId();
        LOGGER.info("工单操作流水：{} 保存工单基本信息 操作人:{}", carLicense, optUserId);

        // batch insert new goodList
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            // 同步物料的成本价(InventoryPrice)
            syncGoodsInventoryPrice(orderGoodsList);

            for (OrderGoods orderGoods : orderGoodsList) {
                // refer new order
                orderGoods.setOrderId(newOrderId);
                orderGoods.setOrderSn(newOrderSn);
                // set operate message
                orderGoods.setCreator(optUserId);
                orderGoods.setModifier(optUserId);
                orderGoods.setShopId(shopId);
            }

            orderGoodsService.batchInsert(orderGoodsList);
            LOGGER.info("工单操作流水：{} 保存工单物料信息 操作人:{}", carLicense, optUserId);
        }

        // batch insert new serviceList
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                // refer new order
                orderServices.setOrderId(newOrderId);
                orderServices.setOrderSn(newOrderSn);
                // set operate message
                orderServices.setCreator(optUserId);
                orderServices.setModifier(optUserId);
                orderServices.setShopId(shopId);
            }

            orderServicesService.batchSave(orderServicesList);
            LOGGER.info("工单操作流水：{} 保存工单服务信息 操作人:{}", carLicense, optUserId);
        }
        // 添加工单预检信息
        if (!CollectionUtils.isEmpty(orderPrecheckDetailsList)) {
            for (OrderPrecheckDetails orderPrecheckDetails : orderPrecheckDetailsList) {
                orderPrecheckDetails.setOrderId(newOrderId);
                orderPrecheckDetails.setShopId(shopId);
                orderPrecheckDetails.setCreator(optUserId);
            }
            LOGGER.info("工单操作流水：{} 操作人:{},添加预检单信息{}", carLicense, optUserId, orderPrecheckDetailsList);
            orderPrecheckDetailsService.batchInsert(orderPrecheckDetailsList);
        }
    }

    /**
     * 同步物料成本价、单位
     *
     * @param orderGoodsList
     */
    private void syncGoodsInventoryPrice(List<OrderGoods> orderGoodsList) {
        Set<Long> newGoodsIdSet = new HashSet<Long>();
        for (OrderGoods orderGoods : orderGoodsList) {
            // gather goods Id
            newGoodsIdSet.add(orderGoods.getGoodsId());
        }
        // sync good InventoryPrice (APP下单时，未传入成本价)
        Long[] newGoodsIdArray = new Long[newGoodsIdSet.size()];
        List<Goods> goodsList = goodsService.selectByIds(newGoodsIdSet.toArray(newGoodsIdArray));
        // 物料(ID):成本价格(inventoryPrice)
        Map<Long, Goods> goodsMap = new HashMap<Long, Goods>(goodsList.size());
        for (Goods goods : goodsList) {
            goodsMap.put(goods.getId(), goods);
        }
        // refer good成本价/单位
        for (OrderGoods orderGoods : orderGoodsList) {
            Long goodsId = orderGoods.getGoodsId();
            if(goodsMap.containsKey(goodsId)){
                Goods goods = goodsMap.get(orderGoods.getGoodsId());
                BigDecimal inventoryPrice = goods.getInventoryPrice();
                // default 0
                inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
                orderGoods.setInventoryPrice(inventoryPrice);
                orderGoods.setMeasureUnit(goods.getMeasureUnit());
            }
        }
    }

    /**
     * 工单更新
     * @param orderFormEntityBo
     * @param userInfo
     * @param isUseNewPrecheck 是否需要更新工单预检信息，如果是，则更新order_precheck_details表（钣喷中心只有综合维修单才有预检信息）
     * @return
     * @throws BusinessCheckedException
     */
    @Override
    public Result update(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo, boolean isUseNewPrecheck)
            throws BusinessCheckedException {

        // order's basic info
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        Long orderId = orderInfo.getId();
        Long shopId = userInfo.getShopId();

        // check order exsit
        OrderInfo orderLatestData = this.selectByIdAndShopId(orderInfoDao, orderId, shopId);
        if (orderLatestData == null) {
            throw new BusinessCheckedException("21", "工单不存在！");
        }
        orderInfo.setOrderSn(orderLatestData.getOrderSn());
        // 上一次开单日期
        orderInfo.setLastCreateTime(orderLatestData.getCreateTime());

        // check 同时操作
        String modifiedYMDHMS = orderInfo.getGmtModifiedYMDHMS();
        if (StringUtils.isNotEmpty(modifiedYMDHMS)) {
            Date oldGmtModified = DateUtil.convertStringToDate(modifiedYMDHMS);
            Date newGmtModified = orderLatestData.getGmtModified();
            long diff = newGmtModified.getTime() - oldGmtModified.getTime();
            long diffSeconds = diff / 1000 % 60;

            // <=3秒内:不是同时操作
            if (newGmtModified != null && (Math.abs(diffSeconds) > 3)) {
                throw new BusinessCheckedException("10000", "已经有人修改过这条工单记录,请刷新后重试！");
            }
        }

        // check customerCar is exsit
        Optional<CustomerCar> customerCarOptional =
                customerCarService.getCustomerCar(orderInfo.getCustomerCarId());
        if (!customerCarOptional.isPresent()) {
            throw new BusinessCheckedException("1", "车牌信息不存在!");
        }

        // copy customerCar's property to order
        CustomerCar customerCar = customerCarOptional.get();
        //回写下次保养时间
        CustomerCar tempCar = orderFormEntityBo.getCustomerCar();
        if (tempCar != null) {
            String keepupTimeStr = tempCar.getKeepupTimeStr();
            if (!StringUtils.isBlank(keepupTimeStr)) {
                Date keepupTime = DateUtil.convertStringToDateYMD(keepupTimeStr);
                customerCar.setKeepupTime(keepupTime);
            }
        }
        //回写下次保养里程
        String upkeepMileage = orderInfo.getUpkeepMileage();
        if (!StringUtils.isBlank(upkeepMileage)) {
            customerCar.setUpkeepMileage(upkeepMileage);
        }

        // 开单时间(yyyy-MM-dd HH:mm)
        Date createTime = DateUtil.convertStringToDate(orderInfo.getCreateTimeStr(), "yyyy-MM-dd HH:mm");
        orderInfo.setCreateTime(createTime);

        orderInfo.setCustomerId(customerCar.getCustomerId());
        orderInfo.setCarBrand(customerCar.getCarBrand());
        orderInfo.setCarBrandId(customerCar.getCarBrandId());
        orderInfo.setCarSeries(customerCar.getCarSeries());
        orderInfo.setCarSeriesId(customerCar.getCarSeriesId());
        orderInfo.setCarCompany(customerCar.getCarCompany());
        orderInfo.setImportInfo(customerCar.getImportInfo());
        orderInfo.setCarAlias(customerCar.getByName());

        // ordered goods
        List<OrderGoods> orderedGoodsList = new ArrayList<OrderGoods>();
        // valid ordered goods
        List<OrderGoods> validGoodsList = new ArrayList<OrderGoods>();
        // ordered services
        List<OrderServices> orderedServiceList = new ArrayList<OrderServices>();
        // valid ordered services
        List<OrderServices> validServiceList = new ArrayList<OrderServices>();

        // transfer json to list
        Gson gson = new Gson();
        String orderGoodsJson = orderFormEntityBo.getOrderGoodJson();
        String orderServiceJson = orderFormEntityBo.getOrderServiceJson();

        try {
            orderedGoodsList = gson.fromJson(orderGoodsJson,
                    new TypeToken<List<OrderGoods>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购物料JSON：{}", orderGoodsJson);
            throw new RuntimeException("选购物料JSON转对象异常");
        }

        try {
            orderedServiceList = gson.fromJson(orderServiceJson,
                    new TypeToken<List<OrderServices>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购服务JSON：{}", orderServiceJson);
            throw new RuntimeException("选购服务JSON转对象异常");
        }

        // ordered goods total number
        Long orderedGoodsTotalNumber = 0l;
        //  filter valid goods and calculate goods's amount
        if (!CollectionUtils.isEmpty(orderedGoodsList)) {
            Set<Long> goodsIdSet = new HashSet<Long>();
            for (OrderGoods goods : orderedGoodsList) {
                Long goodsId = goods.getGoodsId();
                if (goodsId != null) {
                    goodsIdSet.add(goodsId);

                    // calculator goods’s amount
                    BigDecimal goodsPrice = goods.getGoodsPrice();
                    // goodsPrice default 0
                    goodsPrice = (goodsPrice == null) ? BigDecimal.ZERO : goodsPrice;
                    BigDecimal goodsNumber = goods.getGoodsNumber();
                    // goodsNumber default 1
                    goodsNumber = (goodsNumber == null ||
                            goodsNumber.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE : goodsNumber;
                    goods.setGoodsNumber(goodsNumber);
                    BigDecimal goodsAmountTemp = goodsPrice.multiply(goodsNumber);
                    goods.setGoodsAmount(goodsAmountTemp);
                    BigDecimal discount = goods.getDiscount();
                    // discount default 0
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal soldAmountTemp = goodsAmountTemp.subtract(discount);
                    goods.setSoldAmount(soldAmountTemp);
                    goods.setSoldPrice(soldAmountTemp.divide(goodsNumber, 8, BigDecimal.ROUND_HALF_UP));

                    validGoodsList.add(goods);
                }
            }

            // check goods is exsit
            int goodsIdSize = goodsIdSet.size();
            if (goodsIdSize > 0) {
                List<Goods> goodsLatestData =
                        goodsService.selectByIds(goodsIdSet.toArray(new Long[goodsIdSize]));
                int goodsLatestDataSize = goodsLatestData.size();
                // Map<goods.id,goods>
                Map<Long, Goods> goodsLatestDataMap = new HashMap<Long, Goods>(goodsLatestDataSize);
                for (Goods good : goodsLatestData) {
                    goodsLatestDataMap.put(good.getId(), good);
                }
                if (goodsIdSize != goodsLatestDataSize) {
                    StringBuffer goodsErrorMsgSB = new StringBuffer("选择的配件不存在,配件编号如下：");
                    Iterator<Long> goodsIdSetIT = goodsIdSet.iterator();
                    while (goodsIdSetIT.hasNext()) {
                        Long goodsId = goodsIdSetIT.next();
                        if (!goodsLatestDataMap.containsKey(goodsId)) {
                            goodsErrorMsgSB.append("<br/>");
                            goodsErrorMsgSB.append(goodsId);
                        }
                    }

                    throw new BusinessCheckedException("2", goodsErrorMsgSB.toString());
                }
            }

            orderedGoodsTotalNumber = Long.valueOf(validGoodsList.size() + "");
        }
        // goods's total number
        orderInfo.setGoodsCount(orderedGoodsTotalNumber);

        // ordered service total number
        Long orderedServiceNumber = 0L;
        if (!CollectionUtils.isEmpty(orderedServiceList)) {
            Set<Long> serviceIdSet = new HashSet<Long>();
            Set<Long> workerIdSet = new HashSet<Long>();
            // filter valid service and calculate servie's amount
            for (OrderServices service : orderedServiceList) {
                Long serviceId = service.getServiceId();
                if (serviceId != null) {
                    serviceIdSet.add(serviceId);
                    String workerIds = service.getWorkerIds();
                    //TODO 兼容APP 开单
                    Long workId = service.getWorkerId();
                    if(StringUtils.isBlank(workerIds) &&  workId != null && workId > 0){
                        workerIds = String.valueOf(workId);
                        service.setWorkerIds(workerIds);
                    }
                    if (StringUtils.isNotBlank(workerIds) && !"0".equals(workerIds)) {
                        // 通过逗号切分
                        String[] workerIdArr = workerIds.split(",");
                        if (ArrayUtils.isNotEmpty(workerIdArr)) {
                            if (workerIdArr.length > Constants.MAX_WORKER_NUMBER) {
                                workerIds = StringUtils.join(workerIdArr, ",", 0, Constants.MAX_WORKER_NUMBER);
                                service.setWorkerIds(workerIds);
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
                    BigDecimal servicePrice = service.getServicePrice();
                    // servicePrice default 0
                    servicePrice = (servicePrice == null) ? BigDecimal.ZERO : servicePrice;
                    // serviceHour default 0
                    BigDecimal serviceHour = service.getServiceHour();
                    serviceHour = (serviceHour == null) ? BigDecimal.ZERO : serviceHour;
                    // discount default 0
                    BigDecimal discount = service.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal serviceAmountTemp = servicePrice.multiply(serviceHour);
                    service.setServiceAmount(serviceAmountTemp);
                    BigDecimal soldAmountTemp = serviceAmountTemp.subtract(discount);
                    service.setSoldAmount(soldAmountTemp);
                    if (serviceHour.compareTo(BigDecimal.ZERO) <= 0) {
                        service.setSoldPrice(BigDecimal.ZERO);
                    } else {
                        service.setSoldPrice(soldAmountTemp.divide(serviceHour, 8, BigDecimal.ROUND_HALF_UP));
                    }

                    validServiceList.add(service);
                }
            }

            // check service is exsit
            int serviceIdSetSize = serviceIdSet.size();
            if (serviceIdSetSize > 0) {
                List<ShopServiceInfo> serviceLatestData =
                        shopServiceInfoService.selectByIds(serviceIdSet.toArray(new Long[serviceIdSetSize]));
                int serviceLatestDataSize = serviceLatestData.size();
                // Map<service.id,service>
                Map<Long, ShopServiceInfo> orderServiceListDBMap = new HashMap<Long, ShopServiceInfo>(serviceLatestDataSize);
                for (ShopServiceInfo serviceInfo : serviceLatestData) {
                    orderServiceListDBMap.put(serviceInfo.getId(), serviceInfo);
                }
                if (serviceIdSetSize != serviceLatestDataSize) {
                    StringBuffer serviceErrorMsgSB = new StringBuffer("选购的服务不存在,服务编号如下：");
                    Iterator<Long> serviceIdSetIT = serviceIdSet.iterator();
                    while (serviceIdSetIT.hasNext()) {
                        Long serviceId = serviceIdSetIT.next();
                        if (!orderServiceListDBMap.containsKey(serviceId)) {
                            serviceErrorMsgSB.append("<br/>");
                            serviceErrorMsgSB.append(serviceId);
                        }
                    }

                    throw new BusinessCheckedException("3", serviceErrorMsgSB.toString());
                }
            }

            // check worker is exsit
            int workerIdSetSize = workerIdSet.size();
            if (workerIdSetSize > 0) {
                List<ShopManager> shopManagerLatestData =
                        shopManagerService.selectByIds(workerIdSet.toArray(new Long[workerIdSetSize]));
                int managerLatestDataSize = shopManagerLatestData.size();
                // Map<shopManager.id,shopManager>
                Map<Long, ShopManager> shopManagerListDBMap = new HashMap<Long, ShopManager>(managerLatestDataSize);
                for (ShopManager shopManager : shopManagerLatestData) {
                    shopManagerListDBMap.put(shopManager.getId(), shopManager);
                }
                if (workerIdSetSize != managerLatestDataSize) {
                    StringBuffer workerErrorMsgSB = new StringBuffer("指派的维修工不存在,维修工名称如下：");
                    Iterator<Long> workerIdSetIT = workerIdSet.iterator();
                    while (workerIdSetIT.hasNext()) {
                        Long workerId = workerIdSetIT.next();
                        if (!shopManagerListDBMap.containsKey(workerId)) {
                            workerErrorMsgSB.append("<br/>");
                            ShopManager shopManager = shopManagerListDBMap.get(workerId);
                            if(shopManager == null){
                                throw new BusinessCheckedException("5", "您选择了已删除的维修工，请重新选择维修工");
                            }
                            workerErrorMsgSB.append(shopManager.getName());
                        }
                    }

                    throw new BusinessCheckedException("4", workerErrorMsgSB.toString());
                }
            }

            orderedServiceNumber = Long.valueOf(validServiceList.size() + "");
        }
        orderInfo.setServiceCount(orderedServiceNumber);

        // calculate order's expense
        calculateOrderExpense(orderInfo, validGoodsList, validServiceList);

        // 检验工单金额是否超过限度
        checkOrderAmountIsValid(orderInfo);

        // TODO 设置公共信息(创建时间、更新者、更新时间)
        orderInfo.setModifier(userInfo.getUserId());
        orderInfo.setOperatorName(userInfo.getName());
        orderInfo.setShopId(userInfo.getShopId());

        // set expectedTime
        orderInfo.setExpectedTime(DateUtil.convertStringToDate(
                orderInfo.getExpectedTimeYMD(), DateUtil.DATE_FORMAT_YMDHM));
        // set buyTime
        orderInfo.setBuyTime(DateUtil.convertStringToDate(orderInfo.getBuyTimeYMD(),
                DateUtil.DATE_FORMAT_YMD));
        String orderStatus = orderInfo.getOrderStatus();
        if (orderStatus != null && orderStatus.equals("DDWC")) {
            orderInfo.setFinishTime(new Date());
        }
        //set postscript
        String postscript = orderInfo.getPostscript();
        orderInfo.setPostscript(postscript == null ? "" : postscript);
        //set vin
        String vin = orderInfo.getVin();
        orderInfo.setVin(vin == null ? "" : vin);

        //set insuranceCompanyName
        Long insuranceCompanyId = orderInfo.getInsuranceCompanyId();
        Long otherInsuranceCompanyId = orderInfo.getOtherInsuranceCompanyId();
        if ((insuranceCompanyId != null && insuranceCompanyId > 0) || (otherInsuranceCompanyId != null && otherInsuranceCompanyId > 0)) {
            List<InsuranceCompany> insuranceCompanyList = insuranceCompanyService.select(null);
            for (InsuranceCompany insuranceCompany : insuranceCompanyList) {
                if (insuranceCompany.getId().equals(insuranceCompanyId)) {
                    orderInfo.setInsuranceCompanyName(insuranceCompany.getName());
                }
                if (insuranceCompany.getId().equals(otherInsuranceCompanyId)) {
                    orderInfo.setOtherInsuranceCompanyName(insuranceCompany.getName());
                }
            }
        } else if (insuranceCompanyId != null && insuranceCompanyId == 0) {
            orderInfo.setInsuranceCompanyName("");
        } else if (otherInsuranceCompanyId != null && otherInsuranceCompanyId == 0) {
            orderInfo.setOtherInsuranceCompanyName("");
        }
        //设置预检信息
        List<OrderPrecheckDetails> orderPrecheckDetailsList = setOrderPrecheckDetails(orderFormEntityBo, userInfo.getShopId(), orderFormEntityBo.getPrecheckId());
        // update orderInfo and goodList and serviceList
        updateOrder(orderInfo, validGoodsList, validServiceList, orderPrecheckDetailsList, userInfo, isUseNewPrecheck);

        // 捕获无关紧要信息更新异常，确保工单主数据更新成功。
        try {
            orderInfoExtUpdateThread.init(customerCar, orderInfo, orderLatestData, userInfo);
            threadManager.execute(orderInfoExtUpdateThread);
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }

        return Result.wrapSuccessfulResult(orderId);
    }

    /**
     * 校验工单金额是否超过限度
     *
     * @param orderInfo
     * @throws BusinessCheckedException
     */
    private void checkOrderAmountIsValid(OrderInfo orderInfo) throws BusinessCheckedException {

        // 数据字段 12位\2精度最大数=999999999999.99
        BigDecimal ceilingAmount = new BigDecimal("999999999999.99");

        BigDecimal goodsTotalAmount = orderInfo.getGoodsAmount();
        if (goodsTotalAmount != null && goodsTotalAmount.compareTo(ceilingAmount) == 1) {
            LOGGER.error("创建工单失败，工单金额超过最大额度，工单物料总金额:{}", goodsTotalAmount);
            throw new BusinessCheckedException("", "无效金额");
        }
        BigDecimal serviceTotalAmount = orderInfo.getServiceAmount();
        if (serviceTotalAmount != null && serviceTotalAmount.compareTo(ceilingAmount) == 1) {
            LOGGER.error("创建工单失败，工单金额超过最大额度，工单服务总金额:{}", serviceTotalAmount);
            throw new BusinessCheckedException("", "无效金额");
        }
        BigDecimal totalAmount = orderInfo.getTotalAmount();
        if (totalAmount != null && totalAmount.compareTo(ceilingAmount) == 1) {
            LOGGER.error("创建工单失败，工单金额超过最大额度，工单总金额:{}", totalAmount);
            throw new BusinessCheckedException("", "无效金额");
        }
    }


    /**
     * update orderInfo、goodList、serviceList
     *
     * @param orderInfo      工单基本信息
     * @param allGoodList    选购的物料
     * @param allServiceList 选购的服务
     * @param userInfo       当前操作人
     * @return
     */
    // TODO transactional invalid
    @Transactional
    public void updateOrder(OrderInfo orderInfo,
                            List<OrderGoods> allGoodList,
                            List<OrderServices> allServiceList,
                            List<OrderPrecheckDetails> orderPrecheckDetailsList,
                            UserInfo userInfo,
                            boolean isUseNewPrecheck) {
        // current car's license
        String carLicense = orderInfo.getCarLicense();
        // current operator and shop
        Long optUserId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        // current orderId and orderSn
        Long orderId = orderInfo.getId();
        String orderSn = orderInfo.getOrderSn();

        // update orderInfo
        orderInfoDao.updateById(orderInfo);
        LOGGER.info("工单操作流水：{} 更新工单信息 操作人:{}", carLicense, optUserId);

        // 编辑前:已选购的物料
        List<OrderGoods> preOrderedGoodList = orderGoodsService.queryOrderGoodList(orderId, shopId);
        // 编辑前:已选购的物料ID集合
        Set<Long> preOldGoodIdSet = new HashSet<Long>();
        if (!CollectionUtils.isEmpty(preOrderedGoodList)) {
            for (OrderGoods good : preOrderedGoodList) {
                preOldGoodIdSet.add(good.getId());
            }
        }
        // 编辑后:已选购的物料
        Set<Long> afterOrderedGoodIdSet = new HashSet<Long>();
        // 编辑后:本次新增的商品集合
        List<OrderGoods> newGoodList = new ArrayList<OrderGoods>();
        // 删除之前已选购的物料集合
        Set<Long> delPreOrderedGoodIdSet = new HashSet<Long>();
        // 同步物料成本价
        syncGoodsInventoryPrice(allGoodList);

        for (OrderGoods orderGood : allGoodList) {
            Long goodId = orderGood.getId();
            if (goodId == null) {
                orderGood.setOrderId(orderId);
                orderGood.setOrderSn(orderSn);
                orderGood.setCreator(optUserId);
                orderGood.setModifier(optUserId);
                orderGood.setShopId(shopId);
                newGoodList.add(orderGood);
            } else {
                if (preOldGoodIdSet != null && !preOldGoodIdSet.contains(goodId)) {
                    LOGGER.error("传递的工单配件ID不正确,配件ID:{}", goodId);
                    throw new RuntimeException("传递的工单配件ID不正确");
                }
                orderGoodsService.update(orderGood);
                afterOrderedGoodIdSet.add(goodId);
            }
        }

        // batch save
        if (!newGoodList.isEmpty()) {
            orderGoodsService.batchInsert(newGoodList);
        }

        // 筛选 删除之前已选购的物料
        if (!CollectionUtils.isEmpty(preOrderedGoodList)) {
            for (OrderGoods oldOrderGoods : preOrderedGoodList) {
                Long preOrderedGoodId = oldOrderGoods.getId();
                if (!afterOrderedGoodIdSet.contains(preOrderedGoodId)) {
                    // orderGoodsService.update(oldOrderGoods);
                    delPreOrderedGoodIdSet.add(preOrderedGoodId);
                }
            }
        }
        // batch delete
        if (!delPreOrderedGoodIdSet.isEmpty()) {
            orderGoodsService.batchDel(delPreOrderedGoodIdSet.toArray());
        }
        LOGGER.info("工单操作流水：{} 更新工单物料信息 操作人:{}", carLicense, optUserId);

        // update service

        // 编辑前:已选购的服务
        List<OrderServices> preOrderedServiceList =
                orderServicesService.queryOrderServiceList(orderId, shopId);
        // 编辑前:已选购的服务ID集合
        Set<Long> preServiceIdSet = new HashSet<Long>();
        // 删除之前已选购的物料集合
        Set<Long> delPreOrderedServiceIdSet = new HashSet<Long>();
        if (!CollectionUtils.isEmpty(preOrderedServiceList)) {
            for (OrderServices service : preOrderedServiceList) {
                preServiceIdSet.add(service.getId());
            }
        }

        // 编辑后:已选购的服务
        Set<Long> afterOrderedServiceIdSet = new HashSet<Long>();
        // 编辑后：新增服务
        List<OrderServices> newOrderServiceList = new ArrayList<OrderServices>();

        for (OrderServices service : allServiceList) {
            Long serviceId = service.getId();
            if (serviceId == null) {
                // 关联工单
                service.setOrderId(orderId);
                service.setOrderSn(orderSn);
                service.setCreator(optUserId);
                service.setModifier(optUserId);
                service.setShopId(shopId);
                newOrderServiceList.add(service);
            } else {
                // 伪造服务ID:更新操作
                if (preServiceIdSet != null && !preServiceIdSet.contains(serviceId)) {
                    LOGGER.error("传递的工单服务ID不正确,服务ID:{}", serviceId);
                    throw new RuntimeException("传递的工单服务ID不正确");
                }
                service.setModifier(optUserId);
                if (StringUtils.isEmpty(service.getWorkerIds())) {
                    service.setWorkerIds("");
                }
                orderServicesService.update(service);
                afterOrderedServiceIdSet.add(service.getId());
            }
        }

        // batch save new orderService
        if (!newOrderServiceList.isEmpty()) {
            orderServicesService.batchSave(newOrderServiceList);
        }

        // 之前选择的：不存在情况，删除掉
        if (!CollectionUtils.isEmpty(preOrderedServiceList)) {
            for (OrderServices oldOrderServices : preOrderedServiceList) {
                Long serviceId = oldOrderServices.getId();
                if (!afterOrderedServiceIdSet.contains(serviceId)) {
                    delPreOrderedServiceIdSet.add(serviceId);
                }
            }
        }
        if (!delPreOrderedServiceIdSet.isEmpty()) {
            orderServicesService.batchDel(delPreOrderedServiceIdSet.toArray());
        }
        if(isUseNewPrecheck){
            //更新预检信息
            Map<String, Object> searchOrderPrecheckDetailsMap = new HashMap<>();
            searchOrderPrecheckDetailsMap.put("shopId", shopId);
            searchOrderPrecheckDetailsMap.put("orderId", orderId);
            List<OrderPrecheckDetails> existList = orderPrecheckDetailsService.select(searchOrderPrecheckDetailsMap);
            Map<Long,OrderPrecheckDetails> existMap = new HashMap<>();
            for (OrderPrecheckDetails orderPrecheckDetails : existList) {
                Long precheckItemId = orderPrecheckDetails.getPrecheckItemId();
                //需要更新的数据
                existMap.put(precheckItemId, orderPrecheckDetails);
            }
            List<OrderPrecheckDetails> insertList = new ArrayList<>();
            //设置需要添加的数据
            Map<Long,Long> newOrderPrecheckDetailsMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(orderPrecheckDetailsList)){
                for (OrderPrecheckDetails orderPrecheckDetails : orderPrecheckDetailsList) {
                    Long precheckItemId = orderPrecheckDetails.getPrecheckItemId();
                    Long precheckValueId = orderPrecheckDetails.getPrecheckValueId();
                    if(!existMap.containsKey(precheckItemId)){
                        insertList.add(orderPrecheckDetails);
                    }else{
                        OrderPrecheckDetails existDetails = existMap.get(precheckItemId);
                        Long existPrecheckValueId = existDetails.getPrecheckValueId();
                        if(!existPrecheckValueId.equals(precheckValueId)){
                            //更新操作
                            existDetails.setPrecheckValueId(precheckValueId);
                            existDetails.setPrecheckValue(orderPrecheckDetails.getPrecheckValue());
                            existDetails.setModifier(userInfo.getUserId());
                            LOGGER.info("工单操作流水：{} 操作人:{},更新工单,更新原预检单某个项目信息{}", carLicense, optUserId, existDetails);
                            orderPrecheckDetailsService.updateById(existDetails);
                        }
                    }
                    newOrderPrecheckDetailsMap.put(precheckItemId,precheckItemId);
                }
            }
            //设置需要删除的数据
            if (!CollectionUtils.isEmpty(existList)) {
                List<Long> deleteIds = new ArrayList<>();
                for (OrderPrecheckDetails orderPrecheckDetails : existList) {
                    Long id = orderPrecheckDetails.getId();
                    Long precheckItemId = orderPrecheckDetails.getPrecheckItemId();
                    if(!newOrderPrecheckDetailsMap.containsKey(precheckItemId)){
                        deleteIds.add(id);
                    }
                }
                if(!CollectionUtils.isEmpty(deleteIds)){
                    LOGGER.info("工单操作流水：{} 操作人:{},删除原有的工单预检信息，工单id{}", carLicense, optUserId, orderId);
                    orderPrecheckDetailsService.deleteByIds(deleteIds.toArray());
                }
            }
            if (!CollectionUtils.isEmpty(insertList)) {
                for (OrderPrecheckDetails orderPrecheckDetails : insertList) {
                    orderPrecheckDetails.setShopId(shopId);
                    orderPrecheckDetails.setOrderId(orderId);
                    orderPrecheckDetails.setCreator(optUserId);
                }
                LOGGER.info("工单操作流水：{} 操作人:{},更新工单,添加预检单信息{}", carLicense, optUserId, insertList);
                orderPrecheckDetailsService.batchInsert(insertList);
            }
        }
        LOGGER.info("工单操作流水：{} 更新工单服务信息 操作人:{}", carLicense, optUserId);

    }


    /**
     * 核算订单各项金额
     *
     * @param orderInfo        工单基本信息
     * @param orderServiceList 选购服务
     */
    @Override
    public void calculateOrderExpense(OrderInfo orderInfo,
                                      List<OrderServices> orderServiceList) {
        // 服务项目总金额
        BigDecimal serviceTotalAmount = BigDecimal.ZERO;
        // 商品总金额
        BigDecimal goodsTotalAmount = BigDecimal.ZERO;
        // 订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 订单总折扣
        BigDecimal totalDiscount = BigDecimal.ZERO;
        // 商品总折扣
        BigDecimal goodsTotalDiscount = BigDecimal.ZERO;
        // 服务总折扣
        BigDecimal serviceTotalDiscount = BigDecimal.ZERO;
        // 费用总折扣
        BigDecimal feeTotalDiscount = BigDecimal.ZERO;
        // 费用总金额
        BigDecimal feeTotalAmount = BigDecimal.ZERO;
        // 管理费
        BigDecimal manageFee = BigDecimal.ZERO;
        // 商品总费用
        manageFee = goodsTotalAmount.subtract(goodsTotalDiscount);

        if (!CollectionUtils.isEmpty(orderServiceList)) {
            for (OrderServices orderServices : orderServiceList) {
                Integer serviceType = orderServices.getType();
                // 1:基本服务
                if (serviceType == OrderServiceTypeEnum.BASIC.getCode()) {
                    BigDecimal discount = orderServices.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    serviceTotalDiscount = serviceTotalDiscount.add(discount);
                    BigDecimal serviceAmount = orderServices.getServiceAmount();
                    serviceAmount = (serviceAmount == null) ? BigDecimal.ZERO : serviceAmount;
                    serviceTotalAmount = serviceTotalAmount.add(serviceAmount);
                }

                // 2:附属服务
                if (serviceType == OrderServiceTypeEnum.ANCILLARY.getCode()) {
                    String flags = orderServices.getFlags();
                    BigDecimal discount = orderServices.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    if (flags != null && flags.equals(Constants.GLF)) {
                        feeTotalDiscount = feeTotalDiscount.add(discount);
                        BigDecimal manageRate = orderServices.getManageRate();
                        manageRate = (manageRate == null) ? BigDecimal.ZERO : manageRate;
                        manageFee = manageFee.multiply(manageRate);
                        feeTotalAmount = feeTotalAmount.add(manageFee);
                    } else {
                        feeTotalDiscount = feeTotalDiscount.add(discount);
                        BigDecimal serviceAmount = orderServices.getServiceAmount();
                        serviceAmount = (serviceAmount == null) ? BigDecimal.ZERO : serviceAmount;
                        feeTotalAmount = feeTotalAmount.add(serviceAmount);
                    }
                }
            }
        }

        totalAmount = goodsTotalAmount.add(serviceTotalAmount).add(feeTotalAmount);
        totalDiscount = goodsTotalDiscount.add(serviceTotalDiscount).add(feeTotalDiscount);
        orderInfo.setGoodsAmount(goodsTotalAmount);
        orderInfo.setServiceAmount(serviceTotalAmount);
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setDiscount(totalDiscount);
        orderInfo.setGoodsDiscount(goodsTotalDiscount);
        orderInfo.setServiceDiscount(serviceTotalDiscount);
        orderInfo.setFeeAmount(feeTotalAmount);
        orderInfo.setFeeDiscount(feeTotalDiscount);
        orderInfo.setManageFee(manageFee);
        BigDecimal taxAmount = orderInfo.getTaxAmount();
        taxAmount = (taxAmount == null) ? BigDecimal.ZERO : taxAmount;
        totalAmount = totalAmount.add(taxAmount).subtract(totalDiscount);

        // TODO 已被取消 *折扣=1 + 费用-优惠
        // 核算工单优惠(兼容普通工单)
        // 折扣
        BigDecimal preDiscountRate = BigDecimal.ONE;
        orderInfo.setPreDiscountRate(preDiscountRate);
        // 费用
        BigDecimal preTaxAmount = BigDecimal.ZERO;
        orderInfo.setPreTaxAmount(preTaxAmount);
        // 优惠
        BigDecimal prePreferentiaAmount = BigDecimal.ZERO;
        orderInfo.setPrePreferentiaAmount(prePreferentiaAmount);
        // 代金券
        BigDecimal preCouponAmount = BigDecimal.ZERO;
        orderInfo.setPreCouponAmount(preCouponAmount);
        // 实际应收金额 =工单总金额 * 折扣 + 费用 - 优惠 - 代金券
        BigDecimal preTotalAmount = totalAmount.multiply(preDiscountRate)
                .add(preTaxAmount)
                .subtract(prePreferentiaAmount)
                .subtract(preCouponAmount);
        orderInfo.setPreTotalAmount(preTotalAmount);

        orderInfo.setOrderAmount(totalAmount);
    }


    /**
     * 核算订单各项金额
     *
     * @param orderInfo        工单基本信息
     * @param orderGoodsList   选购物料
     * @param orderServiceList 选购服务
     */
    @Override
    public void calculateOrderExpense(OrderInfo orderInfo,
                                      List<OrderGoods> orderGoodsList,
                                      List<OrderServices> orderServiceList) {
        // 服务项目总金额
        BigDecimal serviceTotalAmount = BigDecimal.ZERO;
        // 商品总金额
        BigDecimal goodsTotalAmount = BigDecimal.ZERO;
        // 订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 订单总折扣
        BigDecimal totalDiscount = BigDecimal.ZERO;
        // 商品总折扣
        BigDecimal goodsTotalDiscount = BigDecimal.ZERO;
        // 服务总折扣
        BigDecimal serviceTotalDiscount = BigDecimal.ZERO;
        // 费用总折扣
        BigDecimal feeTotalDiscount = BigDecimal.ZERO;
        // 费用总金额
        BigDecimal feeTotalAmount = BigDecimal.ZERO;
        // 管理费
        BigDecimal manageFee = BigDecimal.ZERO;

        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                BigDecimal goodsAmount = orderGoods.getGoodsAmount();
                goodsAmount = (goodsAmount == null) ? BigDecimal.ZERO : goodsAmount;
                goodsTotalAmount = goodsTotalAmount.add(goodsAmount);
                totalAmount = totalAmount.add(goodsAmount);
                BigDecimal discount = orderGoods.getDiscount();
                discount = (discount == null) ? BigDecimal.ZERO : discount;
                totalDiscount = totalDiscount.add(discount);
                goodsTotalDiscount = goodsTotalDiscount.add(discount);
            }
        }

        // 商品总费用
        manageFee = goodsTotalAmount.subtract(goodsTotalDiscount);

        if (!CollectionUtils.isEmpty(orderServiceList)) {
            for (OrderServices orderServices : orderServiceList) {
                Integer serviceType = orderServices.getType();
                // 1:基本服务
                if (serviceType == OrderServiceTypeEnum.BASIC.getCode()) {
                    BigDecimal discount = orderServices.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    serviceTotalDiscount = serviceTotalDiscount.add(discount);
                    BigDecimal serviceAmount = orderServices.getServiceAmount();
                    serviceAmount = (serviceAmount == null) ? BigDecimal.ZERO : serviceAmount;
                    serviceTotalAmount = serviceTotalAmount.add(serviceAmount);
                }

                // 2:附属服务
                if (serviceType == OrderServiceTypeEnum.ANCILLARY.getCode()) {
                    String flags = orderServices.getFlags();
                    BigDecimal discount = orderServices.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    if (flags != null && flags.equals(Constants.GLF)) {
                        feeTotalDiscount = feeTotalDiscount.add(discount);
                        BigDecimal manageRate = orderServices.getManageRate();
                        manageRate = (manageRate == null) ? BigDecimal.ZERO : manageRate;
                        manageFee = manageFee.multiply(manageRate);
                        feeTotalAmount = feeTotalAmount.add(manageFee);
                    } else {
                        feeTotalDiscount = feeTotalDiscount.add(discount);
                        BigDecimal serviceAmount = orderServices.getServiceAmount();
                        serviceAmount = (serviceAmount == null) ? BigDecimal.ZERO : serviceAmount;
                        feeTotalAmount = feeTotalAmount.add(serviceAmount);
                    }
                }
            }
        }

        totalAmount = goodsTotalAmount.add(serviceTotalAmount).add(feeTotalAmount);
        totalDiscount = goodsTotalDiscount.add(serviceTotalDiscount).add(feeTotalDiscount);
        orderInfo.setGoodsAmount(goodsTotalAmount);
        orderInfo.setServiceAmount(serviceTotalAmount);
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setDiscount(totalDiscount);
        orderInfo.setGoodsDiscount(goodsTotalDiscount);
        orderInfo.setServiceDiscount(serviceTotalDiscount);
        orderInfo.setFeeAmount(feeTotalAmount);
        orderInfo.setFeeDiscount(feeTotalDiscount);
        orderInfo.setManageFee(manageFee);
        BigDecimal taxAmount = orderInfo.getTaxAmount();
        taxAmount = (taxAmount == null) ? BigDecimal.ZERO : taxAmount;
        totalAmount = totalAmount.add(taxAmount).subtract(totalDiscount);

        // TODO 已被取消 *折扣=1 + 费用-优惠
        // 核算工单优惠(兼容普通工单)
        // 折扣
        BigDecimal preDiscountRate = BigDecimal.ONE;
        orderInfo.setPreDiscountRate(preDiscountRate);
        // 费用
        BigDecimal preTaxAmount = BigDecimal.ZERO;
        orderInfo.setPreTaxAmount(preTaxAmount);
        // 优惠
        BigDecimal prePreferentiaAmount = BigDecimal.ZERO;
        orderInfo.setPrePreferentiaAmount(prePreferentiaAmount);
        // 代金券
        BigDecimal preCouponAmount = BigDecimal.ZERO;
        orderInfo.setPreCouponAmount(preCouponAmount);
        // 实际应收金额 =工单总金额 * 折扣 + 费用 - 优惠 - 代金券
        BigDecimal preTotalAmount = totalAmount.multiply(preDiscountRate)
                .add(preTaxAmount)
                .subtract(prePreferentiaAmount)
                .subtract(preCouponAmount);
        orderInfo.setPreTotalAmount(preTotalAmount);
        orderInfo.setOrderAmount(totalAmount);
    }


    public Result virtualCalcPrice(List<Map<String, Object>> orderGoodsMapList,
                                   List<Map<String, Object>> orderServicesMapList) {
        return Result.wrapSuccessfulResult(doVirtualCalcPrice(orderGoodsMapList, orderServicesMapList));
    }


    @Override
    public Result updateOrderOfWareHouse(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo) throws BusinessCheckedException {
        // order's basic info
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        Long orderId = orderInfo.getId();
        Long shopId = userInfo.getShopId();

        // check order exsit
        OrderInfo orderLatestData = this.selectByIdAndShopId(orderInfoDao,
                orderId, shopId);
        if (orderLatestData == null) {
            throw new BusinessCheckedException("21", "工单不存在！");
        }
        if(orderLatestData.getOrderStatus().equals(OrderStatusEnum.DDYFK.getKey()) ||
                orderLatestData.getOrderStatus().equals(OrderStatusEnum.DDWC.getKey())){
            throw new BusinessCheckedException("21", "工单状态不正确，请刷新页面再试");
        }
        // 设置orderStatus
        orderLatestData.setOrderStatus(orderInfo.getOrderStatus());

        // check 2015-09-15 3秒内，不是同时操作
        String modifiedYMDHMS = orderInfo.getGmtModifiedYMDHMS();
        if (StringUtils.isNotEmpty(modifiedYMDHMS)) {
            Date oldGmtModified = DateUtil.convertStringToDate(modifiedYMDHMS);
            Date newGmtModified = orderLatestData.getGmtModified();
            long diff = newGmtModified.getTime() - oldGmtModified.getTime();
            long diffSeconds = diff / 1000 % 60;

            // <=3秒内:不是同时操作
            if (newGmtModified != null && (Math.abs(diffSeconds) > 3)) {
                throw new BusinessCheckedException("10000", "已经有人修改过这条工单记录,请刷新后重试！");
            }
        }

        // check customerCar is exsit
        Optional<CustomerCar> customerCarOptional =
                customerCarService.getCustomerCar(orderInfo.getCustomerCarId());
        if (!customerCarOptional.isPresent()) {
            throw new BusinessCheckedException("1", "车牌信息不存在!");
        }

        // ordered goods
        List<OrderGoods> orderedGoodsList = new ArrayList<OrderGoods>();
        // valid ordered goods
        List<OrderGoods> actualGoodsList = new ArrayList<OrderGoods>();
        List<OrderGoods> actualGoodsListTemp = new ArrayList<OrderGoods>();

        // transfer json to list
        Gson gson = new Gson();
        String orderGoodsJson = orderFormEntityBo.getOrderGoodJson();
        try {
            orderedGoodsList = gson.fromJson(orderGoodsJson,
                    new TypeToken<List<OrderGoods>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购物料JSON：{}", orderGoodsJson);
            throw new RuntimeException("选购物料JSON转对象异常");
        }

        // ordered goods total number
        Long orderedGoodsTotalNumber = 0l;
        //  filter valid goods and calculate goods's amount
        if (!CollectionUtils.isEmpty(orderedGoodsList)) {
            Set<Long> goodsIdSet = new HashSet<Long>();
            for (OrderGoods goods : orderedGoodsList) {
                Long goodsId = goods.getGoodsId();
                if (goodsId != null) {
                    goodsIdSet.add(goodsId);

                    // calculator goods’s amount
                    BigDecimal goodsPrice = goods.getGoodsPrice();
                    // goodsPrice default 0
                    goodsPrice = (goodsPrice == null) ? BigDecimal.ZERO : goodsPrice;
                    BigDecimal goodsNumber = goods.getGoodsNumber();
                    // goodsNumber default 1
                    goodsNumber = (goodsNumber == null ||
                            goodsNumber.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE : goodsNumber;
                    goods.setGoodsNumber(goodsNumber);
                    BigDecimal goodsAmountTemp = goodsPrice.multiply(goodsNumber);
                    goods.setGoodsAmount(goodsAmountTemp);
                    BigDecimal discount = goods.getDiscount();
                    // discount default 0
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal soldAmountTemp = goodsAmountTemp.subtract(discount);
                    goods.setSoldAmount(soldAmountTemp);
                    goods.setSoldPrice(soldAmountTemp.divide(goodsNumber, 8, BigDecimal.ROUND_HALF_UP));

                    actualGoodsList.add(goods);
                    actualGoodsListTemp.add(goods);
                }
            }

            // check goods is exsit
            int goodsIdSize = goodsIdSet.size();
            if (goodsIdSize > 0) {
                List<Goods> goodsLatestData =
                        goodsService.selectByIds(goodsIdSet.toArray(new Long[goodsIdSize]));
                int goodsLatestDataSize = goodsLatestData.size();
                // Map<goods.id,goods>
                Map<Long, Goods> goodsLatestDataMap = new HashMap<Long, Goods>(goodsLatestDataSize);
                for (Goods good : goodsLatestData) {
                    goodsLatestDataMap.put(good.getId(), good);
                }
                if (goodsIdSize != goodsLatestDataSize) {
                    StringBuffer goodsErrorMsgSB = new StringBuffer("选择的配件不存在,配件编号如下：");
                    Iterator<Long> goodsIdSetIT = goodsIdSet.iterator();
                    while (goodsIdSetIT.hasNext()) {
                        Long goodsId = goodsIdSetIT.next();
                        if (!goodsLatestDataMap.containsKey(goodsId)) {
                            goodsErrorMsgSB.append("<br/>");
                            goodsErrorMsgSB.append(goodsId);
                        }
                    }

                    throw new BusinessCheckedException("2", goodsErrorMsgSB.toString());
                }
            }

            orderedGoodsTotalNumber = Long.valueOf(actualGoodsList.size() + "");
        }

        // 虚开物料
        List<OrderGoods> vituralOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.VIRTUAL);
        // 加上虚开物料数量
        orderedGoodsTotalNumber = orderedGoodsTotalNumber + vituralOrderGoodsList.size();
        // goods's total number
        orderLatestData.setGoodsCount(orderedGoodsTotalNumber);


        // oldServiceList
        List<OrderServices> validServiceList =
                orderServicesService.queryOrderServiceList(orderId, shopId);

        // 追加虚开物料
        actualGoodsListTemp.addAll(vituralOrderGoodsList);
        calculateOrderExpense(orderLatestData, actualGoodsListTemp, validServiceList);
        orderLatestData.setModifier(userInfo.getUserId());
        orderLatestData.setOperatorName(userInfo.getName());
        orderLatestData.setPostscript(orderInfo.getPostscript());

        updateOrder(orderLatestData, actualGoodsList, userInfo);

        return Result.wrapSuccessfulResult(orderId);
    }


    /**
     * update 工单基本信息、选购实开物料
     *
     * @param orderInfo       工单实体
     * @param actualGoodsList 实开物料
     * @param userInfo        当前操作人
     * @return
     */
    // TODO 事务不起作用
    @Transactional
    public void updateOrder(OrderInfo orderInfo,
                            List<OrderGoods> actualGoodsList,
                            UserInfo userInfo) {
        // userId
        Long userId = userInfo.getUserId();

        // 更新orderInfo
        orderInfoDao.updateById(orderInfo);
        // orderId
        Long orderId = orderInfo.getId();
        // shopId
        Long shopId = userInfo.getShopId();
        // 编辑前：选购的实开物料
        List<OrderGoods> oldActualGoodsList =
                orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);

        Set<Long> orderGoodsIds = new HashSet<Long>();
        Set<Long> oldGoodsIds = new HashSet<Long>();
        if (!CollectionUtils.isEmpty(oldActualGoodsList)) {
            for (OrderGoods good : oldActualGoodsList) {
                oldGoodsIds.add(good.getId());
            }
        }

        if (!CollectionUtils.isEmpty(actualGoodsList)) {
            // new orderGoods
            List<OrderGoods> newOrderGoodseList = new ArrayList<OrderGoods>();

            for (OrderGoods orderGood : actualGoodsList) {
                Long goodId = orderGood.getId();
                if (goodId == null) {
                    // 关联工单
                    orderGood.setOrderId(orderInfo.getId());
                    orderGood.setOrderSn(orderInfo.getOrderSn());
                    orderGood.setCreator(userInfo.getUserId());
                    orderGood.setModifier(userInfo.getUserId());
                    orderGood.setShopId(userInfo.getShopId());

                    newOrderGoodseList.add(orderGood);
                } else {
                    // 伪造物料ID:更新操作
                    if (oldGoodsIds != null && !oldGoodsIds.contains(goodId)) {
                        LOGGER.error("传递的工单配件ID不正确,配件ID:{}", goodId);
                        throw new RuntimeException("传递的工单配件ID不正确");
                    }
                    orderGood.setModifier(userId);
                    orderGoodsService.update(orderGood);
                    orderGoodsIds.add(goodId);
                }
            }

            // batch save new OrderGoods
            if (!newOrderGoodseList.isEmpty()) {
                orderGoodsService.batchInsert(newOrderGoodseList);
            }
        }

        // 之前选择的：不存在情况，删除掉
        if (!CollectionUtils.isEmpty(oldActualGoodsList)) {
            for (OrderGoods oldOrderGoods : oldActualGoodsList) {
                if (orderGoodsIds != null && !orderGoodsIds.contains(oldOrderGoods.getId())) {
                    oldOrderGoods.setModifier(userId);
                    oldOrderGoods.setIsDeleted("Y");
                    orderGoodsService.update(oldOrderGoods);
                }
            }
        }
    }


    @Override
    public Result virtualSave(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo)
            throws BusinessCheckedException {
        // order's basic info
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();

        // ordered goods
        List<OrderGoods> orderedGoodsList = new ArrayList<OrderGoods>();
        // valid ordered goods
        List<OrderGoods> validGoodsList = new ArrayList<OrderGoods>();
        // ordered services
        List<OrderServices> orderedServiceList = new ArrayList<OrderServices>();
        // valid ordered services
        List<OrderServices> validServiceList = new ArrayList<OrderServices>();

        // transfer json to list
        Gson gson = new Gson();
        String orderGoodsJson = orderFormEntityBo.getOrderGoodJson();
        String orderServiceJson = orderFormEntityBo.getOrderServiceJson();
        try {
            orderedGoodsList = gson.fromJson(orderGoodsJson,
                    new TypeToken<List<OrderGoods>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购物料JSON：{}", orderGoodsJson);
            throw new RuntimeException("选购物料JSON转对象异常");
        }

        try {
            orderedServiceList = gson.fromJson(orderServiceJson,
                    new TypeToken<List<OrderServices>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购服务JSON：{}", orderServiceJson);
            throw new RuntimeException("选购服务JSON转对象异常");
        }

        // ordered goods‘s total number
        Long orderedGoodsTotalNumber = 0l;
        //  filter valid goods and calculate goods's amount
        if (!CollectionUtils.isEmpty(orderedGoodsList)) {
            // valid goods's ids
            for (OrderGoods goods : orderedGoodsList) {
                String goodsFormat = goods.getGoodsFormat();
                String goodsName = goods.getGoodsName();
                if (!StringUtils.isEmpty(goodsFormat) ||
                        !StringUtils.isEmpty(goodsName)) {
                    // calculator goods’s amount
                    BigDecimal goodsPrice = goods.getGoodsPrice();
                    // goodsPrice default 0
                    goodsPrice = (goodsPrice == null) ? BigDecimal.ZERO : goodsPrice;
                    BigDecimal goodsNumber = goods.getGoodsNumber();
                    // goodsNumber default 1
                    goodsNumber = (goodsNumber == null ||
                            goodsNumber.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE : goodsNumber;
                    goods.setGoodsNumber(goodsNumber);
                    BigDecimal goodsAmountTemp = goodsPrice.multiply(goodsNumber);
                    goods.setGoodsAmount(goodsAmountTemp);
                    BigDecimal discount = goods.getDiscount();
                    // discount default 0
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal soldAmountTemp = goodsAmountTemp.subtract(discount);
                    goods.setSoldAmount(soldAmountTemp);
                    goods.setSoldPrice(soldAmountTemp.divide(goodsNumber, 8, BigDecimal.ROUND_HALF_UP));
                    validGoodsList.add(goods);
                }
            }

            orderedGoodsTotalNumber = Long.valueOf(validGoodsList.size() + "");
        }
        // goods's total number
        orderInfo.setGoodsCount(orderedGoodsTotalNumber);

        // ordered service total number
        Long orderedServiceNumber = 0L;
        if (!CollectionUtils.isEmpty(orderedServiceList)) {
            // filter valid service and calculate servie's amount
            for (OrderServices service : orderedServiceList) {
                String serviceName = service.getServiceName();
                if (!StringUtils.isEmpty(serviceName)) {
                    BigDecimal servicePrice = service.getServicePrice();
                    // servicePrice default 0
                    servicePrice = (servicePrice == null) ? BigDecimal.ZERO : servicePrice;
                    // serviceHour default 0
                    BigDecimal serviceHour = service.getServiceHour();
                    serviceHour = (serviceHour == null) ? BigDecimal.ZERO : serviceHour;
                    // discount default 0
                    BigDecimal discount = service.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal serviceAmountTemp = servicePrice.multiply(serviceHour);
                    service.setServiceAmount(serviceAmountTemp);
                    BigDecimal soldAmountTemp = serviceAmountTemp.subtract(discount);
                    service.setSoldAmount(soldAmountTemp);
                    if (serviceHour.compareTo(BigDecimal.ZERO) <= 0) {
                        service.setSoldPrice(BigDecimal.ZERO);
                    } else {
                        service.setSoldPrice(soldAmountTemp.divide(serviceHour, 8, BigDecimal.ROUND_HALF_UP));
                    }

                    validServiceList.add(service);
                }
            }

            orderedServiceNumber = Long.valueOf(validServiceList.size() + "");
        }
        orderInfo.setServiceCount(orderedServiceNumber);

        // calculate order's Amount
        calculateOrderExpense(orderInfo, validGoodsList, validServiceList);

        // TODO 设置公共信息(创建者、创建时间、更新者、更新时间)
        orderInfo.setCreator(userInfo.getUserId());
        orderInfo.setModifier(userInfo.getUserId());
        orderInfo.setOperatorName(userInfo.getName());
        orderInfo.setShopId(userInfo.getShopId());

        //set postscript
        String postscript = orderInfo.getPostscript();
        orderInfo.setPostscript(postscript == null ? "" : postscript);
        //set vin
        String vin = orderInfo.getVin();
        orderInfo.setVin(vin == null ? "" : vin);

        // insert DB
        insertVirtualOrder(orderInfo, validGoodsList, validServiceList, userInfo);

        return Result.wrapSuccessfulResult(orderInfo.getParentId());
    }


    /**
     * 保存虚拟工单基本信息、选购商品、选购服务
     *
     * @param orderInfo
     * @param orderGoodsList
     * @param orderServicesList
     * @param userInfo
     */
    // TODO 声明式事务，不起作用
    @Transactional
    public Long insertVirtualOrder(OrderInfo orderInfo,
                                   List<OrderGoods> orderGoodsList,
                                   List<OrderServices> orderServicesList,
                                   UserInfo userInfo) {
        String orderSn = orderInfo.getOrderSn();
        orderInfo.setOrderSn(orderSn);

        //插入orderInfo
        VirtualOrder virtualOrder = copyOrderPropertyToVirtualOrder(orderInfo);
        virtualOrder.setParentId(orderInfo.getParentId());
        String identityCard = orderInfo.getIdentityCard();
        virtualOrder.setIdentityCard(identityCard == null ? "" : identityCard);

        // TODO 设置公共信息(创建者、创建时间、更新者、更新时间)
        virtualOrder.setCreator(userInfo.getUserId());
        virtualOrder.setModifier(userInfo.getUserId());
        virtualOrder.setOperatorName(userInfo.getName());
        virtualOrder.setShopId(userInfo.getShopId());
        virtualOrderService.save(virtualOrder);

        //插入orderGoods
        // TODO 调整为批量保存
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                VirtualOrdergood virtualOrdergood = copyGoodPropertyToVirtualGood(userInfo, virtualOrder, orderGoods);
                virtualOrdergoodService.save(virtualOrdergood);
            }
        }

        //插入orderService基本信息
        // TODO 调整为批量保存
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                VirtualOrderservice virtualOrderservice = copyServicePropertyToVirtualService(userInfo, virtualOrder, orderServices);
                virtualOrderserviceService.save(virtualOrderservice);
            }
        }

        return virtualOrder.getId();
    }

    /**
     * copy Service's Property To VirtualService's property
     *
     * @param userInfo
     * @param virtualOrder
     * @param orderServices
     * @return
     */
    private VirtualOrderservice copyServicePropertyToVirtualService(UserInfo userInfo, VirtualOrder virtualOrder, OrderServices orderServices) {
        VirtualOrderservice virtualOrderservice = new VirtualOrderservice();
        virtualOrderservice.setOrderId(virtualOrder.getId());
        String orderSn = virtualOrder.getOrderSn();
        virtualOrderservice.setOrderSn(orderSn == null ? "" : orderSn);
        virtualOrderservice.setCreator(userInfo.getUserId());
        virtualOrderservice.setModifier(userInfo.getUserId());
        virtualOrderservice.setShopId(userInfo.getShopId());
        virtualOrderservice.setServiceId(orderServices.getServiceId());
        virtualOrderservice.setServiceSn(orderServices.getServiceSn());
        virtualOrderservice.setFlags(orderServices.getFlags());
        virtualOrderservice.setType(orderServices.getType());
        virtualOrderservice.setServiceCatId(orderServices.getServiceCatId());
        String serviceName = orderServices.getServiceName();
        virtualOrderservice.setServiceName(serviceName == null ? "" : serviceName);
        String serviceCatName = orderServices.getServiceCatName();
        virtualOrderservice.setServiceCatName(serviceCatName == null ? "" : serviceCatName);
        BigDecimal servicePrice = orderServices.getServicePrice();
        virtualOrderservice.setServicePrice(servicePrice == null ? BigDecimal.ZERO : servicePrice);
        BigDecimal serviceHour = orderServices.getServiceHour();
        virtualOrderservice.setServiceHour(serviceHour == null ? BigDecimal.ONE : serviceHour);
        virtualOrderservice.setServiceAmount(orderServices.getServiceAmount());
        BigDecimal discount = orderServices.getDiscount();
        virtualOrderservice.setDiscount(discount == null ? BigDecimal.ZERO : discount);
        virtualOrderservice.setWorkerId(orderServices.getWorkerId());
        String serviceNote = orderServices.getServiceNote();
        virtualOrderservice.setServiceNote(serviceNote == null ? "" : serviceNote);
        virtualOrderservice.setFlags(orderServices.getFlags());

        return virtualOrderservice;
    }

    /**
     * copy Good's Property To VirtualGood's property
     *
     * @param userInfo
     * @param virtualOrder
     * @param orderGoods
     * @return
     */
    private VirtualOrdergood copyGoodPropertyToVirtualGood(UserInfo userInfo, VirtualOrder virtualOrder, OrderGoods orderGoods) {
        VirtualOrdergood virtualOrdergood = new VirtualOrdergood();
        virtualOrdergood.setOrderId(virtualOrder.getId());
        String orderSn = virtualOrder.getOrderSn();
        virtualOrdergood.setOrderSn(orderSn == null ? "" : orderSn);
        virtualOrdergood.setCreator(userInfo.getUserId());
        virtualOrdergood.setModifier(userInfo.getUserId());
        virtualOrdergood.setShopId(userInfo.getShopId());
        virtualOrdergood.setGoodsId(orderGoods.getGoodsId());
        String goodsSn = orderGoods.getGoodsSn();
        virtualOrdergood.setGoodsSn(goodsSn == null ? "" : goodsSn);
        virtualOrdergood.setInventoryPrice(orderGoods.getInventoryPrice());
        virtualOrdergood.setGoodsType(orderGoods.getGoodsType());
        String goodsFormat = orderGoods.getGoodsFormat();
        virtualOrdergood.setGoodsFormat(goodsFormat == null ? "" : goodsFormat);
        String goodsName = orderGoods.getGoodsName();
        virtualOrdergood.setGoodsName(goodsName == null ? "" : goodsName);
        BigDecimal goodsPrice = orderGoods.getGoodsPrice();
        virtualOrdergood.setGoodsPrice(goodsPrice == null ? BigDecimal.ZERO : goodsPrice);
        BigDecimal goodsNumber = orderGoods.getGoodsNumber();
        virtualOrdergood.setGoodsNumber(goodsNumber == null ? BigDecimal.ONE : goodsNumber);
        virtualOrdergood.setGoodsAmount(orderGoods.getGoodsAmount());
        BigDecimal discount = orderGoods.getDiscount();
        virtualOrdergood.setDiscount(discount == null ? BigDecimal.ZERO : discount);
        String goodsNote = orderGoods.getGoodsNote();
        virtualOrdergood.setGoodsNote(goodsNote == null ? "" : goodsNote);
        virtualOrdergood.setMeasureUnit(orderGoods.getMeasureUnit());
        virtualOrdergood.setSaleId(orderGoods.getSaleId());
        return virtualOrdergood;
    }

    /**
     * copy Order's Property To VirtualOrder's property
     *
     * @param orderInfo
     * @return
     */
    private VirtualOrder copyOrderPropertyToVirtualOrder(OrderInfo orderInfo) {
        VirtualOrder virtualOrder = new VirtualOrder();
        String orderSn = orderInfo.getOrderSn();
        virtualOrder.setOrderSn(orderSn == null ? "" : orderSn);
        virtualOrder.setOrderStatus(orderInfo.getOrderStatus());
        virtualOrder.setPayStatus(orderInfo.getPayStatus());
        String carLicense = orderInfo.getCarLicense();
        virtualOrder.setCarLicense(carLicense == null ? "" : carLicense);
        String carModels = orderInfo.getCarModels();
        virtualOrder.setCarModels(carModels == null ? "" : carModels);
        //add by twg
        virtualOrder.setCarBrand(orderInfo.getCarBrand());
        virtualOrder.setCarBrandId(orderInfo.getCarBrandId());
        virtualOrder.setCarSeriesId(orderInfo.getCarSeriesId());
        virtualOrder.setCarSeries(orderInfo.getCarSeries());
        virtualOrder.setCarModelsId(orderInfo.getCarModelsId());
        virtualOrder.setImportInfo(orderInfo.getImportInfo());
        //end
        String carAlias = orderInfo.getCarAlias();
        virtualOrder.setCarAlias(carAlias == null ? "" : carAlias);
        virtualOrder.setOrderType(orderInfo.getOrderType());
        virtualOrder.setExpectedTime(orderInfo.getExpectedTime());
        String mileage = orderInfo.getMileage();
        virtualOrder.setMileage(mileage == null ? "" : mileage);
        virtualOrder.setReceiver(orderInfo.getReceiver());
        virtualOrder.setReceiverName(orderInfo.getReceiverName());
        String contactName = orderInfo.getContactName();
        virtualOrder.setContactName(contactName == null ? "" : contactName);
        String contactMobile = orderInfo.getContactMobile();
        virtualOrder.setContactMobile(contactMobile == null ? "" : contactMobile);
        String customerName = orderInfo.getCustomerName();
        virtualOrder.setCustomerName(customerName == null ? "" : customerName);
        String customerMobile = orderInfo.getCustomerMobile();
        virtualOrder.setCustomerMobile(customerMobile == null ? "" : customerMobile);
        virtualOrder.setInsuranceCompanyId(orderInfo.getInsuranceCompanyId());
        virtualOrder.setInsuranceCompanyName(orderInfo.getInsuranceCompanyName());
        virtualOrder.setOtherInsuranceCompanyId(orderInfo.getOtherInsuranceCompanyId());
        virtualOrder.setOtherInsuranceCompanyName(orderInfo.getOtherInsuranceCompanyName());
        String carColor = orderInfo.getCarColor();
        virtualOrder.setCarColor(carColor == null ? "" : carColor);
        virtualOrder.setBuyTime(orderInfo.getBuyTime());
        virtualOrder.setCustomerId(orderInfo.getCustomerId());
        virtualOrder.setCustomerCarId(orderInfo.getCustomerCarId());
        String vin = orderInfo.getVin();
        virtualOrder.setVin(vin == null ? "" : vin);
        String engineNo = orderInfo.getEngineNo();
        virtualOrder.setEngineNo(engineNo == null ? "" : engineNo);
        String customerAddress = orderInfo.getCustomerAddress();
        virtualOrder.setCustomerAddress(customerAddress == null ? "" : customerAddress);
        String oilMeter = orderInfo.getOilMeter();
        virtualOrder.setOilMeter(oilMeter == null ? "" : oilMeter);
        virtualOrder.setServiceAmount(orderInfo.getServiceAmount());
        virtualOrder.setServiceDiscount(orderInfo.getServiceDiscount());
        virtualOrder.setGoodsAmount(orderInfo.getGoodsAmount());
        virtualOrder.setGoodsDiscount(orderInfo.getGoodsDiscount());
        virtualOrder.setFeeAmount(orderInfo.getFeeAmount());
        virtualOrder.setFeeDiscount(orderInfo.getFeeDiscount());
        virtualOrder.setOrderAmount(orderInfo.getOrderAmount());
        virtualOrder.setPostscript(orderInfo.getPostscript());
        //设置开子单时间
        virtualOrder.setCreateTime(orderInfo.getCreateTime());
        //设置子单结算日期
        virtualOrder.setPayTime(orderInfo.getPayTime());
        //设置子单打印时间
        virtualOrder.setPrintTime(orderInfo.getPrintTime());
        // 客户单位
        virtualOrder.setCompany(orderInfo.getCompany());
        //设置完工日期
        virtualOrder.setExpectedTime(orderInfo.getExpectedTime());
        return virtualOrder;
    }

    @Override
    public Result virtualUpdate(OrderFormEntityBo orderFormEntityBo, UserInfo userInfo)
            throws BusinessCheckedException {

        // order's basic info
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        Long orderId = orderInfo.getId();

        // ordered goods
        List<OrderGoods> orderedGoodsList = new ArrayList<OrderGoods>();
        // valid ordered goods
        List<OrderGoods> validGoodsList = new ArrayList<OrderGoods>();
        // ordered services
        List<OrderServices> orderedServiceList = new ArrayList<OrderServices>();
        // valid ordered services
        List<OrderServices> validServiceList = new ArrayList<OrderServices>();

        // transfer json to list
        Gson gson = new Gson();
        String orderGoodsJson = orderFormEntityBo.getOrderGoodJson();
        String orderServiceJson = orderFormEntityBo.getOrderServiceJson();

        try {
            orderedGoodsList = gson.fromJson(orderGoodsJson,
                    new TypeToken<List<OrderGoods>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购物料JSON：{}", orderGoodsJson);
            throw new RuntimeException("选购物料JSON转对象异常");
        }

        try {
            orderedServiceList = gson.fromJson(orderServiceJson,
                    new TypeToken<List<OrderServices>>() {
                    }.getType());
        } catch (JsonSyntaxException e) {
            LOGGER.error("选购服务JSON：{}", orderServiceJson);
            throw new RuntimeException("选购服务JSON转对象异常");
        }

        // ordered goods total number
        Long orderedGoodsTotalNumber = 0l;
        //  filter valid goods and calculate goods's amount
        if (!CollectionUtils.isEmpty(orderedGoodsList)) {
            for (OrderGoods goods : orderedGoodsList) {
                Long goodsId = goods.getId();
                String goodsFormat = goods.getGoodsFormat();
                String goodsName = goods.getGoodsName();
                if ((goodsId != null && goodsId != 0)
                        || (!StringUtils.isEmpty(goodsFormat) ||
                        !StringUtils.isEmpty(goodsName))) {
                    // calculator goods’s amount
                    BigDecimal goodsPrice = goods.getGoodsPrice();
                    // goodsPrice default 0
                    goodsPrice = (goodsPrice == null) ? BigDecimal.ZERO : goodsPrice;
                    BigDecimal goodsNumber = goods.getGoodsNumber();
                    // goodsNumber default 1
                    goodsNumber = (goodsNumber == null ||
                            goodsNumber.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE : goodsNumber;
                    goods.setGoodsNumber(goodsNumber);
                    BigDecimal goodsAmountTemp = goodsPrice.multiply(goodsNumber);
                    goods.setGoodsAmount(goodsAmountTemp);
                    BigDecimal discount = goods.getDiscount();
                    // discount default 0
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal soldAmountTemp = goodsAmountTemp.subtract(discount);
                    goods.setSoldAmount(soldAmountTemp);
                    goods.setSoldPrice(soldAmountTemp.divide(goodsNumber, 8, BigDecimal.ROUND_HALF_UP));

                    validGoodsList.add(goods);
                }
            }

            orderedGoodsTotalNumber = Long.valueOf(validGoodsList.size() + "");
        }
        // goods's total number
        orderInfo.setGoodsCount(orderedGoodsTotalNumber);

        // ordered service total number
        Long orderedServiceNumber = 0L;
        if (!CollectionUtils.isEmpty(orderedServiceList)) {
            // filter valid service and calculate servie's amount
            for (OrderServices service : orderedServiceList) {
                Long serviceId = service.getId();
                String serviceName = service.getServiceName();
                if ((serviceId != null && serviceId != 0) ||
                        !StringUtils.isEmpty(serviceName)) {
                    BigDecimal servicePrice = service.getServicePrice();
                    // servicePrice default 0
                    servicePrice = (servicePrice == null) ? BigDecimal.ZERO : servicePrice;
                    // serviceHour default 0
                    BigDecimal serviceHour = service.getServiceHour();
                    serviceHour = (serviceHour == null) ? BigDecimal.ZERO : serviceHour;
                    // discount default 0
                    BigDecimal discount = service.getDiscount();
                    discount = (discount == null) ? BigDecimal.ZERO : discount;
                    BigDecimal serviceAmountTemp = servicePrice.multiply(serviceHour);
                    service.setServiceAmount(serviceAmountTemp);
                    BigDecimal soldAmountTemp = serviceAmountTemp.subtract(discount);
                    service.setSoldAmount(soldAmountTemp);
                    if (serviceHour.compareTo(BigDecimal.ZERO) <= 0) {
                        service.setSoldPrice(BigDecimal.ZERO);
                    } else {
                        service.setSoldPrice(soldAmountTemp.divide(serviceHour, 8, BigDecimal.ROUND_HALF_UP));
                    }

                    validServiceList.add(service);
                }
            }

            orderedServiceNumber = Long.valueOf(validServiceList.size() + "");
        }
        orderInfo.setServiceCount(orderedServiceNumber);

        // calculate order's Amount
        calculateOrderExpense(orderInfo, validGoodsList, validServiceList);

        // TODO 设置公共信息(创建者、创建时间、更新者、更新时间)
        orderInfo.setModifier(userInfo.getUserId());
        orderInfo.setOperatorName(userInfo.getName());
        orderInfo.setShopId(userInfo.getShopId());

        updateVirtualOrder(orderInfo, validGoodsList, validServiceList, userInfo);

        return Result.wrapSuccessfulResult(orderInfo.getParentId());
    }

    @Override
    @Transactional
    public void virtualDelete(Long orderId, UserInfo userInfo) throws BusinessCheckedException {
        Long shopId = userInfo.getShopId();
        // 主对象为null，直接退出逻辑，前端给公共错误提示
        Optional<VirtualOrder> virtualOrderOptional = virtualOrderService.getOrderById(orderId);
        if (virtualOrderOptional.isPresent()) {
            VirtualOrder virtualOrder = virtualOrderOptional.get();
            Long virtualOrderId = virtualOrder.getId();
            try {
                virtualOrderService.delete(virtualOrderId);
            } catch (Exception e) {
                LOGGER.error("删除虚拟子单失败，工单ID:{}", virtualOrderId);
                throw new RuntimeException("删除虚拟子单异常");
            }

            // 物料
            List<VirtualOrdergood> virtualOrdergoodList = virtualOrdergoodService.queryOrderGoods(orderId, shopId);
            Set<Long> goodIdSet = new HashSet<Long>(virtualOrdergoodList.size());
            for (VirtualOrdergood good : virtualOrdergoodList) {
                goodIdSet.add(good.getId());
            }
            if (goodIdSet.size() > 0) {
                try {
                    virtualOrdergoodService.deleteByIds(goodIdSet);
                } catch (Exception e) {
                    LOGGER.error("批量删除虚拟子单的物料失败，物料IDS:{}", goodIdSet.toString());
                    throw new RuntimeException("批量删除虚拟子单的物料异常");
                }
            }

            // 服务
            List<VirtualOrderservice> virtualOrderserviceList = virtualOrderserviceService.queryOrderServices(orderId, shopId);
            Set<Long> serviceIdSet = new HashSet<Long>(virtualOrderserviceList.size());
            for (VirtualOrderservice service : virtualOrderserviceList) {
                serviceIdSet.add(service.getId());
            }
            if (serviceIdSet.size() > 0) {
                try {
                    virtualOrderserviceService.deleteByIds(serviceIdSet);
                } catch (Exception e) {
                    LOGGER.error("批量删除虚拟子单的服务失败，服务IDS:{}", serviceIdSet.toString());
                    throw new RuntimeException("批量删除虚拟子单的服务异常");
                }

            }
        } else {
            throw new BusinessCheckedException("12", "虚拟子单不存在！");
        }
    }


    @Override
    public OrderExpenseEntity calculateOrderExpense(List<OrderGoods> orderGoodsList,
                                                    List<OrderServices> orderServicesList) {
        // 服务总金额
        BigDecimal serviceTotalAmount = BigDecimal.ZERO;
        // 物料总金额
        BigDecimal goodsTotalAmount = BigDecimal.ZERO;
        // 工单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 工单总折扣
        BigDecimal totalDiscount = BigDecimal.ZERO;
        // 物料总折扣
        BigDecimal goodsTotalDiscount = BigDecimal.ZERO;
        // 服务总折扣
        BigDecimal serviceTotalDiscount = BigDecimal.ZERO;
        // 费用总折扣
        BigDecimal feeTotalDiscount = BigDecimal.ZERO;
        // 费用总金额
        BigDecimal feeTotalAmount = BigDecimal.ZERO;
        // 管理费用
        BigDecimal manageFee = BigDecimal.ZERO;

        for (OrderGoods orderGoods : orderGoodsList) {
            //  判断有效物料
            Long goodsId = orderGoods.getGoodsId();
            if (goodsId != null) {
                BigDecimal goodsPrice = orderGoods.getGoodsPrice();
                goodsPrice = (goodsPrice == null) ? BigDecimal.ZERO : goodsPrice;
                BigDecimal goodsNumber = orderGoods.getGoodsNumber();
                goodsNumber = (goodsNumber == null) ? BigDecimal.ZERO : goodsNumber;
                BigDecimal goodsAmount = goodsPrice.multiply(goodsNumber);
                goodsTotalAmount = goodsTotalAmount.add(goodsAmount);
                totalAmount = totalAmount.add(goodsAmount);
                BigDecimal goodDiscount = orderGoods.getDiscount();
                goodDiscount = (goodDiscount == null) ? BigDecimal.ZERO : goodDiscount;
                // 累加总折扣
                totalDiscount = totalDiscount.add(goodDiscount);
                // 累加总折扣
                goodsTotalDiscount = goodsTotalDiscount.add(goodDiscount);
            }
        }

        manageFee = goodsTotalAmount.subtract(goodsTotalDiscount);

        for (OrderServices orderServices : orderServicesList) {
            Long serviceId = orderServices.getServiceId();
            if (serviceId != null) {
                Integer serviceType = orderServices.getType();
                BigDecimal serviceDiscount = orderServices.getDiscount();
                serviceDiscount = (serviceDiscount == null) ? BigDecimal.ZERO : serviceDiscount;
                BigDecimal serviceAmount = orderServices.getServiceAmount();
                serviceAmount = (serviceAmount == null) ? BigDecimal.ZERO : serviceAmount;
                if (serviceType != null && serviceType.intValue() == 1) {
                    // 累加服务折扣
                    serviceTotalDiscount = serviceTotalDiscount.add(serviceDiscount);
                    // 累加服务金额
                    serviceTotalAmount = serviceTotalAmount.add(serviceAmount);
                }

                if (serviceType != null && serviceType.intValue() == 2) {
                    String flags = orderServices.getFlags();
                    if (flags != null && flags.equals(Constants.GLF)) {
                        feeTotalDiscount = feeTotalDiscount.add(serviceDiscount);
                        BigDecimal manageRate = orderServices.getManageRate();
                        manageRate = (manageRate == null) ? BigDecimal.ONE : manageRate;
                        // 管理费 * 比率
                        manageFee = manageFee.multiply(manageRate);
                        // 累加费用总金额
                        feeTotalAmount = feeTotalAmount.add(manageFee);
                    } else {
                        // 累加费用总金额
                        feeTotalDiscount = feeTotalDiscount.add(serviceDiscount);
                        feeTotalAmount = feeTotalAmount.add(serviceAmount);
                    }
                }
            }
        }

        // 工单总金额 =物料总金额 + 服务总金额 + 费用总金额
        totalAmount = goodsTotalAmount.add(serviceTotalAmount).add(feeTotalAmount);
        // 工单总折扣 =物料总折扣 + 服务总折扣 + 费用总折扣
        totalDiscount = goodsTotalDiscount.add(serviceTotalDiscount).add(feeTotalDiscount);

        // 工单费用相应实体
        OrderExpenseEntity expenseEntity = new OrderExpenseEntity();
        expenseEntity.setGoodsAmount(goodsTotalAmount);
        expenseEntity.setServiceAmount(serviceTotalAmount);
        expenseEntity.setTotalAmount(totalAmount);
        expenseEntity.setDiscount(totalDiscount);
        expenseEntity.setGoodsDiscount(goodsTotalDiscount);
        expenseEntity.setServiceDiscount(serviceTotalDiscount);
        expenseEntity.setFeeAmount(feeTotalAmount);
        expenseEntity.setFeeDiscount(feeTotalDiscount);
        expenseEntity.setOrderAmount(totalAmount.subtract(totalDiscount));
        expenseEntity.setManageFee(manageFee);

        return expenseEntity;
    }


    /**
     * update 虚拟工单基本信息、选购商品、选购服务
     *
     * @param orderInfo
     * @param orderGoodsList
     * @param orderServicesList
     * @param userInfo
     * @return
     */
    @Transactional
    public void updateVirtualOrder(OrderInfo orderInfo,
                                   List<OrderGoods> orderGoodsList,
                                   List<OrderServices> orderServicesList,
                                   UserInfo userInfo) {
        // 当前操作人
        Long userId = userInfo.getUserId();
        // 工单ID
        Long orderId = orderInfo.getId();
        Long shopId = orderInfo.getShopId();

        // 更新orderInfo
        VirtualOrder virtualOrder = copyOrderPropertyToVirtualOrder(orderInfo);
        virtualOrder.setId(orderId);
        virtualOrder.setParentId(orderInfo.getParentId());
        String identityCard = orderInfo.getIdentityCard();
        virtualOrder.setIdentityCard(identityCard == null ? "" : identityCard);

        // 操作人信息
        virtualOrder.setModifier(userInfo.getUserId());
        virtualOrder.setOperatorName(userInfo.getName());
        virtualOrder.setShopId(userInfo.getShopId());
        virtualOrderService.update(virtualOrder);

        // 编辑前：选购的商品
        List<VirtualOrdergood> oldGoodsList =
                virtualOrdergoodService.queryOrderGoods(orderId, shopId);

        Set<Long> orderGoodsIds = new HashSet<Long>();
        Set<Long> oldGoodsIds = new HashSet<Long>();
        if (!CollectionUtils.isEmpty(oldGoodsList)) {
            for (VirtualOrdergood good : oldGoodsList) {
                oldGoodsIds.add(good.getId());
            }
        }

        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGood : orderGoodsList) {
                Long goodId = orderGood.getId();

                VirtualOrdergood virtualOrdergood = copyGoodPropertyToVirtualGood(userInfo, virtualOrder, orderGood);
                if (goodId == null) {
                    virtualOrdergoodService.save(virtualOrdergood);
                } else {
                    virtualOrdergood.setId(goodId);
                    virtualOrdergoodService.update(virtualOrdergood);
                    orderGoodsIds.add(goodId);
                }
            }
        }

        // 之前选择的：不存在情况，删除掉
        if (!CollectionUtils.isEmpty(oldGoodsList)) {
            for (VirtualOrdergood oldOrderGoods : oldGoodsList) {
                if (orderGoodsIds != null && !orderGoodsIds.contains(oldOrderGoods.getId())) {
                    oldOrderGoods.setModifier(userId);
                    oldOrderGoods.setIsDeleted("Y");
                    virtualOrdergoodService.update(oldOrderGoods);
                }
            }
        }

        // (Long orderId,Long shopId);
        List<VirtualOrderservice> oldOrderServicesList =
                virtualOrderserviceService.queryOrderServices(orderId, shopId);

        Set<Long> orderServicesIds = new HashSet<>();
        Set<Long> oldServicesIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(oldOrderServicesList)) {
            for (VirtualOrderservice oldOrderServices : oldOrderServicesList) {
                oldServicesIds.add(oldOrderServices.getId());
            }
        }

        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices newOrderServices : orderServicesList) {
                Long newOrderServiceId = newOrderServices.getId();
                VirtualOrderservice virtualOrderservice = copyServicePropertyToVirtualService(userInfo, virtualOrder, newOrderServices);
                if (newOrderServiceId == null) {
                    virtualOrderserviceService.save(virtualOrderservice);
                } else {
                    virtualOrderservice.setId(newOrderServiceId);
                    virtualOrderserviceService.update(virtualOrderservice);
                    orderServicesIds.add(newOrderServiceId);
                }
            }
        }

        // 之前选择的：不存在情况，删除掉
        if (!CollectionUtils.isEmpty(oldOrderServicesList)) {
            for (VirtualOrderservice oldOrderServices : oldOrderServicesList) {
                if (orderServicesIds != null && !orderServicesIds.contains(oldOrderServices.getId())) {
                    oldOrderServices.setModifier(userInfo.getUserId());
                    oldOrderServices.setIsDeleted("Y");
                    virtualOrderserviceService.update(oldOrderServices);
                }
            }
        }

    }


    @Override
    public int statisticsOrderNumber(long shopId, OrderCategoryEnum orderCategoryEnum) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("shopId", shopId);
        map.put("orderTag", orderCategoryEnum.getCode());
        return orderInfoDao.selectCount(map);
    }


    @Override
    public Optional<OrderInfo> getOrder(Long orderId) {
        OrderInfo orderInfo = null;
        try {
            orderInfo = orderInfoDao.selectById(orderId);
        } catch (Exception e) {
            LOGGER.error("根据工单ID，查询数据库的工单实体异常,异常{}", e.toString());
            return Optional.absent();
        }
        return Optional.fromNullable(orderInfo);
    }

    @Override
    public Optional<OrderInfo> getOrder(Long orderId, Long shopId) {
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("id",orderId);
        searchMap.put("shopId",shopId);
        OrderInfo orderInfo = orderInfoDao.selectByIdAndShopId(searchMap);
        return Optional.fromNullable(orderInfo);
    }

    @Override
    @Transactional
    public int updateOrderStatus(long orderId, OrderStatusEnum statusEnum) {
        int updateSign = 0;
        // 查找工单
        Optional<OrderInfo> orderInfoOptional = this.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            LOGGER.error("更新数据库工单状态失败。工单不存在,工单ID:{}", orderId);
            return updateSign;
        }

        // 更新工单实体
        OrderInfo orderInfo = orderInfoOptional.get();
        String orderStatus  = statusEnum.getKey();
        orderInfo.setOrderStatus(orderStatus);
        int success = orderInfoDao.updateById(orderInfo);
        //TODO 工单状态变更需要插入track表，此方法可能还要修改，到时候回写方法直接写在track内，现在暂时先这么写
        if(success == 1) {
            //回写委托单
            writeBackProxy(orderInfo);
        }
        return success;
    }

    @Override
    @Transactional
    public int updateOrder(OrderInfo orderInfo) {
        Long orderId = orderInfo.getId();

        Optional<OrderInfo> orderInfoOptional = this.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            LOGGER.error("更新工单实体失败,原因：工单实体不存在，工单ID:{}", orderId);
            throw new BizException("更新工单实体失败,原因：工单实体不存在");
        }
        return orderInfoDao.updateById(orderInfo);
    }


    /**
     * TODO refactor code
     * <p/>
     * 获取门店最近一次洗车单信息
     *
     * @param shopId
     * @return
     */
    public OrderServicesVo getLastCarwash(Long shopId) {
        //获取门店最近一次洗车单
        Map<String, Object> orderMap = new HashMap<>(5);
        orderMap.put("shopId", shopId);
        orderMap.put("orderTag", OrderCategoryEnum.CARWASH.getCode());

        List<String> sorts = new ArrayList<>();
        sorts.add(" id desc ");
        orderMap.put("sorts", sorts);

        orderMap.put("offset", 0);
        orderMap.put("limit", 1);

        List<OrderInfo> orderInfoList = orderInfoDao.select(orderMap);
        if (CollectionUtils.isEmpty(orderInfoList)) {
            return null;
        }
        OrderInfo orderInfo = orderInfoList.get(0);

        Map<String, Object> orderServiceMap = new HashMap<>();
        orderServiceMap.put("orderId", orderInfo.getId());
        orderServiceMap.put("shopId", shopId);
        List<OrderServices> orderServicesList = orderServicesService.select(orderServiceMap);
        if (CollectionUtils.isEmpty(orderServicesList)) {
            return null;
        }
        OrderServices orderServices = orderServicesList.get(0);
        OrderServicesVo orderServicesVo = null;
        if (orderServices != null) {
            orderServicesVo = OrderServicesVo.getVo(orderServices);
            Set<Long> workerIdSet = new HashSet();
            String workerIds = orderServices.getWorkerIds();
            if (StringUtils.isNotBlank(workerIds) && !"0".equals(workerIds)) {
                // 通过逗号切分
                String[] workerIdArr = workerIds.split(",");
                if (ArrayUtils.isNotEmpty(workerIdArr)) {
                    for (String workerId : workerIdArr) {
                        workerId = workerId.trim();
                        if (!"0".equals(workerId) || !"".equals(workerId)) {
                            long id = Long.parseLong(workerId);
                            workerIdSet.add(id);
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(workerIdSet)) {
                Long[] workerIdArray = new Long[workerIdSet.size()];
                workerIdArray = workerIdSet.toArray(workerIdArray);
                //查询多个维修工
                List<ShopManager> shopManagerList = shopManagerService.selectByIds(workerIdArray);
                StringBuilder workerNames = new StringBuilder();
                for (ShopManager shopManager : shopManagerList) {
                    workerNames.append(shopManager.getName()).append(",");
                }
                if (workerNames.length() > 0) {
                    orderServicesVo.setWorkerNames(workerNames.substring(0, workerNames.length() - 1));
                }
            }
        }
        return orderServicesVo;
    }

    @Override
    public WashCarStats getWashCarTodayStats(Long shopId) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dayStr = fmt.format(date);
        String startTime = dayStr + " 00:00:00";
        String endTime = dayStr + " 23:59:59";

        WashCarStats washCarStats = new WashCarStats();
        //统计今日洗车订单数
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("orderTag", OrderCategoryEnum.CARWASH.getCode());
        searchMap.put("startTime", startTime);
        searchMap.put("endTime", endTime);
        int statsCount = orderInfoDao.selectCount(searchMap);
        BigDecimal statsAmount = BigDecimal.ZERO;
        //统计今日收款数
        try {
            com.tqmall.core.common.entity.Result<Map<Long, BigDecimal>> carWashPaymentAmountResult = rpcBusinessDailyService.getCarWashPaymentAmount(shopId, dayStr);
            if (carWashPaymentAmountResult.isSuccess()) {
                Map<Long, BigDecimal> carWashPaymentAmountMap = carWashPaymentAmountResult.getData();
                for (Map.Entry<Long, BigDecimal> entry : carWashPaymentAmountMap.entrySet()) {
                    if (entry.getKey().compareTo(0l) == 1) {
                        BigDecimal amount = entry.getValue();
                        //目前payment 0为会员卡收款
                        statsAmount = statsAmount.add(amount);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("调用cube获取今日洗车收款金额异常", e);
        }
        washCarStats.setStatsAmount(statsAmount + "");
        washCarStats.setStatsCount(statsCount);
        return washCarStats;
    }

    @Override
    public List<OrderInfo> selectByIdsAndShopId(List<Long> orderIds, Long shopId) {
        return orderInfoDao.selectByIdsAndShopId(orderIds, shopId);
    }

    @Override
    @Transactional
    public int delete(Long orderId) {
        int updateSign = 0;
        // 查找工单
        Optional<OrderInfo> orderInfoOptional = this.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            LOGGER.error("逻辑删除工单失败。工单不存在,工单ID:{}", orderId);
            return updateSign;
        }

        // 更新工单实体
        OrderInfo orderInfo = orderInfoOptional.get();
        orderInfo.setIsDeleted("Y");
        return orderInfoDao.updateById(orderInfo);
    }

    @Override
    public List<OrderInfo> getHistoryOrderByCustomerCarId(Long customerCarId, Long shopId) {

        List<OrderInfo> orderInfoList = null;

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("customerCarId", customerCarId);
        paramMap.put("shopId", shopId);
        try {
            orderInfoList = orderInfoDao.select(paramMap);
        } catch (Exception e) {
            LOGGER.error("[DB]query legend_order_info failure param:{} exception:{}", "customerCarId:" + customerCarId + ",shopId:" + shopId, e);
            orderInfoList = new ArrayList<OrderInfo>();
        }

        if (CollectionUtils.isEmpty(orderInfoList)) {
            orderInfoList = new ArrayList<OrderInfo>();
        }

        return orderInfoList;
    }

    @Override
    public int selectCount(Map<String, Object> params) {
        return orderInfoDao.selectCount(params);
    }

    public void writeBackProxy(OrderInfo orderInfo){
        if(orderInfo.getProxyType() != null && orderInfo.getProxyType().equals(2)){
            /**
             * 同步委托单状态
             * order_status     proxy_status
             * FPDD 分配订单 ==> FPDD：已派工
             * DDWC 订单完成 ==> DDWC：已完工
             */
            Long orderId = orderInfo.getId();
            Long shopId = orderInfo.getShopId();
            String orderStatus = orderInfo.getOrderStatus();
            if(orderStatus.equals(OrderStatusEnum.FPDD.getKey()) || orderStatus.equals(OrderStatusEnum.DDWC.getKey())){
                LOGGER.info("【委托单转工单，工单状态变更同步委托单】：查询委托单信息，门店id:{},受托的工单id:{}", shopId, orderId);
                com.tqmall.core.common.entity.Result<ProxyDTO> proxyDTOResult = rpcProxyService.getProxyInfoByShopIdAndProxyOrderId(shopId, orderId);
                if (proxyDTOResult.isSuccess()) {
                    ProxyDTO proxyDTO = proxyDTOResult.getData();
                    proxyDTO.setOrderStatus(orderStatus);
                    proxyDTO.setProxyStatus(orderStatus);
                    com.tqmall.core.common.entity.Result updateResult = rpcProxyService.updateProxyOrder(proxyDTO);
                    LOGGER.info("【委托单转工单，工单状态变更同步委托单】：更新委托单，委托单id：{}，orderStatus：{}，proxyStatus：{}", proxyDTO.getId(), orderStatus, orderStatus);
                    if(!updateResult.isSuccess()){
                        throw new RuntimeException("同步委托单失败");
                    }
                }
            }
        }
    }

    /**
     * 设置工单预检信息
     * @param orderFormEntityBo
     * @return
     */
    private List<OrderPrecheckDetails> setOrderPrecheckDetails(OrderFormEntityBo orderFormEntityBo,Long shopId,Long precheckId){
        if(orderFormEntityBo == null){
            return null;
        }
        String orderPrecheckDetailsStr = orderFormEntityBo.getOrderPrecheckDetailsJson();
        if(StringUtils.isNotBlank(orderPrecheckDetailsStr)){
            try {
                Map<String, String> tmpItemsAppearance = new Gson().fromJson(orderPrecheckDetailsStr,
                        new TypeToken<Map<String, String>>() {
                        }.getType());
                List<PrecheckDetails> precheckDetailsList = prechecksFacade.getItemList(tmpItemsAppearance,null,null,shopId);
                if(!CollectionUtils.isEmpty(precheckDetailsList)){
                    List<OrderPrecheckDetails> orderPrecheckDetailsList = new ArrayList<>();
                    for(PrecheckDetails precheckDetails : precheckDetailsList){
                        OrderPrecheckDetails orderPrecheckDetails = new OrderPrecheckDetails();
                        BeanUtils.copyProperties(precheckDetails, orderPrecheckDetails);
                        orderPrecheckDetails.setPrecheckId(precheckId);
                        orderPrecheckDetails.setShopId(shopId);
                        orderPrecheckDetailsList.add(orderPrecheckDetails);
                    }
                    return orderPrecheckDetailsList;
                }
            } catch (JsonSyntaxException e) {
                LOGGER.error("工单预检信息Json转换出错：{}", orderPrecheckDetailsStr);
                throw new RuntimeException("工单预检信息有误");
            }
        }
        return null;
    }
}

