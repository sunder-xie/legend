package com.tqmall.legend.biz.api.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.api.entity.OrderCountSearchVO;
import com.tqmall.legend.biz.api.IOrderApiService;
import com.tqmall.legend.biz.api.entity.PageRequstParam;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.IVirtualOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTrackService;
import com.tqmall.legend.biz.settlement.ISettlementService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServiceTypeEnum;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.entity.shop.ShopServiceTypeEnum;
import com.tqmall.legend.facade.order.OrderInvalidFacade;
import com.tqmall.legend.facade.order.OrderServicesFacade;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.object.param.order.OrderSearchParam;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 工单Api对外Service
 */
@Slf4j
@Service
public class OrderApiServiceImpl implements IOrderApiService {

    @Autowired
    ISettlementService settlementService;
    @Autowired
    IOrderService orderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerCarService customerCarService;
    @Autowired
    OrderGoodsService orderGoodsService;
    @Autowired
    WarehouseOutService warehouseOutService;
    @Autowired
    OrderServicesService orderSrvicesService;
    @Autowired
    ShopServiceInfoService shopServiceInfoService;
    @Autowired
    IVirtualOrderService virtualOrderService;
    @Autowired
    OrderTrackService orderTrackService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private OrderInvalidFacade orderInvalidFacade;
    @Autowired
    private OrderServicesFacade orderServicesFacade;

    /**
     * 从搜索或本地获取工单列表
     *
     * @param orderSearchParam
     */
    @Override
    public Result getOrderInfoListFromSearchOrLocal(OrderSearchParam orderSearchParam) {
        if (orderSearchParam == null) {
            log.error("[工单搜索] 工单搜索参数为空.");
            return LegendErrorCode.APP_ORDER_SEARCH_ERROR.newResult();
        }
        log.info("[工单查询] 搜索门店工单 [主要参数]shopId={},likeKeyWords={},contactLikeKeyWords={},orderTags={}",
                orderSearchParam.getShopId(), orderSearchParam.getLikeKeyWords(), orderSearchParam.getContactLikeKeyWords(), orderSearchParam.getOrderTag());

        if (orderSearchParam.getShopId() == null || orderSearchParam.getShopId() < 1) {
            log.error("[工单搜索] 工单搜索shopId参数错误. shopId:{}", orderSearchParam.getShopId());
            return LegendErrorCode.APP_SHOP_ID_ERROR.newResult();
        }

        LegendOrderRequest orderRequest = getLegendOrderRequest(orderSearchParam);
        PageableRequest pageableRequest = getPageableRequest(orderSearchParam);

        DefaultPage<OrderInfoVo> orderInfoVos = orderServicesFacade.getOrderListFromSearch(pageableRequest, orderRequest);
        return Result.wrapSuccessfulResult(orderInfoVos);
    }

    /**
     * 从搜索获取对应的工单数
     *
     * @param searchParam
     * @return
     */
    @Override
    public Result<Map<String, Long>> getOrderCount(List<OrderCountSearchVO> searchParam) {
        Map<String, Long> result = orderServicesFacade.getOrderCountFromSearchToApp(searchParam);
        return Result.wrapSuccessfulResult(result);
    }

    @Override
    public Result getOrder(Long orderId, Long shopId) {

        // check order exsit
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return LegendErrorCode.APP_ORDER_NOTEXSIT_ERROR.newResult();
        }

        // 客户信息
        OrderInfo orderInfo = orderInfoOptional.get();
        Optional<Customer> customerOptional = customerService.getCustomer(orderInfo.getCustomerId());
        if (customerOptional.isPresent()) {
            orderInfo.setCustomer(customerOptional.get());
        }

        // 客户车辆
        Optional<CustomerCar> customerCarOptional = customerCarService.getCustomerCar(orderInfo.getCustomerCarId());
        if (customerCarOptional.isPresent()) {
            CustomerCar customerCar = customerCarOptional.get();
            orderInfo.setCustomerCar(customerCarOptional.get());
            orderInfo.setCarGearBox(customerCar.getCarGearBox());
            orderInfo.setCarGearBoxId(customerCar.getCarGearBoxId());
        }

        // 实开物料
        List<OrderGoods> realOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        if (!CollectionUtils.isEmpty(realOrderGoodsList)) {
            for (OrderGoods orderGoods : realOrderGoodsList) {
                orderGoods.setOutNumber(warehouseOutService.countOutGoods(orderGoods.getGoodsId(), orderGoods.getShopId(), orderGoods.getOrderId(), orderGoods.getId()));
            }
        }
        orderInfo.setGoodsList(realOrderGoodsList);

