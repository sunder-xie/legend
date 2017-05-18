package com.tqmall.legend.facade.settlement.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.DebitAndRedBillDTO;
import com.tqmall.legend.billcenter.client.dto.DebitBillAndFlowDTO;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.billcenter.client.dto.DebitBillFlowDTO;
import com.tqmall.legend.billcenter.client.enums.DebitTypeEnum;
import com.tqmall.legend.billcenter.client.param.DebitBillFlowParam;
import com.tqmall.legend.billcenter.client.param.DebitBillFlowSaveParam;
import com.tqmall.legend.billcenter.client.param.DebitFlowSearchParam;
import com.tqmall.legend.billcenter.client.result.DebitBillAndFlowResult;
import com.tqmall.legend.billcenter.client.result.DefaultResult;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.settlement.vo.BatchDebitVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillAndFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillVo;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.PayStatusEnum;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.enums.order.OrderProxyTypeEnum;
import com.tqmall.legend.enums.payment.PaymentEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.magic.ProxyFacade;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowBo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowSaveBo;
import com.tqmall.mace.result.wxpay.PayLogDTO;
import com.tqmall.mace.service.wxpay.RpcPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xin on 16/6/3.
 */
@Service
@Slf4j
public class DebitFacadeImpl implements DebitFacade {

    private static final Gson gson = new Gson();

    @Autowired
    private RpcDebitBillService rpcDebitBillService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private AccountFacadeService accountFacadeService;
    @Autowired
    private ProxyFacade proxyFacade;
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private RpcPayService rpcPayService;
    @Autowired
    private ShopManagerService shopManagerService;

    /**
     * 保存收款单
     *
     * @param debitBillVo
     */
    @Override
    public void saveBill(DebitBillVo debitBillVo) {
        DebitBillDTO debitBillDto = new DebitBillDTO();
        String billTimeStr = debitBillVo.getBillTimeStr();
        Date billTime = null;
        if (StringUtils.isEmpty(billTimeStr)) {
            billTime = new Date();
        } else {
            billTime = DateUtil.convertStringToDate(billTimeStr, "yyyy-MM-dd HH:mm");
        }
        debitBillDto.setBillTime(billTime);
        debitBillDto.setCreator(debitBillVo.getCreator());
        debitBillDto.setShopId(debitBillVo.getShopId());
        debitBillDto.setBillName(debitBillVo.getBillName());
        debitBillDto.setDebitTypeId(debitBillVo.getDebitTypeId());
        debitBillDto.setRelId(debitBillVo.getRelId());

        // 总计
        debitBillDto.setTotalAmount(debitBillVo.getTotalAmount());
        // 应收
        debitBillDto.setReceivableAmount(debitBillVo.getReceivableAmount());
        // 实收
        BigDecimal paidAmount = debitBillVo.getPaidAmount();
        debitBillDto.setPaidAmount(debitBillVo.getPaidAmount());
        // 挂账
        debitBillDto.setSignAmount(debitBillVo.getSignAmount());
        // 坏账
        debitBillDto.setBadAmount(debitBillVo.getBadAmount());

        debitBillDto.setOperatorName(debitBillVo.getOperatorName());
        debitBillDto.setPayerName(debitBillVo.getPayerName());
        debitBillDto.setRemark(debitBillVo.getRemark());

        List<DebitBillFlowDTO> debitBillFlowDtoList = null;
        if (paidAmount != null && paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            debitBillFlowDtoList = new ArrayList<>();
            DebitBillFlowDTO debitBillFlowDto = new DebitBillFlowDTO();
            debitBillFlowDto.setFlowTime(billTime);
            debitBillFlowDto.setCreator(debitBillVo.getCreator());
            debitBillFlowDto.setShopId(debitBillVo.getShopId());
            debitBillFlowDto.setPaymentId(debitBillVo.getPaymentId());
            debitBillFlowDto.setPaymentName(debitBillVo.getPaymentName());
            debitBillFlowDto.setPayAmount(debitBillVo.getPayAmount());
            debitBillFlowDto.setPayAccount(debitBillVo.getPayAccount());
            debitBillFlowDto.setRemark(debitBillVo.getRemark());
            debitBillFlowDtoList.add(debitBillFlowDto);
        }

        Result result = null;
        try {
            log.info("保存收款单调用账单中心dubbo接口, 参数 debitBillDto: {}, debitBillFlowDtoList: {}", gson.toJson(debitBillDto), gson.toJson(debitBillFlowDtoList));
            result = rpcDebitBillService.saveBill(debitBillDto, debitBillFlowDtoList);
            log.info("保存收款单调用账单中心dubbo接口返回值 result: {}", gson.toJson(result));
        } catch (Exception e) {
            log.error("保存收款单异常:", e);
            throw new BizException(e);
        }

        if (!result.isSuccess()) {
            log.error("保存收款单失败: {}", result.getMessage());
            throw new BizException(result.getMessage());
        }

    }

