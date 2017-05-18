package com.tqmall.legend.web.settlement;

import com.google.common.base.Optional;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.discount.DiscountCenter2;
import com.tqmall.legend.facade.discount.bo.DiscountInfoBo;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;
import com.tqmall.legend.facade.settlement.SettlementFacade;
import com.tqmall.legend.facade.settlement.bo.DiscountCarWashOrderBo;
import com.tqmall.legend.facade.settlement.bo.DiscountOrderBo;
import com.tqmall.legend.facade.settlement.bo.GuestMobileCheckBo;
import com.tqmall.legend.facade.settlement.bo.SettlementSmsBO;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * Created by zsy on 16/6/13.
 * 工单优惠
 * 1、同一张工单只能使用一张会员卡优惠及结算，即使是分多次结算，仍然只能使用同一张会员卡结算；
 * 2、优惠金额：系统默认取该车牌对应账户下有效会员卡的本单优惠金额最大者，如果优惠金额相等，
 *    次选会员卡余额较大者，再次取会员卡过期时间最近，最次随机选择；
 * 3、如果用户已选使用会员卡优惠，则结算环节，只能使用已选会员卡结算；
 * 4、如果用户已选使用会员卡优惠，优惠金额仍可编辑，且认为是使用会员卡优惠；
 * 5、如果用户不使用会员卡优惠，则结算环节，用户可以从全部会员卡中选择任意一张会员卡结算（即使会员卡余额不足，可挂账）
 * 6、工单结算可以使用其他客户的卡券，但必须通过持卡车主电话短信验证
 * 7、一张工单只能使用同一个非关联车主的会员卡和优惠券
 */
@Slf4j
@Controller
@RequestMapping("shop/settlement/debit/discount")
public class OrderDiscountController extends BaseController {
    @Autowired
    private SettlementFacade settlementFacade;
    @Autowired
    private DiscountCenter2 discountCenter2;
    @Autowired
    private IOrderService orderService;

    /**
     * 根据工单信息和已选中优惠信息获取优惠信息
     * 快修快保、综合维修、销售单
     * @param discountOrderBo
     * @return
     */
    @RequestMapping(value = "discount-order", method = RequestMethod.POST)
    @ResponseBody
    public Result<DiscountInfoBo> discountOrder(@RequestBody final DiscountOrderBo discountOrderBo) {
        return new ApiTemplate<DiscountInfoBo>() {
            Long shopId = UserUtils.getShopIdForSession(request);

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(discountOrderBo, "请选择优惠信息");
                Long orderId = discountOrderBo.getOrderId();
                Assert.notNull(orderId, "工单id为空");
                Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
                Assert.isTrue(orderInfoOptional.isPresent(), "工单不存在");
                DiscountSelectedBo discountSelectedBo = discountOrderBo.getDiscountSelectedBo();
                Assert.notNull(discountSelectedBo, "优惠参数为空");
            }

            @Override
            protected DiscountInfoBo process() throws BizException {
                Long orderId = discountOrderBo.getOrderId();
                DiscountSelectedBo discountSelectedBo = discountOrderBo.getDiscountSelectedBo();
                DiscountInfoBo discountInfoBo = discountCenter2.discountOrder(shopId, orderId, discountSelectedBo);
                return discountInfoBo;
            }
        }.execute();
    }

    /**
     * 根据车牌,金额,服务ID和已选中优惠信息获取优惠信息
     * 洗车单
     * @param discountCarWashOrderBo
     * @return
     */
    @RequestMapping(value = "discount-carwash-order", method = RequestMethod.POST)
    @ResponseBody
    public Result<DiscountInfoBo> discountCarWashOrder(@RequestBody final DiscountCarWashOrderBo discountCarWashOrderBo) {
        return new ApiTemplate<DiscountInfoBo>() {
            Long shopId = UserUtils.getShopIdForSession(request);

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(discountCarWashOrderBo, "请选择优惠信息");
                String license = discountCarWashOrderBo.getCarLicense();
                Assert.notNull(license, "车牌为空");
                Assert.notNull(discountCarWashOrderBo.getCarWashAmount(), "洗车金额为空");
                DiscountSelectedBo discountSelectedBo = discountCarWashOrderBo.getDiscountSelectedBo();
                Assert.notNull(discountSelectedBo, "优惠参数为空");
            }

            @Override
            protected DiscountInfoBo process() throws BizException {
                String carLicense = discountCarWashOrderBo.getCarLicense();
                Long serviceId = discountCarWashOrderBo.getCarWashServiceId();
                BigDecimal carWashAmount = discountCarWashOrderBo.getCarWashAmount();
                DiscountSelectedBo discountSelectedBo = discountCarWashOrderBo.getDiscountSelectedBo();
                DiscountInfoBo discountInfoBo = discountCenter2.discountCarWashOrder(shopId, carLicense, serviceId, carWashAmount, discountSelectedBo);
                return discountInfoBo;
            }
        }.execute();
    }

    /**
     * 使用其他客户的优惠券、会员卡，发送短信验证码接口
     *
     * @return
     */
    @RequestMapping(value = "send-code", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> sendCode(@RequestBody final SettlementSmsBO settlementSmsBO) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(settlementSmsBO,"参数为空");
                Assert.notNull(settlementSmsBO.getLicense(),"车牌为空");
                boolean checkMobile = StringUtil.isMobileNO(settlementSmsBO.getMobile());
                Assert.isTrue(checkMobile, "手机号为空或格式有误");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return settlementFacade.sendCode(settlementSmsBO, shopId);
            }
        }.execute();
    }

    /**
     * 使用其他客户的优惠券、会员卡，发送短信验证码接口
     *
     * @return
     */
    @RequestMapping(value = "check-code", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> checkCode(@RequestBody final GuestMobileCheckBo guestMobileCheckBo) {
        return new ApiTemplate<Boolean>() {
            Long shopId = UserUtils.getShopIdForSession(request);

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(guestMobileCheckBo, "参数为空");
                String mobile = guestMobileCheckBo.getGuestMobile();
                Assert.notNull(mobile, "手机号为空");
                boolean checkMobile = StringUtil.isMobileNO(mobile);
                Assert.isTrue(checkMobile, "手机号为空或格式有误");
                Assert.notNull(guestMobileCheckBo.getCode(), "验证码为空");
            }

            @Override
            protected Boolean process() throws BizException {
                String mobile = guestMobileCheckBo.getGuestMobile();
                String code = guestMobileCheckBo.getCode();
                return settlementFacade.checkCode(mobile, code, shopId);
            }
        }.execute();
    }
}
