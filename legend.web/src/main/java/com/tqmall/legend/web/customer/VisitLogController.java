package com.tqmall.legend.web.customer;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.VisitLogService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.VisitLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;

/**
 * 
 * @Author 王佳超<jiachao.wang@tqmall.com>
 * @Create 2015年7月8日下午7:40:03
 */
@Controller
@RequestMapping("shop/customer/visit")
public class VisitLogController extends BaseController {

    Logger logger = LoggerFactory.getLogger(VisitLogController.class);

    @Autowired
    private VisitLogService visitLogService;

    /**
     * 插入回访记录
     * 
     * @param visitLog
     * @param request
     * @return
     */
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public Object insert(@RequestBody VisitLog visitLog, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        // 设置基本值
        visitLog.setShopId(shopId);
        visitLog.setCreator(userId);
        visitLog.setModifier(userId);
        // 设置时间值
        String timeAppend = " 00:00:00";
        if (visitLog.getVisitType() == 2) {
            timeAppend = "-01" + timeAppend;
        }
        Date expiredTime = DateUtil.convertStringToDate(visitLog.getExpiredTimeFormat()
                + timeAppend);
        visitLog.setExpiredTime(expiredTime);
        Date visitTime = DateUtil.convertStringToDate(visitLog.getVisitTimeFormat());
        visitLog.setVisitTime(visitTime);
        if (!StringUtil.isNull(visitLog.getNextVisitTimeFormat())) {
            // 设置下次拜访时间
            Date nextVisitTime = DateUtil.convertStringToDate(visitLog.getNextVisitTimeFormat()
                    + " 00:00:00");
            visitLog.setNextVisitTime(nextVisitTime);
        }
        return visitLogService.insert(visitLog);
    }

    /**
     * 获取回访记录
     * 
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(
            @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DefaultPage<VisitLog> page = (DefaultPage<VisitLog>) visitLogService.getList(pageable,
                searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 不再提醒
     * 
     * @param visitLog
     * @param request
     * @return
     */
    @RequestMapping("never_remind")
    @ResponseBody
    public Object neverRemind(@RequestBody VisitLog visitLog, HttpServletRequest request) {
        return visitLogService.changeRemindTime(visitLog.getCustomerCarId(),
                visitLog.getVisitType(), null);
    }
}
