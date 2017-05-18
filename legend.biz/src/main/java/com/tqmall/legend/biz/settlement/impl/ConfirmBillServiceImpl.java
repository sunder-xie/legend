package com.tqmall.legend.biz.settlement.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.billcenter.client.enums.DebitTypeEnum;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderDiscountFlowService;
import com.tqmall.legend.biz.order.bo.ConfirmBillBo;
import com.tqmall.legend.biz.order.bo.DebitBillBo;
import com.tqmall.legend.biz.order.bo.OrderDiscountFlowBo;
import com.tqmall.legend.biz.settlement.ConfirmBillService;
import com.tqmall.legend.biz.settlement.ISettlementService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.order.InsuranceOrderStatusEnum;
import com.tqmall.legend.entity.order.OrderDiscountFlow;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.PayStatusEnum;
import com.tqmall.legend.enums.order.OrderDiscountTypeEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import com.tqmall.legend.facade.discount.bo.SelectedCardBo;
import com.tqmall.legend.facade.discount.bo.SelectedComboBo;
import com.tqmall.legend.facade.discount.bo.SelectedCouponBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/6/4.
 */
@Slf4j
@Service
public class ConfirmBillServiceImpl implements ConfirmBillService {
    @Autowired
    private OrderDiscountFlowService orderDiscountFlowService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RpcDebitBillService rpcDebitBillService;
    @Autowired
    private ISettlementService settlementService;
    @Autowired
    private IInsuranceBillService insuranceBillService;
    @Autowired
    private AccountFacadeService accountFacadeService;

    /**
     * 确认账单
     *
     * @return
     */
    @Override
    @Transactional
    public Result<DebitBillDTO> confirmBill(ConfirmBillBo confirmBillBo, OrderInfo orderInfo, UserInfo userInfo) {
        //调用车主端通知淘汽优惠券已经使用
        String taoqiCouponSn = confirmBillBo.getTaoqiCouponSn();
        if (StringUtils.isNotBlank(taoqiCouponSn)) {
            Result taoqiCouponPush2CAPPResult = settleTaoqiCoupon(confirmBillBo, orderInfo);
            if (taoqiCouponPush2CAPPResult.isSuccess()) {
                //添加报销单
                try {
                    //查询有没有报销单记录
                    Map<String,Object> searchMap = Maps.newHashMap();
                    searchMap.put("orderId",orderInfo.getId());
                    searchMap.put("shopId",orderInfo.getShopId());
                    List<InsuranceBill> insuranceBillList = insuranceBillService.select(searchMap);
                    if(CollectionUtils.isEmpty(insuranceBillList)){
                        insuranceBillService.create(userInfo, orderInfo, taoqiCouponSn, InsuranceOrderStatusEnum.PASS);
                    }
                } catch (Exception e) {
                    //出现异常，不影响正确业务流程
                    log.error("【工单确认对账】：添加报销单出现异常", e);
                }
            } else {
                throw new BizException("淘汽优惠券核销失败");
            }
        }
        BigDecimal orderAmount = orderInfo.getOrderAmount();//工单应收金额
        //计算总优惠价格，添加优惠券流水order_discount_flow
        BigDecimal totalDiscountAmount = saveOrderDiscountFlow(confirmBillBo, orderInfo, userInfo);
        //工单费用，注： 之前是税费，没有用，所以将费用放在这个字段中
        BigDecimal taxAmount = confirmBillBo.getTaxAmount();
        //工单实际应收金额:应收金额+费用-工单总优惠
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        BigDecimal receivableAmount = orderAmount.add(taxAmount).subtract(totalDiscountAmount);
        //更新工单数据
        //如果 工单应收金额 + 费用 > 优惠总金额大，则工单挂账，否则工单结清
        orderInfo.setOrderStatus(OrderNewStatusEnum.DDYFK.getOrderStatus());
        if (orderAmount.compareTo(totalDiscountAmount) > 0) {
            orderInfo.setPayStatus(PayStatusEnum.SIGN.getCode());//挂账
            orderInfo.setSignAmount(receivableAmount);//全额挂账 = 应收金额
        } else {
            orderInfo.setPayStatus(PayStatusEnum.PAYED.getCode());//结清
            orderInfo.setSignAmount(BigDecimal.ZERO);
        }
        orderInfo.setPayTime(new Date());//回写工单结算日期
        orderInfo.setPayAmount(receivableAmount);//工单应收金额
        orderInfo.setOrderDiscountAmount(totalDiscountAmount);//回写工单优惠总金额
        orderInfo.setConfirmTime(new Date());//回写工单确认账单日期
        orderInfo.setTaxAmount(taxAmount);//回写工单费用字段
        //设置工单总优惠金额
        try {
            log.info("【工单确认对账】：工单更新{}", orderInfo);
            orderService.updateOrder(orderInfo);
        } catch (Exception e) {
            log.error("【工单确认对账】：工单更新,出现异常", e);
            throw new BizException("工单更新异常");
        }
        //调用结算中心往收款单插入记录
        DebitBillBo debitBillBo = confirmBillBo.getDebitBillBo();
        Result<DebitBillDTO> debitBillResult = saveBill(debitBillBo, userInfo, orderInfo, orderAmount, receivableAmount);
        if (!debitBillResult.isSuccess()) {
            throw new BizException("收款单添加失败<br/>" + debitBillResult.getErrorMsg());
        }
        return debitBillResult;
    }

