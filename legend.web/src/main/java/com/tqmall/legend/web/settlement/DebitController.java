package com.tqmall.legend.web.settlement;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.*;
import com.tqmall.legend.billcenter.client.enums.DebitTypeEnum;
import com.tqmall.legend.billcenter.client.param.DebitFlowSearchParam;
import com.tqmall.legend.billcenter.client.param.DebitInvalidByBillIdParam;
import com.tqmall.legend.billcenter.client.param.DebitTypeBatchParam;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.IVirtualOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.vo.OrderDiscountFlowVo;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.setting.ShopPrintConfigService;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.biz.settlement.vo.*;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerPerfectOfCarWashEntity;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.payment.PaymentEnum;
import com.tqmall.legend.facade.account.AccountFacadeService;
import com.tqmall.legend.facade.account.MemberFacadeService;
import com.tqmall.legend.facade.account.vo.MemberCardBo;
import com.tqmall.legend.facade.order.CarWashFacade;
import com.tqmall.legend.facade.order.OrderDiscountFlowFacade;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowBo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowSaveBo;
import com.tqmall.legend.facade.shop.ShopConfigureFacade;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.object.result.settlement.PaymentDTO;
import com.tqmall.legend.service.settlement.RpcSettlementService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 收款结算
 * Created by lixiao on 16/6/2.
 */
@Controller
@RequestMapping("shop/settlement/debit")
@Slf4j
public class DebitController extends BaseController {

    private static final Gson gson = new Gson();

    @Autowired
    private IVirtualOrderService virtualOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CarWashFacade carWashFacade;
    @Autowired
    private WrapperOrderFacade wrapperOrderFacade;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RpcDebitBillService rpcDebitBillService;
    @Autowired
    private DebitFacade debitFacade;
    @Autowired
    private OrderDiscountFlowFacade orderDiscountFlowFacade;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private MemberFacadeService memberFacadeService;
    @Autowired
    private RpcSettlementService rpcSettlementService;
    @Autowired
    private ShopPrintConfigService printConfigService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private AccountFacadeService accountFacadeService;
    @Autowired
    private AccountInfoService accountInfoService;

    private final static Integer OPEN_STATUE = 1;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopConfigureFacade shopConfigureFacade;

