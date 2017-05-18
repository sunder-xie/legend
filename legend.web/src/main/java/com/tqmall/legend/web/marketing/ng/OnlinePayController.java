package com.tqmall.legend.web.marketing.ng;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.WebUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.pay.OnlinePayService;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * Created by wanghui on 3/15/16.
 */
@Controller
@RequestMapping("/shop/onlinepay")
@Slf4j
public class OnlinePayController extends BaseController{

    @Autowired
    private OnlinePayService onlinePayService;

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public String onlinePay(@RequestParam("payFee")BigDecimal payFee,
                            @RequestParam("smsNum")Integer smsNum,
                            @RequestParam("payWay")String payWay,
                            @RequestParam(value = "payRemark",required = false)String payRemark, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);


        if("zhifubao".equals(payWay)) {
            String htmlText = onlinePayService.payByZhifubao(userInfo, payFee,smsNum, payRemark, WebUtils.getHostUrl(request));

            if (htmlText != null) {
                model.addAttribute("payHtml", htmlText);
            } else {
                model.addAttribute("payHtml", "获取支付信息失败.");
            }
            return "onlinepay/zhifubao";
        }

        return "onlinepay/pay_error";
    }
}