    /**
     * 优惠券核销
     *
     * @param confirmBillBo
     * @param orderInfo
     * @return
     */
    private Result settleTaoqiCoupon(ConfirmBillBo confirmBillBo, OrderInfo orderInfo) {
        //先校验优惠券
        String taoqiCouponSn = confirmBillBo.getTaoqiCouponSn();
        List<String> itemIds = new ArrayList<String>();
        Result taoqiCouponResult = settlementService.checkTaoqiCoupon(orderInfo, taoqiCouponSn);
        if (!taoqiCouponResult.isSuccess()) {
            throw new BizException("淘汽优惠券核销失败");
        }
        Object taoqiData = taoqiCouponResult.getData();
        if (taoqiData instanceof List) {
            itemIds = (List<String>) taoqiData;
        }
        Result taoqiCouponPush2CAPPResult = settlementService.taoqiCouponPush2CAPP(taoqiCouponSn, orderInfo, itemIds);
        return taoqiCouponPush2CAPPResult;
    }

    /**
     * 保存工单优惠流水
     *
     * @param confirmBillBo
     * @return
     */
    private BigDecimal saveOrderDiscountFlow(ConfirmBillBo confirmBillBo, OrderInfo orderInfo, UserInfo userInfo) {
        Long userId = userInfo.getUserId();
        //优惠总金额
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        Long orderId = orderInfo.getId();
        Long shopId = orderInfo.getShopId();
        List<OrderDiscountFlowBo> orderDiscountFlowBoList = confirmBillBo.getOrderDiscountFlowBoList();
        List<OrderDiscountFlow> orderDiscountFlowList = Lists.newArrayList();
        //设置需要添加的优惠券优惠流水
        if(!CollectionUtils.isEmpty(orderDiscountFlowBoList)){
            for (OrderDiscountFlowBo orderDiscountFlowBo : orderDiscountFlowBoList) {
                BigDecimal couponDiscountAmount = orderDiscountFlowBo.getDiscountAmount();//优惠
                totalDiscountAmount = totalDiscountAmount.add(couponDiscountAmount);//计算优惠总金额
                //添加优惠流水
                OrderDiscountFlow orderDiscountFlow = new OrderDiscountFlow();
                BeanUtils.copyProperties(orderDiscountFlowBo, orderDiscountFlow);
                Long relId;
                if(orderDiscountFlowBo.getCouponId() != null) {
                    relId = orderDiscountFlowBo.getCouponId();
                } else {
                    relId = orderDiscountFlowBo.getComboServiceId();
                }
                orderDiscountFlow.setRelId(relId);
                setSameOrderDiscountFlow(userId, orderId, shopId, orderDiscountFlow);
                orderDiscountFlowList.add(orderDiscountFlow);
            }
        }
        //会员卡id
        Long memberCardId = confirmBillBo.getMemberCardId();
        //优惠金额
        BigDecimal discountAmount = confirmBillBo.getDiscountAmount();
        boolean useMemberCard = false;
        if (memberCardId != null && Long.compare(memberCardId, 0l) > 0 && discountAmount != null) {
            useMemberCard = true;
        }
        //使用优惠券/使用会员卡特权优惠时调接口核销
        if (!CollectionUtils.isEmpty(orderDiscountFlowBoList) || useMemberCard) {
            DiscountSelectedBo discountSelectedBo = new DiscountSelectedBo();
            if (useMemberCard) {
                SelectedCardBo selectedCardBo = new SelectedCardBo();
                selectedCardBo.setCardId(memberCardId);
                selectedCardBo.setCardDiscountAmount(discountAmount);
                discountSelectedBo.setSelectedCard(selectedCardBo);
            }
            //调用接口优惠券、折扣券、计次卡、转赠券已经使用
            boolean bool = settlementAccount(userInfo, orderId, orderDiscountFlowBoList, discountSelectedBo);
            if (!bool) {
                throw new BizException("优惠券使用失败");
            }
        }
        //折扣
        BigDecimal discountRate = confirmBillBo.getDiscountRate();
        //会员卡编号
        String cardNumber = confirmBillBo.getCardNumber();
        //如果有折扣且比1小，则保存折扣流水
        if (discountRate != null && discountRate.compareTo(BigDecimal.ONE) <= 0) {
            // 添加折扣,注：目前设计需求未涉及到，此判断为了兼容以前的老版本结算功能的折扣
            totalDiscountAmount = totalDiscountAmount.add(discountAmount);
            OrderDiscountFlow orderDiscountFlow = new OrderDiscountFlow();
            orderDiscountFlow.setDiscountType(OrderDiscountTypeEnum.DISCOUNT.getCode());
            orderDiscountFlow.setDiscountName(OrderDiscountTypeEnum.DISCOUNT.getMessage());
            setSameOrderDiscountFlow(userId, orderId, shopId, orderDiscountFlow);
            orderDiscountFlow.setDiscountRate(discountRate);
            orderDiscountFlow.setDiscountAmount(discountAmount);
            orderDiscountFlow.setDiscountSn(cardNumber);
            orderDiscountFlowList.add(orderDiscountFlow);
        } else if (useMemberCard) {
            //使用会员卡优惠
            totalDiscountAmount = totalDiscountAmount.add(discountAmount);
            OrderDiscountFlow orderDiscountFlow = new OrderDiscountFlow();
            orderDiscountFlow.setDiscountReason(confirmBillBo.getCardDiscountReason());
            orderDiscountFlow.setRelId(memberCardId);//会员卡id
            orderDiscountFlow.setDiscountType(OrderDiscountTypeEnum.MEMBERCOUPON.getCode());
            orderDiscountFlow.setDiscountName(OrderDiscountTypeEnum.MEMBERCOUPON.getMessage());
            setSameOrderDiscountFlow(userId, orderId, shopId, orderDiscountFlow);
            orderDiscountFlow.setDiscountRate(BigDecimal.ONE);//不打折，默认1
            orderDiscountFlow.setDiscountAmount(discountAmount);
            orderDiscountFlow.setDiscountSn(cardNumber);
            orderDiscountFlow.setAccountId(confirmBillBo.getAccountId());
            orderDiscountFlowList.add(orderDiscountFlow);
        } else if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            //单纯的优惠
            totalDiscountAmount = totalDiscountAmount.add(discountAmount);
            OrderDiscountFlow orderDiscountFlow = new OrderDiscountFlow();
            orderDiscountFlow.setDiscountType(OrderDiscountTypeEnum.BENEFIT.getCode());
            orderDiscountFlow.setDiscountName(OrderDiscountTypeEnum.BENEFIT.getMessage());
            setSameOrderDiscountFlow(userId, orderId, shopId, orderDiscountFlow);
            orderDiscountFlow.setDiscountRate(BigDecimal.ONE);//不打折，默认1
            orderDiscountFlow.setDiscountAmount(discountAmount);
            orderDiscountFlow.setDiscountSn(cardNumber);
            orderDiscountFlowList.add(orderDiscountFlow);
        }
        //添加淘汽优惠
        String taoqiCouponSn = confirmBillBo.getTaoqiCouponSn();
        BigDecimal taoqiCouponAmount = confirmBillBo.getTaoqiCouponAmount();
        if (StringUtils.isNotBlank(taoqiCouponSn)) {
            //添加优惠流水
            totalDiscountAmount = totalDiscountAmount.add(taoqiCouponAmount);
            OrderDiscountFlow orderDiscountFlow = new OrderDiscountFlow();
            orderDiscountFlow.setDiscountType(OrderDiscountTypeEnum.TAOQICOUPON.getCode());
            orderDiscountFlow.setDiscountName(OrderDiscountTypeEnum.TAOQICOUPON.getMessage());
            setSameOrderDiscountFlow(userId, orderId, shopId, orderDiscountFlow);
            orderDiscountFlow.setDiscountRate(BigDecimal.ONE);//不打折，默认1
            orderDiscountFlow.setDiscountAmount(taoqiCouponAmount);
            orderDiscountFlow.setDiscountSn(taoqiCouponSn);
            orderDiscountFlowList.add(orderDiscountFlow);
        }