    /**
     * 工单结算列表
     *
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "/order-list")
    public String orderList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        return "/yqx/page/settlement/debit/order-list";
    }


    /**
     * 结算单详情页
     *
     * @param model
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/order-detail", method = RequestMethod.GET)
    public String orderDetail(Model model, @RequestParam("orderId") Long orderId, HttpServletRequest request) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        List<ShopPrintConfig> shopPrintConfigs = printConfigService.getShopOpenConfig(request);
        if (!CollectionUtils.isEmpty(shopPrintConfigs)){
            model.addAttribute("openPrintConfig",shopPrintConfigs);
        }
        // current user and shop
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();

        if (orderId == null || orderId < 0) {
            // 工单不存在提示信息
            log.error("工单不存在!");
            return "common/error";
        }

        // 根据工单类型跳转到对应的工单详情页
        Optional<OrderInfo> orderOptional = orderService.getOrder(orderId, shopId);
        // IF 校验跨门店 THEN 返回空页面
        if (!orderOptional.isPresent()) {
            return "yqx/page/settlement/debit/order-list";
        }

        OrderInfo orderInfo = orderOptional.get();
        //设置debitBill流水
        Set<Long> relIds = new HashSet<>();
        relIds.add(orderId);
        com.tqmall.core.common.entity.Result<DebitAndRedBillDTO> billResult = null;
        try {
            log.info("查询工单对应的收款单调用账单中心dubbo接口, 参数: shopId: {}, debitTypeId: {}, relIds: {}, hasRed: {}", shopId, DebitTypeEnum.ORDER.getId(), gson.toJson(relIds), true);
            billResult = rpcDebitBillService.findBillListByRelIds(shopId, DebitTypeEnum.ORDER.getId(), relIds, true);
            log.info("查询工单对应的收款单调用账单中心dubbo接口, 参数: result: {}", gson.toJson(billResult));
        } catch (Exception e) {
            log.error("查询工单对应的收款单调用账单中心dubbo接口异常, {}", e);
            return "common/error";
        }
        if (billResult.isSuccess()) {
            DebitAndRedBillDTO resultData = billResult.getData();
            if (resultData != null) {
                List<DebitBillDTO> debitBillDTOList = resultData.getDebitBillDTOList();
                if (!CollectionUtils.isEmpty(debitBillDTOList)) {
                    model.addAttribute("debitBill", debitBillDTOList.get(0));
                }
                // 冲红单
                List<DebitBillDTO> redBillDTOList = resultData.getRedBillDTOList();
                if (!CollectionUtils.isEmpty(redBillDTOList)) {
                    model.addAttribute("redBill", redBillDTOList.get(0));
                }
            }
        }

        Integer orderTag = orderInfo.getOrderTag();
        if (orderTag != null) {
            if (orderTag.intValue() == OrderCategoryEnum.CARWASH.getCode()) {
                // 洗车单详情
                logTrac(com.tqmall.legend.web.order.SiteUrls.CARWASHDETAIL, userId, null);

                Long customerCarId = orderInfo.getCustomerCarId();
                // 获取客户车辆信息
                Result<CustomerPerfectOfCarWashEntity> result = customerCarService.selectCustomerCar(shopId, customerCarId);
                if (!result.isSuccess()) {
                    log.error("洗车单详情获取客户车辆信息失败, shopId: {}, customerCarId: {}", shopId, customerCarId);
                    return "common/error";
                }
                CustomerPerfectOfCarWashEntity customerPerfectEntity = result.getData();
                if (customerPerfectEntity != null) {
                    // wrapper完善洗车表单实体
                    CustomerCompletionFormEntity formEntity = carWashFacade.wrapperPerfectCarWashFormEntity(orderInfo, customerPerfectEntity);
                    formEntity.setPayStatus(orderInfo.getPayStatus());
                    model.addAttribute("formEntity", formEntity);
                }
                model.addAttribute("orderInfo", orderInfo);
                return "yqx/page/settlement/debit/carwash-detail";
            } else if (orderTag.intValue() == OrderCategoryEnum.SPEEDILY.getCode() || orderTag.intValue() == OrderCategoryEnum.INSURANCE.getCode()) {
                // 快修快保单/引流活动详情
                logTrac(com.tqmall.legend.web.order.SiteUrls.SPEEDILYDETAIL, userId, null);

                try {
                    wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, shopId);
                } catch (BusinessCheckedException e) {
                    log.error("快修快保页封装失败，原因:", e);
                    return "common/error";
                }
                // exist virtual order
                model.addAttribute("virtualOrderId", virtualOrderService.isExistVirtualOrder(orderId));

                return "yqx/page/settlement/debit/speedily-detail";
            } else if (orderTag.intValue() == OrderCategoryEnum.COMMON.getCode()) {
                // 综合维修单详情
                logTrac(com.tqmall.legend.web.order.SiteUrls.DETAIL, userId, null);

                try {
                    wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, userInfo.getShopId());
                } catch (BusinessCheckedException e) {
                    log.error("工单编辑失败，原因:", e);
                    model.addAttribute("isexist", 0);
                    return "common/error";
                }

                // exist virtual order
                model.addAttribute("virtualOrderId", virtualOrderService.isExistVirtualOrder(orderId));

                return "yqx/page/settlement/debit/common-detail";
            } else if (orderTag.intValue() == OrderCategoryEnum.SELLGOODS.getCode()) {
                // 销售单详情
                logTrac(com.tqmall.legend.web.order.SiteUrls.SELLGOODDETAIL, userId, null);

                try {
                    wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, shopId);
                } catch (BusinessCheckedException e) {
                    log.error("销售单页封装失败，原因:", e);
                    return "common/error";
                }

                return "yqx/page/settlement/debit/sell-good-detail";
            } else {
                log.error("未知的工单类型!");
                return "common/error";
            }
        }

        return "yqx/page/settlement/debit/order-list";
    }

    /**
     * 收款页面
     *
     * @param model
     * @param orderId
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "/order-debit", method = RequestMethod.GET)
    public String orderDebit(Model model, @RequestParam Long orderId, HttpServletRequest request) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();

        //结算方式列表
        getDefaultPayment(model, shopId);

        // IF 校验跨门店 THEN 返回空页面
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return "common/error";
        }

        OrderInfo orderInfo = orderInfoOptional.get();
        String orderStatus = orderInfo.getOrderStatus();
        Integer payStatus = orderInfo.getPayStatus();
        // 工单状态已付款, 已挂账
        if (!(orderStatus.equals(OrderStatusEnum.DDYFK.getKey()) && payStatus.equals(PayStatusEnum.SIGN.getCode()))) {
            return "redirect:/shop/settlement/debit/order-detail?orderId=" + orderId;
        }

        model.addAttribute("orderInfo", orderInfo);
        try {
            //工单服务信息
            List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId);
            model.addAttribute("orderServicesList", orderServicesList);
            //工单物料信息
            List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId);
            model.addAttribute("orderGoodsList", orderGoodsList);

            // 查询工单收款单及流水
            DebitBillAndFlowVo debitBillAndFlow = debitFacade.findDebitBillAndFlow(shopId, orderId);
            if (debitBillAndFlow != null) {
                DebitBillVo debitBillVo = debitBillAndFlow.getDebitBillVo();
                if (debitBillVo == null) {
                    log.error("该工单没有对应的收款单, orderId: {}", orderId);
                    return "common/error";
                }
                //存在预付定金时收款逻辑
                setDebitBillAboutDownPayment(orderInfo, debitBillVo, model);

                model.addAttribute("debitBill", debitBillVo);
                model.addAttribute("debitBillFlowList", debitBillAndFlow.getDebitBillFlowVoList());
                // 冲红单
                List<DebitBillVo> redBillVoList = debitBillAndFlow.getRedBillVoList();
                if (!CollectionUtils.isEmpty(redBillVoList)) {
                    DebitBillVo redBillVo = redBillVoList.get(0);
                    BigDecimal totalReceivableAmount = BigDecimal.ZERO;
                    BigDecimal totalPaidAmount = BigDecimal.ZERO;
                    BigDecimal totalSignAmount = BigDecimal.ZERO;
                    BigDecimal totalBadAmount = BigDecimal.ZERO;
                    for (DebitBillVo redBill : redBillVoList) {
                        totalReceivableAmount = totalReceivableAmount.add(redBill.getReceivableAmount());
                        totalPaidAmount = totalPaidAmount.add(redBill.getPaidAmount());
                        totalSignAmount = totalSignAmount.add(redBill.getSignAmount());
                        totalBadAmount = totalBadAmount.add(redBill.getBadAmount());
                    }
                    redBillVo.setReceivableAmount(totalReceivableAmount);
                    redBillVo.setPaidAmount(totalPaidAmount);
                    redBillVo.setSignAmount(totalSignAmount);
                    redBillVo.setBadAmount(totalBadAmount);
                    model.addAttribute("redBill", redBillVo);
                }
            }
        } catch (BizException e) {
            log.error("工单收款页封装失败，原因:", e);
            return "common/error";
        }

        // 查询工单优惠流水
        List<OrderDiscountFlowVo> discountFlowVoList = orderDiscountFlowFacade.getOrderDiscountFlow(orderId, shopId);
        model.addAttribute("discountFlowList", discountFlowVoList);

        // 获取他人账户的手机号
        String guestMobile = getGuestMobile(shopId, orderInfo, discountFlowVoList);
        model.addAttribute("guestMobile", guestMobile);

        // 查询上一次使用的会员卡信息
        MemberCardBo memberCardBo = memberFacadeService.getUsedForOrderLastSettle(shopId, orderId);
        model.addAttribute("memberCard",memberCardBo);

        //设置是否使用他人账户
        String confValue = shopConfigureFacade.getConfValue(ShopConfigureTypeEnum.USE_GUEST_ACCOUNT, shopId);
        if (StringUtils.isNotBlank(confValue)) {
            model.addAttribute("USE_GUEST_ACCOUNT", confValue);
        }
        return "/yqx/page/settlement/debit/order-debit";
    }

    /**
     * 查询优惠记录是否存在使用他人的券，未使用卡的情况，此时必须使用他人的卡结算
     *
     * @param shopId
     * @param orderInfo
     * @param discountFlowVoList
     * @return
     */
    private String getGuestMobile(Long shopId, OrderInfo orderInfo, List<OrderDiscountFlowVo> discountFlowVoList) {
        String mobile = "";
        if(CollectionUtils.isEmpty(discountFlowVoList)){
            return mobile;
        }
        Long customerCarId = orderInfo.getCustomerCarId();
        Map<Long, Long> accountCustomerMap = accountFacadeService.getAccountIdAndCustomerIdByCarId(shopId, customerCarId);
        Long guestAccountId = null;
        for (OrderDiscountFlowVo discountFlowVo : discountFlowVoList) {
            Long accountId = discountFlowVo.getAccountId();
            //accountId 为空或者0的时候不是用账户优惠
            if (accountId == null || accountId.compareTo(0l) == 0) {
                continue;
            }
            //使用了他人的券
            if (!accountCustomerMap.containsKey(accountId)) {
                guestAccountId = accountId;
                //取此人的账户的手机号，跳出循环
                break;
            }
        }
        if (guestAccountId == null) {
            return mobile;
        }
        //查询账户对应的客户手机号
        AccountInfo accountInfo = accountInfoService.getAccountInfoById(guestAccountId);
        if (accountInfo != null) {
            mobile = accountInfo.getMobile();
        }
        return mobile;
    }

