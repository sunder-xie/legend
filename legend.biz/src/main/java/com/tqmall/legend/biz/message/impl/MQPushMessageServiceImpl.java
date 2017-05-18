package com.tqmall.legend.biz.message.impl;

import com.tqmall.common.Constants;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.jms.ddlwechat.MessagePlatformDdlWechat;
import com.tqmall.legend.biz.jms.yunxiu.MessagePlatformCApp;
import com.tqmall.legend.biz.message.MQPushMessageService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.shop.Shop;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/6/24.
 */
@Slf4j
@Service
public class MQPushMessageServiceImpl implements MQPushMessageService {

    @Autowired
    private MessagePlatformCApp cAppMp;
    @Autowired
    private RpcDebitBillService rpcDebitBillService;
    @Autowired
    private MessagePlatformDdlWechat ddlWechatMp;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderServicesService orderServicesService;

    /**
     * 发消息给车主端
     * @param type
     * APPOINT_CREATE = "1";// 预约单创建待确认
     * ORDER_CREATE = "2";// 工单创建
     * ORDER_FINISH = "3";// 工单完成
     * ORDER_SETTLE = "4";// 工单结算
     * ORDER_INVALID = "5";// 工单无效
     * APPOINT_CONFIRM = "6";// 预约单确认
     * APPOINT_C_CANCEL = "7";// C端预约单取消
     * APPOINT_B_CANCEL = "8";// B端预约单取消
     * APPOINT_WECHAT_CANCEL = "9";// 微信端预约单取消
     * @param orderInfo 工单消息
     * @return
     */
    @Override
    public boolean pushMsgToApp(String type, OrderInfo orderInfo) {
        try {
            Map<String, String> message = new HashMap<String, String>();
            message.put("orderId", String.valueOf(orderInfo.getId()));
            String customerMobile = orderInfo.getCustomerMobile();
            if (StringUtil.isNotStringEmpty(customerMobile)) {
                message.put("customerMobile", customerMobile);
            }
            String contactMobile = orderInfo.getContactMobile();
            if (StringUtil.isNotStringEmpty(contactMobile)) {
                message.put("contactMobile", contactMobile);
            }
            message.put("type", type);
            //推送到2C-APP
            if (Constants.ORDER_FINISH.equals(type) || type.equals(Constants.ORDER_SETTLE)) {
                message.put("url", "");
                return cAppMp.pushMsgToCApp(message);
            } else if (type.equals(Constants.ORDER_CREATE)) {
                return cAppMp.pushMsgToCApp(message);
            }
        } catch (Exception e) {
            log.error("推送消息异常:{},type类型:{}", e, type);
        }
        return false;
    }

    @Override
    public boolean pushMsgToDdlwechat(OrderInfo orderInfo) {
        try {
            Map<String, String> message = new HashMap<String, String>();
            message.put("orderId", String.valueOf(orderInfo.getId()));
            String customerMobile = orderInfo.getCustomerMobile();
            if (StringUtil.isNotStringEmpty(customerMobile)) {
                message.put("contactMobile", customerMobile);
            } else {
                Long customerId = orderInfo.getCustomerId();
                Customer customer = customerService.selectById(customerId);
                if (customer == null) {
                    log.error("无法推送消息,customerId为{}的客户不存在", customerId);
                }
                if (StringUtil.isStringEmpty(customer.getMobile())) {
                    log.error("无法推送消息,customerId为{}的客户手机号为空", customerId);
                    return false;
                }
                message.put("contactMobile", customer.getMobile());
            }
            message.put("type", "1");//工单结算
            message.put("orderAmount", String.valueOf(orderInfo.getPayAmount()));//应收金额
            message.put("carLicense", String.valueOf(orderInfo.getCarLicense()));//车牌号
            message.put("finishTime", new DateTime(orderInfo.getFinishTime() == null ? new Date() : orderInfo.getFinishTime()).toString("yyyy-MM-dd HH:mm:ss"));//完成时间
            message.put("orderTag", orderInfo.getOrderTag().toString());
            Shop shop = shopService.selectById(orderInfo.getShopId());
            if (null == shop) {
                log.error("无法推送消息,门店不存在,门店id为:{}", orderInfo.getShopId());
                return false;
            }
            message.put("shopName", shop.getName());//店铺名称
            message.put("shopAddress", shop.getAddress());//店铺地址
            message.put("shopTel", shop.getTel());//固话
            message.put("shopMobile", shop.getMobile());//手机号
            List<OrderServices> orderServices = orderServicesService.queryOrderServiceList(orderInfo.getId(), orderInfo.getShopId());
            if (!CollectionUtils.isEmpty(orderServices)) {
                StringBuilder orderServiceString = new StringBuilder();
                for (int i = 0; i < orderServices.size(); i++) {
                    String serviceName = orderServices.get(i).getServiceName();
                    if (i == 0) {
                        orderServiceString.append(serviceName);
                    } else {
                        orderServiceString.append(",").append(serviceName);
                    }
                }
                message.put("orderServices", orderServiceString.toString());//手机号
            }
            ddlWechatMp.pushMsgToDdlWechat(message, orderInfo.getShopId());
            return true;
        } catch (Exception e) {
            String errorMsg = "推送消息到微信门店端项目异常,工单号:{}" + orderInfo.getId();
            log.error(errorMsg, e);
        }
        return false;
    }
}
