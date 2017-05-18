package com.tqmall.legend.web.settlement;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.object.result.settlement.PaymentDTO;
import com.tqmall.legend.service.settlement.RpcSettlementService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by QXD on 2014/12/26.
 */
@Controller("PaymentController")
@RequestMapping("shop/payment")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RpcSettlementService rpcSettlementService;

    /**
     * 获取结算方式菜单
     */
    @RequestMapping(value = "get_payment")
    @ResponseBody
    public Result getPayment(HttpServletRequest request) {
        long shopId = UserUtils.getShopIdForSession(request);

        List<Payment> paymentList = paymentService.getPaymentsByShopId(shopId);

        if (CollectionUtils.isEmpty(paymentList)) {
            return Result.wrapErrorResult("", "获取失败");
        }
        return Result.wrapSuccessfulResult(paymentList);
    }
    
    @RequestMapping(value = "search_payment")
    @ResponseBody
    public Result searchPayment(HttpServletRequest request) {
        List<Payment> paymentList = paymentService.searchPayments(ServletUtils.getParametersMapStartWith(request));

        if (CollectionUtils.isEmpty(paymentList)) {
            return Result.wrapErrorResult("", "获取失败");
        }
        return Result.wrapSuccessfulResult(paymentList);
    }

    /**
     * 获取结算方式菜单
     */
    @RequestMapping(value = "get-order-payment")
    @ResponseBody
    public Result getOrderPayment() {
        long shopId = UserUtils.getShopIdForSession(request);
        com.tqmall.core.common.entity.Result<List<PaymentDTO>> paymentDTOListResult = rpcSettlementService.getPaymentList(shopId);
        if(paymentDTOListResult.isSuccess()){
            return Result.wrapSuccessfulResult(paymentDTOListResult.getData());
        }
        return Result.wrapErrorResult("", paymentDTOListResult.getMessage());
    }
}
