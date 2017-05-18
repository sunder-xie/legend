package com.tqmall.legend.web.setting;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.facade.setting.PaymentFacade;
import com.tqmall.legend.facade.shop.ShopConfigureFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zsy on 16/11/8.
 * 结算方式类型设置
 */
@Slf4j
@Controller
@RequestMapping("shop/setting/payment")
public class PaymentSettingController extends BaseController {
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private CacheComponent<List<Payment>> cacheComponent;
    @Autowired
    private ShopConfigureFacade shopConfigureFacade;
    /**
     * 工单结算方式页面
     *
     * @param model
     * @return
     */
    @RequestMapping
    public String index(Model model) {
        //查询门店已存在的支付方式
        Long shopId = UserUtils.getShopIdForSession(request);
        List<Payment> paymentList = paymentFacade.getPaymentByShopId(shopId);
        model.addAttribute("paymentList", paymentList);
        //使用他人卡券设置
        String shopConfValue = shopConfigureFacade.getConfValue(ShopConfigureTypeEnum.USE_GUEST_ACCOUNT, shopId);
        model.addAttribute("shopConfValue", shopConfValue);
        return "yqx/page/setting/payment";
    }

    /**
     * 更新结算方式
     *
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Result insert(@RequestBody Payment payment) {
        if(payment == null){
            return Result.wrapErrorResult("","数据为空");
        }
        Long id = payment.getId();
        if(id == null){
            return Result.wrapErrorResult("","id不存在");
        }
        Payment existPayment = paymentFacade.getCachePaymentById(id);
        if(existPayment == null){
            return Result.wrapErrorResult("","结算方式不存在");
        }
        Long userId = UserUtils.getUserIdForSession(request);
        Integer showStatus = payment.getShowStatus();
        existPayment.setShowStatus(showStatus);
        existPayment.setModifier(userId);
        paymentFacade.updatePaymentById(payment);
        //刷新缓存 TODO 后续缓存改造
        cacheComponent.reload(CacheKeyConstant.PAYMENT);
        return Result.wrapSuccessfulResult(true);
    }
}
