package com.tqmall.legend.web.onlinepay;

import com.tqmall.common.util.WebUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.onlinepay.OnlinePayFacade;
import com.tqmall.legend.facade.onlinepay.bo.OnlinePayBo;
import com.tqmall.legend.facade.onlinepay.enums.OnlinePayMethodEnum;
import com.tqmall.legend.facade.onlinepay.vo.OnlinePayVo;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by sven on 2017/2/21.
 */
@Slf4j
@Controller
@RequestMapping("onlinepay")
public class OnlinepayController extends BaseController {
    @Resource
    private OnlinePayFacade onlinePayFacade;

    /**
     * 支持银行卡类型的支付
     *
     * @param orderSn
     * @param payMethod
     * @param source
     * @param model
     * @return
     */
    @RequestMapping(value = "pay-card-route", method = RequestMethod.GET)
    public String payCardRoute(@RequestParam("orderSn") String orderSn,
                               @RequestParam("payMethod") Integer payMethod,
                               @RequestParam("source") Integer source, Model model) {
        BigDecimal totalFee = onlinePayFacade.selectPayAmount(source, orderSn);
        String url = "comm/error";
        if (BigDecimal.ZERO.compareTo(totalFee) == 0) {
            return url;
        }
        if (OnlinePayMethodEnum.isLianlianPay(payMethod)) {
            url = "yqx/page/onlinepay/lianlian/card-select";
        }
        Integer ucshopId = onlinePayFacade.selectUserGlobalShopId(orderSn, source, request);
        OnlinePayVo vo = new OnlinePayVo();
        vo.setOrderSn(orderSn);
        vo.setPayMethod(payMethod);
        vo.setSource(source);
        vo.setUcShopId(ucshopId);
        vo.setTotalFee(totalFee);
        model.addAttribute("payVo", vo);
        return url;
    }

    /**
     * 支付
     *
     * @param onlinePayBo
     * @return
     */
    @RequestMapping(value = "pay-choice", method = RequestMethod.POST)
    @ResponseBody
    public Result pay(@RequestBody OnlinePayBo onlinePayBo) {
        if (onlinePayBo == null) {
            return Result.wrapErrorResult("", "数据有误,支付失败");
        }
        String url = WebUtils.getHostUrl(request) + OnlinePayMethodEnum.getReturnUrl(onlinePayBo.getPaymentMethod());
        onlinePayBo.setReturnUrl(url);
        String htmlInfo = onlinePayFacade.payChoice(onlinePayBo);
        if (htmlInfo == null || "".equals(htmlInfo)) {
            return Result.wrapErrorResult("", "获取支付信息失败");
        }
        return Result.wrapSuccessfulResult(htmlInfo);
    }
}