    /**
     * 查询收款单和流水
     *
     * @param shopId
     * @param relId
     * @return
     */
    @Override
    public DebitBillAndFlowVo findDebitBillAndFlow(Long shopId, Long relId) {
        Result<DebitBillAndFlowDTO> result = null;
        try {
            log.info("查询工单的收款单和流水记录调用账单中心dubbo接口, 参数 shopId: {}, relId: {}", shopId, relId);
            result = rpcDebitBillService.findFlowListByBillId(shopId, DebitTypeEnum.ORDER.getId(), relId, true);
            log.info("查询工单的收款单和流水记录调用账单中心dubbo接口返回值 result: {}", gson.toJson(result));
        } catch (Exception e) {
            log.error("查询收款单和流水异常", e);
            throw new BizException(e);
        }

        if (!result.isSuccess()) {
            log.error("查询收款单和流水异常: {}", result.getMessage());
            throw new BizException(result.getMessage());
        }

        DebitBillAndFlowDTO resultData = result.getData();
        if (resultData != null) {
            DebitBillAndFlowVo billAndFlowVo = new DebitBillAndFlowVo();
            DebitBillDTO billDTO = resultData.getDebitBillDTO();
            List<DebitBillDTO> redBillDTOList = resultData.getRedBillDTOList();
            List<DebitBillFlowDTO> debitBillFlowDTOList = resultData.getDebitBillFlowDTOList();
            if (billDTO != null) {
                Map<Long, String> operatorMap = Maps.newHashMap();
                DebitBillVo billVo = new DebitBillVo();
                BeanUtils.copyProperties(billDTO, billVo);
                billAndFlowVo.setDebitBillVo(billVo);
                operatorMap.put(billDTO.getId(), billDTO.getOperatorName());

                if (!CollectionUtils.isEmpty(redBillDTOList)) {
                    List<DebitBillVo> redBillVoList = new ArrayList<>();
                    for (DebitBillDTO redBillDTO : redBillDTOList) {
                        DebitBillVo redBillVo = new DebitBillVo();
                        BeanUtils.copyProperties(redBillDTO, redBillVo);
                        redBillVoList.add(redBillVo);
                        operatorMap.put(redBillDTO.getId(), redBillDTO.getOperatorName());

                    }
                    billAndFlowVo.setRedBillVoList(redBillVoList);
                }

                if (!CollectionUtils.isEmpty(debitBillFlowDTOList)) {
                    List<DebitBillFlowVo> flowVoList = new ArrayList<>();
                    for (DebitBillFlowDTO flowDTO : debitBillFlowDTOList) {
                        DebitBillFlowVo flowVo = new DebitBillFlowVo();
                        BeanUtils.copyProperties(flowDTO, flowVo);
                        flowVo.setOperatorName(operatorMap.get(flowDTO.getBillId()));
                        flowVoList.add(flowVo);
                    }
                    billAndFlowVo.setDebitBillFlowVoList(flowVoList);
                }
            }
            return billAndFlowVo;
        }
        return null;
    }

