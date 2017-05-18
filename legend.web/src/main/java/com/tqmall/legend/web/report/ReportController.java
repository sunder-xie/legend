package com.tqmall.legend.web.report;

import com.google.common.collect.Sets;
import com.tqmall.common.Constants;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.entity.privilege.FuncF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * Created by xin on 16/8/25.
 */
@Controller
@RequestMapping("shop/report")
@Slf4j
public class ReportController {

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    /**
     * 报表首页
     *
     * @return
     */
    @HttpRequestLog
    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        if (UserUtils.getUserInfo(request).getUserIsAdmin() == 1) {
            return "redirect:/shop/report/business/daily";
        }

        List<FuncF> funcFList = (List<FuncF>) request.getAttribute(Constants.SESSION_USER_ROLE_FUNC);
        Set<String> permissionSet = Sets.newHashSet();
        for (FuncF funcF : funcFList) {
            permissionSet.add(funcF.getName());
        }

        if (permissionSet.contains("营业日报")) {
            return "redirect:/shop/report/business/daily";
        } else if (permissionSet.contains("营业月报")) {
            return "redirect:/shop/report/business/month";
        } else if (permissionSet.contains("绩效管理")) {
            if (UserUtils.isYBD(request) && UserUtils.getUserInfo(request).getUserIsAdmin() != 1) {
                return "redirect:/shop/report/gather/staff/perf";
            }
            return "redirect:/shop/report/staff/perf";
        } else if (permissionSet.contains("经营分析报告")) {
            return "redirect:/shop/report/business/month/summary";
        } else if (permissionSet.contains("工单结算收款表")) {
            return "redirect:/shop/stats/order_payment";
        } else if (permissionSet.contains("工单流水表")) {
            return "redirect:/shop/stats/order_info_detail";
        } else if (permissionSet.contains("经营毛利明细")) {
            return "redirect:/shop/report/gross-profits";
        } else if (permissionSet.contains("出入库明细表")) {
            return "redirect:/shop/stats/warehouse-info/in";
        } else if (permissionSet.contains("员工考情统计")) {
            return "redirect:/shop/stats/staff/attendance";
        } else if (permissionSet.contains("卡券充值记录表")) {
            return "redirect:/shop/stats/card/coupon-recharge";
        }
        return "yqx/page/report/statistics/home";
    }
}

