package com.tqmall.legend.web.onlinepay;

import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.finance.model.result.user.BankCardDTO;
import com.tqmall.finance.model.result.user.UserBankCardDTO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.facade.onlinepay.LianlianPayFacade;
import com.tqmall.legend.facade.sell.SellOrderPayFaced;
import com.tqmall.legend.pojo.sell.SellOrderPayStatusChangeVO;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 2017/2/21.
 */
@Slf4j
@Controller
@RequestMapping("onlinepay/lianlian")
public class LianlianPayController extends BaseController {
    @Autowired
    private LianlianPayFacade lianlianPayFacade;
    @Autowired
    private SellOrderPayFaced sellOrderPayFaced;

    /**
     * 获取已拥有的银行卡列表
     *
     * @return
     */
    @RequestMapping(value = "card-list", method = RequestMethod.GET)
    @ResponseBody
    public Result cardList(@RequestParam("ucShopId") Integer ucShopId) {
        List<UserBankCardDTO> bankCardList = lianlianPayFacade.selectUserBankCardList(ucShopId);
        return Result.wrapSuccessfulResult(bankCardList);
    }

    /**
     * 获取支持的借记卡列表
     *
     * @return
     */
    @RequestMapping(value = "support-debit-card-list", method = RequestMethod.GET)
    @ResponseBody
    public Result supportDebitCardlist() {
        List<BankCardDTO> bankCardList = lianlianPayFacade.selectSupportDebitCardList();
        return Result.wrapSuccessfulResult(bankCardList);
    }

    /**
     * 获取支持的信用卡列表
     *
     * @return
     */
    @RequestMapping(value = "support-credit-card-list", method = RequestMethod.GET)
    @ResponseBody
    public Result supportCreditCardlist() {
        List<BankCardDTO> bankCardList = lianlianPayFacade.selectSupportCreditCardList();
        return Result.wrapSuccessfulResult(bankCardList);
    }

    @RequestMapping(value = "check-card", method = RequestMethod.GET)
    @ResponseBody
    public Result checkCardNo(@RequestParam("cardNo") String cardNo,
                              @RequestParam("bankCode") String bankCode,
                              @RequestParam("cardType") Integer cardType) {
        //获取银行卡列表信用卡cardType是1,校验接口信用卡cardType0
        boolean flag = lianlianPayFacade.checkCardNo(cardNo, bankCode, cardType);
        if (!flag) {
            return Result.wrapErrorResult("", "银行卡号与所选银行不符");
        }
        return Result.wrapSuccessfulResult(flag);
    }


    /**
     * 连连支付回调
     *
     * @return
     */
    @RequestMapping(value = "verify")
    public String verify(Model model) {
        Map<String, String[]> paramMap = request.getParameterMap();
        if (MapUtils.isEmpty(paramMap)) {
            return "common/error";
        }
        log.info("连连支付回调返回参数:{}", LogUtils.objectToString(paramMap));
        boolean success = lianlianPayFacade.verify(paramMap);
        SellOrder order = updatePayStatusForSyn(paramMap, success);
        model.addAttribute("order", order);
        if (success) {
            return "yqx/page/onlinepay/lianlian/payment-success";
        }

        return "yqx/page/onlinepay/lianlian/payment-failure";
    }

    private SellOrder updatePayStatusForSyn(Map<String, String[]> param, boolean success) {
        String orderSn = param.get("no_order")[0];
        String payNo = param.get("oid_paybill")[0];
        String totalAmount = param.get("money_order")[0];
        String date = param.get("dt_order")[0];
        SellOrderPayStatusChangeVO vo = new SellOrderPayStatusChangeVO();
        vo.setPayOrderSn(orderSn);
        vo.setPayNo(payNo);
        vo.setPayAmount(new BigDecimal(totalAmount));
        vo.setPayTime(DateUtil.convertStringToDate(date, "yyyyMMddHHmmss"));
        vo.setCheckStatus(2);
        if (!success) {
            vo.setCheckStatus(1);
        }
        try {
            sellOrderPayFaced.updatePayStatusForSyn(vo);
        } catch (Exception e) {
            log.error("更新销售订单失败,错误原因:{}", e);
        }
        SellOrder order = sellOrderPayFaced.getSellOrderByPayOrderSn(orderSn);
        return order;
    }
}
