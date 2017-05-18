package com.tqmall.legend.facade.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.billcenter.client.enums.DebitTypeEnum;
import com.tqmall.legend.billcenter.client.param.DebitInvalidParam;
import com.tqmall.legend.biz.bo.dandelion.TaoqiCouponParam;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.order.OrderDiscountFlowService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTrackService;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.remote.dandelion.RemoteCouponHttp;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderDiscountFlow;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.order.PayStatusEnum;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.enums.order.OrderDiscountTypeEnum;
import com.tqmall.legend.enums.order.OrderProxyTypeEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.order.OrderInvalidFacade;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;

/**
 * Created by xin on 16/4/25.
 */
@Service
@Slf4j
public class OrderInvalidFacadeImpl implements OrderInvalidFacade {

    @Autowired
    private WarehouseOutService warehouseOutService;
    @Autowired
    private OrderTrackService orderTrackService;
    @Autowired
    private RpcDebitBillService rpcDebitBillService;
    @Autowired
    private AccountFacadeService accountFacadeService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private RemoteCouponHttp remoteCouponHttp;
    @Autowired
    private OrderDiscountFlowService orderDiscountFlowService;
    @Autowired
    private IInsuranceBillService iInsuranceBillService;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private OrderInfoService orderInfoService;

    private Gson gson = new Gson();

    /**
     * 工单无效
     * 步骤:
     * 1.调用账单中心无效方法, 插负的收款单和收款流水
     * 2.删payment_log表
     * 3.回退会员流水
     * 4.回退淘汽优惠券
     * 5.判断工单是否有物料,如果有则退回
     * 3.工单状态流转
     *
     * @param orderInfo
     * @param userInfo
     * @return
     */
    @Override
    @Transactional
    public Result invalid(OrderInfo orderInfo, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        Long orderId = orderInfo.getId();
        String orderStatus = orderInfo.getOrderStatus();
        //参加体系的门店查询委托单信息
        if(checkProxyInvalid(shopId, orderInfo)){
            return Result.wrapErrorResult("", "有关联的委托单，请先无效委托单");
        }
        if(orderStatus.equals(OrderStatusEnum.DDYFK.getKey())) {
            // 调用账单无效(全额冲红)
            com.tqmall.core.common.entity.Result<Boolean> invalidResult = debitInvalid(orderInfo, userInfo);
            if (!invalidResult.isSuccess()) {
                log.error("[工单无效]工单全额冲红失败,工单ID: {}, 错误信息: {} ", orderId, invalidResult.getMessage());
                throw new RuntimeException("[工单无效]工单全额冲红失败" + invalidResult.getMessage());
            }
            // 全额冲红是否成功
            Boolean invalidResultData = invalidResult.getData();
            log.info("[工单无效]工单全额冲红 成功");

            // 回退会员消费流水
            if (invalidResultData != null && invalidResultData) {
                revertAccountSettlement(shopId, orderId, userId);
                log.info("[工单无效]工单退回会员消费流水 成功");
            }

            // 回退淘汽优惠券
            boolean revertTaoqiCouponIsSuccess = revertTaoqiCoupon(shopId, orderInfo);
            if (!revertTaoqiCouponIsSuccess) {
                log.error("[工单无效]回退淘汽优惠券异常,工单ID: {}", orderId);
                throw new RuntimeException("回退淘汽优惠券异常");
            }
            log.info("[工单无效]淘汽优惠券作废、重新发放成功");
            //更新工单优惠流水，valid_status = 0（无效）
            orderDiscountFlowService.batchUpdateAuditStatusByOrderId(orderId);
            log.info("[工单无效]更新优惠流水（order_discount_flow）为无效状态，shopuserId：{}orderId为：{}", orderId);
        }

        Result result = null;
        // 物料出库单无效
        try {
            result = warehouseOutService.invalid(orderId, userInfo);
            log.info("[工单无效]出库单无效: 工单号为:{} 操作人:{}", orderId, userId);
        } catch (Exception e) {
            log.error("[工单无效]出库单无效失败", e);
            throw new RuntimeException("出库单无效失败");
        }
        if (!result.isSuccess()) {
            throw new RuntimeException("出库单无效失败");
        }
        // 如果存在对应的报销单，则删除
        Result insuranceBillResult = iInsuranceBillService.deleteBillByOrderId(orderId, userInfo);
        if(insuranceBillResult == null || !insuranceBillResult.isSuccess()){
            throw new RuntimeException("报销单删除失败");
        }
        // 工单状态变更
        result = invalidOrder(orderInfo, userInfo);
        // 判断'无效'是否成功
        if (!result.isSuccess()) {
            log.error("[工单无效]出库单无效失败, {}", result.getErrorMsg());
            throw new RuntimeException("工单无效失败");
        }
        //添加工单详情日志start
        StringBuffer orderLog = new StringBuffer("工单无效成功: 工单号为:");
        orderLog.append(orderInfo.getId());
        orderLog.append(" 操作人:");
        orderLog.append(userInfo.getUserId());
        String operationLog = OrderOperationLog.getOperationLog(orderInfo,orderLog);
        log.info(operationLog);
        //添加工单详情日志end
        return result;
    }