    /**
     * 保存收款流水
     */
    @Override
    @Transactional
    public void saveFlowList(DebitBillFlowSaveBo flowSaveBo) {

        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(flowSaveBo.getOrderId(), flowSaveBo.getShopId());
        if (!orderInfoOptional.isPresent()) {
            log.error("结算失败,被结算的工单不存在，工单编号:{}", flowSaveBo.getOrderId());
            throw new BizException("工单不存在");
        }
        OrderInfo orderInfo = orderInfoOptional.get();

        List<DebitBillFlowBo> flowBoList = flowSaveBo.getFlowList();
        if (!CollectionUtils.isEmpty(flowBoList)) {
            List<DebitBillFlowParam> flowList = new ArrayList<>();
            Map<Long, DebitBillFlowParam> filterMap = new HashMap<>();
            for (DebitBillFlowBo flowBo : flowBoList) {
                Long paymentId = flowBo.getPaymentId();
                BigDecimal payAmount = flowBo.getPayAmount();
                payAmount = payAmount != null ? payAmount : BigDecimal.ZERO;
                // 合并相同的收款方式
                if (filterMap.containsKey(paymentId)) {
                    DebitBillFlowParam flowDTO = filterMap.get(paymentId);
                    payAmount = payAmount.add(flowDTO.getPayAmount());
                    payAmount = payAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                    flowDTO.setPayAmount(payAmount);
                } else {
                    DebitBillFlowParam flowDTO = new DebitBillFlowParam();
                    flowDTO.setPaymentId(paymentId);
                    flowDTO.setPaymentName(flowBo.getPaymentName());
                    payAmount = payAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                    flowDTO.setPayAmount(payAmount);
                    filterMap.put(paymentId, flowDTO);
                }
            }
            flowList.addAll(filterMap.values());

            if (CollectionUtils.isEmpty(flowList)) {
                return;
            }

            // 总支付金额
            BigDecimal totalPayAmount = BigDecimal.ZERO;
            for (DebitBillFlowParam flowDTO : flowList) {
                totalPayAmount = totalPayAmount.add(flowDTO.getPayAmount());
            }

            BigDecimal signAmount = orderInfo.getSignAmount();
            if (totalPayAmount.compareTo(signAmount) > 0) {
                throw new BizException("收款总金额不能大于挂账金额");
            }

            // 会员卡余额支付金额
            BigDecimal memberPayAmount = flowSaveBo.getMemberPayAmount();
            if (memberPayAmount == null || memberPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
                memberPayAmount = BigDecimal.ZERO;
            }

            // 调用会员接口, 扣减会员卡余额, 增加会员消费金额
            try {
                Long memberCardId = flowSaveBo.getMemberCardId();
                if (memberCardId != null && memberCardId > 0) {
                    accountFacadeService.debitAccount(flowSaveBo.getShopId(), flowSaveBo.getUserId(), flowSaveBo.getOrderId(), memberCardId, memberPayAmount);
                }
            } catch (Exception e) {
                log.error("扣减会员卡余额出现异常:{}", e);
                throw new BizException(e);
            }

            DebitBillFlowSaveParam param = new DebitBillFlowSaveParam();
            param.setShopId(flowSaveBo.getShopId());
            param.setRelId(flowSaveBo.getOrderId());
            param.setCreator(flowSaveBo.getUserId());
            param.setDebitTypeId(DebitTypeEnum.ORDER.getId());
            param.setRemark(flowSaveBo.getRemark());
            param.setFlowTime(flowSaveBo.getFlowTime());
            param.setFlowList(flowList);
            Result result = null;
            try {
                // 插入收款流水
                log.info("工单收款调用账单中心dubbo接口, 参数 flowDTOList: {}", gson.toJson(param));
                result = rpcDebitBillService.saveFlow(param);
                log.info("工单收款调用账单中心dubbo接口返回值 result: {}", gson.toJson(result));

            } catch (Exception e) {
                log.error("保存收款流水异常", e);
                throw new BizException(e);
            }

            if (!result.isSuccess()) {
                log.error("保存收款流水异常: {}", result.getMessage());
                throw new BizException(result.getMessage());
            }

            // 工单结算状态变更
            changeOrderPayStatus(totalPayAmount, orderInfo, flowSaveBo.getUserId());

            if(PayStatusEnum.PAYED.getCode().equals(orderInfo.getPayStatus())){
                if (OrderProxyTypeEnum.ST.getCode().equals(orderInfo.getProxyType())) {
                    //同步委托单信息
                    String orderStatus = OrderNewStatusEnum.DDYFK.getOrderStatus();
                    String proxyStatus = ProxyStatusEnum.YJQ.getCode();
                    proxyFacade.updateProxyOrder(orderInfo.getId(), orderInfo.getShopId(), orderStatus, proxyStatus);
                }
            }
        }
    }

