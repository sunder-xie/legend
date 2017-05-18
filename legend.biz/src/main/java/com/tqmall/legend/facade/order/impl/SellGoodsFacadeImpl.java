package com.tqmall.legend.facade.order.impl;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.BdUtil;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerPerfectOfCarWashEntity;
import com.tqmall.legend.entity.order.OrderFormEntityBo;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.order.SellGoodsFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Service
public class SellGoodsFacadeImpl implements SellGoodsFacade {


    @Autowired
    CustomerCarService customerCarService;
    @Autowired
    IOrderService orderService;
    @Autowired
    OrderGoodsService orderGoodsService;
    @Autowired
    WarehouseOutService warehouseOutService;
    @Autowired
    private ShopManagerService shopManagerService;


    @Override
    public OrderFormEntityBo wrapperFormBody(Long customerCarId, Long orderId, UserInfo userInfo) {

        Long shopId = userInfo.getShopId();

        // 页面表单实体
        OrderFormEntityBo formEntity = new OrderFormEntityBo();
        // 工单基本信息
        OrderInfo orderInfo = new OrderInfo();
        // 物料列表
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();

        // IF 客户存在 THEN 封装客户信息
        if (customerCarId != null && customerCarId > 0l) {
            wrapperCustomerOfOrder(customerCarId, shopId, orderInfo);
        }

        // IF 工单存在 THEN 封装工单信息
        if (orderId != null && orderId > 0l) {
            Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
            if (orderInfoOptional.isPresent()) {
                orderInfo = orderInfoOptional.get();
            }

            // 包装客户信息
            customerCarId = orderInfo.getCustomerCarId();
            wrapperCustomerOfOrder(customerCarId, shopId, orderInfo);

            // 实开物料列表
            List<OrderGoods> realOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
            if (!CollectionUtils.isEmpty(realOrderGoodsList)) {
                Set<Long> saleIdSet = new HashSet();
                for (OrderGoods orderGoods : realOrderGoodsList) {
                    orderGoods.setOutNumber(warehouseOutService.countOutGoods(orderGoods.getGoodsId(), orderGoods.getShopId(), orderGoods.getOrderId(), orderGoods.getId()));
                    saleIdSet.add(orderGoods.getSaleId());
                }
                //设置销售员
                if (!CollectionUtils.isEmpty(saleIdSet)) {
                    Long[] saleIdArray = new Long[saleIdSet.size()];
                    saleIdArray = saleIdSet.toArray(saleIdArray);
                    //查询多个销售员
                    List<ShopManager> shopManagerList = shopManagerService.selectByIds(saleIdArray);
                    Map<Long, String> shopManagerMap = new HashMap(shopManagerList.size());
                    for (ShopManager shopManager : shopManagerList) {
                        shopManagerMap.put(shopManager.getId(), shopManager.getName());
                    }

                    for (OrderGoods orderGoods : realOrderGoodsList) {
                        Long saleId = orderGoods.getSaleId();
                        if(shopManagerMap.containsKey(saleId)){
                            String saleName = shopManagerMap.get(saleId);
                            orderGoods.setSaleName(saleName);
                        }
                    }
                }
            }
            orderGoodsList = realOrderGoodsList;
        }

        formEntity.setOrderInfo(orderInfo);
        formEntity.setOrderGoodsList(orderGoodsList);

        // TODO 包装表单实体
        return formEntity;
    }

    /**
     * wrapper customer OF order
     *
     * @param customerCarId
     * @param shopId
     * @param orderInfo
     */
    private void wrapperCustomerOfOrder(Long customerCarId, Long shopId, OrderInfo orderInfo) {
        Result<CustomerPerfectOfCarWashEntity> customerResult = customerCarService.selectCustomerCar(shopId, customerCarId);
        if (customerResult.isSuccess()) {
            CustomerPerfectOfCarWashEntity customer = customerResult.getData();
            // 车辆ID
            orderInfo.setCustomerCarId(customerCarId);
            // 车牌号
            orderInfo.setCarLicense(customer.getLicense());
            // 联系人姓名
            orderInfo.setContactName(customer.getContactName());
            // 联系人电话
            orderInfo.setContactMobile(customer.getContactMobile());
            // 客户单位
            orderInfo.setCompany(customer.getCompany());
            // 车架号VIN
            orderInfo.setVin(customer.getVin());
            // 行驶里程
            orderInfo.setMileage(customer.getMileage() + "");
        }
    }


    @Override
    public Object wrapperFormBody(Long copyOrderId, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();

        // 页面表单实体
        OrderFormEntityBo formEntity = new OrderFormEntityBo();
        // 工单基本信息
        OrderInfo newOrderInfo = new OrderInfo();
        // 物料列表
        List<OrderGoods> orderGoodsList = null;

        // IF 工单存在 THEN 封装工单信息
        if (copyOrderId != null && copyOrderId > 0l) {
            Optional<OrderInfo> orderInfoOptional = orderService.getOrder(copyOrderId, shopId);
            if (orderInfoOptional.isPresent()) {
                // 包装客户信息
                OrderInfo oldOrderInfo =orderInfoOptional.get();
                newOrderInfo = BdUtil.bo2do(oldOrderInfo, OrderInfo.class);
                newOrderInfo.setId(null);
                // 实开物料列表
                List<OrderGoods> realOrderGoodsList = orderGoodsService.queryOrderGoodList(copyOrderId, shopId, OrderGoodTypeEnum.ACTUAL);
                if (!CollectionUtils.isEmpty(realOrderGoodsList)) {
                    for (OrderGoods orderGoods : realOrderGoodsList) {
                        // 工单ID=空|goodsNumber=1|goodsAmount=0|discount=0
                        orderGoods.setId(null);
                        orderGoods.setGoodsNumber(BigDecimal.ONE);
                        orderGoods.setGoodsAmount(BigDecimal.ZERO);
                        orderGoods.setDiscount(BigDecimal.ZERO);
                        orderGoods.setOutNumber(warehouseOutService.countOutGoods(
                                orderGoods.getGoodsId(),
                                orderGoods.getShopId(),
                                orderGoods.getOrderId(),
                                orderGoods.getId()));
                    }
                }

                orderGoodsList = realOrderGoodsList;
            }
        }

        formEntity.setOrderInfo(newOrderInfo);
        formEntity.setOrderGoodsList(orderGoodsList);

        return formEntity;
    }
}