    /**
     * 预付定金的逻辑
     * @param orderInfo
     * @param debitBillVo
     */
    private void setDebitBillAboutDownPayment(OrderInfo orderInfo, DebitBillVo debitBillVo,Model model) {
        //如果有预定金，则实收为预定金的金额
        BigDecimal downPayment = orderInfo.getDownPayment();
        if(downPayment.compareTo(BigDecimal.ZERO) == 0){
            return;
        }
        boolean hasDownPaymentFlow = isHasDownPaymentFlow(orderInfo);
        model.addAttribute("hasDownPaymentFlow", hasDownPaymentFlow);
        BigDecimal paidAmount = debitBillVo.getPaidAmount();//工单实收金额
        BigDecimal signAmount = debitBillVo.getSignAmount();//工单挂账金额
        if(!hasDownPaymentFlow){
            //未收过款
            if(downPayment.compareTo(signAmount) == 1){
                //当预付定金 > 挂账金额时 实收为挂账金额
                paidAmount = signAmount;
                signAmount = BigDecimal.ZERO;
            } else {
                //当预付定金 <= 挂账金额时 实收为预付定金
                paidAmount = downPayment;
                signAmount = signAmount.subtract(paidAmount);
            }
        }
        debitBillVo.setPaidAmount(paidAmount);
        debitBillVo.setSignAmount(signAmount);
    }

    /**
     * 是否有预付流水
     *
     * @param orderInfo
     * @return
     */
    private boolean isHasDownPaymentFlow(OrderInfo orderInfo) {
        //如果有预定金，则实收为预定金的金额
        Long orderId = orderInfo.getId();
        Long shopId = orderInfo.getShopId();
        //查询工单收款流水，是否有预付定金收款
        DebitBillAndFlowVo debitBillAndFlow = debitFacade.findDebitBillAndFlow(shopId, orderId);
        boolean hasDownPaymentFlow = false;
        if (debitBillAndFlow != null) {
            List<DebitBillFlowVo> debitBillFlowVoList = debitBillAndFlow.getDebitBillFlowVoList();
            if(!CollectionUtils.isEmpty(debitBillFlowVoList)){
                for(DebitBillFlowVo debitBillFlowVo : debitBillFlowVoList){
                    Long paymentId = debitBillFlowVo.getPaymentId();
                    String paymentName = PaymentEnum.getNameById(paymentId);
                    if(StringUtils.isNotBlank(paymentName)){
                        hasDownPaymentFlow = true;
                    }
                }
            }
        }
        return hasDownPaymentFlow;
    }

