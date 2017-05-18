package com.tqmall.legend.biz.order.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.*;
import com.tqmall.legend.biz.message.MQPushMessageService;
import com.tqmall.legend.biz.order.*;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.customer.*;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.sys.User;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * orderInfoExt service
 * <p/>
 * Created by lixiao on 14-11-3.
 */
@Service
public class OrderExtServiceImpl extends BaseServiceImpl implements OrderExtService {


    public static final Logger LOGGER = LoggerFactory.getLogger(OrderExtServiceImpl.class);

    @Autowired
    CustomerCarService customerCarService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ICustomerContactService customerContactService;
    @Autowired
    IOrderSnapshotService orderSnapshotService;
    @Autowired
    private OrderTrackService orderTrackService;
    @Autowired
    private AppointService appointService;
    @Autowired
    private MQPushMessageService mqPushMessageService;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private OrderInfoExtService orderInfoExtService;
    @Autowired
    private CustomerUserRelService customerUserRelService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderServicesWorkerService orderServicesWorkerService;
    @Autowired
    private ShopManagerService shopManagerService;

    /**
     * 异步保存工单无关紧要信息
     *
     * @param customerCar
     * @param orderInfo
     * @param userInfo
     */
    // 异步执行
    public void saveOtherDataFuture(CustomerCar customerCar,
                                    OrderInfo orderInfo,
                                    UserInfo userInfo,
                                    String orderGoodsJson,
                                    String orderServiceJson,
                                    Long proxyId) {
        try {
            mqPushMessageService.pushMsgToApp(Constants.ORDER_CREATE, orderInfo);
        } catch (Exception e) {
            LOGGER.error("工单创建推送消息到2CAPP端异常!异常信息:{},工单信息:{}", e, orderInfo);
        }

        // 记录订单状态跟踪表
        orderTrackService.track(orderInfo, userInfo);
        LOGGER.info("工单操作流水：{} 记录工单状态流转状态，工单状态:{}, 操作人:{}", orderInfo.getCarLicense(), orderInfo.getOrderStatus(), userInfo.getUserId());

        // 记录订单信息快照
        OrderSnapshot orderSnapshot = new OrderSnapshot();
        orderSnapshot.setShopId(userInfo.getShopId());
        orderSnapshot.setOrderSn(orderInfo.getOrderSn());
        orderSnapshot.setCreator(userInfo.getUserId());
        orderSnapshot.setOrderInfo(orderInfo.toString());
        orderSnapshot.setOrderGoods(orderGoodsJson);
        orderSnapshot.setOrderServices(orderServiceJson);
        orderSnapshotService.save(orderSnapshot);
        LOGGER.info("工单操作流水:{}记录工单快照", orderInfo.getCarLicense());

        // 回写customerCar
        writeBackCustomerCar(orderInfo, customerCar);
        LOGGER.info("工单操作流水:{}回写客户车辆数据", orderInfo.getCarLicense());

        // 回写customerContact
        writeBackCustomerContact(orderInfo, userInfo);
        LOGGER.info("工单操作流水:{}回写客户联系人数据", orderInfo.getCarLicense());

        // 回写customer
        writeBackCustomer(orderInfo);
        LOGGER.info("工单操作流水:{}回写客户数据", orderInfo.getCarLicense());

        //回写appoint
        if (orderInfo.getAppointId() != null) {
            writeBackAppoint(orderInfo);
            LOGGER.info("工单操作流水:{}回写预约单数据", orderInfo.getCarLicense());
        }
        //回写委托单proxy
        if (proxyId != null) {
            writeBackProxy(proxyId, orderInfo);
            LOGGER.info("工单操作流水:委托单id{},回写委托单数据数据", proxyId);
        }
        //回写工单归属
        writeBackOrderInfoExt(orderInfo, userInfo);
        LOGGER.info("工单操作流水:{}回写工单归属", orderInfo.getCarLicense());

        //插入工单服务维修工
        insertOrderServiceWorker(orderInfo, userInfo);
        LOGGER.info("工单操作流水:{}插入工单服务维修工", orderInfo.getCarLicense());
    }

    /**
     * 异步更新工单无关紧要信息
     *
     * @param customerCar
     * @param orderInfo
     * @param userInfo
     */
    public void updateOtherDataFuture(CustomerCar customerCar,
                                      OrderInfo orderInfo,
                                      OrderInfo orderLatestData,
                                      UserInfo userInfo) {

        // 记录订单状态跟踪表
        orderTrackService.track(orderInfo, userInfo);
        LOGGER.info("工单操作流水：{} 记录工单状态流转状态，工单状态:{}, 操作人:{}", orderInfo.getCarLicense(), orderInfo.getOrderStatus(), userInfo.getUserId());

        // 回写customerCar
        writeBackCustomerCar(orderInfo, customerCar);
        LOGGER.info("工单操作流水:{}回写客户车辆数据", orderInfo.getCarLicense());

        // 回写customerContact
        writeBackCustomerContact(orderInfo, userInfo);
        LOGGER.info("工单操作流水:{}回写客户联系人数据", orderInfo.getCarLicense());

        // 回写customer
        writeBackCustomer(orderInfo);
        LOGGER.info("工单操作流水:{}回写客户数据", orderInfo.getCarLicense());

        //回写工单归属
        writeBackOrderInfoExt(orderInfo, userInfo);
        LOGGER.info("工单操作流水:{}回写工单归属", orderInfo.getCarLicense());
        //更新工单服务维修工
        updateOrderServiceWorker(orderInfo, userInfo);
        LOGGER.info("工单操作流水:{}更新工单服务维修工", orderInfo.getCarLicense());

    }


