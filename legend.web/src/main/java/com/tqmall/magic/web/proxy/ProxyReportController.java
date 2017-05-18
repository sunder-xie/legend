package com.tqmall.magic.web.proxy;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import com.tqmall.magic.object.result.proxy.ProxyReportDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by dingbao on 16/10/13.
 */
@Controller
@RequestMapping("proxy/report")
@Slf4j
public class ProxyReportController extends BaseController {

    @Autowired
    private RpcProxyService rpcProxyService;

    @Autowired
    private ShopService shopService;


    /**
     * 跳转到报表列表页面
     * @return
     */
    @RequestMapping("toProxyReport")
    public String toProxyReport(){
        return "yqx/page/magic/report/proxyReport";
    }

    /**
     * 跳转到报表详情页面
     * @return
     */
    @RequestMapping("toProxyReporyDtl")
    public String toProxyReporyDtl(@RequestParam(value = "startTime",required = false)String startTime,@RequestParam(value = "endTime",required = false)String endTime,Long shopId,Model model){
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shopId",shopId);
        if (shop != null){
            model.addAttribute("shopName",shop.getName());
        }
        if (StringUtil.isNotStringEmpty(startTime)){
            model.addAttribute("startTime",startTime);
        }
        if (StringUtil.isNotStringEmpty(endTime)){
            model.addAttribute("endTime",endTime);
        }
        return "yqx/page/magic/report/proxyReportDtl";

    }


    /**
     * 委托单报表统计
     * @param proxyParam
     * @return
     */
    @RequestMapping(value = "proxyReport",method = RequestMethod.POST)
    @ResponseBody
    public Result<ProxyReportDTO> proxyReport(@RequestBody ProxyParam proxyParam){
        Long shopId = UserUtils.getShopIdForSession(request);
        proxyParam.setProxyShopId(shopId);
        log.info("【查询委托单统计报表】Begin proxyReport。proxyShopId={}",proxyParam.getProxyShopId());
        if (proxyParam.getProxyStartTime().equals("")){
            proxyParam.setProxyStartTime(null);
        }
        if (proxyParam.getProxyEndTime().equals("")){
            proxyParam.setProxyEndTime(null);
        }
        Result<ProxyReportDTO> proxyReportDTOResult = null;
        try {
            proxyReportDTOResult = rpcProxyService.proxyReport(proxyParam);
        } catch (Exception e) {
            log.error("调用远程magic异常",e);
            return Result.wrapErrorResult("","调用远程系统异常");
        }
        log.info("【查询委托单统计报表】End proxyReport.isSuccess={}",proxyReportDTOResult.isSuccess());
        return proxyReportDTOResult;
    }

    /**
     * 委托单报表统计明细
     * @param proxyParam
     * @return
     */
    @RequestMapping(value = "proxyReportDtl",method = RequestMethod.POST)
    @ResponseBody
    public Result<ProxyReportDTO> proxyReporyDtl(@RequestBody ProxyParam proxyParam){
        log.info("【查询委托单统计报表明细】Begin proxyReporyDtl。shopId={}",proxyParam.getShopId());
        if (proxyParam.getProxyStartTime().equals("")){
            proxyParam.setProxyStartTime(null);
        }
        if (proxyParam.getProxyEndTime().equals("")){
            proxyParam.setProxyEndTime(null);
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        proxyParam.setProxyShopId(shopId);
        Result<ProxyReportDTO> proxyReportDTOResult = null;
        try {
            proxyReportDTOResult = rpcProxyService.proxyReporyDtl(proxyParam);
        } catch (Exception e) {
            log.error("调用远程magic异常",e);
            return Result.wrapErrorResult("","调用远程系统异常");
        }
        log.info("【查询委托单统计报表】End proxyReporyDtl.isSuccess={}",proxyReportDTOResult.isSuccess());
        return proxyReportDTOResult;
    }


    /**
     * 统计委托单导出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("exportProxyReport")
    @ResponseBody
    public Object exportProxyReport(String startTime,String endTime,Integer partnerStatus, HttpServletRequest request, HttpServletResponse response) {
        log.info("【统计委托单导出】Begin exportProxyReport.startTime={},endTime={},partnerStatus={}",startTime,endTime,partnerStatus);
        Long shopId = UserUtils.getShopIdForSession(request);
        ProxyParam proxyParam = new ProxyParam();
        proxyParam.setProxyShopId(shopId);
        proxyParam.setPartnerStatus(partnerStatus);
        if (StringUtil.isNotStringEmpty(startTime)){
            proxyParam.setProxyStartTime(startTime);
        }

        if (StringUtil.isNotStringEmpty(endTime)){
            proxyParam.setProxyEndTime(endTime);
        }

        Result<ProxyReportDTO> proxyReportDTOResult = null;
        try {
            proxyReportDTOResult = rpcProxyService.proxyReport(proxyParam);
        } catch (Exception e) {
            log.error("调用远程magic异常",e);
            return Result.wrapErrorResult("","调用远程系统异常");
        }
        ModelAndView view = new ModelAndView("yqx/page/magic/report/proxyReportExport");
        if (partnerStatus.equals(-1)){
            view.addObject("partnerStatusStr","");
        }
        if (partnerStatus.equals(1)){
            view.addObject("partnerStatusStr","是");
        }
        if (partnerStatus.equals(2)){
            view.addObject("partnerStatusStr","否");
        }
        view.addObject("proxyReportDTO", proxyReportDTOResult.getData());
        view.addObject("startTime",startTime);
        view.addObject("endtime",endTime);

        // 设置响应头
        response.setContentType("application/x-msdownload");
        String filename = "proxyOrder";
        try {
            filename = URLEncoder.encode("委托数据统计", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("施工单导出URLEncoder转义不正确");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        return view;
    }


    @RequestMapping("exportProxyReportDtl")
    @ResponseBody
    public Object exportProxyReportDtl(String startTime,String endTime,Long shopId,HttpServletRequest request, HttpServletResponse response) {

        Long proxyShopId = UserUtils.getShopIdForSession(request);
        ProxyParam proxyParam = new ProxyParam();
        proxyParam.setShopId(shopId);
        proxyParam.setProxyShopId(proxyShopId);
        if (StringUtil.isNotStringEmpty(startTime)){
            proxyParam.setProxyStartTime(startTime);
        }

        if (StringUtil.isNotStringEmpty(endTime)){
            proxyParam.setProxyEndTime(endTime);
        }

        Result<ProxyReportDTO> proxyReportDTOResult = null;
        try {
            proxyReportDTOResult = rpcProxyService.proxyReporyDtl(proxyParam);
        } catch (Exception e) {
            log.error("调用远程magic异常",e);
            return Result.wrapErrorResult("","调用远程系统异常");
        }
        ModelAndView view = new ModelAndView("yqx/page/magic/report/proxyReportExportDtl");
        view.addObject("proxyReportDTO", proxyReportDTOResult.getData());
        Shop shop = shopService.selectById(shopId);
        if (shop != null){
            view.addObject("shopName", shop.getName());
        }
        view.addObject("startTime",startTime);
        view.addObject("endtime",endTime);
        // 设置响应头
        response.setContentType("application/x-msdownload");
        String filename = "proxyOrderDtl";
        try {
            filename = URLEncoder.encode("委托数据明细", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("施工单导出URLEncoder转义不正确");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        return view;
    }

}
