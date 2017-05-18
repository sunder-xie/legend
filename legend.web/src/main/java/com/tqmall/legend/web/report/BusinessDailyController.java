package com.tqmall.legend.web.report;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.result.*;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.report.BusinessDailyFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 门店营业日报
 * <p/>
 * Created by xin on 16/8/22.
 */
@Controller
@RequestMapping("shop/report/business/daily")
@Slf4j
public class BusinessDailyController {

    @Autowired
    private BusinessDailyFacade businessDailyFacade;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    /**
     * 门店营业日报页面
     *
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("moduleUrlTab", "report_business_daily");
        return "yqx/page/report/statistics/business/daily";
    }

    /**
     * 查询门店营收信息
     *
     * @param request
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "revenue", method = RequestMethod.GET)
    @ResponseBody
    public Result<RevenueDTO> getRevenueInfo(@RequestParam("dateStr") String dateStr, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Result checkResult = checkParam(dateStr);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        RevenueDTO revenueInfo = null;
        try {
            revenueInfo = businessDailyFacade.getRevenueInfo(shopId, dateStr);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        if (revenueInfo == null) {
            revenueInfo = new RevenueDTO();
        }
        return Result.wrapSuccessfulResult(revenueInfo);
    }

    /**
     * 查询门店采销信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "purchase-sale", method = RequestMethod.GET)
    @ResponseBody
    public Result<PurchaseSaleDTO> getPurchaseSaleInfo(@RequestParam("dateStr") String dateStr, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Result checkResult = checkParam(dateStr);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        PurchaseSaleDTO purchaseSaleInfo = null;
        try {
            purchaseSaleInfo = businessDailyFacade.getPurchaseSaleInfo(shopId, dateStr);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        if (purchaseSaleInfo == null) {
            purchaseSaleInfo = new PurchaseSaleDTO();
        }
        return Result.wrapSuccessfulResult(purchaseSaleInfo);
    }

    /**
     * 查询门店卡券充值信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "account-trade", method = RequestMethod.GET)
    @ResponseBody
    public Result<AccountCardCouponDTO> getAccountCardCouponInfo(@RequestParam("dateStr") String dateStr, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Result checkResult = checkParam(dateStr);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        AccountCardCouponDTO accountCardCouponInfo = null;
        try {
            accountCardCouponInfo = businessDailyFacade.getAccountCardCouponInfo(shopId, dateStr);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        if (accountCardCouponInfo == null) {
            accountCardCouponInfo = new AccountCardCouponDTO();
        }
        return Result.wrapSuccessfulResult(accountCardCouponInfo);
    }

    /**
     * 查询门店访客信息
     *
     * @param dateStr
     * @param request
     * @return
     */
    @RequestMapping(value = "customer-statistics", method = RequestMethod.GET)
    @ResponseBody
    public Result<CustomerStatisticsDTO> getCustomerStatistics(@RequestParam("dateStr") String dateStr, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Result checkResult = checkParam(dateStr);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        CustomerStatisticsDTO customerStatistics = null;
        try {
            customerStatistics = businessDailyFacade.getCustomerStatistics(shopId, dateStr);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        if (customerStatistics == null) {
            customerStatistics = new CustomerStatisticsDTO();
        }
        return Result.wrapSuccessfulResult(customerStatistics);
    }

    /**
     * 查询门店业务收款
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "business-statistics", method = RequestMethod.GET)
    @ResponseBody
    public Result<BusinessStatisticsDTO> getBusinessStatistics(@RequestParam("dateStr") String dateStr, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Result checkResult = checkParam(dateStr);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        BusinessStatisticsDTO businessStatistics = null;
        try {
            businessStatistics = businessDailyFacade.getBusinessStatistics(shopId, dateStr);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        if (businessStatistics == null) {
            businessStatistics = new BusinessStatisticsDTO();
        }
        return Result.wrapSuccessfulResult(businessStatistics);
    }

    /**
     * 查询门店工单
     *
     * @param request
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "order-list", method = RequestMethod.GET)
    @ResponseBody
    public Result<DefaultPage<OrderInfoDTO>> getOrderInfoList(@RequestParam("dateStr") String dateStr, @PageableDefault(page = 1, value = 10) Pageable pageable, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Result checkResult = checkParam(dateStr);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        DefaultPage<OrderInfoDTO> page = businessDailyFacade.getOrderInfoList(shopId, dateStr, pageable);
        page.setPageUri(request.getRequestURI());
        return Result.wrapSuccessfulResult(page);
    }


    /**
     * 校验日期格式
     *
     * @param dateStr
     * @return
     */
    private Result checkParam(String dateStr) {
        if (StringUtil.isStringEmpty(dateStr)) {
            return Result.wrapErrorResult("", "date不能为空");
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(dateStr);
            if (date != null && date.after(new Date())) {
                return Result.wrapErrorResult("", "dateStr日期超出范围");
            }
        } catch (ParseException e) {
            return Result.wrapErrorResult("", "dateStr格式不正确");
        }
        return Result.wrapSuccessfulResult(true);
    }
}