        //添加流水前进行前端传的金额值校验
        //前端：工单总计
        DebitBillBo debitBillBo = confirmBillBo.getDebitBillBo();
        BigDecimal debitBillBoTotalAmount = debitBillBo.getTotalAmount();
        if (debitBillBoTotalAmount == null) {
            debitBillBoTotalAmount = BigDecimal.ZERO;
        }
        //前端：工单应收金额
        BigDecimal debitBillBoReceivableAmount = debitBillBo.getReceivableAmount();
        if (debitBillBoReceivableAmount == null) {
            debitBillBoReceivableAmount = BigDecimal.ZERO;
        }
        //前端：工单优惠金额
        BigDecimal debitBillTotalDiscountAmount = debitBillBoTotalAmount.subtract(debitBillBoReceivableAmount);
        //如果金额不一致，则数据有误
        if (totalDiscountAmount.compareTo(debitBillTotalDiscountAmount) != 0) {
            throw new BizException("优惠总金额有误，请检查数据");
        }
        if (!CollectionUtils.isEmpty(orderDiscountFlowList)) {
            //添加优惠流水
            try {
                log.info("【工单确认对账】：添加优惠流水，添加数据{}", orderDiscountFlowList);
                orderDiscountFlowService.batchInsert(orderDiscountFlowList);
            } catch (Exception e) {
                log.error("【工单确认对账】：添加优惠流水出现异常", e);
                throw new BizException("添加优惠流水异常");
            }
        }
        return totalDiscountAmount;
    }

    /**
     * 设置相同字段
     *
     * @param userId
     * @param orderId
     * @param shopId
     * @param orderDiscountFlow
     */
    private void setSameOrderDiscountFlow(Long userId, Long orderId, Long shopId, OrderDiscountFlow orderDiscountFlow) {
        orderDiscountFlow.setCreator(userId);
        orderDiscountFlow.setShopId(shopId);
        orderDiscountFlow.setOrderId(orderId);
    }

    /**
     * 添加收款单
     *
     * @param debitBillBo
     * @param userInfo
     * @param orderInfo
     * @param orderAmount
     * @param receivableAmount
     */
    private Result<DebitBillDTO> saveBill(DebitBillBo debitBillBo, UserInfo userInfo, OrderInfo orderInfo, BigDecimal orderAmount, BigDecimal receivableAmount) {
        DebitBillDTO debitBillDTO = new DebitBillDTO();
        debitBillDTO.setCreator(userInfo.getUserId());
        debitBillDTO.setShopId(orderInfo.getShopId());
        debitBillDTO.setRelId(orderInfo.getId());
        debitBillDTO.setTotalAmount(orderAmount);
        debitBillDTO.setBillName("工单号" + orderInfo.getOrderSn());
        debitBillDTO.setDebitTypeId(DebitTypeEnum.ORDER.getId());//1表示工单收款 TODO 枚举
        debitBillDTO.setReceivableAmount(receivableAmount);//应收金额
        debitBillDTO.setSignAmount(receivableAmount);//全额挂账：挂账金额 = 应收金额
        debitBillDTO.setOperatorName(userInfo.getName());//收款人姓名
        debitBillDTO.setPayerName(orderInfo.getCustomerName());//付款方是车主姓名
        debitBillDTO.setRemark(debitBillBo.getRemark());
        try {
            com.tqmall.core.common.entity.Result<DebitBillDTO> result = rpcDebitBillService.saveBill(debitBillDTO, null);
            log.info("【dubbo:工单确认对账，调用结算中心】：添加收款单,入参{},返回{}", LogUtils.objectToString(debitBillDTO),result.isSuccess());
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult(result.getData());
            }else{
                return Result.wrapErrorResult(result.getCode(),result.getMessage());
            }
        } catch (Exception e) {
            log.error("【工单确认对账】：添加收款单,出现异常", e);
            throw new BizException("添加收款单异常");
        }
    }

    /**
     * 调用接口优惠券、折扣券、计次卡、转赠券已经使用  或 使用会员卡特权优惠
     *
     * @param userInfo
     * @param orderId
     * @param orderDiscountFlowBoList
     * @return
     */
    private boolean settlementAccount(UserInfo userInfo, Long orderId, List<OrderDiscountFlowBo> orderDiscountFlowBoList, DiscountSelectedBo discountSelectedBo) {
        if (!CollectionUtils.isEmpty(orderDiscountFlowBoList)) {
            List<SelectedCouponBo> selectedCouponList = Lists.newArrayList();
            List<SelectedComboBo> selectedComboList = Lists.newArrayList();
            for (OrderDiscountFlowBo orderDiscountFlowBo : orderDiscountFlowBoList) {
                Long couponId = orderDiscountFlowBo.getCouponId();
                Integer discountType = orderDiscountFlowBo.getDiscountType();
                if (OrderDiscountTypeEnum.CASHCOUPON.getCode() == discountType || OrderDiscountTypeEnum.UNIVERSALCOUPON.getCode() == discountType) {
                    //现金券、通用券
                    SelectedCouponBo selectedCouponBo = new SelectedCouponBo();
                    selectedCouponBo.setCouponId(couponId);
                    selectedCouponBo.setCouponDiscountAmount(orderDiscountFlowBo.getDiscountAmount());
                    selectedCouponList.add(selectedCouponBo);
                } else if (OrderDiscountTypeEnum.METERCARD.getCode() == discountType) {
                    //计次卡
                    SelectedComboBo selectedComboBo = new SelectedComboBo();
                    selectedComboBo.setComboServiceId(orderDiscountFlowBo.getComboServiceId());
                    selectedComboBo.setCount(orderDiscountFlowBo.getUseCount());
                    selectedComboList.add(selectedComboBo);
                }
            }
            if (!CollectionUtils.isEmpty(selectedCouponList)) {
                discountSelectedBo.setSelectedCouponList(selectedCouponList);
            }
            if (!CollectionUtils.isEmpty(selectedComboList)) {
                discountSelectedBo.setSelectedComboList(selectedComboList);
            }
        }
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        log.info("【工单确认对账】：调用优惠券使用接口，传参shopId：{},userId:{},orderId：{}，discountSelectedBo：{}", shopId, userId, orderId, LogUtils.objectToString(discountSelectedBo));
        try {
            accountFacadeService.settlementAccount(shopId, userId, orderId, discountSelectedBo);
            return true;
        } catch (Exception e) {
            log.error("【工单确认对账】：调用优惠券使用接口,出现异常", e);
        }
        return false;
    }
}
