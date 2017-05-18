package com.tqmall.legend.web.order;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.order.OrderInvoiceLogService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInvoiceLog;
import com.tqmall.legend.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by QXD on 2015/1/15.
 */
@Controller("OrderInvoiceLogController")
@RequestMapping("shop/order_invoice")
public class OrderInvoiceLogController extends BaseController {
    public final static Logger logger = LoggerFactory.getLogger(OrderInvoiceLogController.class);

    @Autowired
    OrderInvoiceLogService orderInvoiceLogService;

    /**
     * 开票单提交
     */
    @RequestMapping(value = "invoice_put", method = RequestMethod.POST)
    @ResponseBody
    public Result invoicePut(HttpServletRequest request, OrderInvoiceLog orderInvoiceLog) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        long creator = userInfo.getUserId();
        long shopId = userInfo.getShopId();
        String createName = userInfo.getName();
        if (null == orderInvoiceLog) {
            logger.warn("orderInvoiceLog为空");
            return Result.wrapErrorResult("", "参数错误");
        }
        orderInvoiceLog.setShopId(shopId);
        orderInvoiceLog.setCreator(creator);
        orderInvoiceLog.setModifier(creator);
        orderInvoiceLog.setOperatorName(createName);
        return orderInvoiceLogService.saveInvoice(orderInvoiceLog);
    }
    /**
     * 开票单获取
     */
    @RequestMapping(value = "invoice_get")
    @ResponseBody
    public Result invoiceGet(@RequestParam("id")Long orderId ,HttpServletRequest request) {
        long shopId = UserUtils.getShopIdForSession(request);
        OrderInvoiceLog orderInvoiceLog = orderInvoiceLogService.getInvoice(orderId,shopId);
        if(null == orderInvoiceLog){
            return Result.wrapErrorResult("","获取失败");
        }else{
            return Result.wrapSuccessfulResult(orderInvoiceLog);
        }
    }
}