    /**
     * 工单收款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "order-debit-post", method = RequestMethod.POST)
    public Result orderDebitPost(@RequestBody OrderDebitVo orderDebitVo, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        Long orderId = orderDebitVo.getOrderId();
        Optional<OrderInfo>  orderInfoOptional = orderService.getOrder(orderId);
        if(!orderInfoOptional.isPresent()){
            return Result.wrapErrorResult("","工单不存在");
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        List<DebitBillFlowVo> flowVoList = orderDebitVo.getFlowList();

        List<DebitBillFlowBo> flowList = new ArrayList<>();

        // 使用会员卡支付数据校验
        MemberCardVo memberCard = orderDebitVo.getMemberCard();
        BigDecimal memberPayAmount = BigDecimal.ZERO;
        Long memberCardId = null;
        if (memberCard != null) {
            memberPayAmount = memberCard.getPayAmount();
            memberCardId = memberCard.getId();
            if (memberCardId != null && memberCardId > 0 && memberPayAmount != null && memberPayAmount.compareTo(BigDecimal.ZERO) > 0) {
                if (memberPayAmount.compareTo(memberCard.getBalance()) > 0) {
                    return Result.wrapErrorResult("", "使用会员卡消费金额大于会员卡余额");
                }

                DebitBillFlowBo memberCardFlow = new DebitBillFlowBo();
                memberCardFlow.setPaymentId(0L);
                memberCardFlow.setPayAmount(memberPayAmount);
                memberCardFlow.setPaymentName("会员卡");
                flowList.add(memberCardFlow);
            }
        }

        if (!CollectionUtils.isEmpty(flowVoList)) {
            for (DebitBillFlowVo flowVo : flowVoList) {
                DebitBillFlowBo flowBo = new DebitBillFlowBo();
                flowBo.setPaymentId(flowVo.getPaymentId());
                flowBo.setPaymentName(flowVo.getPaymentName());
                flowBo.setPayAmount(flowVo.getPayAmount());
                flowBo.setPayAccount(flowVo.getPayAccount());
                flowList.add(flowBo);
            }
        }

        if (CollectionUtils.isEmpty(flowList)) {
            return Result.wrapSuccessfulResult(true);
        }

        DebitBillFlowSaveBo flowSaveBo = new DebitBillFlowSaveBo();
        flowSaveBo.setShopId(shopId);
        flowSaveBo.setUserId(userId);
        flowSaveBo.setOrderId(orderId);
        flowSaveBo.setRemark(orderDebitVo.getRemark());
        flowSaveBo.setMemberPayAmount(memberPayAmount);
        flowSaveBo.setMemberCardId(memberCardId);

        logTrac(SiteUrls.ORDER_DEBIT_POST, userId, orderDebitVo);
        try {
            debitFacade.setDownPaymentFlow(orderInfo.getPayAmount(), flowList, orderInfo);

            flowSaveBo.setFlowList(flowList);
            debitFacade.saveFlowList(flowSaveBo);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 批量收款列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/batch-list", method = RequestMethod.GET)
    public String batchList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());

        return "/yqx/page/settlement/debit/batch-list";
    }

    /**
     * 批量收款页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/batch-debit", method = RequestMethod.GET)
    public String batchDebit(Model model, HttpServletRequest request, Long[] orderIds) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();

        if (orderIds == null || orderIds.length == 0 || orderIds.length > 50) {
            return "/yqx/page/settlement/debit/batch-list";
        }
        List<Long> orderIdsList = Arrays.asList(orderIds);
        // 批量查询工单列表
        List<OrderInfo> orderInfoList = orderService.selectByIdsAndShopId(orderIdsList, shopId);
        BigDecimal downPayment = BigDecimal.ZERO;//预付定金
        if (!CollectionUtils.isEmpty(orderInfoList)) {
            Set<Long> customerIds = new HashSet<>();
            for (OrderInfo orderInfo : orderInfoList) {
                customerIds.add(orderInfo.getCustomerId());
                //批量收款时，只有综合维修单才可能未过收款
                Integer orderTag = orderInfo.getOrderTag();
                if(OrderCategoryEnum.COMMON.getCode() != orderTag){
                    continue;
                }
                BigDecimal checkDownPayment = orderInfo.getDownPayment();
                if(checkDownPayment.compareTo(BigDecimal.ZERO) != 1){
                    continue;
                }
                BigDecimal payAmount = orderInfo.getPayAmount();
                //查询是否收过预付定金
                boolean hasDownPaymentFlow = isHasDownPaymentFlow(orderInfo);
                if(hasDownPaymentFlow){
                    continue;
                }
                //未收过款
                if(checkDownPayment.compareTo(payAmount) == 1){
                    //当预付定金 > 挂账金额时 实收为挂账金额
                    downPayment = downPayment.add(payAmount);
                } else {
                    //当预付定金 <= 挂账金额时 实收为预付定金
                    downPayment = downPayment.add(checkDownPayment);
                }
            }
            List<Customer> customerList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(customerIds)) {
                int size = customerIds.size();
                Long[] customerIdsArr = customerIds.toArray(new Long[size]);
                customerList = customerService.selectByIds(customerIdsArr);
            }
            Map<Long, String> map = new HashMap<>();
            if (!CollectionUtils.isEmpty(customerList)) {
                for (Customer customer : customerList) {
                    map.put(customer.getId(), customer.getCompany());
                }
                for (OrderInfo orderInfo : orderInfoList) {
                    String company = map.get(orderInfo.getCustomerId());
                    orderInfo.setCompany(company);
                }
            }
        }
        model.addAttribute("orderInfoList", orderInfoList);

        //结算方式列表
        getDefaultPayment(model, shopId);

        // 总应收
        BigDecimal totalReceivableAmount = BigDecimal.ZERO;
        // 总实收
        BigDecimal totalPaidAmount = BigDecimal.ZERO;
        // 总挂账
        BigDecimal totalSignAmount = BigDecimal.ZERO;

        // 总冲红应收
        BigDecimal totalRedReceivableAmount = BigDecimal.ZERO;
        // 总冲红实收
        BigDecimal totalRedPaidAmount = BigDecimal.ZERO;
        // 总冲红挂账
        BigDecimal totalRedSignAmount = BigDecimal.ZERO;

        Set<Long> orderIdSet = new HashSet<>(orderIdsList);
        com.tqmall.core.common.entity.Result<DebitAndRedBillDTO> result = null;
        try {
            log.info("批量查询工单收款单调用账单中心dubbo接口, 参数 shopId: {}, relIds: {}, hasRed: {}", shopId, gson.toJson(orderIdSet), true);
            result = rpcDebitBillService.findBillListByRelIds(shopId, DebitTypeEnum.ORDER.getId(), orderIdSet, true);
            log.info("批量查询工单收款单调用账单中心dubbo接口返回值, result: {}", gson.toJson(result));
        } catch (Exception e) {
            log.error("[批量收款]批量查询工单收款单:", e);
            return "common/error";
        }
        if (!result.isSuccess()) {
            log.error("批量查询工单收款单: {}", result.getMessage());
            return "common/error";
        }
        DebitAndRedBillDTO debitAndRedBillDTO = result.getData();
        if (debitAndRedBillDTO != null) {
            List<DebitBillDTO> debitBillDTOList = debitAndRedBillDTO.getDebitBillDTOList();
            if (!CollectionUtils.isEmpty(debitBillDTOList)) {
                for (DebitBillDTO bill : debitBillDTOList) {
                    totalReceivableAmount = totalReceivableAmount.add(bill.getReceivableAmount());
                    totalPaidAmount = totalPaidAmount.add(bill.getPaidAmount());
                    totalSignAmount = totalSignAmount.add(bill.getSignAmount());
                }
            }
            List<DebitBillDTO> redBillDTOList = debitAndRedBillDTO.getRedBillDTOList();
            if (!CollectionUtils.isEmpty(redBillDTOList)) {
                for (DebitBillDTO redBill : redBillDTOList) {
                    totalRedReceivableAmount = totalRedReceivableAmount.add(redBill.getReceivableAmount());
                    totalRedPaidAmount = totalRedPaidAmount.add(redBill.getPaidAmount());
                    totalRedSignAmount = totalRedSignAmount.add(redBill.getSignAmount());
                }
            }
        }
        if(downPayment.compareTo(BigDecimal.ZERO) == 1){
            totalPaidAmount = totalPaidAmount.add(downPayment);
            totalSignAmount = totalSignAmount.subtract(downPayment);
            model.addAttribute("totalDownPayment", downPayment);
        }

        model.addAttribute("totalReceivableAmount", totalReceivableAmount);
        model.addAttribute("totalPaidAmount", totalPaidAmount);
        model.addAttribute("totalSignAmount", totalSignAmount);

        model.addAttribute("totalRedReceivableAmount", totalRedReceivableAmount);
        model.addAttribute("totalRedPaidAmount", totalRedPaidAmount);
        model.addAttribute("totalRedSignAmount", totalRedSignAmount);

        return "/yqx/page/settlement/debit/batch-debit";
    }

    /**
     * 批量收款
     *
     * @param batchDebitVo
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batch-debit-post", method = RequestMethod.POST)
    public Result batchDebitPost(@RequestBody BatchDebitVo batchDebitVo, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        List<Long> orderIdList = batchDebitVo.getOrderIdList();
        if (orderIdList == null || orderIdList.size() == 0) {
            return Result.wrapErrorResult("", "批量收款, 请勾选工单");
        }
        if (orderIdList.size() > 50) {
            return Result.wrapErrorResult("", "批量收款的工单数不能大于50");
        }

        Long paymentId = batchDebitVo.getPaymentId();
        BigDecimal payAmount = batchDebitVo.getPayAmount();
        if (paymentId == null) {
            return Result.wrapErrorResult("", "请选择收款方式");
        }
        if (payAmount == null || payAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.wrapErrorResult("", "收款金额必须大于0");
        }

        batchDebitVo.setShopId(userInfo.getShopId());
        batchDebitVo.setUserId(userInfo.getUserId());
        try {
            debitFacade.batchSaveFlowList(batchDebitVo);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 新建收款单
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/bill-add", method = RequestMethod.GET)
    public String billAdd(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());

        return "/yqx/page/settlement/debit/bill-add";
    }

    /**
     * 保存收款单
     *
     * @param debitBillVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save-bill", method = RequestMethod.POST)
    public Result saveBill(HttpServletRequest request, DebitBillVo debitBillVo) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        debitBillVo.setShopId(shopId);
        debitBillVo.setCreator(userId);

        // 校验
        if (debitBillVo.getDebitTypeId() == null) {
            return Result.wrapErrorResult("", "业务类型不能为空");
        }
        if (StringUtils.isEmpty(debitBillVo.getBillName())) {
            return Result.wrapErrorResult("", "款项名称不能为空");
        }
        if (debitBillVo.getPaymentId() == null) {
            return Result.wrapErrorResult("", "支付方式不能为空");
        }
        if (debitBillVo.getPayAmount() == null) {
            return Result.wrapErrorResult("", "支付金额不能为空");
        }
        debitBillVo.setTotalAmount(debitBillVo.getPayAmount());
        debitBillVo.setReceivableAmount(debitBillVo.getPayAmount());
        debitBillVo.setPaidAmount(debitBillVo.getPayAmount());
        debitBillVo.setRelId(0L);
        try {
            logTrac(SiteUrls.SAVE_DEBIT_BILL, userId, debitBillVo);
            debitFacade.saveBill(debitBillVo);
            return Result.wrapSuccessfulResult(true);
        } catch (BizException e) {
            log.error("新建收款单异常:", e);
            return Result.wrapErrorResult("", "新建收款单失败");
        }
    }

    /**
     * 收款流水页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/flow-list", method = RequestMethod.GET)
    public String flowList(Model model,
                           @RequestParam(value = "startTime", required = false) String startTime,
                           @RequestParam(value = "endTime", required = false) String endTime) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());

        // 营业报表跳转过来
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = df.parse(startTime);
                Date endDate = df.parse(endTime);
                if (startDate.after(new Date()) || startDate.after(endDate)) {
                    return "/yqx/page/settlement/debit/flow-list";
                }
            } catch (ParseException e) {
                log.error("营业报表跳转收款单流水表,日期格式错误:参数:startTime={}, endTime={}, 异常信息:", startTime, endTime, e);
                return "/yqx/page/settlement/debit/flow-list";
            }
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
        }

        return "/yqx/page/settlement/debit/flow-list";
    }

    /**
     * 查询收款流水
     *
     * @param pageable
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/find-flow-list", method = RequestMethod.GET)
    public Result<DefaultPage<DebitBillFlowVo>> findFlowListByCondition(@PageableDefault(page = 1, value = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        DebitFlowSearchParam param = buildSearchParam(shopId, pageable);
        try {
            DefaultPage<DebitBillFlowVo> page = debitFacade.getDebitBillFlowPage(param);
            return Result.wrapSuccessfulResult(page);
        } catch (Exception e) {
            return Result.wrapErrorResult("", e.getMessage());
        }

    }

    /**
     * 导出收款流水
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/export-flow-list", method = RequestMethod.GET)
    public void exportFlowListByCondition(@PageableDefault(page = 1, value = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String fileName = "收款流水信息-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");

        DebitFlowSearchParam param = buildSearchParam(userInfo.getShopId(), pageable);
        param.setPageSize(Constants.MAX_PAGE_SIZE);

        long startTime = System.currentTimeMillis();

        ExcelExportor exportor = null;
        int pageNum = 1;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——收款流水信息";
            exportor.writeTitle(null, headline, DebitBillFlowVo.class);
            while (true) {
                param.setPageNum(pageNum++);
                DefaultPage<DebitBillFlowVo> page = debitFacade.getDebitBillFlowPage(param);
                if (page == null) {
                    break;
                }
                List<DebitBillFlowVo> content = page.getContent();
                if (CollectionUtils.isEmpty(content)) {
                    break;
                }
                exportor.write(content);
                totalSize += content.size();
                if (totalSize >= page.getTotalElements()) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("收款流水信息导出异常，门店id：{}", userInfo.getShopId(), e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("收款流水信息", userInfo, totalSize, endTime - startTime));
    }

    /**
     * 组装查询参数
     *
     * @param shopId
     * @param pageable
     * @return
     */
    private DebitFlowSearchParam buildSearchParam(Long shopId, Pageable pageable) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DebitFlowSearchParam param = new DebitFlowSearchParam();
        param.setShopId(shopId);
        // 款项名称,收银人员,付款方
        if (searchParams.containsKey("conditionLike")) {
            String conditionLike = (String) searchParams.get("conditionLike");
            param.setConditionLike(conditionLike);
        }
        // 结算类型id
        if (searchParams.containsKey("debitTypeId")) {
            Long debitTypeId = Long.parseLong((String) searchParams.get("debitTypeId"));
            param.setDebitTypeId(debitTypeId);
        }
        // 支付方式
        if (searchParams.containsKey("paymentId")) {
            Long paymentId = Long.parseLong((String) searchParams.get("paymentId"));
            if (paymentId != null) {
                if (paymentId.intValue() == -1) { // 坏账
                    param.setFlowStatus((byte) 1);
                    param.setPaymentId(0L);
                } else if (paymentId.intValue() == 0) { // 会员卡
                    param.setFlowStatus((byte) 0);
                    param.setPaymentId(0L);
                } else {
                    param.setFlowStatus((byte) 0);
                    param.setPaymentId(paymentId);
                }
            }
        }
        // 开始时间
        if (searchParams.containsKey("startTime")) {
            String startTime = (String) searchParams.get("startTime");
            param.setStartTime(startTime + " 00:00:00");
        }
        // 结束时间
        if (searchParams.containsKey("endTime")) {
            String endTime = (String) searchParams.get("endTime");
            param.setEndTime(endTime + " 23:59:59");
        }
        // 页码
        int pageNumber = pageable.getPageNumber();
        pageNumber = pageNumber < 1 ? 1 : pageNumber;
        param.setPageNum(pageNumber);
        // 每页显示数量
        int pageSize = pageable.getPageSize();
        pageSize = pageSize < 1 ? 1 : pageSize;
        param.setPageSize(pageSize);
        return param;
    }

    /**
     * 查询收款类型列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/type/list", method = RequestMethod.GET)
    public Result findDebitTypeList(HttpServletRequest request, @RequestParam(value = "needFilter", required = false, defaultValue = "false") Boolean needFilter) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        com.tqmall.core.common.entity.Result<List<DebitTypeDTO>> result = null;
        try {
            log.info("查询收款类型列表调用账单中心dubbo接口, 参数 shopId: {}", shopId);
            result = rpcDebitBillService.findDebitTypeListByShopId(shopId,OPEN_STATUE);
            log.info("查询收款类型列表调用账单中心dubbo接口返回值 result: {}", gson.toJson(result));

        } catch (Exception e) {
            log.error("查询收款类型列表异常: ", e);
            return Result.wrapSuccessfulResult(null);
        }
        if (!result.isSuccess()) {
            log.error("查询收款类型列表失败: {}", result.getMessage());
            return Result.wrapSuccessfulResult(null);
        }
        List<DebitTypeDTO> debitTypeDtoList = result.getData();
        List<DebitTypeVo> debitTypeVoList = null;

        // 需要过滤的类型集合
        List<Long> filterList = Lists.newArrayList(DebitTypeEnum.ORDER.getId(), DebitTypeEnum.ACCOUNT.getId(), DebitTypeEnum.ACTIVITY.getId());

        if (!CollectionUtils.isEmpty(debitTypeDtoList)) {
            debitTypeVoList = new ArrayList<>(debitTypeDtoList.size());
            for (DebitTypeDTO debitTypeDto : debitTypeDtoList) {
                Long id = debitTypeDto.getId();
                if (needFilter != null && needFilter) {
                    if (filterList.contains(id)) {
                        continue;
                    }
                }
                DebitTypeVo debitTypeVo = new DebitTypeVo();
                debitTypeVo.setId(id);
                debitTypeVo.setShopId(debitTypeDto.getShopId());
                debitTypeVo.setTypeName(debitTypeDto.getTypeName());
                debitTypeVoList.add(debitTypeVo);
            }
            return Result.wrapSuccessfulResult(debitTypeVoList);
        } else {
            log.error("查询收款类型列表异常: {}", result.getMessage());
            return Result.wrapSuccessfulResult(true);
        }
    }

    /**
     * 查询历史收支记录(包含工单优惠流水和收款单流水(包含冲红))
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/history-flow-list", method = RequestMethod.GET)
    public Result findFlowListByOrderId(@RequestParam Long orderId, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();

        Map<String, Object> map = new HashMap<>(2);

        // 查询工单优惠流水
        List<OrderDiscountFlowVo> discountFlowVoList = orderDiscountFlowFacade.getOrderDiscountFlow(orderId, shopId);
        map.put("discountFlowList", discountFlowVoList);

        // 查询工单收款单及流水
        DebitBillAndFlowVo debitBillAndFlow = debitFacade.findDebitBillAndFlow(shopId, orderId);
        if (debitBillAndFlow != null) {
            map.put("debitBillFlowList", debitBillAndFlow.getDebitBillFlowVoList());
        }
        return Result.wrapSuccessfulResult(map);
    }

    /**
     * 坏账处理
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bad-bill", method = RequestMethod.POST)
    public Result badBill(HttpServletRequest request, @RequestParam("orderIds") Long[] orderIds, @RequestParam(value = "remark", required = false) String remark) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();

        if (orderIds == null || orderIds.length == 0 || orderIds.length > 50) {
            return Result.wrapErrorResult("", "工单数量不正确");
        }

        try {
            for (Long orderId : orderIds) {
                debitFacade.badBill(shopId, orderId, userId, remark);
            }
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 收款单详情页
     *
     * @param request
     * @param model
     * @param billId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String debitBillDetail(HttpServletRequest request, Model model, @RequestParam("billId") Long billId, @RequestParam("flowId") Long flowId ) {
        boolean flag = wrapperDebitDtail(billId,flowId,model);
        if (!flag){
            return "common/error";
        }
        return "/yqx/page/settlement/debit/debit-detail";
    }

    /**
     * 收款单详情打印
     *
     * @param request
     * @param model
     * @param billId
     * @return
     */
    @RequestMapping(value = "/print/debit", method = RequestMethod.GET)
    public String debitBillDetailPrint(HttpServletRequest request, Model model, @RequestParam("billId") Long billId, @RequestParam("flowId") Long flowId ) {
        boolean flag = wrapperDebitDtail(billId,flowId,model);
        if (!flag){
            return "common/error";
        }
        return "/yqx/page/settlement/debit/print/debit";
    }

    private boolean wrapperDebitDtail(Long billId, Long flowId, Model model){
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop",shop);
        com.tqmall.core.common.entity.Result<DebitBillAndFlowDTO> result = null;
        try {
            log.info("查询收款单详情调用账单中心dubbo接口, 参数: shopId: {}, billId: {}, isRed: {}", shopId, billId, false);
            result = rpcDebitBillService.findBillById(shopId, billId, true);
            log.info("查询收款单详情调用账单中心dubbo接口, 返回值: result: {}", gson.toJson(result));
        } catch (Exception e) {
            log.error("查询收款单详情异常,", e);
            return false;
        }
        if (!result.isSuccess()) {
            log.error("查询收款单详情失败, {}", result.getMessage());
            return false;
        }

        DebitBillAndFlowDTO data = result.getData();
        if (data == null) {
            return false;
        }
        DebitBillDTO debitBillDTO = data.getDebitBillDTO();
        if (debitBillDTO == null) {
            return false;
        }
        model.addAttribute("debitBill", debitBillDTO);

        List<DebitBillFlowDTO> debitBillFlowDTOList = data.getDebitBillFlowDTOList();
        if (!CollectionUtils.isEmpty(debitBillFlowDTOList)) {
            for (DebitBillFlowDTO flowDTO : debitBillFlowDTOList) {
                if (flowDTO.getId().equals(flowId)) {
                    ShopManager shopManager = shopManagerService.selectById(flowDTO.getCreator());
                    if(shopManager != null) {
                        model.addAttribute("flowCreatorName", shopManager.getName());
                    }
                    model.addAttribute("debitBillFlow", flowDTO);
                    break;
                }
            }
        }
        Long relId = debitBillDTO.getRelId();
        List<DebitBillDTO> redBillDTOList = data.getRedBillDTOList();
        // 是否显示无效按钮
        boolean isShowInvalid = false;
        if (relId == 0) {
            if (CollectionUtils.isEmpty(redBillDTOList)) {
                isShowInvalid = true;
            }
        }
        model.addAttribute("isShowInvalid", isShowInvalid);
        return true;
    }

    /**
     * 收款单无效
     *
     * @param billId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/invalid", method = RequestMethod.POST)
    public Result debitBillInvalid(@RequestParam("billId") Long billId, @RequestParam("billName") String billName,HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        DebitInvalidByBillIdParam param = new DebitInvalidByBillIdParam();
        param.setShopId(userInfo.getShopId());
        param.setBillId(billId);
        param.setBillName(billName.concat("(无效)"));
        param.setCreator(userInfo.getUserId());
        param.setOperatorName(userInfo.getName());
        com.tqmall.core.common.entity.Result<Boolean> result = null;
        try {
            log.info("收款单无效调用账单中心dubbo接口, 参数: param: {}", gson.toJson(param));
            result = rpcDebitBillService.invalidByBillId(param);
            log.info("收款单无效调用账单中心dubbo接口, 返回值: result: {}", gson.toJson(result));
        } catch (Exception e) {
            log.error("收款单无效异常, ", e);
            return Result.wrapErrorResult("", "收款单无效失败");
        }
        if (result != null && !result.isSuccess()) {
            log.error("收款单无效异常, {}", result.getMessage());
            return Result.wrapErrorResult("", result.getMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 获取默认的结算方式
     *
     * @param model
     * @param shopId
     */
    private void getDefaultPayment(Model model, Long shopId) {
        List<Payment> paymentList = paymentService.getPaymentsByShopId(shopId);
        if (!CollectionUtils.isEmpty(paymentList)) {
            for (Payment payment : paymentList) {
                String paymentName = payment.getName();
                // 默认收款方式
                if (paymentName.equals("现金")) {
                    Long defaultPaymentId = payment.getId();
                    String defaultPaymentName = paymentName;
                    model.addAttribute("defaultPaymentId", defaultPaymentId);
                    model.addAttribute("defaultPaymentName", defaultPaymentName);
                    break;
                }
            }
        }
    }

    /**
     * 查询收款流水支付方式
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/flow/payment", method = RequestMethod.GET)
    public Result findDebitFlowPayment(HttpServletRequest request) {
        long shopId = UserUtils.getShopIdForSession(request);

        List<PaymentDTO> paymentList = Lists.newArrayList();

        // 坏账
        PaymentDTO badPayment = new PaymentDTO();
        badPayment.setId(-1L);
        badPayment.setName("坏账");
        paymentList.add(badPayment);
        // 会员卡
        PaymentDTO memberPayment = new PaymentDTO();
        memberPayment.setId(0L);
        memberPayment.setName("会员卡");
        paymentList.add(memberPayment);

        // 查询门店的支付方式
        com.tqmall.core.common.entity.Result<List<PaymentDTO>> paymentListResult = rpcSettlementService.getPaymentList(shopId);
        if(!paymentListResult.isSuccess() || CollectionUtils.isEmpty(paymentListResult.getData())){
            return Result.wrapSuccessfulResult(paymentList);
        }
        List<PaymentDTO> paymentDTOList = paymentListResult.getData();
        paymentList.addAll(paymentDTOList);
        return Result.wrapSuccessfulResult(paymentList);
    }

    /**
     * log trac
     * 业务场景:xx|操作人:xx|操作实体:xx
     *
     * @param scene
     * @param userId
     */
    private void logTrac(String scene, Long userId, Object optObj) {
        StringBuffer logSB = new StringBuffer("业务场景:");
        logSB.append(SiteUrlNameEnum.getUrlName(scene));
        logSB.append(" | ");
        logSB.append("操作人:");
        logSB.append(userId);
        logSB.append(" | ");
        logSB.append("操作实体:");
        logSB.append((optObj == null) ? "" : optObj.toString());
        log.info(logSB.toString());
    }

    @RequestMapping("addDebitType")
    @ResponseBody
    public Result addDebitType(@RequestParam("debitTypeStr") String debitTypeStr){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            if (StringUtils.isBlank(debitTypeStr)){
                return Result.wrapSuccessfulResult(true);
            }
            Set<String> debitTypeList = new Gson().fromJson(debitTypeStr, new TypeToken<Set<String>>() {
            }.getType());
            if (!CollectionUtils.isEmpty(debitTypeList)) {
                DebitTypeBatchParam debitTypeBatchParam = new DebitTypeBatchParam();
                debitTypeBatchParam.setShopId(userInfo.getShopId());
                debitTypeBatchParam.setCreator(userInfo.getUserId());
                debitTypeBatchParam.setNameList(debitTypeList);
                //工单类型重复不处理
                com.tqmall.core.common.entity.Result<Integer> result = rpcDebitBillService.batchSaveDebitType(debitTypeBatchParam);
                if (!result.isSuccess()){
                    log.error("[DUBBO]调用billcenter接口批量保存收款类型失败, 参数:{},调用结果:{}", new Gson().toJson(debitTypeBatchParam), new Gson().toJson(result));
                    return Result.wrapErrorResult("", result.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("添加付款类型失败",e);
            return Result.wrapErrorResult("", "保存收款类型失败");
        }
        //跳转至服务类别页面
        return Result.wrapSuccessfulResult(true);
    }

    @RequestMapping("getDebitType/list")
    @ResponseBody
    public Result getDebitTypeList(){
        Long shopId =  UserUtils.getShopIdForSession(request);
        try {
            com.tqmall.core.common.entity.Result<List<DebitTypeDTO>> result= rpcDebitBillService.findDebitTypeListByShopId(shopId,OPEN_STATUE);
            if(null == result || !result.isSuccess()){
                log.error("调用DUBBO接口获取收款业务类型失败,result:{}",result);
                return Result.wrapErrorResult("","调用DUBBO接口获取收款业务类型失败");
            }
            List<DebitTypeDTO> typeList = result.getData();
            return Result.wrapSuccessfulResult(typeList);
        } catch (Exception e) {
            log.error("调用DUBBO接口获取收款业务类型失败,shopId:{}",shopId,e);
        }
        return Result.wrapErrorResult("","调用DUBBO接口获取收款业务类型失败");
    }


}
