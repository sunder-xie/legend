package com.tqmall.legend.web.account;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.account.ComboInfoService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.account.CouponSuiteService;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券\会员卡设置页面
 * Created by wanghui on 6/2/16.
 */
@Slf4j
@RequestMapping("account/setting")
@Controller
public class AccountSettingController extends BaseController {
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private CouponSuiteService couponSuiteService;
    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;

    @RequestMapping
    public String index(Model model) {
        Map<String, Object> param = new HashMap<>();
        Long shopId = UserUtils.getShopIdForSession(request);
        param.put("shopId", shopId);
        model.addAttribute("moduleUrl", "customer");
        model.addAttribute("subModule", "account-setting");
        model.addAttribute("couponInfoCount", couponInfoService.selectCount(param));
        model.addAttribute("couponSuiteCount", couponSuiteService.selectCount(param));
        model.addAttribute("comboInfoCount", comboInfoService.selectCount(shopId));
        model.addAttribute("cardInfoCount", memberCardInfoService.selectCount(shopId));

        return "yqx/page/account/setting";
    }
}
