package com.tqmall.legend.facade.settlement.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.error.LegendSettlementErrorCode;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.message.MQPushMessageService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.bo.ConfirmBillBo;
import com.tqmall.legend.biz.order.bo.DebitBillBo;
import com.tqmall.legend.biz.order.bo.PayChannelBo;
import com.tqmall.legend.biz.order.bo.SpeedilyBillBo;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.settlement.ConfirmBillService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.enums.order.OrderProxyTypeEnum;
import com.tqmall.legend.facade.magic.BoardFacade;
import com.tqmall.legend.facade.settlement.ConfirmBillFacade;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowBo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowSaveBo;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by zsy on 16/6/4.
 */
@Slf4j
@Service
public class ConfirmBillFacadeImpl implements ConfirmBillFacade {
    @Autowired
    private ConfirmBillService confirmBillService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private WarehouseOutService warehouseOutService;
    @Autowired
    private DebitFacade debitFacade;
    @Autowired
    private MQPushMessageService mqPushMessageService;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private BoardFacade boardFacade;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CustomerCarService customerCarService;

    /**
     * 另开线程处理其它业务
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    @Override
    public Result<DebitBillDTO> confirmBill(ConfirmBillBo confirmBillBo, UserInfo userInfo) {
        if (confirmBillBo == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), LegendSettlementErrorCode.PARAMS_ERROR.getErrorMessage());
        }
        DebitBillBo debitBillBo = confirmBillBo.getDebitBillBo();
        if (debitBillBo == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), LegendSettlementErrorCode.PARAMS_ERROR.getErrorMessage());
        }
        Long orderId = debitBillBo.getRelId();
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.ORDER_NOTEXSIT_ERROR.getCode(), LegendSettlementErrorCode.ORDER_NOTEXSIT_ERROR.getErrorMessage());
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        //受托工单无需确认账单
        if(OrderProxyTypeEnum.ST.getCode().equals(orderInfo.getProxyType())){
            return Result.wrapErrorResult(LegendSettlementErrorCode.CONFIRM_ERROR.getCode(), LegendSettlementErrorCode.CONFIRM_ERROR.getErrorMessage());
        }
        Long shopId = orderInfo.getShopId();
        //确认对账
        log.info("【工单确认账单】：门店id：{},优惠信息：{},工单信息：{}", shopId, confirmBillBo, orderInfo);
        Result<DebitBillDTO> result = confirmBillService.confirmBill(confirmBillBo, orderInfo, userInfo);
        if (result.isSuccess()) {
            //确认账单其他事情
            confirmBillDoOtherThings(orderId, orderInfo, shopId);
        }
        return result;
    }

    /**
     * 确认账单其他事情
     * @param orderId
     * @param orderInfo
     * @param shopId
     */
    private void confirmBillDoOtherThings(Long orderId, OrderInfo orderInfo, Long shopId) {
        //.发送工单完工消息到ddl-wechat
        mqPushMessageService.pushMsgToDdlwechat(orderInfo);

        //发消息给钣喷中心，刷新车间看板完工车辆的缓存
        if(shopFunFacade.isUseWorkshop(shopId)){
            boardFacade.sendMessage(shopId, Lists.newArrayList(orderId));
        }
        //另开线程打印日志
        orderConfirmExtThread(orderInfo);
    }