    /**
     * 回写customer
     *
     * @param orderInfo
     */
    private void writeBackCustomer(OrderInfo orderInfo) {
        Long customerId = orderInfo.getCustomerId();
        if (customerId != null && customerId > 0) {
            Customer customer = customerService.selectById(customerId);
            if (customer != null) {
                customer.setContact(orderInfo.getContactName());
                customer.setContactMobile(orderInfo.getContactMobile());
                customer.setCustomerAddr(orderInfo.getCustomerAddress());
                // 车主姓名
                String customerName = orderInfo.getCustomerName();
                // 车主电话
                String customerMobile = orderInfo.getCustomerMobile();
                if (!StringUtils.isEmpty(customerName)) {
                    customer.setCustomerName((customerName));
                }
                if (!StringUtils.isEmpty(customerMobile)) {
                    customer.setMobile((customerMobile));
                }

                // 客户单位
                String company = orderInfo.getCompany();
                if (!StringUtils.isEmpty(company)) {
                    customer.setCompany(company);
                }
                customerService.update(customer);
            }
        }
    }

    /**
     * create by jason 2015-07-21
     * 回写appoint
     *
     * @param orderInfo
     */
    private void writeBackAppoint(OrderInfo orderInfo) {
        Map map = new HashMap();
        map.put("shopId", orderInfo.getShopId());
        map.put("id", orderInfo.getAppointId());
        List<Appoint> appointList = appointService.select(map);
        if (!CollectionUtils.isEmpty(appointList)) {
            Appoint appoint = appointList.get(0);
            if (null != appoint) {
                appoint.setOrderId(orderInfo.getId());
                appoint.setStatus(2l);//预约单转工单
                appoint.setPreviewType(1l);//预约单已处理
                appointService.update(appoint);
            }
        }
    }

    /**
     * 回写customercar
     *
     * @param orderInfo
     * @param customerCar
     */
    private void writeBackCustomerCar(OrderInfo orderInfo, CustomerCar customerCar) {
        customerCar.setColor(orderInfo.getCarColor());
        customerCar.setInsuranceId(orderInfo.getInsuranceCompanyId());
        customerCar.setInsuranceCompany(orderInfo.getInsuranceCompanyName());
        if (orderInfo.getBuyTime() != null) {
            customerCar.setBuyTime(orderInfo.getBuyTime());
        }
        if (StringUtils.isNumeric(orderInfo.getMileage())) {
            Long mileage = Long.parseLong(orderInfo.getMileage());
            customerCar.setMileage(mileage);
        }
        customerCarService.update(customerCar);
    }

    /**
     * 回写 Customer Contact
     *
     * @param orderInfo
     * @param userInfo
     */
    private void writeBackCustomerContact(OrderInfo orderInfo, UserInfo userInfo) {
        if (!StringUtils.isBlank(orderInfo.getContactName())) {
            List<CustomerContact> customerContactList = customerContactService.queryCustomerContact(userInfo.getShopId(),
                    orderInfo.getContactName(), orderInfo.getCustomerId(), orderInfo.getCustomerCarId());

            if (!CollectionUtils.isEmpty(customerContactList)) {
                CustomerContact customerContact = customerContactList.get(0);
                customerContact.setModifier(userInfo.getUserId());
                customerContact.setShopId(userInfo.getShopId());
                customerContact.setContact(orderInfo.getContactName());
                customerContact.setContactMobile(orderInfo.getContactMobile());
                customerContact.setCustomerId(orderInfo.getCustomerId());
                customerContact.setCustomerCarId(orderInfo.getCustomerCarId());
                customerContactService.update(customerContact);
            } else {
                CustomerContact customerContact = new CustomerContact();
                customerContact.setCreator(userInfo.getUserId());
                customerContact.setModifier(userInfo.getUserId());
                customerContact.setShopId(userInfo.getShopId());
                customerContact.setContact(orderInfo.getContactName());
                customerContact.setContactMobile(orderInfo.getContactMobile());
                customerContact.setCustomerId(orderInfo.getCustomerId());
                customerContact.setCustomerCarId(orderInfo.getCustomerCarId());
                customerContactService.save(customerContact);
            }
        }
    }


