package com.tqmall.legend.web.report;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.bi.entity.AttendanceStatis;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.statistics.StatisticsAttandDayService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.base.IdEntity;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.utils.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by cloudgu on 15/6/10.
 */
@Controller
@RequestMapping(value = "/shop/stats/staff/attendance")
public class AttendanceReportController extends BaseController {
    Logger logger = LoggerFactory.getLogger(AttendanceReportController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private StatisticsAttandDayService statisticsAttandDayService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping("")
    public String index(Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            logger.error("用户信息错误");
            return "yqx/page/report/statistics/staff/attendance";
        }
        long shopId = userInfo.getShopId();

        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            logger.error("店铺信息错误");
            return "yqx/page/report/statistics/staff/attendance";
        }
        model.addAttribute("shop", shop);
        model.addAttribute("moduleUrlTab", "attendance");
        return "yqx/page/report/statistics/staff/attendance";
    }

    @RequestMapping("getshopemployee")
    @ResponseBody
    public Object findShopEmployee(Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            logger.error("用户信息错误");
            return Result.wrapErrorResult("", "参数错误");
        }
        Long shopId = userInfo.getShopId();
        List<ShopManager> list = shopManagerService.selectByShopId(shopId);
        return Result.wrapSuccessfulResult(list);
    }

    @RequestMapping(value = "get_stats_amount/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object getStatsAmount(@PathVariable("type") String type,
                                 @PageableDefault(page = 1, value = 5, sort = "sign_date", direction = Sort.Direction.DESC) Pageable pageable,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            logger.error("用户信息错误");
            return Result.wrapErrorResult("", "参数错误");
        }
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        if (!searchParams.containsKey("startTime") || searchParams.get("startTime") == null) {
            logger.error("请输入查询开始时间");
            return Result.wrapErrorResult("", "请输入查询开始时间");
        }

        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            logger.error("店铺信息错误");
            return Result.wrapErrorResult("", "获取店铺信息错误");
        }
        if (null == shopId || Constants.SHOP_ID_FLAG > shopId) {
            logger.error("shopId错误" + "shopId:" + shopId);
            return Result.wrapErrorResult("", "参数错误");
        }
        Object statusnameObj = searchParams.get("statusname");
        if (statusnameObj != null) {
            String statusnameStr = statusnameObj.toString();
            if (Langs.isNotBlank(statusnameStr)) {
                Long userId = Long.parseLong(statusnameStr);
                searchParams.put("userId", userId);
            }
        }

        Date startTime = DateUtil.convertStringToDateYMD(searchParams.get("startTime") + "");
        Date endTime = null;
        if (null != searchParams.get("endTime")) {
            endTime = DateUtil.convertStringToDateYMD(searchParams.get("endTime") + "");
        } else {
            endTime = new Date();
        }
        searchParams.put("endTime", DateUtil.convertDateToYMD(startTime));
        searchParams.put("endTime", DateUtil.convertDateToYMD(endTime));

        searchParams.put("shopId", shopId);
        model.addAttribute("startTime", DateUtil.convertDate(startTime));
        model.addAttribute("endTime", DateUtil.convertDate(endTime));
        model.addAttribute("userName", userInfo.getName());
        model.addAttribute("shop", shop);


        if (type.equals("export_ng")) {
           long sTime=System.currentTimeMillis();
            searchParams.put("sorts", Lists.newArrayList("sign_date desc"));
            List<List> resultlist = getserchresult(searchParams);
            ModelAndView mav = new ModelAndView("statistics/excel_statistics_attend_info");
            mav.addObject("attendData", resultlist);
            mav.addObject("shop",shop);
            mav.addObject("startTime",searchParams.get("startTime") + "");
            mav.addObject("endTime",searchParams.get("endTime") + "");
            mav.addObject("operateTime", DateUtil.convertDate(new Date()));

            this.getExportView(response);
            mav.setViewName("report/excel_attendance_info");
            logger.info("报表导出[员工考勤统计],{}条记录,耗时{}毫秒", resultlist.size()*resultlist.get(0).size(), System.currentTimeMillis() - sTime);
            return mav;
        }
        Page<List> resultpage = getPage(pageable,searchParams);
        if (null==resultpage){
            List<List> emptylist  = new ArrayList<>();
            return Result.wrapSuccessfulResult(emptylist);
        }
        return Result.wrapSuccessfulResult(resultpage);
    }

    private <T extends IdEntity> Page<List> getPage(Pageable pageable,Map<String, Object> searchParams) {
        if(pageable.getSort() != null){
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            searchParams.put("sorts", sorts);
        }
        long totalSize = statisticsAttandDayService.getAttendInfoCount(searchParams);
        if(totalSize==0){
            return null;
        }
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());
        long sTime = System.currentTimeMillis();
        CommonPair<Date, Date> minMaxSignDate = statisticsAttandDayService.getMinMaxSignDate(searchParams);
        if (logger.isInfoEnabled()) {
            logger.info("查询考勤总数耗时:{}毫秒", System.currentTimeMillis() - sTime);
        }
        searchParams.put("startTime", minMaxSignDate.getDataF());
        searchParams.put("endTime", minMaxSignDate.getDataS());

        List<List> resultlist = getserchresult(searchParams);

        DefaultPage<List> page = new DefaultPage<List>(resultlist, pageRequest, totalSize);
        return page;
    }

    private List<List> getserchresult(Map<String, Object> searchParams){
        long sTime = System.currentTimeMillis();
        List<AttendanceStatis> chlidlist = statisticsAttandDayService.getAttandInfoListByDays(searchParams);
        if (logger.isInfoEnabled()) {
            logger.info("查询用户考勤信息耗时:{}毫秒", System.currentTimeMillis() - sTime);
        }
        sTime = System.currentTimeMillis();
        if (Langs.isEmpty(chlidlist)) {
            return Collections.emptyList();
        }

        List<String> daylist = Lists.newArrayList();
        Map<String, List<AttendanceStatis>> attendanceListMap = Maps.newHashMap();
        for (AttendanceStatis attendanceStatis : chlidlist) {
            String signDateStr = DateFormatUtils.toYMD(attendanceStatis.getSignDate());
            attendanceStatis.setSignDateStr(signDateStr);
            attendanceStatis.setSignInTimeStr(attendanceStatis.getSignInTimeStr());
            attendanceStatis.setSignOutTimeStr(attendanceStatis.getSignOutTimeStr());
            attendanceStatis.setSignstatus(attendanceStatis.getSignstatus());

            if (!daylist.contains(signDateStr)) {
                daylist.add(signDateStr);
            }

            List<AttendanceStatis> attendanceStatisList = attendanceListMap.get(signDateStr);
            if (Langs.isEmpty(attendanceStatisList)) {
                attendanceStatisList = Lists.newArrayList();
                attendanceListMap.put(signDateStr, attendanceStatisList);
            }
            attendanceStatisList.add(attendanceStatis);
        }

        List<List> resultlist = new ArrayList<>();
        for(String str : daylist){
            resultlist.add(attendanceListMap.get(str));
        }
        if (logger.isInfoEnabled()) {
            logger.info("组装用户考勤信息耗时:{}毫秒", System.currentTimeMillis() - sTime);
        }
        return resultlist;
    }


    private void getExportView(
                                       HttpServletResponse response) {
        response.setContentType("application/x-msdownload");
        String filename = "stats_attendance";
        try {
            filename = java.net.URLEncoder.encode("员工考勤统计表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");

    }

    /**
     * 打印员工考勤统计信息
     */
    @RequestMapping(value = "print_ng")
    public String printStatsAttend(HttpServletRequest request, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        if (!searchParams.containsKey("startTime") || searchParams.get("startTime") == null) {
            logger.error("请输入查询开始时间");
            return "common/error";
        }
        Object statusnameObj = searchParams.get("statusname");
        if (statusnameObj != null) {
            String statusnameStr = statusnameObj.toString();
            if (Langs.isNotBlank(statusnameStr)) {
                Long userId = Long.parseLong(statusnameStr);
                searchParams.put("userId", userId);
            }
        }

        Date startTime = DateUtil.convertStringToDateYMD(searchParams.get("startTime") + "");
        Date endTime = null;
        if (null != searchParams.get("endTime")) {
            endTime = DateUtil.convertStringToDateYMD(searchParams.get("endTime") + "");
        } else {
            endTime = new Date();
        }

        model.addAttribute("startTime", DateUtil.convertDate(startTime));
        model.addAttribute("endTime", DateUtil.convertDate(endTime));
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop", shop);

        if (searchParams.containsKey("startTime")) {
            searchParams.put("startTime", searchParams.get("startTime") + " 00:00:00");
        }
        if (searchParams.containsKey("endTime")) {
            searchParams.put("endTime", searchParams.get("endTime") + " 23:59:59");
        } else {
            searchParams.put("endTime", DateUtil.convertDateToYMD(endTime) + " 23:59:59");
        }
        searchParams.put("shopId", shopId);
        model.addAttribute("userName", userInfo.getName());
        model.addAttribute("currentDate", DateUtil.convertDateToYMD(new Date()));
        searchParams.put("sorts", Lists.newArrayList("sign_date desc"));
        model.addAttribute("AttendData",getserchresult(searchParams));
        return "statistics/print_statistics_attand_day";
    }
}
