package com.tqmall.legend.web.insurance;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.WebUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.result.InsuranceFeeDTO;
import com.tqmall.legend.facade.insurance.AnxinInsurancePayFacade;
import com.tqmall.legend.facade.insurance.InsuranceFormFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.mace.param.anxin.RpcAxPayParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zwb on 17/3/8.
 * 安心保险支付
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/pay")
public class AnxinInsuracnePayController extends BaseController {
    @Autowired
    private AnxinInsurancePayFacade insurancePayFacade;
    @Autowired
    private InsuranceFormFacade insuranceFormFacade;


    @RequestMapping(value = "choose", method = RequestMethod.GET)
    public String choose(String sn, Model model) {
        String shopId = UserUtils.getUserGlobalIdForSession(request);
        InsuranceFeeDTO insuranceFeeDTO = insuranceFormFacade.selectPayInfoByOrderSn(Integer.valueOf(shopId), sn);
        if (insuranceFeeDTO == null) {
            return "common/error";
        }
        model.addAttribute("insuranceFeeDTO", insuranceFeeDTO);
        return "yqx/page/ax_insurance/create/payPage";
    }

    /**
     * 跳转到确认支付页面获取支付金额
     *
     * @param orderSn
     * @return
     */
    @RequestMapping("confirm-info")
    @ResponseBody
    public Result uploadInfo(String orderSn) {
        String shopId = UserUtils.getUserGlobalIdForSession(request);
        InsuranceFeeDTO insuranceFeeDTO = insuranceFormFacade.selectPayInfoByOrderSn(Integer.valueOf(shopId), orderSn);
        return Result.wrapSuccessfulResult(insuranceFeeDTO);
    }


    /**
     * 支付宝支付
     *
     * @return
     */
    @RequestMapping(value = "ali", method = RequestMethod.GET)
    public String aliPay(String orderSn, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        RpcAxPayParam param = new RpcAxPayParam();
        param.setShopId(userInfo.getShopId());
        param.setUserId(userInfo.getUserId());
//        http://yun2.epei360.com/legend/insurance/anxin/pay/fail
        param.setSuccessPage(WebUtils.getHostUrl(request) + "/insurance/anxin/pay/success?orderSn=" + orderSn);
        param.setFailPage(WebUtils.getHostUrl(request) + "/insurance/anxin/pay/fail?orderSn=" + orderSn);
        param.setInsuranceOrderSn(orderSn);
        String htmlInfo = insurancePayFacade.getAliPayPageInfo(param);
        model.addAttribute("htmlInfo", htmlInfo);
        return "yqx/page/ax_insurance/create/pay";
    }


    /**
     * 跳转到支付结果页面
     *
     * @return
     */
    @RequestMapping(value = "success", method = RequestMethod.GET)
    public String paySuccess(Model model) {
        showPayResult(model);
        return "yqx/page/ax_insurance/create/paySuccess";
    }


    @RequestMapping(value = "fail", method = RequestMethod.GET)
    public String payFail(Model model) {
        showPayResult(model);
        return "yqx/page/ax_insurance/create/payFail";
    }

    private void showPayResult(Model model) {
        String shopId = UserUtils.getUserGlobalIdForSession(request);
        String orderSn = request.getParameter("orderSn");
        if (orderSn != null && !"".equals(orderSn)) {
            InsuranceFeeDTO insuranceFeeDTO = insuranceFormFacade.selectPayInfoByOrderSn(Integer.valueOf(shopId), orderSn);
            model.addAttribute("orderSn", orderSn);
            model.addAttribute("insuranceFeeDTO", insuranceFeeDTO);
        }
    }

}