    /**
     * 工单批量收款流水
     *
     * @param batchDebitVo
     */
    @Override
    public void batchSaveFlowList(BatchDebitVo batchDebitVo) {
        Result<DebitAndRedBillDTO> result = null;
        List<Long> orderIdList = batchDebitVo.getOrderIdList();
        try {
            log.info("批量查询工单收款单调用账单中心dubbo接口, 参数 shopId: {}, relIds: {}, hasRed: {}", batchDebitVo.getShopId(), gson.toJson(batchDebitVo.getOrderIdList()), false);
            Set<Long> orderIdSet = new HashSet<>(orderIdList);
            result = rpcDebitBillService.findBillListByRelIds(batchDebitVo.getShopId(), DebitTypeEnum.ORDER.getId(), orderIdSet, true);
            log.info("批量查询工单收款单调用账单中心dubbo接口返回值, result: {}", gson.toJson(result));
        } catch (Exception e) {
            log.error("保存批量收款流水异常", e);
            throw new BizException(e);
        }
        if (!result.isSuccess()) {
            log.error("保存批量收款流水异常: {}", result.getMessage());
            throw new BizException(result.getMessage());
        }
        DebitAndRedBillDTO debitAndRedBillDTO = result.getData();
        if (debitAndRedBillDTO == null) {
            return;
        }
        // 收款单
        List<DebitBillDTO> billList = debitAndRedBillDTO.getDebitBillDTOList();
        // 收款单对应的冲红单
        List<DebitBillDTO> redBillDTOList = debitAndRedBillDTO.getRedBillDTOList();
        // 累加冲红挂账金额
        Map<Long, BigDecimal> redSignAmountMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(redBillDTOList)) {
            for (DebitBillDTO redBill : redBillDTOList) {
                Long relId = redBill.getRelId();
                if (redSignAmountMap.containsKey(relId)) {
                    BigDecimal redSignAmount = redSignAmountMap.get(relId);
                    redSignAmount = redSignAmount.add(redBill.getSignAmount());
                    redSignAmountMap.put(relId, redSignAmount);
                } else {
                    redSignAmountMap.put(relId, BigDecimal.ZERO);
                }
            }
        }
        if (!CollectionUtils.isEmpty(billList)) {
            Map<Long, DebitBillDTO> billDTOMap = new HashMap<>();
            for (DebitBillDTO bill : billList) {
                billDTOMap.put(bill.getRelId(), bill);
            }
            BigDecimal totalPayAmount = batchDebitVo.getPayAmount();
            for (Long orderId : orderIdList) {
                DebitBillDTO bill = billDTOMap.get(orderId);
                if (bill == null) {
                    continue;
                }
                BigDecimal signAmount = bill.getSignAmount();
                BigDecimal redSignAmount = redSignAmountMap.get(bill.getId());
                // 如果有冲红挂账金额, 挂账减去冲红挂账(冲红挂账金额是负值)
                if (redSignAmount != null && redSignAmount.compareTo(BigDecimal.ZERO) != 0) {
                    signAmount = signAmount.add(redSignAmount);
                }
                if (signAmount.compareTo(BigDecimal.ZERO) != 1) {
                    continue;
                }
                List<DebitBillFlowBo> flowList = new ArrayList<>(1);
                DebitBillFlowBo flow = new DebitBillFlowBo();
                flow.setPaymentId(batchDebitVo.getPaymentId());
                flow.setPaymentName(batchDebitVo.getPaymentName());

                BigDecimal payAmount = BigDecimal.ZERO;
                Optional<OrderInfo>  orderInfoOptional = orderService.getOrder(orderId);
                if(!orderInfoOptional.isPresent()){
                    throw new BizException("工单id：" + orderId + "不存在");
                }
                OrderInfo orderInfo = orderInfoOptional.get();

                BigDecimal downPayment = setDownPaymentFlow(bill.getReceivableAmount(), flowList, orderInfo);
                if(downPayment == null){
                    if (totalPayAmount.compareTo(signAmount) >= 0) {
                        payAmount = payAmount.add(signAmount);
                        totalPayAmount = totalPayAmount.subtract(signAmount);
                    } else {
                        payAmount = payAmount.add(totalPayAmount);
                        totalPayAmount = BigDecimal.ZERO;
                    }
                } else {
                    BigDecimal thisPayAmount = BigDecimal.ZERO;//本次收款金额
                    if(signAmount.compareTo(downPayment) == 1){
                        //如果挂账金额 > 预付定金 ，本次收款金额为 挂账金额 - 预付定金 否则无需收款为0
                        thisPayAmount = signAmount.subtract(downPayment);
                    }
                    payAmount = payAmount.add(thisPayAmount);
                    //本次收款金额 大于剩余的收款金额时，本次为剩余的收款金额
                    if(payAmount.compareTo(totalPayAmount) == 1){
                        payAmount = totalPayAmount;
                    }
                    totalPayAmount = totalPayAmount.subtract(payAmount);
                }
                flow.setPayAmount(payAmount);
                flowList.add(flow);

                DebitBillFlowSaveBo flowSaveBo = new DebitBillFlowSaveBo();
                flowSaveBo.setShopId(batchDebitVo.getShopId());
                flowSaveBo.setUserId(batchDebitVo.getUserId());
                flowSaveBo.setRemark(batchDebitVo.getRemark());
                flowSaveBo.setOrderId(orderId);
                flowSaveBo.setFlowList(flowList);
                //循环调用收款方法
                saveFlowList(flowSaveBo);

                if (totalPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }
        }
    }

