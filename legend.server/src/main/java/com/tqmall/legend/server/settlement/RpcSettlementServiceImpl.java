package com.tqmall.legend.server.settlement;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.provider.popularsort.RpcPopularSortService;
import com.tqmall.cube.shop.result.popularsort.PopularDataDTO;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.error.LegendSettlementErrorCode;
import com.tqmall.legend.api.entity.OrderCountSearchVO;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.legend.biz.api.IOrderApiService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.bo.*;
import com.tqmall.legend.biz.order.vo.OrderDiscountFlowVo;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.biz.settlement.vo.DebitBillAndFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillVo;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.order.CreateCarWashResponse;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.enums.order.OrderDiscountTypeEnum;
import com.tqmall.legend.facade.discount.DiscountCenter2;
import com.tqmall.legend.facade.discount.bo.*;
import com.tqmall.legend.facade.order.CarWashFacade;
import com.tqmall.legend.facade.order.OrderDiscountFlowFacade;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.facade.settlement.ConfirmBillFacade;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.SettlementFacade;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowBo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowSaveBo;
import com.tqmall.legend.facade.settlement.bo.SettlementSmsBO;
import com.tqmall.legend.object.param.account.DiscountSelectedComboParam;
import com.tqmall.legend.object.param.account.DiscountSelectedCouponParam;
import com.tqmall.legend.object.param.order.OrderSearchParam;
import com.tqmall.legend.object.param.settlement.*;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.order.OrderInfoDTO;
import com.tqmall.legend.object.result.settlement.HistoryFlowDTO;
import com.tqmall.legend.object.result.settlement.OrderDebitBillDTO;
import com.tqmall.legend.object.result.settlement.PaymentDTO;
import com.tqmall.legend.service.settlement.RpcSettlementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 16/3/15.
 */
@Service ("rpcSettlementService")
@Slf4j
public class RpcSettlementServiceImpl implements RpcSettlementService {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private IOrderApiService orderApiService;
    @Autowired
    private SettlementFacade settlementFacade;
    @Autowired
    private DebitFacade debitFacade;
    @Autowired
    private ConfirmBillFacade confirmBillFacade;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private CarWashFacade carWashFacade;
    @Autowired
    private OrderDiscountFlowFacade orderDiscountFlowFacade;
    @Autowired
    private DiscountCenter2 discountCenter2;
    @Autowired
    private RpcPopularSortService rpcPopularSortService;
    @Autowired
    private JedisClient jedisClient;