        //虚开物料
        List<OrderGoods> virtualOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.VIRTUAL);
        if (!CollectionUtils.isEmpty(virtualOrderGoodsList)) {
            for (OrderGoods orderGoods : virtualOrderGoodsList) {
                orderGoods.setOutNumber(orderGoods.getGoodsNumber());
            }
        }
        orderInfo.setVirtualGoodsList(virtualOrderGoodsList);

        // 工单基本服务
        orderInfo.setServiceList(orderSrvicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC));
        // 附加服务
        List<OrderServices> additionalServiceList = orderSrvicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.ANCILLARY);
        // 管理费
        if (!CollectionUtils.isEmpty(additionalServiceList)) {
            for (OrderServices orderServices : additionalServiceList) {
                if (orderServices.getFlags() != null && orderServices.getFlags().contains(Constants.GLF)) {
                    List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.queryShopServiceList(shopId, ShopServiceTypeEnum.OTHER, Constants.GLF);
                    if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                        ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
                        BigDecimal price = shopServiceInfo.getServicePrice();
                        orderServices.setManageRate(price);
                    }
                }
            }
        }
        orderInfo.setAdditionalServiceList(additionalServiceList);
        return Result.wrapSuccessfulResult(orderInfo);
    }

    @Override
    public Result invalidOrder(@NotNull Long orderId, @NotNull Long userId) {

        // TODO 提取工单接口 获取当前操作用户
        ShopManager shopManager = userInfoService.getUserInfo(userId);
        if (shopManager == null) {
            return LegendErrorCode.SHOPMANAGER_IS_NOT_EXISITE.newResult();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(shopManager.getId());
        userInfo.setName(shopManager.getName());
        Long shopId = shopManager.getShopId();
        userInfo.setShopId(shopManager.getShopId());

        // check order exsit
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return LegendErrorCode.APP_ORDER_NOTEXSIT_ERROR.newResult();
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        if (OrderStatusEnum.WXDD.getKey().equals(orderInfo.getOrderStatus())) {
            return LegendErrorCode.APP_ORDER_INVALID_ERROR.newResult("工单已经无效,无需重复无效!");
        }
        try {
            com.tqmall.legend.common.Result invalidResult = orderInvalidFacade.invalid(orderInfo, userInfo);
            if (!invalidResult.isSuccess()) {
                return LegendErrorCode.APP_ORDER_INVALID_ERROR.newResult(invalidResult.getErrorMsg());
            }
            log.info("工单无效成功 工单ID:{}", orderId);
            return Result.wrapSuccessfulResult("工单无效成功");
        } catch (Exception e) {
            log.error("工单无效 工单ID:{} 异常信息:{}", orderId, e.getMessage());
            return LegendErrorCode.APP_ORDER_INVALID_ERROR.newResult("工单无效失败");
        }
    }


    private PageableRequest getPageableRequest(OrderSearchParam orderSearchParam) {
        PageRequstParam pageRequstParam = new PageRequstParam();
        //页数
        if (orderSearchParam.getPageNum() != null && orderSearchParam.getPageNum() > 1) {
            pageRequstParam.setPageNum(orderSearchParam.getPageNum() - 1);
        }
        //每页大小
        if (orderSearchParam.getPageSize() != null && orderSearchParam.getPageSize() > 1) {
            pageRequstParam.setPageSize(orderSearchParam.getPageSize());
        }
        //排序对象
        if (StringUtils.isNotBlank(orderSearchParam.getSortBy())) {
            pageRequstParam.setSortBy(orderSearchParam.getSortBy());
        }
        //排序类型
        if (!StringUtils.equals(orderSearchParam.getSortType(), "asc") && !StringUtils.equals(orderSearchParam.getSortType(), "desc")) {
            pageRequstParam.setSortType("desc");
        } else {
            pageRequstParam.setSortType(orderSearchParam.getSortType());
        }

        return new PageableRequest(pageRequstParam.getPageNum(), pageRequstParam.getPageSize(), Sort.Direction.fromString(pageRequstParam.getSortType()), new String[]{pageRequstParam.getSortBy()});
    }

    private LegendOrderRequest getLegendOrderRequest(OrderSearchParam orderSearchParam) {
        LegendOrderRequest orderRequest = new LegendOrderRequest();
        if (orderSearchParam.getShopId() != null && orderSearchParam.getShopId() > 0) {
            orderRequest.setShopId(orderSearchParam.getShopId().toString());
        }
        //联系人电话
        if (StringUtils.isNotBlank(orderSearchParam.getMobileLike())) {
            orderRequest.setMobileLike(orderSearchParam.getMobileLike());
        }
        //服务顾问
        if (orderSearchParam.getReceiver() != null && orderSearchParam.getReceiver() > 0) {
            orderRequest.setReceiver(orderSearchParam.getReceiver().toString());
        }
        //工单类型
        if (orderSearchParam.getOrderTag() != null && orderSearchParam.getOrderTag().length > 0) {
            List<Integer> orderTags = Lists.newArrayList(orderSearchParam.getOrderTag());
            orderRequest.setOrderTag(orderTags);
        }
        if (orderSearchParam.getProxyType() != null && orderSearchParam.getProxyType().length > 0) {
            List<Integer> proxyTypes = Lists.newArrayList(orderSearchParam.getProxyType());
            orderRequest.setProxyType(proxyTypes);
        }
        //业务类型
        if (orderSearchParam.getOrderType() != null && orderSearchParam.getOrderType() > 0) {
            List<String> orderTypes = Lists.newArrayList();
            orderTypes.add(orderSearchParam.getOrderType().toString());
            orderRequest.setOrderType(orderTypes);
        }
        //公司
        if (StringUtils.isNotBlank(orderSearchParam.getCompany())) {
            orderRequest.setCompany(orderSearchParam.getCompany());
        }
        //工单创建开始时间
        if (StringUtils.isNotBlank(orderSearchParam.getStartTime())) {
            orderRequest.setStartTime(orderSearchParam.getStartTime());
        }
        //工单创建结束时间
        if (StringUtils.isNotBlank(orderSearchParam.getEndTime())) {
            orderRequest.setEndTime(orderSearchParam.getEndTime());
        }
        //付款开始时间
        if (StringUtils.isNotBlank(orderSearchParam.getPayStartTime())) {
            orderRequest.setPayStartTime(orderSearchParam.getPayStartTime());
        }
        //付款结束时间
        if (StringUtils.isNotBlank(orderSearchParam.getPayEndTime())) {
            orderRequest.setPayEndTime(orderSearchParam.getPayEndTime());
        }
        //工单确认开始、结束时间
        if (StringUtils.isNotBlank(orderSearchParam.getConfirmStartTime())) {
            orderRequest.setConfirmStartTime(orderSearchParam.getConfirmStartTime());
        }
        if (StringUtils.isNotBlank(orderSearchParam.getConfirmEndTime())) {
            orderRequest.setConfirmEndTime(orderSearchParam.getConfirmEndTime());
        }
        //客户列表
        if (null != orderSearchParam.getCustomerIds() && orderSearchParam.getCustomerIds().length > 0) {
            Long[] customerIds = orderSearchParam.getCustomerIds();
            StringBuffer str2 = new StringBuffer();
            for (int i = 0; i < customerIds.length; i++) {
                if (i == customerIds.length - 1) {
                    str2.append(customerIds[i]);
                } else {
                    str2.append(customerIds[i] + ",");
                }
            }
            orderRequest.setCustomerIds(str2.toString());
        }
        //客户列表
        if (null != orderSearchParam.getCustomerCarIds() && orderSearchParam.getCustomerCarIds().length > 0) {
            List<Integer> customerCarIds = Lists.newArrayList();
            Long[] c = orderSearchParam.getCustomerCarIds();
            for (Long aLong : c) {
                customerCarIds.add(aLong.intValue());
            }
            orderRequest.setCustomerCarId(customerCarIds);
        }
        //工单状态列表
        if (null != orderSearchParam.getOrderStatus() && orderSearchParam.getOrderStatus().length > 0) {
            String[] orderStatus = orderSearchParam.getOrderStatus();
            StringBuffer str2 = new StringBuffer();
            for (int i = 0; i < orderStatus.length; i++) {
                if (i == orderStatus.length - 1) {
                    str2.append(orderStatus[i]);
                } else {
                    str2.append(orderStatus[i] + ",");
                }
            }
            orderRequest.setOrderStatus(str2.toString());
        }
        //工单结算状态列表
        if (null != orderSearchParam.getPayStatus() && orderSearchParam.getPayStatus().length > 0) {
            List<Integer> payStatus = Lists.newArrayList(orderSearchParam.getPayStatus());
            orderRequest.setPayStatus(payStatus);
        }
        // 查找车牌号、customer_mobile手机号、工单号、customer_name车主
        if (StringUtils.isNotBlank(orderSearchParam.getLikeKeyWords())) {
            orderRequest.setLikeKeyWords(orderSearchParam.getLikeKeyWords());
        }
        // 查找车牌号、contact_mobile手机号、工单号、contact_name车主
        if (!StringUtils.isBlank(orderSearchParam.getContactLikeKeyWords())) {
            orderRequest.setContactLikeKeyWords(orderSearchParam.getContactLikeKeyWords());
        }
        //是否回访过
        if (null != orderSearchParam.getIsVisit()) {
            if (orderSearchParam.getIsVisit() == 0 || orderSearchParam.getIsVisit() == 1) {
                orderRequest.setIsVisit(orderSearchParam.getIsVisit());
            }
        }
        return orderRequest;
    }
}