    /**
     * 打印特殊日志，报表用
     */
    private void orderConfirmExtThread(final OrderInfo orderInfo) {
        if(orderInfo == null){
            return;
        }
        try {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Long orderId = orderInfo.getId();
                    Long shopId = orderInfo.getShopId();
                    //查询车辆
                    Long customerCarId = orderInfo.getCustomerCarId();
                    CustomerCar customerCar = customerCarService.selectById(customerCarId);
                    if (customerCar == null) {
                        return;
                    }
                    String carBrand = customerCar.getCarBrand();
                    String carSeries = customerCar.getCarSeries();
                    //品牌、系列为空不打日志
                    if (StringUtils.isBlank(carBrand) || StringUtils.isBlank(carSeries)) {
                        return;
                    }
                    List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId);
                    if (CollectionUtils.isEmpty(orderGoodsList)) {
                        return;
                    }
                    //如果工单配件包含指定配件，则打印日志(去除重复的配件)
                    Map<Long,Goods> goodsMap = Maps.newHashMap();
                    for (OrderGoods orderGoods : orderGoodsList) {
                        Goods goods = checkName(orderGoods);
                        if (goods != null && !goodsMap.containsKey(goods.getId())) {
                            Long goodsId = goods.getId();
                            goodsMap.put(goodsId, goods);
                        }
                    }
                    for (Goods goods : goodsMap.values()) {
                        String carInfoLog = OrderOperationLog.getCarInfoLog(orderInfo, customerCar, goods);
                        log.info(carInfoLog);
                    }
                }
            });
        } catch (Exception e) {
            log.error("【确认账单另起线程处理报表日志规则打印】：出现异常",e);
        }
    }


    @Override
    @Transactional
    public Result speedilyDrawUp(SpeedilyBillBo drawUpBo,OrderInfo orderInfo, UserInfo userInfo) {
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        Long orderId = orderInfo.getId();
        // 工单
        //如果有预付定金，则设置预付定金
        DebitBillBo debitBillBo = drawUpBo.getDebitBillBo();
        List<PayChannelBo> payChannelBoList = drawUpBo.getPayChannelBoList();
        List<DebitBillFlowBo> flowList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(payChannelBoList)) {
            for (PayChannelBo channelBo : payChannelBoList) {
                DebitBillFlowBo flowBo = new DebitBillFlowBo();
                flowBo.setPaymentId(channelBo.getChannelId());
                flowBo.setPaymentName(channelBo.getChannelName());
                flowBo.setPayAmount(channelBo.getPayAmount());
                flowList.add(flowBo);
            }
        }
        debitFacade.setDownPaymentFlow(debitBillBo.getReceivableAmount(), flowList,orderInfo);

        // 车牌
        String carLicense = orderInfo.getCarLicense();

        // 1.出库
        Result stackoutResult = warehouseOutService.stackOut(orderInfo, userInfo);
        if (!stackoutResult.isSuccess()) {
            throw new BizException(stackoutResult.getErrorMsg());
        }
        log.info("工单操作流水：{} 快速工单出库成功 操作人:{}", carLicense, userId);

        // 2.确认账单
        Result result = confirmBill(drawUpBo, userInfo);
        if (!result.isSuccess()) {
            log.error("[账单收款]账单收款失败");
            throw new BizException(result.getErrorMsg());
        }
        log.info("工单操作流水：{} 快速工单 账单确认成功 操作人:{}", carLicense, userId);

        // 3.收款
        if (!CollectionUtils.isEmpty(flowList)) {
            DebitBillFlowSaveBo debitBillFlowSaveBo = new DebitBillFlowSaveBo();
            debitBillFlowSaveBo.setShopId(shopId);
            debitBillFlowSaveBo.setUserId(userId);
            debitBillFlowSaveBo.setOrderId(orderId);
            debitBillFlowSaveBo.setMemberCardId(drawUpBo.getMemberIdForSettle());
            debitBillFlowSaveBo.setMemberPayAmount(drawUpBo.getMemberBalancePay());
            debitBillFlowSaveBo.setFlowList(flowList);
            debitBillFlowSaveBo.setRemark(debitBillBo.getRemark());
            debitFacade.saveFlowList(debitBillFlowSaveBo);
        }
        log.info("工单操作流水：{} 快速工单 收款成功 操作人:{}", carLicense, userId);

        return Result.wrapSuccessfulResult("");
    }

    @Override
    public Result shareConfirmBill(Long proxyOrderId, UserInfo userInfo) {
        log.info("【dubbo:共享中心单确认账单】查询委托单信息，委托单id:{}",proxyOrderId);
        com.tqmall.core.common.entity.Result<ProxyDTO> proxyDTOResult = null;
        try {
            proxyDTOResult = rpcProxyService.getProxyInfoById(proxyOrderId);
        } catch (Exception e) {
            log.error("【dubbo:共享中心单确认账单】查询委托单信息，出现异常",e);
            return Result.wrapErrorResult(LegendSettlementErrorCode.PROXY_SEARCH_ERROR.getCode(),LegendSettlementErrorCode.PROXY_SEARCH_ERROR.getErrorMessage());
        }
        if (proxyDTOResult != null && proxyDTOResult.isSuccess()) {
            ProxyDTO proxyDTO = proxyDTOResult.getData();
            if (proxyDTO != null) {
                //受托方工单id
                Long orderId = proxyDTO.getProxyId();
                Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
                if (orderInfoOptional.isPresent()) {
                    OrderInfo orderInfo = orderInfoOptional.get();
                    ConfirmBillBo confirmBillBo = new ConfirmBillBo();
                    DebitBillBo debitBillBo = new DebitBillBo();
                    debitBillBo.setRelId(orderId);
                    debitBillBo.setReceivableAmount(orderInfo.getOrderAmount());
                    debitBillBo.setTotalAmount(orderInfo.getOrderAmount());
                    confirmBillBo.setDebitBillBo(debitBillBo);
                    Result result = confirmBillService.confirmBill(confirmBillBo, orderInfo, userInfo);
                    return result;
                }
            } else {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PROXY_NOT_EXIST_ERROR.getCode(),LegendSettlementErrorCode.PROXY_NOT_EXIST_ERROR.getErrorMessage());
            }
        }
        return Result.wrapErrorResult(LegendSettlementErrorCode.PROXY_SEARCH_ERROR.getCode(),LegendSettlementErrorCode.PROXY_SEARCH_ERROR.getErrorMessage());
    }

    /**
     * 校验配件名称是否服务规则
     * @param orderGoods
     * @return
     */
    private Goods checkName(OrderGoods orderGoods){
        String goodsName = orderGoods.getGoodsName();
        if(StringUtils.isBlank(goodsName)){
            return null;
        }
        Long goodsId = orderGoods.getGoodsId();
        Optional<Goods> goodsOptional = goodsService.selectById(goodsId);
        if(!goodsOptional.isPresent()){
            return null;
        }
        Goods goods = goodsOptional.get();
        Long tqmallGoodsId = goods.getTqmallGoodsId();
        if(Long.compare(tqmallGoodsId,0l) != 1){
            return null;
        }
        if(goodsName.contains("淘汽云修 100臻")){
            return goods;
        }
        return null;
    }
}
