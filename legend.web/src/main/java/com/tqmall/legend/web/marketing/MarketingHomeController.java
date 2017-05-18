package com.tqmall.legend.web.marketing;

import com.google.common.collect.Sets;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.entity.privilege.FuncF;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * Created by wanghui on 15/12/2016.
 */
@RequestMapping("marketing")
@Controller
public class MarketingHomeController {

    @HttpRequestLog
    @RequestMapping
    public String index(HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (UserUtils.isYBD(request)) {
            if (userInfo.getUserIsAdmin() == 1) {
                return "redirect:/marketing/gather";
            }
            Set<String> permissionSet = getPermissionSet(request);
            if (permissionSet.contains("提醒中心")) {
                return "redirect:/marketing/gather/plan";
            }
            return "redirect:/marketing/gather/rule";
        } else {
            if (userInfo.getUserIsAdmin() == 1) {
                return "redirect:/marketing/ng/analysis";
            }
            Set<String> permissionSet = getPermissionSet(request);
            if (permissionSet.contains("客户分析")) {
                return "redirect:/marketing/ng/analysis";
            } else if (permissionSet.contains("提醒中心")) {
                return "redirect:/marketing/ng/maintain/center";
            } else if (permissionSet.contains("精准营销")) {
                return "redirect:/marketing/ng/center/accurate";
            } else if (permissionSet.contains("客户管理")) {
                return "redirect:/account";
            } else if (permissionSet.contains("短信充值")) {
                return "redirect:/marketing/ng/center/sms";
            } else if (permissionSet.contains("门店推广")) {
                return "redirect:/marketing/ng/analysis/promotion";
            } else {
                return "redirect:/shop/wechat";
            }
        }
    }

    private Set<String> getPermissionSet(HttpServletRequest request) {
        List<FuncF> funcFList = (List<FuncF>) request.getAttribute(Constants.SESSION_USER_ROLE_FUNC);
        Set<String> permissionSet = Sets.newHashSet();
        for (FuncF funcF : funcFList) {
            permissionSet.add(funcF.getName());
        }
        return permissionSet;
    }
}