    @Override
    public boolean proxyInvalid(Long orderId) {
        OrderInfo orderInfo = orderInfoService.selectById(orderId);
        if (orderInfo == null) {
            throw new BizException("工单不存在");
        }
        if(OrderStatusEnum.WXDD.getKey().equals(orderInfo.getOrderStatus())) {
            throw new BizException("工单已经无效,无需重复无效!");
        }
        if(!orderInfo.getProxyType().equals(2)){
            throw new BizException("工单不是委托单转的工单，无法无效");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(orderInfo.getShopId());
        com.tqmall.legend.common.Result result = invalid(orderInfo, userInfo);
        if (result.isSuccess()) {
            log.info("工单无效成功: 工单号为:{}", orderInfo.getId());
            return true;
        }
        throw new BizException(result.getErrorMsg());
    }

    private boolean checkProxyInvalid(Long shopId, OrderInfo orderInfo) {
        Long orderId = orderInfo.getId();
        Integer proxyType = orderInfo.getProxyType();
        if(OrderProxyTypeEnum.ST.getCode().equals(proxyType)){
            log.info("【钣喷中心无效受托工单，查询委托单】：门店id：{}，工单id：{}", shopId, orderId);
            com.tqmall.core.common.entity.Result<ProxyDTO>  proxyDTOResult = null;
            try {
                proxyDTOResult = rpcProxyService.getProxyInfoByShopIdAndProxyOrderId(shopId, orderId);
            } catch (Exception e) {
                log.error("【钣喷中心无效受托工单，查询委托单】出现异常",e);
                throw new RuntimeException("查询委托单失败");
            }
            //有委托单信息，则判断状态，没有可以正常无效
            if(proxyDTOResult != null && proxyDTOResult.isSuccess()){
                //有委托单，委托单无效才能无效
                ProxyDTO proxyDTO = proxyDTOResult.getData();
                String proxyStatus = proxyDTO.getProxyStatus();
                if(!proxyStatus.equals(ProxyStatusEnum.YQX.getCode())){
                    return true;
                }
            }
        }else if(OrderProxyTypeEnum.WT.getCode().equals(proxyType)){
            //查询工单委托的所有委托单
            ProxyParam proxyParam = new ProxyParam();
            proxyParam.setOrderId(orderId);
            proxyParam.setShopId(shopId);
            log.info("【委托方无效工单，查询委托单】：门店id：{}，工单id：{}", shopId, orderId);
            com.tqmall.core.common.entity.Result<List<ProxyDTO>>  rpcProxyServiceProxyList = null;
            try {
                rpcProxyServiceProxyList = rpcProxyService.showProxyList(proxyParam);
            } catch (Exception e) {
                log.error("【委托方无效工单，查询委托单】出现异常", e);
                throw new RuntimeException("查询委托单失败");
            }
            if(rpcProxyServiceProxyList != null && rpcProxyServiceProxyList.isSuccess()){
                boolean flags = false;
                for(ProxyDTO proxyDTO : rpcProxyServiceProxyList.getData()){
                    String proxyStatus = proxyDTO.getProxyStatus();
                    if(!proxyStatus.equals(ProxyStatusEnum.YQX.getCode())){
                        flags = true;
                    }
                }
                if(flags){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 账单无效(全额冲红)
     *
     * @param orderInfo
     * @param userInfo
     * @return
     */
    private com.tqmall.core.common.entity.Result<Boolean> debitInvalid(OrderInfo orderInfo, UserInfo userInfo) {
        com.tqmall.core.common.entity.Result<Boolean> invalidResult = null;
        try {
            DebitInvalidParam param = new DebitInvalidParam();
            param.setShopId(userInfo.getShopId());
            param.setBillName("工单号".concat(orderInfo.getOrderSn()).concat("(无效)"));
            param.setRelId(orderInfo.getId());
            param.setDebitTypeId(DebitTypeEnum.ORDER.getId());
            param.setCreator(userInfo.getUserId());
            param.setOperatorName(userInfo.getName());
            log.info("工单无效-调用账单中心全额冲红, 参数: param: {}", gson.toJson(param));
            invalidResult = rpcDebitBillService.invalid(param);
            log.info("工单无效-调用账单中心全额冲红, 返回值: result: {}", gson.toJson(invalidResult));
        } catch (Exception e) {
            log.error("[工单无效]工单全额冲红失败,工单ID:{}", orderInfo.getId());
            throw new RuntimeException("工单全额冲红失败", e);
        }
        return invalidResult;
    }

    /**
     * 回退会员消费流水
     *
     * @param shopId
     * @param orderId
     * @param userId
     */
    private void revertAccountSettlement(Long shopId, Long orderId, Long userId) {
        com.tqmall.core.common.entity.Result<DebitBillDTO> debitBillDTOResult = null;
        try {
            log.info("工单无效-调用账单中心查询工单对应的收款单, 参数: shopId: {}, debitTypeId: {}, relId: {}", shopId, DebitTypeEnum.ORDER.getId(), orderId);
            debitBillDTOResult = rpcDebitBillService.findBillByRelId(shopId, DebitTypeEnum.ORDER.getId(), orderId);
            log.info("工单无效-调用账单中心查询工单对应的收款单, 返回为: {}", gson.toJson(debitBillDTOResult));
        } catch (Exception e) {
            StringBuffer errorMag = new StringBuffer();
            errorMag.append("[工单无效]调用结算中心,获取工单实付金额失败, 工单ID:");
            errorMag.append(orderId);
            errorMag.append("原因:");
            log.error(errorMag.toString(), e);
            throw new RuntimeException("调用结算中心,获取工单实付金额失败");
        }

        if (debitBillDTOResult != null && debitBillDTOResult.isSuccess()) {
            try {
                accountFacadeService.revertSettlement(shopId, userId, orderId);
            } catch (BizException e){
                //业务异常。可以正常无效工单
                StringBuffer errorMag = new StringBuffer();
                errorMag.append("[工单无效]工单退回会员消费流水,抛业务异常, 工单ID:");
                errorMag.append(orderId);
                log.error(errorMag.toString(), e);
            } catch (Exception e) {
                StringBuffer errorMag = new StringBuffer();
                errorMag.append("[工单无效]工单退回会员消费流水失败, 工单ID:");
                errorMag.append(orderId);
                errorMag.append("原因:");
                log.error(errorMag.toString(), e);
                throw new RuntimeException("工单退回会员消费流水 失败");
            }
        }
    }

    /**
     * 重新结算淘汽优惠券
     * 告诉2CAPP 优惠券已作废，需要重新发一张优惠券
     *
     * @param orderInfo
     * @return
     */
    private boolean revertTaoqiCoupon(Long shopId, OrderInfo orderInfo) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Long orderId = orderInfo.getId();
        searchMap.put("orderId", orderId);
        searchMap.put("shopId", shopId);
        searchMap.put("discountType", OrderDiscountTypeEnum.TAOQICOUPON.getCode());
        List<OrderDiscountFlow> flowList = orderDiscountFlowService.select(searchMap);
        if (!CollectionUtils.isEmpty(flowList)) {
            Shop shop = shopService.selectById(shopId);
            List<String> itemIds = orderServicesService.getTaoqiFirstCateIds(orderId);
            if (CollectionUtils.isEmpty(itemIds)) {
                itemIds = new ArrayList<>();
                itemIds.add("0");
            }
            for (OrderDiscountFlow flow : flowList) {
                BigDecimal taoqiCouponAmount = flow.getDiscountAmount();
                if (taoqiCouponAmount != null && taoqiCouponAmount.compareTo(BigDecimal.ZERO) > 0) {
                    String taoqiCouponSn = flow.getDiscountSn();
                    TaoqiCouponParam taoqiCouponParam = new TaoqiCouponParam();
                    String orderSn = orderInfo.getOrderSn();
                    String carLicense = orderInfo.getCarLicense();
                    taoqiCouponParam.setMobile(orderInfo.getContactMobile());
                    taoqiCouponParam.setLicense(carLicense);
                    taoqiCouponParam.setCouponCode(taoqiCouponSn);
                    taoqiCouponParam.setSaId(orderInfo.getReceiver() + "");
                    taoqiCouponParam.setSaName(orderInfo.getReceiverName());
                    taoqiCouponParam.setSaPhone("");
                    taoqiCouponParam.setSettleTime(DateUtil.convertDateToYMDHMS(new Date()));
                    taoqiCouponParam.setShopId(shopId);
                    if(shop != null) {
                        taoqiCouponParam.setShopPhone(shop.getTel());
                    }
                    taoqiCouponParam.setWorkOrderId(orderId);
                    taoqiCouponParam.setItemIds(itemIds);
                    Map resultMap = remoteCouponHttp.couponResettle(taoqiCouponParam);
                    if (resultMap == null || !(Boolean) resultMap.get("success")) {
                        log.error("工单号:{} 车牌:{} 使用优惠券:{} 金额:{} 重新结算淘汽优惠券异常", orderSn, carLicense, taoqiCouponSn, taoqiCouponAmount);
                        return Boolean.FALSE;
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 工单无效状态变更
     *
     * @param orderInfo
     * @param userInfo
     * @return
     */
    private Result invalidOrder(OrderInfo orderInfo, UserInfo userInfo) {
        orderInfo.setOrderStatus(OrderStatusEnum.WXDD.getKey());
        orderInfo.setPayStatus(PayStatusEnum.UNPAY.getCode());
        orderInfo.setSignAmount(BigDecimal.ZERO);
        orderInfo.setPayAmount(BigDecimal.ZERO);
        orderInfo.setGmtModified(new Date());
        orderInfo.setModifier(userInfo.getUserId());
        return orderTrackService.track(orderInfo, userInfo);
    }
}