    @Override
    public Result<List<PaymentDTO>> getPaymentList(final Long shopId) {
        return new ApiTemplate<List<PaymentDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId,"门店id不能为空");
            }

            @Override
            protected List<PaymentDTO> process() throws BizException {
                List<PaymentDTO> paymentDTOList = Lists.newArrayList();
                Result<List<PopularDataDTO>>  paymentListResult = null;
                try {
                    paymentListResult = rpcPopularSortService.getPaymentList(shopId);
                } catch (Exception e) {
                    log.error("调用cube获取热门结算方式排序异常", e);
                    return paymentDTOList;
                }
                if(!paymentListResult.isSuccess() || CollectionUtils.isEmpty(paymentListResult.getData())){
                    return paymentDTOList;
                }
                List<PopularDataDTO> popularDataDTOList = paymentListResult.getData();
                for (PopularDataDTO popularDataDTO : popularDataDTOList) {
                    PaymentDTO paymentDTO = new PaymentDTO();
                    paymentDTO.setId(popularDataDTO.getId());
                    paymentDTO.setName(popularDataDTO.getName());
                    paymentDTOList.add(paymentDTO);
                }
                return paymentDTOList;
            }
        }.execute();
    }

    @Override
    public Result<Map<String, Integer>> getSettlementOrderCount(Long shopId) {
        if (null == shopId || shopId.compareTo(1l) < 0) {
            log.error("[结算工单计数] 店铺的ID错误. shopId:{}", shopId);
            return Result.wrapErrorResult(LegendErrorCode.APP_SHOP_ID_ERROR.getCode(), LegendErrorCode.APP_SHOP_ID_ERROR.getErrorMessage());
        }
        Map<String, Integer> resultMap = new HashMap<>();
        List<OrderCountSearchVO> paramList = new ArrayList<>();
        OrderCountSearchVO paramYGZ = new OrderCountSearchVO();
        paramYGZ.setSymbol(1);
        paramYGZ.setOrderTag(new Integer[] { 1, 2, 3 });
        paramYGZ.setPayStatus(new Integer[] { 1 });
        paramYGZ.setShopId(shopId);
        paramYGZ.setProxyType(new Integer[] { 0, 1 });
        paramYGZ.setOrderStatus("DDYFK");
        paramList.add(paramYGZ);
        //待结算
        OrderCountSearchVO paramDJS = new OrderCountSearchVO();
        paramDJS.setSymbol(2);
        paramDJS.setOrderTag(new Integer[] { 1, 3 });
        paramDJS.setPayStatus(new Integer[] { 0 });
        paramDJS.setProxyType(new Integer[] { 0, 1 });
        paramDJS.setShopId(shopId);
        paramDJS.setOrderStatus("DDWC");
        paramList.add(paramDJS);
        //待结算快修快保单 CJDD
        OrderCountSearchVO paramDJSOne = new OrderCountSearchVO();
        paramDJSOne.setSymbol(3);
        paramDJSOne.setOrderTag(new Integer[] { 3 });
        paramDJSOne.setPayStatus(new Integer[] { 0 });
        paramDJSOne.setProxyType(new Integer[] { 0, 1 });
        paramDJSOne.setShopId(shopId);
        paramDJSOne.setOrderStatus("CJDD");
        paramList.add(paramDJSOne);
        com.tqmall.zenith.errorcode.support.Result<Map<String, Long>> result = orderApiService.getOrderCount(paramList);
        if (null == result || !result.isSuccess()) {
            log.error("[结算工单计数] 调用搜索获取结算订单数量失败. paramList:{}", paramList);
            return Result.wrapErrorResult("", "获取结算订单数量失败");
        } else {
            resultMap.put("YGZ", result.getData().get("1").intValue());
            resultMap.put("DJS", result.getData().get("2").intValue() + result.getData().get("3").intValue());
        }
        return Result.wrapSuccessfulResult(resultMap);
    }

    /**
     * 工单搜索
     *
     * @param orderSearchParam
     */
    @Override
    public Result<PageEntityDTO<OrderInfoDTO>> searchOrderInfo(OrderSearchParam orderSearchParam) {
        PageEntityDTO<OrderInfoDTO> resultEntity = new PageEntityDTO<>();
        com.tqmall.zenith.errorcode.support.Result<DefaultPage<OrderInfoVo>> result = orderApiService.getOrderInfoListFromSearchOrLocal(orderSearchParam);
        if (!result.isSuccess()) {
            log.error("[工单搜索] 获取工单信息失败.");
            return Result.wrapErrorResult(LegendErrorCode.APP_ORDER_LIST_ERROR.getCode(), LegendErrorCode.APP_ORDER_LIST_ERROR.getErrorMessage());
        }
        DefaultPage<OrderInfoVo> defaultPage = result.getData();
        if(defaultPage != null){
            resultEntity.setTotalNum(Long.valueOf(defaultPage.getTotalElements()).intValue());
            resultEntity.setPageNum(defaultPage.getNumber());
            List<OrderInfoDTO> orderInfoDTOList = Lists.newArrayList();
            for (OrderInfoVo orderInfo : defaultPage.getContent()) {
                OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
                BeanUtils.copyProperties(orderInfo, orderInfoDTO);
                setOrderInfDTO(orderInfo,orderInfoDTO);
                orderInfoDTOList.add(orderInfoDTO);
            }
            resultEntity.setRecordList(orderInfoDTOList);
        }
        return Result.wrapSuccessfulResult(resultEntity);
    }

    private void setOrderInfDTO(OrderInfoVo orderInfoVo, OrderInfoDTO orderInfoDTO) {
        orderInfoDTO.setGmtCreateStr(orderInfoVo.getCreateTimeStr());
        orderInfoDTO.setShopId(Long.parseLong(orderInfoVo.getShopId().toString()));
        if(StringUtils.isNotBlank(orderInfoVo.getId())){
            orderInfoDTO.setId(Long.parseLong(orderInfoVo.getId()));
        }
        if(StringUtils.isNotBlank(orderInfoVo.getGmtCreate())){
            orderInfoDTO.setGmtCreate(DateUtil.convertStringToDate(orderInfoVo.getGmtCreate()));
        }
        if(StringUtils.isNotBlank(orderInfoVo.getGmtModified())) {
            orderInfoDTO.setGmtModified(DateUtil.convertStringToDate(orderInfoVo.getGmtModified()));
        }
        if(orderInfoVo.getOrderType() != null){
            orderInfoDTO.setOrderType(Long.parseLong(orderInfoVo.getOrderType().toString()));
        }
        if(orderInfoVo.getCustomerId() != null){
            orderInfoDTO.setCustomerId(Long.parseLong(orderInfoVo.getCustomerId().toString()));
        }
        if(orderInfoVo.getCustomerCarId() != null){
            orderInfoDTO.setCustomerCarId(Long.parseLong(orderInfoVo.getCustomerCarId().toString()));
        }
        if(StringUtils.isNotBlank(orderInfoVo.getExpectedTime())){
            orderInfoDTO.setExpectedTime(DateUtil.convertStringToDate(orderInfoVo.getExpectedTime()));
        }
        if(StringUtils.isNotBlank(orderInfoVo.getBuyTime())){
            orderInfoDTO.setBuyTime(DateUtil.convertStringToDate(orderInfoVo.getBuyTime()));
        }
        if(orderInfoVo.getCarBrandId() != null){
            orderInfoDTO.setCarBrandId(Long.parseLong(orderInfoVo.getCarBrandId().toString()));
        }
        if(orderInfoVo.getCarModelsId() != null){
            orderInfoDTO.setCarModelsId(Long.parseLong(orderInfoVo.getCarModelsId().toString()));
        }
        if(orderInfoVo.getCarPowerId() != null){
            orderInfoDTO.setCarPowerId(Long.parseLong(orderInfoVo.getCarPowerId().toString()));
        }
        if(orderInfoVo.getCarSeriesId() != null){
            orderInfoDTO.setCarSeriesId(Long.parseLong(orderInfoVo.getCarSeriesId().toString()));
        }
        if(orderInfoVo.getCarYearId() != null){
            orderInfoDTO.setCarYearId(Long.parseLong(orderInfoVo.getCarYearId().toString()));
        }
        if(orderInfoVo.getDiscount() != null){
            orderInfoDTO.setDiscount(new BigDecimal(orderInfoVo.getDiscount()));
        }
        if(orderInfoVo.getFeeAmount() != null){
            orderInfoDTO.setFeeAmount(new BigDecimal(orderInfoVo.getFeeAmount()));
        }
        if(orderInfoVo.getFeeDiscount() != null){
            orderInfoDTO.setFeeDiscount(new BigDecimal(orderInfoVo.getFeeDiscount()));
        }
        if(orderInfoVo.getGoodsAmount() != null){
            orderInfoDTO.setGoodsAmount(new BigDecimal(orderInfoVo.getGoodsAmount()));
        }
        if(orderInfoVo.getGoodsDiscount() != null){
            orderInfoDTO.setGoodsDiscount(new BigDecimal(orderInfoVo.getGoodsDiscount()));
        }
        if(orderInfoVo.getGoodsCount() != null){
            orderInfoDTO.setGoodsCount(Long.parseLong(orderInfoVo.getGoodsCount().toString()));
        }
        if(orderInfoVo.getServiceAmount() != null){
            orderInfoDTO.setServiceAmount(new BigDecimal(orderInfoVo.getServiceAmount()));
        }
        if(orderInfoVo.getTaxAmount() != null){
            orderInfoDTO.setTaxAmount(new BigDecimal(orderInfoVo.getTaxAmount()));
        }
        if(orderInfoVo.getTotalAmount() != null){
            orderInfoDTO.setTotalAmount(new BigDecimal(orderInfoVo.getTotalAmount()));
        }
        if(StringUtils.isNotBlank(orderInfoVo.getOrderAmount())){
            orderInfoDTO.setOrderAmount(new BigDecimal(orderInfoVo.getOrderAmount()));
        }
        if(orderInfoVo.getPreCouponAmount() != null){
            orderInfoDTO.setPreCouponAmount(new BigDecimal(orderInfoVo.getPreCouponAmount()));
        }
        if(orderInfoVo.getPayAmount() != null){
            orderInfoDTO.setPayAmount(new BigDecimal(orderInfoVo.getPayAmount()));
        }
        if(orderInfoVo.getPreDiscountRate() != null){
            orderInfoDTO.setPreDiscountRate(new BigDecimal(orderInfoVo.getPreDiscountRate()));
        }
        if(orderInfoVo.getPrePreferentiaAmount() != null){
            orderInfoDTO.setPrePreferentiaAmount(new BigDecimal(orderInfoVo.getPrePreferentiaAmount()));
        }
        if(orderInfoVo.getPreTaxAmount() != null){
            orderInfoDTO.setPreTaxAmount(new BigDecimal(orderInfoVo.getPreTaxAmount()));
        }
        if(orderInfoVo.getPreTotalAmount() != null){
            orderInfoDTO.setPreTotalAmount(new BigDecimal(orderInfoVo.getPreTotalAmount()));
        }
        if(StringUtils.isNotBlank(orderInfoVo.getFinishTime())){
            orderInfoDTO.setFinishTime(DateUtil.convertStringToDate(orderInfoVo.getFinishTime()));
        }
        if(orderInfoVo.getInsuranceCompanyId() != null){
            orderInfoDTO.setInsuranceCompanyId(Long.parseLong(orderInfoVo.getInsuranceCompanyId().toString()));
        }
        if(orderInfoVo.getInvoiceType() != null){
            orderInfoDTO.setInvoiceType(Long.parseLong(orderInfoVo.getInvoiceType().toString()));
        }
        if(orderInfoVo.getReceiver() != null){
            orderInfoDTO.setReceiver(Long.parseLong(orderInfoVo.getReceiver().toString()));
        }
        if(orderInfoVo.getServiceCount() != null){
            orderInfoDTO.setServiceCount(Long.parseLong(orderInfoVo.getServiceCount().toString()));
        }
        if(orderInfoVo.getSignAmount() != null){
            orderInfoDTO.setSignAmount(new BigDecimal(orderInfoVo.getSignAmount()));
        }
        if(orderInfoVo.getServiceDiscount() != null){
            orderInfoDTO.setServiceDiscount(new BigDecimal(orderInfoVo.getServiceDiscount()));
        }
        if(StringUtils.isNotBlank(orderInfoVo.getPayTime())){
            orderInfoDTO.setPayTime(DateUtil.convertStringToDate(orderInfoVo.getPayTime()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getCreateTime())) {
            orderInfoDTO.setCreateTime(DateUtil.convertStringToDate(orderInfoVo.getCreateTime()));
        }
    }

    /**
     * 获取淘汽优惠券金额核销接口
     *
     * @param source        来源
     * @param orderId       工单id
     * @param taoqiCouponSn 淘汽优惠券
     *
     * @return
     */
    @Override
    public Result<BigDecimal> getTaoqiCouponSn(String source, Long orderId, String taoqiCouponSn) {
        log.info("【工单结算】：使用淘汽优惠券，查询优惠券金额，来源：{}，工单id：{},优惠券：{}", source, orderId, taoqiCouponSn);
        if (StringUtils.isBlank(source)) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(), LegendErrorCode.DUBBO_SOURCE_NULL_EX.getErrorMessage());
        }
        if (orderId == null || Long.compare(orderId, 0l) <= 0) {
            return Result.wrapErrorResult(LegendErrorCode.APP_ORDER_NOTEXSIT_ERROR.getCode(), "工单id有误");
        }
        if (StringUtils.isBlank(taoqiCouponSn)) {
            return Result.wrapErrorResult(LegendErrorCode.APP_PARAM_ERROR.getCode(), "优惠券为空");
        }
        try {
            com.tqmall.legend.common.Result couponCheck = settlementFacade.couponCheck(orderId, taoqiCouponSn);
            if (couponCheck.isSuccess()) {
                BigDecimal couponAmount = new BigDecimal((String) couponCheck.getData());
                return Result.wrapSuccessfulResult(couponAmount);
            } else {
                String errorMsg = couponCheck.getErrorMsg();
                log.info("【工单结算】：使用淘汽优惠券，查询优惠券金额，返回失败，错误信息为{}", errorMsg);
                return Result.wrapErrorResult(couponCheck.getCode(), errorMsg);
            }
        } catch (Exception e) {
            log.info("【工单结算】：使用淘汽优惠券，查询优惠券金额，出现异常", e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }
    }

    /**
     * 工单收款
     *
     * @param param
     *
     * @return
     */
    @Override
    public Result debitOrder(DebitBillFlowSaveParam param) {
        if (param == null) {
            return Result.wrapErrorResult("", "参数为空");
        }
        Long shopId = param.getShopId();
        log.info("【工单收款】：来源：{}，工单id：{}", param.getSource(), param.getOrderId());
        if (StringUtils.isBlank(param.getSource())) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(), LegendErrorCode.DUBBO_SOURCE_NULL_EX.getErrorMessage());
        }
        if (param.getShopId() == null || param.getShopId().compareTo(1l) < 0) {
            log.error("[工单收款] 店铺的ID错误. shopId:{}", param.getShopId());
            return Result.wrapErrorResult(LegendErrorCode.APP_SHOP_ID_ERROR.getCode(), "门店id错误");
        }
        if (param.getOrderId() == null || Long.compare(param.getOrderId(), 0l) <= 0) {
            return Result.wrapErrorResult(LegendErrorCode.APP_ORDER_NOTEXSIT_ERROR.getCode(), "工单id有误");
        }
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(param.getOrderId(), param.getShopId());
        if (!orderInfoOptional.isPresent()) {
            return Result.wrapErrorResult(LegendErrorCode.APP_ORDER_NOTEXSIT_ERROR.getCode(), "工单不存在");
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        List<DebitBillFlowParam> flowList = param.getFlowList();

        DebitBillFlowSaveBo flowSaveBo = new DebitBillFlowSaveBo();
        flowSaveBo.setShopId(param.getShopId());
        flowSaveBo.setOrderId(param.getOrderId());
        flowSaveBo.setUserId(param.getCreator().longValue());
        flowSaveBo.setRemark(param.getRemark());
        flowSaveBo.setFlowTime(param.getFlowTime());
        List<DebitBillFlowBo> flowBoList = new ArrayList<>();

        // 使用会员余额支付
        CardMemberParam cardMember = param.getCardMember();
        if (cardMember != null && cardMember.getId() != null) {

            // check 会员卡信息 [[
            // check 会员卡ID是否存在
            Long cardMemberId = cardMember.getId();
            BigDecimal memberBalancePay = cardMember.getMemberPayAmount();
            if (cardMemberId == null && (memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) == 1)) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,对应的会员卡不存在");
            }
            // check 会员卡是否存在
            MemberCard memberCard = memberCardService.getMemberCardById(shopId, cardMemberId);
            if (memberCard == null) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,对应的会员卡不存在");
            }
            // check 会员卡过期
            if (memberCard.isExpired()) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,对应的会员卡已过期");
            }
            // check 会员卡余额支付金额 <0
            if (memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) == -1) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付的金额小于0元");
            }
            // check 会员卡余额支付金额 <=会员卡余额
            BigDecimal memberCardBalance = memberCard.getBalance();
            if (memberBalancePay != null && memberBalancePay.compareTo(memberCardBalance) == 1) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,会员卡余额不足");
            }
            // ]]

            if (memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) > 0) {
                DebitBillFlowBo flowBo = new DebitBillFlowBo();
                flowBo.setPaymentId(0L);
                flowBo.setPaymentName("会员卡");
                flowBo.setPayAmount(memberBalancePay);
                flowBoList.add(flowBo);

                flowSaveBo.setMemberPayAmount(memberBalancePay);
                flowSaveBo.setMemberCardId(cardMemberId);
            }
        }

        // 使用支付方式支付
        if (!CollectionUtils.isEmpty(flowList)) {
            Map<Long, String> paymentNameMap = Maps.newHashMap();
            // 查询门店所有支付方式
            List<Payment> paymentList = paymentService.getPaymentsByShopId(param.getShopId());
            if (!CollectionUtils.isEmpty(paymentList)) {
                for (Payment payment : paymentList) {
                    paymentNameMap.put(payment.getId(), payment.getName());
                }
            }
            for (DebitBillFlowParam flowParam : flowList) {
                DebitBillFlowBo flowBo = new DebitBillFlowBo();
                flowBo.setPaymentId(flowParam.getPaymentId());
                flowBo.setPaymentName(paymentNameMap.get(flowParam.getPaymentId()));
                flowBo.setPayAmount(flowParam.getPayAmount());
                flowBoList.add(flowBo);
            }
        }
        //工单收款
        try {
            debitFacade.setDownPaymentFlow(orderInfo.getPayAmount(), flowBoList, orderInfo);
            flowSaveBo.setFlowList(flowBoList);
            debitFacade.saveFlowList(flowSaveBo);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * app确认账单
     *
     * @param confirmBillParam
     *
     * @return
     */
    @Override
    public Result confirmBill(ConfirmBillParam confirmBillParam) {
        log.info("【dubbo：确认对账】传参{}", confirmBillParam);
        try {
            Result<ConfirmBillBo> result = checkParamAndGetConfirmBo(confirmBillParam);
            if (result.isSuccess()) {
                ConfirmBillBo confirmBillBo = result.getData();
                UserInfo userInfo = new UserInfo();
                Long shopId = confirmBillParam.getShopId();
                Long userId = confirmBillParam.getUserId();
                ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
                userInfo.setShopId(shopId);
                userInfo.setUserId(userId);
                userInfo.setName(shopManager.getName());
                com.tqmall.legend.common.Result confirmBillResult = confirmBillFacade.confirmBill(confirmBillBo, userInfo);
                if (confirmBillResult.isSuccess()) {
                    return Result.wrapSuccessfulResult(true);
                }
                return Result.wrapErrorResult(confirmBillResult.getCode(), confirmBillResult.getErrorMsg());
            } else {
                return Result.wrapErrorResult(result.getCode(), result.getMessage());
            }
        } catch (BizException e) {
            log.error("【dubbo：确认对账】：出现异常", e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("【dubbo：确认对账】：出现异常", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "确认对账失败");
    }

    @Override
    public Result speedilyConfirmBill(@NotNull SpeedilyConfirmBillParam confirmBillParam) {

        log.info("[dubbo] 快修快保账单确认收款 ,参数:{}", confirmBillParam.toString());

        // 包装当前客户信息[[
        Long shopId = confirmBillParam.getShopId();
        Long userId = confirmBillParam.getUserId();
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        if (shopManager == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "用户不存在");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(shopManager.getId());
        userInfo.setName(shopManager.getName());
        userInfo.setShopId(shopManager.getShopId());
        // ]]

        // check param  [[
        // 工单
        Long orderId = confirmBillParam.getOrderId();
        if (orderId == null || orderId < 0l) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.ORDER_NOTEXSIT_ERROR.getCode(), "工单ID不存在或无效");
        }
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.ORDER_NOTEXSIT_ERROR.getCode(), "此门店下无该工单");
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        // 是否重复结算
        String orderStatus = orderInfo.getOrderStatus();
        if (orderStatus != null && OrderStatusEnum.DDYFK.getKey().equals(orderStatus)) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.CONFIRM_BILL_ERROR.getCode(), "");
        }
        // 工单应收总金额(>0)
        BigDecimal receivableAmount = confirmBillParam.getReceivableAmount();
        if (receivableAmount == null || receivableAmount.compareTo(BigDecimal.ZERO) == -1) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "工单应收总金额小于0元");
        }
        // ]]

        // 支付 [[
        // 会员余额支付(>0)
        BigDecimal memberBalancePay = confirmBillParam.getMemberBalancePay();
        // check 会员卡ID是否存在
        Long memberIdForSettle = confirmBillParam.getMemberIdForSettle();
        if (memberIdForSettle == null && (memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) == 1)) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,对应的会员卡不存在");
        }
        if (memberIdForSettle != null) {
            // check 会员卡是否存在
            MemberCard memberCard = memberCardService.getMemberCardById(shopId, memberIdForSettle);
            if (memberCard == null) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,对应的会员卡不存在");
            }
            // check 会员卡过期
            if (memberCard.isExpired()) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,对应的会员卡已过期");
            }
            // check 会员卡余额支付金额 <0
            if (memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) == -1) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付的金额小于0元");
            }
            // check 会员卡余额支付金额 <=会员卡余额
            BigDecimal memberCardBalance = memberCard.getBalance();
            if (memberBalancePay != null && memberBalancePay.compareTo(memberCardBalance) == 1) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡余额支付失败,会员卡余额不足");
            }
        }

        // 收款渠道
        List<PayChannel> payChannels = confirmBillParam.getPayChannelList();
        if (!CollectionUtils.isEmpty(payChannels)) {
            for (PayChannel payChannel : payChannels) {
                Long payChannelId = payChannel.getChannelId();
                String ChannelName = payChannel.getChannelName();
                BigDecimal payAmount = payChannel.getPayAmount();
                // 是否有效的收款方式
                if (payChannelId == null || payAmount == null) {
                    return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "无效收款方式");

                }
                if (payChannelId < 0l) {
                    return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "无效收款方式");
                }

                // check 是否有效收款金额
                if (payAmount.compareTo(BigDecimal.ZERO) == -1) {
                    return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "收款方式为'" + ChannelName + "',对应的收款金额小于0元");
                }
            }
        }
        // ]]

        // 包装确认账单实体
        Result<ConfirmBillBo> wrapperConfirmBillBoResult = checkParamAndGetConfirmBo(confirmBillParam);
        if (!wrapperConfirmBillBoResult.isSuccess()) {
            return wrapperConfirmBillBoResult;
        }
        ConfirmBillBo wrapperConfirmBill = wrapperConfirmBillBoResult.getData();

        // wrapper 实体包装[[
        SpeedilyBillBo speedilyBillBo = new SpeedilyBillBo();
        speedilyBillBo.setMemberIdForSettle(memberIdForSettle);
        try {
            BeanUtils.copyProperties(wrapperConfirmBill, speedilyBillBo);
        } catch (BeansException e) {
            log.error("[dubbo快修快报结算] 确认账单数据有误,异常信息:{}", e);
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "确认账单数据有误!");
        }

        // 收款渠道
        List<PayChannelBo> payChannelBoList = new ArrayList<PayChannelBo>();
        // 计算总收款
        BigDecimal totalPayAmount = BigDecimal.ZERO;
        try {
            if (!CollectionUtils.isEmpty(payChannels)) {
                PayChannelBo payChannelBo = null;
                for (PayChannel payChannel : payChannels) {
                    payChannelBo = new PayChannelBo();
                    BeanUtils.copyProperties(payChannel, payChannelBo);
                    payChannelBoList.add(payChannelBo);
                    //计算收款总金额
                    totalPayAmount = totalPayAmount.add(payChannel.getPayAmount());
                }
            }
        } catch (BeansException e) {
            log.error("[dubbo快修快报结算]收款方式数据有误,异常信息:{}", e);
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "收款渠道数据有误!");
        }

        // 收录现金渠道
        if (memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) == 1) {
            PayChannelBo memberBalancePayChannel = new PayChannelBo();
            // TODO 会员卡对应编号enum
            memberBalancePayChannel.setChannelId(0l);
            memberBalancePayChannel.setChannelName("会员卡");
            memberBalancePayChannel.setPayAmount(memberBalancePay);
            payChannelBoList.add(memberBalancePayChannel);

            speedilyBillBo.setMemberBalancePay(memberBalancePay);
            //计算收款总金额
            totalPayAmount = totalPayAmount.add(memberBalancePay);
        }
        // 校验收款是否大于应收
        if(totalPayAmount.compareTo(receivableAmount) == 1){
            return Result.wrapErrorResult("", "'收款金额'大于'应收金额'");
        }
        // 数据有效，设置收款方式
        speedilyBillBo.setPayChannelBoList(payChannelBoList);
        // 提交快修快保结算
        com.tqmall.legend.common.Result drawUpResult = null;
        try {
            drawUpResult = confirmBillFacade.speedilyDrawUp(speedilyBillBo, orderInfoOptional.get(), userInfo);
        } catch (BizException e1) {
            log.error("[dubbo快修快报结算]工单收款异常,原因:{}", e1);
            String errorMessage = e1.getMessage();
            errorMessage =(errorMessage ==null) ? "账单确认收款失败!" :"[原因] "+errorMessage;
            return Result.wrapErrorResult(LegendSettlementErrorCode.CONFIRM_BILL_ERROR.getCode(), errorMessage);
        }catch (Exception e2) {
            log.error("[dubbo快修快报结算]工单收款失败,原因:{}", e2);
            return Result.wrapErrorResult(LegendSettlementErrorCode.CONFIRM_BILL_ERROR.getCode(), "账单确认收款失败!");
        }

        if (!drawUpResult.isSuccess()) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.CONFIRM_BILL_ERROR.getCode(), drawUpResult.getErrorMsg());
        }

        return Result.wrapSuccessfulResult("快修快保账单确认和收款 成功");
    }


    @Override
    public Result carwashCreate(@NotNull CarwashCreateBillParam carwashCreateParam) {

        log.info("[dubbo]洗车单创建, 入参:{}", carwashCreateParam.toString());

        // TODO extract 获取当前操作人
        Long shopId = carwashCreateParam.getShopId();
        Long userId = carwashCreateParam.getUserId();
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        if (shopManager == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "用户不存在");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(shopManager.getId());
        userInfo.setName(shopManager.getName());
        userInfo.setShopId(shopManager.getShopId());

        // check [[
        // 车牌
        String carLicense = carwashCreateParam.getCarLicense();
        if (StringUtils.isEmpty(carLicense)) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "车牌不存在");
        }
        if(carLicense.equals(Constants.CARWASH_DEFAULT_LICENSE)){
            Long increment = jedisClient.incrHash("CARWASH_DEFAULT_LICENSE" , ""+userInfo.getShopId() , 1);
            carwashCreateParam.setCarLicense(Constants.CARWASH_DEFAULT_LICENSE + increment);
        }
        // 服务顾问 APP 没有服务顾问,默认为当前用户
        carwashCreateParam.setReceiver(userId);
        carwashCreateParam.setReceiverName(userInfo.getName());

        // 开单日期
        String createTimeStr = carwashCreateParam.getCreateTimeStr();
        if (StringUtils.isEmpty(createTimeStr)) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "开单日期不能为空");
        }
        // 洗车工
        String workerIds = carwashCreateParam.getWorkerIds();
        if (StringUtils.isEmpty(workerIds)) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "未指派洗车工");
        }
        String[] workerIdsArray = workerIds.split(",");
        if(workerIdsArray.length > 8){
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "洗车工最多选择8个");
        }
        // 收款金额
        BigDecimal servicePrice = carwashCreateParam.getServicePrice();
        if (servicePrice == null || servicePrice.compareTo(BigDecimal.ZERO) == -1) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "收款金额无效");
        }
        // 结算方式(现金\会员卡\优惠券\其他)
        PayChannel payChannel = carwashCreateParam.getPayChannel();
        Long cardMemberId = carwashCreateParam.getCardMemberId();
        List<DiscountSelectedComboParam> discountSelectedComboParamList = carwashCreateParam.getDiscountSelectedComboParamList();
        List<DiscountSelectedCouponParam> discountSelectedCouponParamList = carwashCreateParam.getDiscountSelectedCouponParamList();
        if (payChannel == null && cardMemberId == null && (CollectionUtils.isEmpty(discountSelectedComboParamList) && CollectionUtils.isEmpty(discountSelectedCouponParamList))) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "未选中'结算方式'");

        }
        // 使用会员卡余额结算,检验会员卡是否存在
        if (cardMemberId != null && cardMemberId > 0l) {
            MemberCard memberCard = memberCardService.getMemberCardById(shopId, cardMemberId);
            if (memberCard == null) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "使用'会员卡余额'结算,对应会员卡不存在");
            }
            //如果会员卡过期,返回错误信息
            if(memberCard.isExpired()){
                return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "会员卡已过期,请选择其他方式结算");
            }
        }

        // 包装参数
        CarwashOrderFormBo carwashOrderFormBo = new CarwashOrderFormBo();
        // 优惠券列表
        if (!CollectionUtils.isEmpty(discountSelectedComboParamList) || !CollectionUtils.isEmpty(discountSelectedCouponParamList)) {
            Result<List<OrderDiscountFlowBo>> wrapperConfirmBillBoResult = wrapperCouponListForCarWash(carwashCreateParam);
            if (!wrapperConfirmBillBoResult.isSuccess()) {
                return wrapperConfirmBillBoResult;
            }
            List<OrderDiscountFlowBo> orderDiscountFlowBos = wrapperConfirmBillBoResult.getData();
            if (!CollectionUtils.isEmpty(orderDiscountFlowBos)) {
                carwashOrderFormBo.setOrderDiscountFlowParamList(orderDiscountFlowBos);
            }
        }

        try {
            BeanUtils.copyProperties(carwashCreateParam, carwashOrderFormBo);
        } catch (BeansException e) {
            log.error("洗车单数据有误,异常信息:{}", e);
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "");
        }

        com.tqmall.legend.common.Result<CreateCarWashResponse> drawUpResult = null;
        try {
            drawUpResult = carWashFacade.createForApp(carwashOrderFormBo, userInfo);
        } catch (Exception e) {
            log.error("APP创建洗车单异常,异常信息:{}", e);
            return Result.wrapErrorResult(LegendSettlementErrorCode.CONFIRM_BILL_ERROR.getCode(), "洗车单创建失败");
        }

        if (!drawUpResult.isSuccess()) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.CONFIRM_BILL_ERROR.getCode(), drawUpResult.getErrorMsg());
        }
        CreateCarWashResponse response = drawUpResult.getData();

        return Result.wrapSuccessfulResult(response.getOrderId());
    }

    /**
     * 查询历史收款记录
     *
     * @param shopId
     * @param orderId
     *
     * @return
     */
    @Override
    public Result<OrderDebitBillDTO> findDebitBill(Long shopId, Long orderId) {
        if (shopId == null || shopId < 1) {
            log.error("[收款记录] 店铺id错误. shopId:{}", shopId);
            return Result.wrapErrorResult("", "门店不存在");
        }
        if (orderId == null || orderId < 1) {
            log.error("[收款记录] 工单id错误. orderId:{}", orderId);
            return Result.wrapErrorResult(LegendSettlementErrorCode.ORDER_NOTEXSIT_ERROR.getCode(), "工单不存在");
        }
        // 查询工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            log.error("[收款记录] 工单id错误. orderId:{}, shopId:{}", orderId, shopId);
            return Result.wrapErrorResult(LegendSettlementErrorCode.ORDER_NOTEXSIT_ERROR.getCode(), "门店下此工单不存在");
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        OrderDebitBillDTO orderDebitBillDTO = new OrderDebitBillDTO();
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        try {
            BeanUtils.copyProperties(orderInfo, orderInfoDTO);
        } catch (Exception e) {
            log.error("[收款单] 属性复制错误,e={}", e);
            return Result.wrapErrorResult("", "内部错误");
        }
        orderDebitBillDTO.setOrderInfoDTO(orderInfoDTO);
        orderDebitBillDTO.setTotalAmount(orderInfo.getOrderAmount());

        List<HistoryFlowDTO> historyFlowDTOList = Lists.newArrayList();
        // 查询优惠流水
        List<OrderDiscountFlowVo> discountFlowVoList = orderDiscountFlowFacade.getOrderDiscountFlow(orderId, shopId);
        if (!CollectionUtils.isEmpty(discountFlowVoList)) {
            for (OrderDiscountFlowVo flowVo : discountFlowVoList) {
                HistoryFlowDTO flowDTO = new HistoryFlowDTO();
                flowDTO.setGmtCreate(flowVo.getGmtCreate());
                flowDTO.setName(flowVo.getDiscountName());
                flowDTO.setAmount(flowVo.getDiscountAmount());
                flowDTO.setOperatorName(flowVo.getOperatorName());
                historyFlowDTOList.add(flowDTO);
            }
        }

        // 查询收款单和流水
        DebitBillAndFlowVo debitBillAndFlow = debitFacade.findDebitBillAndFlow(shopId, orderId);
        if (debitBillAndFlow != null) {
            DebitBillVo debitBillVo = debitBillAndFlow.getDebitBillVo();
            if (debitBillVo != null) {
                orderDebitBillDTO.setBillId(debitBillVo.getId());
                orderDebitBillDTO.setTotalAmount(debitBillVo.getTotalAmount());
                orderDebitBillDTO.setReceivableAmount(debitBillVo.getReceivableAmount());
                orderDebitBillDTO.setPaidAmount(debitBillVo.getPaidAmount());
                orderDebitBillDTO.setSignAmount(debitBillVo.getSignAmount());
            }
            List<DebitBillFlowVo> debitBillFlowVoList = debitBillAndFlow.getDebitBillFlowVoList();
            if (!CollectionUtils.isEmpty(debitBillFlowVoList)) {
                for (DebitBillFlowVo flowVo : debitBillFlowVoList) {
                    HistoryFlowDTO flowDTO = new HistoryFlowDTO();
                    flowDTO.setGmtCreate(flowVo.getFlowTime());
                    String paymentName = flowVo.getPaymentName();
                    if (flowVo.getFlowStatus() == 0) {
                        if (flowVo.getFlowType() == 1) {
                            paymentName = "冲红" + paymentName;
                        }
                    } else {
                        paymentName = "坏账";
                    }
                    flowDTO.setName(paymentName);
                    flowDTO.setAmount(flowVo.getPayAmount());
                    flowDTO.setOperatorName(flowVo.getOperatorName());
                    historyFlowDTOList.add(flowDTO);
                }
            }
        }
        orderDebitBillDTO.setHistoryFlowList(historyFlowDTOList);
        return Result.wrapSuccessfulResult(orderDebitBillDTO);
    }

    @Override
    public Result<Boolean> sendCode(final SettlementSmsParam settlementSmsParam) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(settlementSmsParam, "参数为空");
                Assert.notNull(settlementSmsParam.getLicense(), "车牌为空");
                boolean checkMobile = StringUtil.isMobileNO(settlementSmsParam.getMobile());
                Assert.isTrue(checkMobile, "手机号为空或格式有误");
                Assert.notNull(settlementSmsParam.getShopId(), "shopId为空");
            }

            @Override
            protected Boolean process() throws BizException {
                SettlementSmsBO settlementSmsBO = new SettlementSmsBO();
                BdUtil.do2bo(settlementSmsParam, settlementSmsBO);
                return settlementFacade.sendCode(settlementSmsBO, settlementSmsParam.getShopId());
            }
        }.execute();
    }

    @Override
    public Result<Boolean> checkCode(final GuestMobileCheckParam guestMobileCheckParam) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(guestMobileCheckParam, "参数为空");
                String mobile = guestMobileCheckParam.getGuestMobile();
                boolean checkMobile = StringUtil.isMobileNO(mobile);
                Assert.isTrue(checkMobile, "手机号为空或格式有误");
                Assert.notNull(guestMobileCheckParam.getShopId(), "shopId为空");
                Assert.notNull(guestMobileCheckParam.getCode(), "验证码为空");
            }

            @Override
            protected Boolean process() throws BizException {
                String mobile = guestMobileCheckParam.getGuestMobile();
                String code = guestMobileCheckParam.getCode();
                Long shopId = guestMobileCheckParam.getShopId();
                return settlementFacade.checkCode(mobile, code, shopId);
            }
        }.execute();
    }

    /**
     * 校验参数，并获取包装好后了确认账单对象
     *
     * @param confirmBillParam
     *
     * @return
     */
    private Result<ConfirmBillBo> checkParamAndGetConfirmBo(ConfirmBillParam confirmBillParam) {
        //数据校验
        if (confirmBillParam == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "参数为空");
        }
        Long orderId = confirmBillParam.getOrderId();
        if (orderId == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "工单id不能为空");
        }
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "工单不存在");
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        Long shopId = confirmBillParam.getShopId();
        if (shopId == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "门店id为空");
        }
        if (!shopId.equals(orderInfo.getShopId())) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "不是此门店的工单，无法确认账单");
        }
        Long userId = confirmBillParam.getUserId();
        if (userId == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "用户id为空");
        }
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        if (shopManager == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "用户不存在");
        }
        BigDecimal receivableAmount = confirmBillParam.getReceivableAmount();//应收金额
        if (receivableAmount == null) {
            return Result.wrapErrorResult(LegendSettlementErrorCode.PARAMS_ERROR.getCode(), "应收金额不能为空");
        }
        ConfirmBillBo confirmBillBo = new ConfirmBillBo();
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;//优惠总金额
        //调用会员接口获取优惠信息
        Long memberCardId = confirmBillParam.getMemberCardId();
        List<DiscountSelectedCouponParam> discountSelectedCouponParamList = confirmBillParam.getDiscountSelectedCouponParamList();
        List<DiscountSelectedComboParam> discountSelectedComboParamList = confirmBillParam.getDiscountSelectedComboParamList();
        if (memberCardId != null || !CollectionUtils.isEmpty(discountSelectedCouponParamList) || !CollectionUtils.isEmpty(discountSelectedComboParamList)) {
            //使用其他车主的会员卡、优惠券，需要传手机号和验证码
            String mobile = confirmBillParam.getGuestMobile();
            DiscountSelectedBo discountSelectedBo = getDiscountSelectedBo(mobile, memberCardId, confirmBillParam.getDiscountAmount(), discountSelectedCouponParamList, discountSelectedComboParamList);
            DiscountInfoBo discountInfoBo = null;
            try {
                discountInfoBo = discountCenter2.discountOrder(shopId, orderId, discountSelectedBo);
                log.info("【dubbo：确认对账】：获取优惠信息：{}", discountInfoBo);
            } catch (BizException e) {
                log.error("【dubbo：确认对账】：获取会员信息异常", e);
                return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
            } catch (Exception e) {
                log.error("【dubbo：确认对账】：获取会员信息异常", e);
                return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "确认对账失败");
            }
            if (discountInfoBo != null) {
                //校验会员卡传的是否正确
                if (memberCardId != null) {
                    List<AccountCardDiscountBo> accountCardDiscountBoList = Lists.newArrayList();
                    List<AccountCardDiscountBo> cardList = discountInfoBo.getSortedCardList();
                    List<AccountCardDiscountBo> guestCardList = discountInfoBo.getSortedGuestCardList();
                    accountCardDiscountBoList.addAll(cardList);
                    accountCardDiscountBoList.addAll(guestCardList);
                    if(CollectionUtils.isEmpty(accountCardDiscountBoList)){
                        return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "会员卡不存在或数据不完整");
                    }
                    AccountCardDiscountBo cardDiscountBo = null;
                    for(AccountCardDiscountBo accountCardDiscountBo : accountCardDiscountBoList){
                        if(accountCardDiscountBo.isSelected()){
                            cardDiscountBo = accountCardDiscountBo;
                        }
                    }
                    if (cardDiscountBo == null) {
                        return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "会员卡不存在或数据不完整");
                    }
                    Long id = cardDiscountBo.getCardId();
                    if (!memberCardId.equals(id)) {
                        log.info("【dubbo：确认对账】：会员卡有误，传入id：{}，实际id：{}", memberCardId, id);
                        return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "会员卡有误");
                    }
                    if (!cardDiscountBo.isAvailable()) {
                        log.info("【dubbo：确认对账】：会员卡未选择或者不可用", memberCardId, id);
                        return Result.wrapErrorResult("", "会员卡未选择或者不可用");
                    }
                    //会员优惠金额
                    BigDecimal discountAmount = confirmBillParam.getDiscountAmount();
                    if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) == -1) {
                        return Result.wrapErrorResult(LegendSettlementErrorCode.MONNY_ERROR.getCode(), "会员卡优惠金额有误");
                    }
                    totalDiscountAmount = totalDiscountAmount.add(discountAmount);
                    //设置会员卡信息
                    confirmBillBo.setMemberCardId(memberCardId);
                    confirmBillBo.setAccountId(cardDiscountBo.getAccountId());
                    confirmBillBo.setCardNumber(cardDiscountBo.getCardNumber());
                    confirmBillBo.setDiscountAmount(discountAmount);
                    confirmBillBo.setCardDiscountReason(cardDiscountBo.getDiscountDesc());
                } else {
                    //使用账户优惠，但是未使用会员卡优惠
                    BigDecimal discountAmount = confirmBillParam.getDiscountAmount();
                    if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                        confirmBillBo.setDiscountAmount(discountAmount);
                        totalDiscountAmount = totalDiscountAmount.add(discountAmount);
                    }
                }
                List<OrderDiscountFlowBo> orderDiscountFlowBoList = Lists.newArrayList();
                //设置优惠流水
                if (!CollectionUtils.isEmpty(discountSelectedCouponParamList)) {
                    //校验优惠券是否存在
                    List<AccountCouponDiscountBo> accountCouponDiscountBoList = discountInfoBo.getSortedAllCouponList();
                    if (CollectionUtils.isEmpty(accountCouponDiscountBoList)) {
                        log.info("【dubbo：确认对账】：优惠券不存在或已经被使用");
                        return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "优惠券不存在或已经被使用");
                    }
                    Map<Long, AccountCouponDiscountBo> discountCouponBoMap = Maps.newHashMap();
                    for (AccountCouponDiscountBo accountCouponDiscountBo : accountCouponDiscountBoList) {
                        Long couponId = accountCouponDiscountBo.getCouponId();
                        discountCouponBoMap.put(couponId, accountCouponDiscountBo);
                    }
                    for (DiscountSelectedCouponParam discountSelectedCouponParam : discountSelectedCouponParamList) {
                        Long couponId = discountSelectedCouponParam.getAccountCouponId();
                        if (!discountCouponBoMap.containsKey(couponId)) {
                            log.info("【dubbo：确认对账】：优惠券不存在或已经被使用，券id：{}", couponId);
                            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "优惠券不存在或已经被使用");
                        }
                        AccountCouponDiscountBo accountCouponDiscountBo = discountCouponBoMap.get(couponId);
                        //券类型不同不同逻辑
                        CouponTypeEnum couponTypeEnum = accountCouponDiscountBo.getCouponType();
                        Integer couponType = couponTypeEnum.getCode();
                        OrderDiscountFlowBo orderDiscountFlowBo = new OrderDiscountFlowBo();
                        orderDiscountFlowBo.setCouponId(couponId);
                        orderDiscountFlowBo.setShopId(shopId);
                        orderDiscountFlowBo.setOrderId(orderId);
                        orderDiscountFlowBo.setAccountId(accountCouponDiscountBo.getAccountId());
                        orderDiscountFlowBo.setDiscountName(couponTypeEnum.getAlias());
                        orderDiscountFlowBo.setDiscountSn(accountCouponDiscountBo.getCouponSn());
                        orderDiscountFlowBo.setDiscountRate(BigDecimal.ONE);//不打折，设置默认值
                        //不同券类型设置不同值
                        BigDecimal discountAmount = BigDecimal.ZERO;
                        if (couponType == CouponTypeEnum.CASH_COUPON.getCode()) {
                            //现金券，取接口返回的优惠金额
                            discountAmount = accountCouponDiscountBo.getFinalDiscount();
                            orderDiscountFlowBo.setDiscountType(OrderDiscountTypeEnum.CASHCOUPON.getCode());
                        } else if (couponType == CouponTypeEnum.UNIVERSAL_COUPON.getCode()) {
                            //通用券，可输入金额
                            discountAmount = discountSelectedCouponParam.getDiscountAmount();//传的优惠金额
                            if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) == -1) {
                                log.info("【dubbo：确认对账】：通用券金额为空或小于0，券id：{}", couponId);
                                return Result.wrapErrorResult(LegendSettlementErrorCode.MONNY_ERROR.getCode(), "通用券金额有误");
                            }
                            orderDiscountFlowBo.setDiscountType(OrderDiscountTypeEnum.UNIVERSALCOUPON.getCode());
                        }
                        orderDiscountFlowBo.setDiscountAmount(discountAmount);
                        orderDiscountFlowBoList.add(orderDiscountFlowBo);
                        totalDiscountAmount = totalDiscountAmount.add(discountAmount);
                    }
                }
                //选择计次卡
                if (!CollectionUtils.isEmpty(discountSelectedComboParamList)) {
                    //校验优惠券是否存在
                    List<AccountComboDiscountBo> accountComboDiscountBoList = Lists.newArrayList();
                    List<AccountComboDiscountBo> comboList = discountInfoBo.getSortedComboList();
                    List<AccountComboDiscountBo> guestComboList = discountInfoBo.getSortedGuestComboList();
                    accountComboDiscountBoList.addAll(comboList);
                    accountComboDiscountBoList.addAll(guestComboList);
                    if (CollectionUtils.isEmpty(accountComboDiscountBoList)) {
                        log.info("【dubbo：确认对账】：计次卡不存在或已经被使用");
                        return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "计次卡不存在或已经被使用");
                    }
                    Map<Long, AccountComboDiscountBo> discountComboBoMap = Maps.newHashMap();
                    for (AccountComboDiscountBo accountComboDiscountBo : accountComboDiscountBoList) {
                        Long comboServiceId = accountComboDiscountBo.getComboServiceId();
                        discountComboBoMap.put(comboServiceId, accountComboDiscountBo);
                    }
                    for (DiscountSelectedComboParam discountSelectedComboParam : discountSelectedComboParamList) {
                        Long comboServiceId = discountSelectedComboParam.getComboServiceId();
                        if (!discountComboBoMap.containsKey(comboServiceId)) {
                            log.info("【dubbo：确认对账】：计次卡不存在或被使用，comboServiceId：{}", comboServiceId);
                            return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "计次卡不存在或已经被使用");
                        }
                        AccountComboDiscountBo accountComboDiscountBo = discountComboBoMap.get(comboServiceId);
                        OrderDiscountFlowBo orderDiscountFlowBo = new OrderDiscountFlowBo();
                        orderDiscountFlowBo.setShopId(shopId);
                        orderDiscountFlowBo.setOrderId(orderId);
                        orderDiscountFlowBo.setAccountId(accountComboDiscountBo.getAccountId());
                        orderDiscountFlowBo.setDiscountType(OrderDiscountTypeEnum.METERCARD.getCode());
                        orderDiscountFlowBo.setDiscountName(accountComboDiscountBo.getComboName());
                        orderDiscountFlowBo.setDiscountRate(BigDecimal.ONE);//不打折，设置默认值
                        BigDecimal discountAmount = accountComboDiscountBo.getDiscount();
                        orderDiscountFlowBo.setDiscountAmount(discountAmount);
                        orderDiscountFlowBo.setComboServiceId(comboServiceId);
                        orderDiscountFlowBo.setUseCount(discountSelectedComboParam.getUseCount());
                        orderDiscountFlowBoList.add(orderDiscountFlowBo);
                        totalDiscountAmount = totalDiscountAmount.add(discountAmount);
                    }
                }
                confirmBillBo.setOrderDiscountFlowBoList(orderDiscountFlowBoList);
            }
        } else {
            //未使用账户优惠，但是使用了优惠
            BigDecimal discountAmount = confirmBillParam.getDiscountAmount();
            if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                confirmBillBo.setDiscountAmount(discountAmount);
                totalDiscountAmount = totalDiscountAmount.add(discountAmount);
            }
        }
        BigDecimal orderAmount = orderInfo.getOrderAmount();
        if (orderAmount == null) {
            orderAmount = BigDecimal.ZERO;
        }
        //基本bo数据设置
        //后台计算:应收金额 = 工单总计金额 + 费用 - 总优惠金额(包括淘汽优惠)
        BigDecimal taxAmount = confirmBillParam.getTaxAmount();
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }
        BigDecimal checkReceivableAmount = orderAmount.add(taxAmount).subtract(totalDiscountAmount);
        //减去淘汽优惠
        String taoqiCouponSn = confirmBillParam.getTaoqiCouponSn();
        if (StringUtils.isNotBlank(taoqiCouponSn)) {
            //获取淘汽优惠券
            Result<BigDecimal> couponCheck = getTaoqiCouponSn(Constants.CUST_SOURCE, orderId, taoqiCouponSn);
            if (couponCheck == null || !couponCheck.isSuccess()) {
                return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "淘汽优惠券不可用");
            }
            BigDecimal taoqiCouponAmount = couponCheck.getData();
            //如果应收金额小于淘汽优惠金额，则优惠金额为0
            checkReceivableAmount = checkReceivableAmount.subtract(taoqiCouponAmount);
            if (checkReceivableAmount.compareTo(BigDecimal.ZERO) == -1) {
                //如果应收金额为100，淘汽优惠金额为200，则以上checkReceivableAmount=-100，实际淘汽优惠为100，因此taoqiCouponAmount=200+（-100）
                taoqiCouponAmount = taoqiCouponAmount.add(checkReceivableAmount);
                checkReceivableAmount = BigDecimal.ZERO;
            }
            confirmBillBo.setTaoqiCouponSn(taoqiCouponSn);
            confirmBillBo.setTaoqiCouponAmount(taoqiCouponAmount);
        }
        if (checkReceivableAmount.compareTo(receivableAmount) != 0) {
            log.info("【dubbo：确认对账】：传入的应收金额：{}，实际计算应收金额：{}，不一致", receivableAmount, checkReceivableAmount);
            return Result.wrapErrorResult(LegendSettlementErrorCode.MONNY_ERROR.getCode(), "应收金额有误");
        }
        confirmBillBo.setTaxAmount(taxAmount);

        //设置收款单
        DebitBillBo debitBillBo = new DebitBillBo();
        debitBillBo.setTotalAmount(orderAmount);//总计金额
        debitBillBo.setReceivableAmount(checkReceivableAmount);
        debitBillBo.setRemark(confirmBillParam.getRemark());
        debitBillBo.setRelId(orderId);
        confirmBillBo.setDebitBillBo(debitBillBo);
        return Result.wrapSuccessfulResult(confirmBillBo);
    }

    /**
     * 组织优惠对象
     *
     * @param mobile
     * @param memberCardId
     * @param discountAmount 会员卡优惠,如果大于0
     * @param discountSelectedCouponParamList
     * @param discountSelectedComboParamList
     * @return
     */
    private DiscountSelectedBo getDiscountSelectedBo(String mobile, Long memberCardId, BigDecimal discountAmount, List<DiscountSelectedCouponParam> discountSelectedCouponParamList, List<DiscountSelectedComboParam> discountSelectedComboParamList) {
        DiscountSelectedBo discountSelectedBo = new DiscountSelectedBo();
        discountSelectedBo.setGuestMobile(mobile);
        //使用卡
        if(memberCardId != null){
            SelectedCardBo selectedCardBo = new SelectedCardBo();
            selectedCardBo.setCardId(memberCardId);
            selectedCardBo.setCardDiscountAmount(discountAmount);
            discountSelectedBo.setSelectedCard(selectedCardBo);
        }
        //使用了券
        if (!CollectionUtils.isEmpty(discountSelectedCouponParamList)) {
            //使用优惠券
            List<SelectedCouponBo> selectedCouponBoList = Lists.newArrayList();
            for (DiscountSelectedCouponParam discountSelectedCouponParam : discountSelectedCouponParamList) {
                SelectedCouponBo selectedCouponBo = new SelectedCouponBo();
                selectedCouponBo.setCouponId(discountSelectedCouponParam.getAccountCouponId());
                selectedCouponBo.setCouponDiscountAmount(discountSelectedCouponParam.getDiscountAmount());
                selectedCouponBoList.add(selectedCouponBo);
            }
            discountSelectedBo.setSelectedCouponList(selectedCouponBoList);
        }
        if (!CollectionUtils.isEmpty(discountSelectedComboParamList)) {
            //使用计次卡
            List<SelectedComboBo> selectedComboBoList = Lists.newArrayList();
            for (DiscountSelectedComboParam discountSelectedComboParam : discountSelectedComboParamList) {
                SelectedComboBo selectedComboBo = new SelectedComboBo();
                selectedComboBo.setComboServiceId(discountSelectedComboParam.getComboServiceId());
                selectedComboBo.setCount(discountSelectedComboParam.getUseCount());
                selectedComboBoList.add(selectedComboBo);
            }
            discountSelectedBo.setSelectedComboList(selectedComboBoList);
        }
        return discountSelectedBo;
    }


    /**
     * 洗车单 合并优惠券
     *
     * @param carwashCreateParam
     *
     * @return
     */
    private Result<List<OrderDiscountFlowBo>> wrapperCouponListForCarWash(CarwashCreateBillParam carwashCreateParam) {
        // 合并折扣券集合
        List<OrderDiscountFlowBo> orderDiscountFlowBoList = Lists.newArrayList();

        // shopId
        Long shopId = carwashCreateParam.getShopId();
        // 车牌
        String carLicense = carwashCreateParam.getCarLicense();
        // 洗车服务ID
        Long serviceId = carwashCreateParam.getOrderServiceId();
        // 洗车总价格
        BigDecimal orderAmount = carwashCreateParam.getServicePrice();
        Long useMemberCardId = carwashCreateParam.getUseMemberCardId();
        // 已选择的计次卡
        List<DiscountSelectedComboParam> discountSelectedComboParamList = carwashCreateParam.getDiscountSelectedComboParamList();
        // 已选择的优惠券
        List<DiscountSelectedCouponParam> discountSelectedCouponParamList = carwashCreateParam.getDiscountSelectedCouponParamList();
        if(useMemberCardId == null && CollectionUtils.isEmpty(discountSelectedComboParamList) && CollectionUtils.isEmpty(discountSelectedCouponParamList)){
            return Result.wrapSuccessfulResult(orderDiscountFlowBoList);
        }
        String mobile = carwashCreateParam.getGuestMobile();
        //使用了券
        DiscountSelectedBo discountSelectedBo = getDiscountSelectedBo(mobile, useMemberCardId, carwashCreateParam.getDiscountAmount(), discountSelectedCouponParamList, discountSelectedComboParamList);
        DiscountInfoBo discountInfoBo = null;
        try {
            discountInfoBo = discountCenter2.discountCarWashOrder(shopId, carLicense, serviceId, orderAmount, discountSelectedBo);
            log.info("【dubbo：确认对账】：获取优惠信息：{}", discountInfoBo);
        } catch (BizException e) {
            log.error("【dubbo：确认对账】：获取会员信息异常", e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("【dubbo：确认对账】：获取会员信息异常", e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "确认对账失败");
        }
        if (discountInfoBo != null) {
            //设置优惠流水
            if (!CollectionUtils.isEmpty(discountSelectedCouponParamList)) {
                //校验优惠券是否存在
                List<AccountCouponDiscountBo> accountCouponDiscountBoList = discountInfoBo.getSortedAllCouponList();
                if (CollectionUtils.isEmpty(accountCouponDiscountBoList)) {
                    log.info("【dubbo：确认对账】：优惠券不存在或已经被使用");
                    return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "优惠券不存在或已经被使用");
                }
                Map<Long, AccountCouponDiscountBo> discountCouponBoMap = Maps.newHashMap();
                for (AccountCouponDiscountBo accountCouponDiscountBo : accountCouponDiscountBoList) {
                    Long couponId = accountCouponDiscountBo.getCouponId();
                    discountCouponBoMap.put(couponId, accountCouponDiscountBo);
                }
                for (DiscountSelectedCouponParam discountSelectedCouponParam : discountSelectedCouponParamList) {
                    Long couponId = discountSelectedCouponParam.getAccountCouponId();
                    if (!discountCouponBoMap.containsKey(couponId)) {
                        log.info("【dubbo：确认对账】：优惠券不存在或已经被使用，券id：{}", couponId);
                        return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "优惠券不存在或已经被使用");
                    }
                    AccountCouponDiscountBo accountCouponDiscountBo = discountCouponBoMap.get(couponId);
                    //券类型不同不同逻辑
                    CouponTypeEnum couponTypeEnum = accountCouponDiscountBo.getCouponType();
                    Integer couponType = couponTypeEnum.getCode();
                    OrderDiscountFlowBo orderDiscountFlowBo = new OrderDiscountFlowBo();
                    orderDiscountFlowBo.setCouponId(couponId);
                    orderDiscountFlowBo.setAccountId(accountCouponDiscountBo.getAccountId());
                    orderDiscountFlowBo.setShopId(shopId);
                    orderDiscountFlowBo.setDiscountName(couponTypeEnum.getAlias());
                    orderDiscountFlowBo.setDiscountSn(accountCouponDiscountBo.getCouponSn());
                    orderDiscountFlowBo.setDiscountRate(BigDecimal.ONE);//不打折，设置默认值
                    BigDecimal discountAmount = BigDecimal.ZERO;
                    if (couponType == CouponTypeEnum.CASH_COUPON.getCode()) {
                        //现金券，取接口返回的优惠金额
                        discountAmount = accountCouponDiscountBo.getFinalDiscount();
                        orderDiscountFlowBo.setDiscountType(OrderDiscountTypeEnum.CASHCOUPON.getCode());
                    } else if (couponType == CouponTypeEnum.UNIVERSAL_COUPON.getCode()) {
                        //通用券，可输入金额
                        discountAmount = discountSelectedCouponParam.getDiscountAmount();//传的优惠金额
                        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) == -1) {
                            log.info("【dubbo：确认对账】：通用券金额为空或小于0，券id：{}", couponId);
                            return Result.wrapErrorResult(LegendSettlementErrorCode.MONNY_ERROR.getCode(), "通用券金额有误");
                        }
                        orderDiscountFlowBo.setDiscountType(OrderDiscountTypeEnum.UNIVERSALCOUPON.getCode());
                    }
                    orderDiscountFlowBo.setDiscountAmount(discountAmount);
                    orderDiscountFlowBoList.add(orderDiscountFlowBo);
                }
            }
            //选择计次卡
            if (!CollectionUtils.isEmpty(discountSelectedComboParamList)) {
                //校验优惠券是否存在
                List<AccountComboDiscountBo> accountComboDiscountBoList = Lists.newArrayList();
                List<AccountComboDiscountBo> comboList = discountInfoBo.getSortedComboList();
                List<AccountComboDiscountBo> guestComboList = discountInfoBo.getSortedGuestComboList();
                accountComboDiscountBoList.addAll(comboList);
                accountComboDiscountBoList.addAll(guestComboList);
                if (CollectionUtils.isEmpty(accountComboDiscountBoList)) {
                    log.info("【dubbo：确认对账】：计次卡不存在或已经被使用");
                    return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "计次卡不存在或已经被使用");
                }
                Map<Long, AccountComboDiscountBo> discountComboBoMap = Maps.newHashMap();
                for (AccountComboDiscountBo accountComboDiscountBo : accountComboDiscountBoList) {
                    Long comboServiceId = accountComboDiscountBo.getComboServiceId();
                    discountComboBoMap.put(comboServiceId, accountComboDiscountBo);
                }
                for (DiscountSelectedComboParam discountSelectedComboParam : discountSelectedComboParamList) {
                    Long comboServiceId = discountSelectedComboParam.getComboServiceId();
                    if (!discountComboBoMap.containsKey(comboServiceId)) {
                        log.info("【dubbo：确认对账】：计次卡不存在或被使用，comboServiceId：{}", comboServiceId);
                        return Result.wrapErrorResult(LegendSettlementErrorCode.COUPON_ERROR.getCode(), "计次卡不存在或已经被使用");
                    }
                    AccountComboDiscountBo accountComboDiscountBo = discountComboBoMap.get(comboServiceId);
                    OrderDiscountFlowBo orderDiscountFlowBo = new OrderDiscountFlowBo();
                    orderDiscountFlowBo.setAccountId(accountComboDiscountBo.getAccountId());
                    orderDiscountFlowBo.setShopId(shopId);
                    orderDiscountFlowBo.setDiscountType(OrderDiscountTypeEnum.METERCARD.getCode());
                    orderDiscountFlowBo.setDiscountName(accountComboDiscountBo.getComboName());
                    orderDiscountFlowBo.setDiscountRate(BigDecimal.ONE);//不打折，设置默认值
                    BigDecimal discountAmount = accountComboDiscountBo.getDiscount();
                    orderDiscountFlowBo.setDiscountAmount(discountAmount);
                    orderDiscountFlowBo.setComboServiceId(comboServiceId);
                    orderDiscountFlowBo.setUseCount(discountSelectedComboParam.getUseCount());
                    orderDiscountFlowBoList.add(orderDiscountFlowBo);
                }
            }
        }
        return Result.wrapSuccessfulResult(orderDiscountFlowBoList);
    }

}
