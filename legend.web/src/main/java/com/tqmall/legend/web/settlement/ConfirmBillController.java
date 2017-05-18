package com.tqmall.legend.web.settlement;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.bo.ConfirmBillBo;
import com.tqmall.legend.biz.order.bo.DebitBillBo;
import com.tqmall.legend.biz.order.bo.PayChannelBo;
import com.tqmall.legend.biz.order.bo.SpeedilyBillBo;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.order.PayStatusEnum;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.facade.settlement.ConfirmBillFacade;
import com.tqmall.legend.facade.shop.ShopConfigureFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 确认账单
 * Created by zsy on 16/6/6.
 */
@Slf4j
@Controller
@RequestMapping("shop/settlement/debit")
public class ConfirmBillController extends BaseController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private ConfirmBillFacade confirmBillFacade;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShopConfigureFacade shopConfigureFacade;

    /**
     * 综合维修单确认账单页面
     *
     * @param model
     * @param orderId
     * @return
     */
    @RequestMapping(value = "confirm-bill")
    public String confirmBill(Model model, @RequestParam(value = "orderId", required = false) Long orderId) {
        //查询工单
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return "common/error";
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        //DDWC 才能对账
        String orderStatus = orderInfo.getOrderStatus();
        Integer payStatus = orderInfo.getPayStatus();
        if (!OrderNewStatusEnum.YWG.getOrderStatus().equals(orderStatus) || !payStatus.equals(0)) {
            return "redirect:/shop/settlement/debit/order-detail?orderId=" + orderId;
        }
        model.addAttribute("orderInfo", orderInfo);
        setSameModel(model, orderId, shopId);
        return "/yqx/page/settlement/debit/confirm-bill";
    }

    private void setSameModel(Model model, Long orderId, Long shopId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String userName = userInfo.getName();
        //收银人员
        model.addAttribute("operatorName", userName);
        //工单服务信息
        List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId);
        model.addAttribute("orderServicesList", orderServicesList);
        //工单物料信息
        List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId);
        model.addAttribute("orderGoodsList", orderGoodsList);
        //设置是否使用他人账户
        String confValue = shopConfigureFacade.getConfValue(ShopConfigureTypeEnum.USE_GUEST_ACCOUNT, shopId);
        if (StringUtils.isNotBlank(confValue)) {
            model.addAttribute("USE_GUEST_ACCOUNT", confValue);
        }
    }

    /**
     * 保存账单
     *
     * @param confirmBillBo
     * @return
     */
    @RequestMapping(value = "save-debit-bill", method = RequestMethod.POST)
    @ResponseBody
    public Result<DebitBillDTO> saveDebitBill(@RequestBody final ConfirmBillBo confirmBillBo) {
        return new ApiTemplate<DebitBillDTO>() {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = UserUtils.getShopIdForSession(request);

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(confirmBillBo, "参数为空");
                DebitBillBo debitBillBo = confirmBillBo.getDebitBillBo();
                Assert.notNull(debitBillBo, "参数为空");
                Long orderId = debitBillBo.getRelId();
                Assert.notNull(orderId, "工单id为空");
            }

            @Override
            protected DebitBillDTO process() throws BizException {
                Long orderId = confirmBillBo.getDebitBillBo().getRelId();
                Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
                if (!orderInfoOptional.isPresent()) {
                    throw new BizException("工单不存在");
                }
                OrderInfo orderInfo = orderInfoOptional.get();
                //DDWC 才能对账
                String orderStatus = orderInfo.getOrderStatus();
                Integer payStatus = orderInfo.getPayStatus();
                if (!OrderNewStatusEnum.YWG.getOrderStatus().equals(orderStatus) || !payStatus.equals(PayStatusEnum.UNPAY.getCode())) {
                    throw new BizException("工单不是待结算状态");
                }
                com.tqmall.legend.common.Result<DebitBillDTO> confirmBill = confirmBillFacade.confirmBill(confirmBillBo, userInfo);
                if (confirmBill.isSuccess()) {
                    return confirmBill.getData();
                }
                throw new BizException(confirmBill.getErrorMsg());
            }
        }.execute();
    }


    /**
     * 快修快保 收款
     *
     * @param model
     * @param orderId
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "speedily-confirm-bill")
    public String speedilyConfirmBill(Model model, @RequestParam(value = "orderId", required = false) Long orderId) {
        //查询工单
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return "common/error";
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        // 待报价状态:进入账单确认收款页面
        String orderStatus = orderInfo.getOrderStatus();
        Integer payStatus = orderInfo.getPayStatus();
        if (!OrderNewStatusEnum.DBJ.getOrderStatus().equals(orderStatus) || !payStatus.equals(0)) {
            return "redirect:/shop/settlement/debit/order-detail?orderId=" + orderId;
        }
        model.addAttribute("orderInfo", orderInfo);
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
        setSameModel(model, orderId, shopId);
        return "/yqx/page/settlement/debit/speedily-confirm-bill";
    }


    /**
     * <p/>
     * 快修快保|销售单 结算
     *
     * @param drawUpBo
     * @return
     */
    @RequestMapping(value = "speedily-save-bill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> speedilySaveDebitBill(@RequestBody final SpeedilyBillBo drawUpBo) {
        return new ApiTemplate<Boolean>() {
            UserInfo userInfo = UserUtils.getUserInfo(request);

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(drawUpBo, "数据为空");
                // 工单存在校验
                DebitBillBo debitBillBo = drawUpBo.getDebitBillBo();
                Assert.notNull(debitBillBo, "收款信息为空");
                Long orderId = debitBillBo.getRelId();
                Assert.notNull(orderId, "工单id为空");
                // 会员优惠金额校验 >0
                BigDecimal memberDiscount = drawUpBo.getDiscountAmount();
                if (memberDiscount != null && memberDiscount.compareTo(BigDecimal.ZERO) == -1) {
                    throw new IllegalArgumentException("工单收款失败,[原因]优惠金额不能小于0元!");
                }
                // 淘汽优惠 有效性校验
                BigDecimal taoqiCouponAmount = drawUpBo.getTaoqiCouponAmount();
                if (taoqiCouponAmount != null && taoqiCouponAmount.compareTo(BigDecimal.ZERO) == -1) {
                    throw new IllegalArgumentException("工单收款失败,[原因]淘汽优惠金额不能小于0元!");
                }
                // 收款金额校验 数字
                List<PayChannelBo> payChannelList = drawUpBo.getPayChannelBoList();
                for (PayChannelBo payChannel : payChannelList) {
                    Long payChannelId = payChannel.getChannelId();
                    BigDecimal payAmount = payChannel.getPayAmount();
                    // 有效的收款
                    if (payChannelId != null && payChannelId > 0l && payAmount != null && payAmount.compareTo(BigDecimal.ZERO) == -1) {
                        String ChannelName = payChannel.getChannelName();
                        throw new IllegalArgumentException("工单收款失败,[原因]收款方式是:" + ChannelName + ",收款金额 小于0元!");
                    }
                }
                // 会员卡余额支付金额校验 >0
                Long memberIdForSettle = drawUpBo.getMemberIdForSettle();
                BigDecimal memberBalancePay = drawUpBo.getMemberBalancePay();
                if (memberIdForSettle != null && memberIdForSettle > 0 && memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) == -1) {
                    throw new IllegalArgumentException("工单收款失败,[原因]使用'会员卡余额支付的金额' 不能小于0元!");
                }
            }

            @Override
            protected Boolean process() throws BizException {
                // IF 会员余额结算 THEN 添加到收款列表
                BigDecimal memberBalancePay = drawUpBo.getMemberBalancePay();
                if (memberBalancePay != null && memberBalancePay.compareTo(BigDecimal.ZERO) == 1) {
                    List<PayChannelBo> payChannelList = drawUpBo.getPayChannelBoList();
                    PayChannelBo cardMemberPayChannel = new PayChannelBo();
                    cardMemberPayChannel.setChannelId(0l);
                    cardMemberPayChannel.setPayAmount(memberBalancePay);
                    cardMemberPayChannel.setChannelName("会员卡");
                    payChannelList.add(cardMemberPayChannel);
                }
                Long orderId = drawUpBo.getDebitBillBo().getRelId();
                Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
                if (!orderInfoOptional.isPresent()) {
                    throw new BizException("工单收款失败,[原因]工单不存在!");
                }
                try {
                    confirmBillFacade.speedilyDrawUp(drawUpBo, orderInfoOptional.get(), userInfo);
                    return true;
                } catch (RuntimeException e) {
                    log.error("[快修快保首次收款]工单收款失败,原因：", e);
                    throw new BizException(e.getMessage());
                }
            }
        }.execute();
    }
}
