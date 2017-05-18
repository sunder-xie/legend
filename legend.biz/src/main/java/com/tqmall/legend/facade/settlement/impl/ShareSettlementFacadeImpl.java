package com.tqmall.legend.facade.settlement.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.error.LegendShareErrorCode;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.bo.DebitBillBo;
import com.tqmall.legend.biz.order.bo.SpeedilyBillBo;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.ShareSettlementFacade;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowBo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowSaveBo;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zsy on 16/5/23.
 * 共享中心确认对账
 */
@Slf4j
@Service
public class ShareSettlementFacadeImpl implements ShareSettlementFacade {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private DebitFacade debitFacade;

    /**
     * 共享中心对账
     *
     * @param proxyDTO 委托单信息
     * @param userInfo
     * @return
     */
    @Override
    public Result shareOrderSettle(ProxyDTO proxyDTO, UserInfo userInfo) {
        if (proxyDTO == null || userInfo == null) {
            return Result.wrapErrorResult(LegendShareErrorCode.PARAMS_ERROR.getCode(), LegendShareErrorCode.PARAMS_ERROR.getErrorMessage());
        }
        Long shopId = userInfo.getShopId();
        Long orderId = proxyDTO.getProxyId();//受托方转的工单id
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(proxyDTO.getProxyId(), shopId);
        if (!orderInfoOptional.isPresent()) {
            return Result.wrapErrorResult(LegendErrorCode.ORDER_NOT_FIND_EX.getCode(), LegendErrorCode.ORDER_NOT_FIND_EX.getErrorMessage());
        }
        //挂账的工单/已结清的工单（金额是0）才能对账结算
        OrderInfo orderInfo = orderInfoOptional.get();
        String orderStatus = orderInfo.getOrderStatus();
        Integer payStatus = orderInfo.getPayStatus();
        if(OrderNewStatusEnum.DDYFK.getOrderStatus().equals(orderStatus) && OrderNewStatusEnum.DDYFK.getPayStatus().equals(payStatus)){
            return Result.wrapSuccessfulResult(true);
        }
        if (!OrderNewStatusEnum.YGZ.getOrderStatus().equals(orderStatus) && !OrderNewStatusEnum.YGZ.getPayStatus().equals(payStatus)) {
            return Result.wrapErrorResult(LegendShareErrorCode.ORDER_STATUS_ERROR.getCode(), LegendShareErrorCode.ORDER_STATUS_ERROR.getErrorMessage());
        }
        //委托单和工单应收金额不匹配则无法结算
        BigDecimal proxyAmount = proxyDTO.getProxyAmount();
        BigDecimal orderAmount = orderInfo.getOrderAmount();
        if (proxyAmount.compareTo(orderAmount) != 0) {
            log.error("【共享中心对账】：工单id:{},委托单id:{},工单金额：{}，委托单金额：{}，不匹配，无法结算", orderId, proxyDTO.getId(), orderAmount, proxyAmount);
            return Result.wrapErrorResult(LegendShareErrorCode.PRICE_NOT_MATCH_ERROR.getCode(), LegendShareErrorCode.PRICE_NOT_MATCH_ERROR.getErrorMessage(orderId));
        }
        //数据正确，可以用现金结算
        //获取支付方式
        List<Payment> paymentList = paymentService.getPaymentsByShopId(shopId);
        //TODO 对账默认现金结算
        Payment payment = null;
        for (Payment temp : paymentList) {
            if (temp.getPaymentTag().equals(1) && temp.getName().equals("现金")) {
                payment = temp;
            }
        }
        if (payment == null) {
            return Result.wrapErrorResult(LegendShareErrorCode.PAYMENT_ERROR.getCode(), LegendShareErrorCode.PAYMENT_ERROR.getErrorMessage());
        }
        //走确认账单+收款逻辑（即快修快保收款）
        SpeedilyBillBo speedilyBillBo = new SpeedilyBillBo();
        //组装收款单信息
        DebitBillBo debitBillBo = new DebitBillBo();
        debitBillBo.setRelId(orderId);
        debitBillBo.setTotalAmount(orderAmount);
        debitBillBo.setReceivableAmount(orderAmount);
        debitBillBo.setRemark("委托单收款");//TODO 先写死，后续看需求而定
        speedilyBillBo.setDebitBillBo(debitBillBo);
        //设置收款方式
        List<DebitBillFlowBo> flowList = Lists.newArrayList();
        DebitBillFlowBo flowBo = new DebitBillFlowBo();
        flowBo.setPaymentId(payment.getId());
        flowBo.setPaymentName(payment.getName());
        flowBo.setPayAmount(orderAmount);
        flowList.add(flowBo);
        try {
            DebitBillFlowSaveBo debitBillFlowSaveBo = new DebitBillFlowSaveBo();
            debitBillFlowSaveBo.setShopId(shopId);
            debitBillFlowSaveBo.setUserId(userInfo.getUserId());
            debitBillFlowSaveBo.setOrderId(orderId);
            debitBillFlowSaveBo.setFlowList(flowList);
            debitBillFlowSaveBo.setRemark(debitBillBo.getRemark());
            debitFacade.saveFlowList(debitBillFlowSaveBo);
            return Result.wrapSuccessfulResult(true);
        } catch (RuntimeException e) {
            log.error("【委托单确认对账】：出现异常", e);
            return Result.wrapErrorResult(LegendShareErrorCode.ORDER_CONFIRM_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("【委托单确认对账】：出现异常", e);
            return Result.wrapErrorResult(LegendShareErrorCode.ORDER_CONFIRM_ERROR.getCode(), LegendShareErrorCode.ORDER_CONFIRM_ERROR.getErrorMessage());
        }
    }
}
