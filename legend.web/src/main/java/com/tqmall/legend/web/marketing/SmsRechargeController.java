package com.tqmall.legend.web.marketing;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.SnGenerator;
import com.tqmall.legend.biz.marketing.MarketingOrderService;
import com.tqmall.legend.biz.marketing.MarketingSmsRechargeTplService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.MarketingOrder;
import com.tqmall.legend.entity.marketing.MarketingSmsRechargeTpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by lixiao on 15/6/10.
 */
@Controller
@RequestMapping("shop/marketing/sms/recharge")
public class SmsRechargeController {

    Logger logger = LoggerFactory.getLogger(SmsRechargeController.class);
    @Autowired
    private MarketingSmsRechargeTplService marketingSmsRechargeTplService;
    @Autowired
    private MarketingOrderService marketingOrderService;


    /**
     * TODO 未放开在线充值功能
     * @param smsTplId
     * @param request
     * @return
     */
    @RequestMapping("create_marketing_order")
    @ResponseBody
    public Result createMarketingOrder(Long smsTplId, HttpServletRequest request){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        MarketingSmsRechargeTpl marketingSmsRechargeTpl = marketingSmsRechargeTplService.selectById(smsTplId);
        if(marketingSmsRechargeTpl==null){
            return Result.wrapErrorResult("","充值方式不正确");
        }
        BigDecimal rechargeMoney = marketingSmsRechargeTpl.getRechargeMoney();
        String orderSn = SnGenerator.generateWithMillisecond(SnGenerator.SMS);

        MarketingOrder marketingOrder = new MarketingOrder();
        marketingOrder.setShopId(userInfo.getShopId());
        marketingOrder.setCreator(userInfo.getUserId());
        marketingOrder.setModifier(userInfo.getUserId());
        marketingOrder.setOrderAmount(rechargeMoney);
        marketingOrder.setPayStatus(0);
        marketingOrder.setType(0);
        marketingOrder.setOrderSn(orderSn);
        int flag = marketingOrderService.create(marketingOrder);
        logger.info("生成营销短信订单"+orderSn);
        if(flag >0) {
            return Result.wrapSuccessfulResult(orderSn);
        }else {
            return Result.wrapErrorResult("","操作失败");
        }
    }



}
