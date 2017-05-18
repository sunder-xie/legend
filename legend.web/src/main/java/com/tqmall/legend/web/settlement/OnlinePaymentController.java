package com.tqmall.legend.web.settlement;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.settlement.OnlinePaymentService;
import com.tqmall.legend.biz.shop.ShopApplyRecordService;
import com.tqmall.legend.entity.shop.ShopApplyRecord;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/10/18.
 * 在线支付页面
 */
@Controller
@RequestMapping("shop/settlement/online")
@Slf4j
public class OnlinePaymentController extends BaseController{

    @Autowired
    private OnlinePaymentService onlinePaymentService;

    @Autowired
    private ShopApplyRecordService shopApplyRecordService;

    @RequestMapping("online-payment")
    public String index(Model model){
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTLEMENT.getModuleUrl());
        return "yqx/page/settlement/online/online-payment";
    }

    /**
     * 申请在线支付
     * @return
     */
    @RequestMapping(value = "/apply-pay", method = RequestMethod.POST)
    @ResponseBody
    public Result applyOnlinePayment() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        if (null == shopId || shopId < 1) {
            log.info("[支付申请] 门店信息错误, shopId:{}", shopId);
            return Result.wrapErrorResult("", "门店信息错误");
        }
        Result result = onlinePaymentService.applyOnlinePayment(shopId);
        return result;
    }

    /**
     * 获取门店支付申请记录
     * @return
     */
    @RequestMapping(value = "/apply-record", method = RequestMethod.GET)
    @ResponseBody
    public Result getApplyOnlinePay() {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        if (null == shopId || shopId < 1) {
            log.info("[支付申请记录] 门店信息错误, shopId:{}", shopId);
            return Result.wrapErrorResult("", "门店信息错误");
        }
        ShopApplyRecord shopApplyRecord = null;
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("shopId", shopId);
        List<ShopApplyRecord> shopApplyRecordList = shopApplyRecordService.select(searchParams);
        if (!CollectionUtils.isEmpty(shopApplyRecordList)) {
            shopApplyRecord = shopApplyRecordList.get(0);
        }
        return Result.wrapSuccessfulResult(shopApplyRecord);
    }

}