    /**
     * 回写委托单
     *
     * @param proxyId
     * @param orderInfo
     */
    private void writeBackProxy(Long proxyId, OrderInfo orderInfo) {
        //如果是受委托的工单，则创建的时候需要回填委托单信息
        if (orderInfo.getProxyType() != null && orderInfo.getProxyType().equals(2)) {
            Long orderId = orderInfo.getId();
            //TODO 查询委托单，并更新委托单状态，回填proxy_id = orderId,proxy_status=YYC,order_status=CJDD
            LOGGER.info("【委托单转工单，工单创建】：查询委托单信息，委托单id:{}", proxyId);
            com.tqmall.core.common.entity.Result<ProxyDTO> proxyDTOResult = rpcProxyService.getProxyInfoById(proxyId);
            if (proxyDTOResult.isSuccess()) {
                ProxyDTO proxyDTO = proxyDTOResult.getData();
                proxyDTO.setProxyId(orderId);
                String proxyStatus = ProxyStatusEnum.YYC.getCode();
                String orderStatus = OrderStatusEnum.CJDD.getKey();
                proxyDTO.setProxyStatus(proxyStatus);
                proxyDTO.setOrderStatus(orderStatus);
                LOGGER.info("【委托单转工单，工单状态变更同步委托单】：更新委托单,proxyId：{}，proxyStatus：{}，orderStatus：{}", orderId, proxyStatus, orderStatus);
                rpcProxyService.updateProxyOrder(proxyDTO);
            }
        }
    }

    private void writeBackOrderInfoExt(OrderInfo orderInfo , UserInfo userInfo) {
        CustomerUserRel customerUserRel = customerUserRelService.selectByCustomerCarId(orderInfo.getCustomerCarId());
        OrderInfoExt orderInfoExt = new OrderInfoExt();
        orderInfoExt.setShopId(userInfo.getShopId());
        orderInfoExt.setOrderId(orderInfo.getId());
        Long userId = 0l;
        if (customerUserRel != null) {
            userId = customerUserRel.getUserId();
        }
        orderInfoExt.setUserId(userId);
        orderInfoExtService.save(orderInfoExt, userInfo);
    }

    private void insertOrderServiceWorker(OrderInfo orderInfo, UserInfo userInfo) {
        Long orderId = orderInfo.getId();
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC);
        if (CollectionUtils.isEmpty(orderServicesList)) {
            return;
        }
        List<OrderServicesWorker> orderServicesWorkerList = new ArrayList<>();
        for (OrderServices orderServices : orderServicesList) {
            String workerIds = orderServices.getWorkerIds();
            if (StringUtils.isBlank(workerIds) || "0".equals(workerIds)) {
                continue;
            }
            // 通过逗号切分
            String[] workerIdArr = workerIds.split(",");
            int workerIdSetSize = workerIdArr.length;
            if (workerIdSetSize < 1) {
                continue;
            }
            Long[] workerIdArrLong = new Long[workerIdSetSize];
            for (int i = 0; i < workerIdArr.length; i++) {
                workerIdArrLong[i] = Long.parseLong(workerIdArr[i]);
            }
            List<ShopManager> shopManagerLatestData =
                    shopManagerService.selectByIds(workerIdArrLong);
            int managerLatestDataSize = shopManagerLatestData.size();
            // Map<shopManager.id,shopManager>
            Map<Long, ShopManager> shopManagerListDBMap = new HashMap<Long, ShopManager>(managerLatestDataSize);
            for (ShopManager shopManager : shopManagerLatestData) {
                shopManagerListDBMap.put(shopManager.getId(), shopManager);
            }
            for (int j = 0; j < workerIdArrLong.length; j++) {
                Long workerId = workerIdArrLong[j];
                OrderServicesWorker orderServicesWorker = new OrderServicesWorker();
                orderServicesWorker.setCreator(userId);
                orderServicesWorker.setModifier(userId);
                orderServicesWorker.setShopId(shopId);
                orderServicesWorker.setOrderId(orderId);
                orderServicesWorker.setOrderServicesId(orderServices.getId());
                orderServicesWorker.setWorkerId(workerId);
                ShopManager shopManager = shopManagerListDBMap.get(workerId);
                if (shopManager != null) {
                    orderServicesWorker.setWorkerName(shopManager.getName());
                }
                orderServicesWorkerList.add(orderServicesWorker);
            }
        }
        if(!CollectionUtils.isEmpty(orderServicesWorkerList)) {
            LOGGER.info("批量插入工单服务维修工,{}", new Gson().toJson(orderServicesWorkerList));
            orderServicesWorkerService.batchInsert(orderServicesWorkerList);
        }
    }


    private void updateOrderServiceWorker(OrderInfo orderInfo, UserInfo userInfo) {
        //先删除后插入
        orderServicesWorkerService.deletebByOrderId(orderInfo.getId());
        insertOrderServiceWorker(orderInfo, userInfo);
    }


}