    /**
     * 坏账处理
     *
     * @param orderId
     * @param remark
     */
    @Override
    public void badBill(Long shopId, Long orderId, Long userId, String remark) {
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            log.error("结算失败,被结算的工单不存在，工单id:{}", orderId);
            throw new BizException("工单不存在");
        }

        Result result = null;
        try {
            log.info("收款单坏账处理调用账单中心dubbo接口, 参数 orderId: {}, userId: {}, remark: {}", orderId, userId, remark);
            result = rpcDebitBillService.badBill(shopId, DebitTypeEnum.ORDER.getId(), orderId, userId, remark);
            log.info("收款单坏账处理调用账单中心dubbo接口返回值,  result: {}", gson.toJson(result));
        } catch (Exception e) {
            log.error("收款单坏账处理出现异常", e);
            throw new BizException(e);
        }
        if (!result.isSuccess()) {
            log.error("收款单坏账处理出现异常: {}", result.getMessage());
            throw new BizException(result.getMessage());
        }

        // 工单状态流转
        OrderInfo orderInfo = orderInfoOptional.get();
        orderInfo.setPayStatus(PayStatusEnum.PAYED.getCode());
        orderInfo.setModifier(userId);
        orderInfo.setSignAmount(BigDecimal.ZERO);
        orderService.updateOrder(orderInfo);
        log.info("工单结算状态流转, 工单id: {}, 结算状态: {}", orderInfo.getId(), orderInfo.getPayStatus());
        if (OrderProxyTypeEnum.ST.getCode().equals(orderInfo.getProxyType())) {
            //同步委托单信息
            String orderStatus = OrderNewStatusEnum.DDYFK.getOrderStatus();
            String proxyStatus = ProxyStatusEnum.YJQ.getCode();
            proxyFacade.updateProxyOrder(orderId, shopId, orderStatus, proxyStatus);
        }
    }

    /**
     * 工单结算状态变更
     *
     * @param totalPayAmount
     * @param orderInfo
     * @param userId
     */
    private void changeOrderPayStatus(BigDecimal totalPayAmount, OrderInfo orderInfo, Long userId) {
        BigDecimal signAmount = orderInfo.getSignAmount();
        if (totalPayAmount.compareTo(signAmount) == 0) {
            orderInfo.setPayStatus(PayStatusEnum.PAYED.getCode());
        } else {
            orderInfo.setPayStatus(PayStatusEnum.SIGN.getCode());
        }
        orderInfo.setModifier(userId);
        orderInfo.setPayTime(new Date());
        orderInfo.setSignAmount(signAmount.subtract(totalPayAmount));
        orderService.updateOrder(orderInfo);
        log.info("工单结算状态流转, 工单id: {}, 结算状态: {}", orderInfo.getId(), orderInfo.getPayStatus());

    }

    /**
     * 设置预付定金收款
     *
     * @param flowList
     * @return
     */
    public BigDecimal setDownPaymentFlow(BigDecimal receivableAmount, List<DebitBillFlowBo> flowList,OrderInfo orderInfo) {

        Long orderId = orderInfo.getId();
        Long shopId = orderInfo.getShopId();
        //如果工单存在预付金，则添加预付金流水
        BigDecimal downPayment = orderInfo.getDownPayment();
        //查询对应的预付定金
        if(downPayment.compareTo(BigDecimal.ZERO) != 1){
            return null;
        }
        //如果有预定金，则实收为预定金的金额
        DebitBillFlowBo debitBillFlowBo = new DebitBillFlowBo();
        Long appointId = orderInfo.getAppointId();
        Appoint appoint;
        if(appointId != null){
            appoint = appointFacade.getAppointById(appointId);
        } else {
            appoint = appointFacade.getAppointByOrderIdAndShopId(orderId, shopId);
        }
        String orderSn;
        Integer orderType;
        //TODO 需要调用mace支付中心接口获取支付方式,目前工单无法标识是预约单转的工单，只能先查询预约单
        if(appoint != null){
            orderSn = appoint.getAppointSn();
            orderType = 1;//预约单预付款类型
        } else{
            orderSn = orderInfo.getOrderSn();
            orderType = 2;//工单预付款类型
        }
        Result<PayLogDTO> payLogDTOResult;
        try {
            payLogDTOResult = rpcPayService.orderPayLog(shopId, orderSn, orderType);
        } catch (Exception e) {
            log.error("【dubbo调用mace】获取支付流水异常", e);
            throw new BizException("获取支付流水异常，请稍后再试");
        }
        if (!payLogDTOResult.isSuccess()) {
            log.info("【dubbo调用mace】获取支付流水失败,门店id：{}，编号：{}，类型：{}，错误为：{}", shopId, orderSn, orderType, payLogDTOResult.isSuccess(), payLogDTOResult.getMessage());
            throw new BizException("查询不到对应预付定金的流水，收款失败");
        }
        PayLogDTO payLogDTO = payLogDTOResult.getData();
        Long paymentId = payLogDTO.getTradeChannel().longValue();
        String paymentName = PaymentEnum.getNameById(paymentId);
        if (StringUtils.isBlank(paymentName)) {
            //默认微信预付定金
            throw new BizException("查询不到对应预付定金的收款类型，收款失败");
        }
        //查询工单收款流水，是否有预付定金收款
        DebitBillAndFlowVo debitBillAndFlow = findDebitBillAndFlow(shopId, orderId);
        boolean hasDownPaymentFlow = false;
        if (debitBillAndFlow != null) {
            List<DebitBillFlowVo> debitBillFlowVoList = debitBillAndFlow.getDebitBillFlowVoList();
            if(!CollectionUtils.isEmpty(debitBillFlowVoList)){
                for(DebitBillFlowVo debitBillFlowVo : debitBillFlowVoList){
                    Long tempPaymentId = debitBillFlowVo.getPaymentId();
                    if(tempPaymentId != null && tempPaymentId.equals(paymentId)){
                        hasDownPaymentFlow = true;
                    }
                }
            }
        }
        if(hasDownPaymentFlow){
            return null;
        }
        debitBillFlowBo.setPaymentId(paymentId);
        //校验其他方式收款金额
        BigDecimal checkPayAmount = BigDecimal.ZERO;
        //如果收款方式不为空，需要校验金额
        if(!CollectionUtils.isEmpty(flowList)){
            for(DebitBillFlowBo temp : flowList){
                BigDecimal tempPayAmount = temp.getPayAmount();
                checkPayAmount =checkPayAmount.add(tempPayAmount);
            }
            if(receivableAmount.compareTo(downPayment) != 1 && checkPayAmount.compareTo(BigDecimal.ZERO) == 1){
                throw new BizException("预付定金大于等于应收金额，无需其他收款方式收款。");
            }
            //总付金额大于应付金额，返回错误
            BigDecimal totalPayment = downPayment.add(checkPayAmount);
            if(checkPayAmount.compareTo(BigDecimal.ZERO) == 1 && receivableAmount.compareTo(totalPayment) == -1){
                throw new BizException("总收款金额大于应收金额，请确认。");
            }
        }
        if(receivableAmount.compareTo(downPayment) == -1){
            downPayment = receivableAmount;
        }
        debitBillFlowBo.setPayAmount(downPayment);
        debitBillFlowBo.setPaymentName(paymentName);
        flowList.add(debitBillFlowBo);
        return downPayment;
    }

    /**
     * 查询收款流水
     *
     * @param param
     * @return
     */
    @Override
    public DefaultPage<DebitBillFlowVo> getDebitBillFlowPage(final DebitFlowSearchParam param) {
        return new BizTemplate<DefaultPage<DebitBillFlowVo>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected DefaultPage<DebitBillFlowVo> process() throws BizException {
                com.tqmall.core.common.entity.Result<DefaultResult<DebitBillAndFlowResult>> result = null;
                log.info("查询收款流水调用dubbo接口, 参数 param: {}", gson.toJson(param));
                result = rpcDebitBillService.findFlowListBySearch(param);
                if (result == null || !result.isSuccess()) {
                    log.error("查询收款流水失败, {}", gson.toJson(result));
                    throw new BizException("查询收款流水失败");
                }
                DefaultResult<DebitBillAndFlowResult> data = result.getData();
                List<DebitBillFlowVo> flowVoList = new ArrayList<>();
                PageRequest pageRequest = new PageRequest(param.getPageNum(), param.getPageSize());
                if (data == null) {
                    return new DefaultPage<>(flowVoList, pageRequest, 0);
                }
                List<DebitBillAndFlowResult> content = data.getContent();
                Set<Long> userIds = new HashSet<>();
                if (!CollectionUtils.isEmpty(content)) {
                    for (DebitBillAndFlowResult flowDTO : content) {
                        DebitBillFlowVo flowVo = new DebitBillFlowVo();
                        userIds.add(flowDTO.getCreator());
                        BeanUtils.copyProperties(flowDTO, flowVo);
                        flowVoList.add(flowVo);
                    }
                }
                if(!CollectionUtils.isEmpty(userIds)) {
                    Long[] idsArr = userIds.toArray(new Long[userIds.size()]);
                    List<ShopManager> shopManagerList = shopManagerService.selectByIds(idsArr);
                    Map<Long,ShopManager> shopManagerMap = Maps.uniqueIndex(shopManagerList, new Function<ShopManager, Long>() {
                        @Override
                        public Long apply(ShopManager shopManager) {
                            return shopManager.getId();
                        }
                    });
                    for(DebitBillFlowVo flowVo : flowVoList){
                        if(shopManagerMap.containsKey(flowVo.getCreator())){
                            flowVo.setCreatorName(shopManagerMap.get(flowVo.getCreator()).getName());
                        }
                    }
                }
                Integer total = data.getTotal();
                return new DefaultPage<>(flowVoList, pageRequest, total);
            }
        }.execute();

    }
}
