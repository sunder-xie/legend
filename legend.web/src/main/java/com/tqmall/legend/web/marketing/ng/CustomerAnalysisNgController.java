package com.tqmall.legend.web.marketing.ng;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcCustomerAnalysisService;
import com.tqmall.cube.shop.result.*;
import com.tqmall.legend.annotation.Condition;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.annotation.Param;
import com.tqmall.legend.biz.marketing.ng.CustomerLevelAnalysisService;
import com.tqmall.legend.biz.marketing.ng.CustomerLostAnalysisService;
import com.tqmall.legend.biz.marketing.ng.CustomerTypeAnalysisService;
import com.tqmall.legend.biz.marketing.ng.adaptor.CustomerAnalysisConverter;
import com.tqmall.legend.biz.marketing.ng.bo.CustomerAnalysisBO;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 客户分析控制器
 * Created by wanghui on 2/25/16.
 */
@Slf4j
@Controller
@RequestMapping("/marketing/ng/analysis")
public class CustomerAnalysisNgController extends BaseController{

    /**
     * 客户流失分析服务类
     */
    @Autowired
    private CustomerLostAnalysisService lostAnalysisService;
    /**
     * 客户登记分析服务类
     */
    @Autowired
    private CustomerLevelAnalysisService levelAnalysisService;
    /**
     * 客户类型分析服务类
     */
    @Autowired
    private CustomerTypeAnalysisService typeAnalysisService;

    @Autowired
    private RpcCustomerAnalysisService rpcCustomerAnalysisService;
    @Autowired
    private ShopService shopService;

    private void _init(Model model) {
        model.addAttribute("moduleUrl","marketing");
    }


    private BigDecimal getLostAmountOneYear(Integer highCustomerCount, Integer middleCustomerCount, Integer lowCustomerCount) {
        /**
         * 高质量客户价值
         */
        BigDecimal CUSTOMER_HIGHT_VALUE = BigDecimal.valueOf(2500);
        /**
         * 中等质量客户价值
         */
        BigDecimal CUSTOMER_MIDDLE_VALUE = BigDecimal.valueOf(1500);
        /**
         * 低等质量客户价值
         */
        BigDecimal CUSTOMER_LOW_VALUE = BigDecimal.valueOf(500);


        BigDecimal highCustomerCountDec = BigDecimal.valueOf(highCustomerCount);
        BigDecimal middleCustomerCountDec = BigDecimal.valueOf(middleCustomerCount);
        BigDecimal lowCustomerCountDec = BigDecimal.valueOf(lowCustomerCount);
        return CUSTOMER_HIGHT_VALUE.multiply(highCustomerCountDec)
                .add( CUSTOMER_MIDDLE_VALUE.multiply(middleCustomerCountDec))
                .add(CUSTOMER_LOW_VALUE.multiply(lowCustomerCountDec));
    }

    private StringBuilder getLevelString(CustomerAnalysisSummaryDTO analysisDTO) {BigDecimal highCustomerAmount = analysisDTO.getHighTotalAmount();
        BigDecimal middleCustomerAmount = analysisDTO.getMiddleTotalAmount();
        BigDecimal lowCustomerAmount = analysisDTO.getLowTotalAmount();
        Integer highCustomerCount = analysisDTO.getHighLevelCustomerCount();
        Integer middleCustomerCount = analysisDTO.getMiddleLevelCustomerCount();
        Integer lowCustomerCount = analysisDTO.getLowLevelCustomerCount();
        StringBuilder sb = new StringBuilder();
        if(highCustomerAmount != null && middleCustomerAmount != null && lowCustomerAmount != null) {
            /**
             * 组装级别分析语句
             */
            sb.append("亲，您的");
            String minStr = "";
            Integer minCount;
            minCount = highCustomerCount > middleCustomerCount ?middleCustomerCount:highCustomerCount;
            minCount = minCount > lowCustomerCount?lowCustomerCount:minCount;

            if(minCount == highCustomerCount) {
                minStr = "高端车比例过低";
            }
            if(minCount == middleCustomerCount) {
                if(!minStr.equals("")) {
                    minStr += "，";
                }
                minStr += "中端车比例过低";
            }
            if(minCount == lowCustomerCount) {
                if(!minStr.equals("")) {
                    minStr += "，";
                }
                minStr += "低端车比例过低";
            }
            sb.append(minStr).append("；");

            BigDecimal minAmount;
            minAmount = highCustomerAmount.compareTo(middleCustomerAmount) < 0? highCustomerAmount:middleCustomerAmount;
            minAmount = minAmount.compareTo(lowCustomerAmount) < 0 ?minAmount:lowCustomerAmount;

            minStr = "";

            if(highCustomerAmount.equals(minAmount)) {
                minStr = "高端车产值有待提高";
            }
            if(middleCustomerAmount.equals(minAmount)) {
                if(!minStr.equals("")) {
                    minStr += "，";
                }
                minStr += "中端车产值有待提高";
            }
            if(lowCustomerAmount.equals(minAmount)) {
                if(!minStr.equals("")) {
                    minStr += "，";
                }
                minStr += "低端车产值有待提高";
            }
            sb.append(minStr).append("。");
        }
        return sb;
    }

    /**
     * 客户分析首页
     */
    @HttpRequestLog
    @RequestMapping
    public String index(Model model, HttpServletRequest request) {
        this._init(model);
        Long shopId = UserUtils.getShopIdForSession(request);

        model.addAttribute("subModule", "analysis");
        com.tqmall.core.common.entity.Result<CustomerAnalysisSummaryDTO> dubboResult = rpcCustomerAnalysisService.getSummaryAnalysis(shopId);
        if (dubboResult.isSuccess()) {
            CustomerAnalysisSummaryDTO analysisDTO =  dubboResult.getData();
            //----------获取客户流失分析数据---------------
//            _initCustomerLostInfo(model, analysisDTO);
            //客户总数
            model.addAttribute("totalCustomerCount", analysisDTO.getTotalCustomerCount());
            //已流失客户数
            model.addAttribute("lostCustomerCount", analysisDTO.getLostCustomerCount());
            //损失
            Integer highFrequencyCustomerLostCount = analysisDTO.getHighFrequencyCustomerLostCount();
            Integer middleFrequencyCustomerLostCount = analysisDTO.getMiddleFrequencyCustomerLostCount();
            Integer lowFrequencyCustomerLostCount = analysisDTO.getLowFrequencyCustomerLostCount();
            BigDecimal lostAmountOneYear = getLostAmountOneYear(highFrequencyCustomerLostCount, middleFrequencyCustomerLostCount, lowFrequencyCustomerLostCount);
            model.addAttribute("lostAmountOneYear", lostAmountOneYear);
            //--------获取客户类型分析统计数据--------
//            _initCustomerTypeInfo(model, analysisDTO);
            model.addAttribute("oldCustomerTypeCount", analysisDTO.getOldCustomerCount());
            model.addAttribute("newCustomerTypeCount", analysisDTO.getNewCustomerCount());
            model.addAttribute("sleepCustomerTypeCount", analysisDTO.getLazyCustomerCount());
            model.addAttribute("lostCustomerTypeCount", analysisDTO.getLostCustomerCount());
            model.addAttribute("activeCustomerTypeCount", analysisDTO.getActiveCustomerCount());

            //--------获取客户级别分析统计数据-----
//            _initCustomerLevelInfo(model, analysisDTO);
            model.addAttribute("highCustomerTypeConsumeAmount", analysisDTO.getHighTotalAmount());
            model.addAttribute("middleCustomerTypeConsumeAmount", analysisDTO.getMiddleTotalAmount());
            model.addAttribute("lowCustomerTypeConsumeAmount", analysisDTO.getLowTotalAmount());
            model.addAttribute("highCustomerTypeCount", analysisDTO.getHighLevelCustomerCount());
            model.addAttribute("middleCustomerTypeCount", analysisDTO.getMiddleLevelCustomerCount());
            model.addAttribute("lowCustomerTypeCount", analysisDTO.getLowLevelCustomerCount());
            model.addAttribute("analysisStr", getLevelString(analysisDTO).toString());

            //--------获取客户流失分析统计数据--------
            model.addAttribute("highFrequencyCustomerLostCount", highFrequencyCustomerLostCount);
            model.addAttribute("middleFrequencyCustomerLostCount", middleFrequencyCustomerLostCount);
            model.addAttribute("lowFrequencyCustomerLostCount", lowFrequencyCustomerLostCount);
            model.addAttribute("totalCustomerLostCount", highFrequencyCustomerLostCount + middleFrequencyCustomerLostCount + lowFrequencyCustomerLostCount);
            model.addAttribute("lostAnalysisStr", getLostAnalysisStr(analysisDTO));
        }
        return "marketing/ng/analysis/index";
    }

    private String getLostAnalysisStr(CustomerAnalysisSummaryDTO analysisDTO) {
        StringBuilder sb = new StringBuilder("亲，您已流失客户");
        Integer highFrequencyCustomerLostCount = analysisDTO.getHighFrequencyCustomerLostCount();
        Integer middleFrequencyCustomerLostCount = analysisDTO.getMiddleFrequencyCustomerLostCount();
        Integer lowFrequencyCustomerLostCount = analysisDTO.getLowFrequencyCustomerLostCount();
        sb.append(highFrequencyCustomerLostCount + middleFrequencyCustomerLostCount + lowFrequencyCustomerLostCount).append("位，其中");
        List<Integer> customerLostCountList = Arrays.asList(highFrequencyCustomerLostCount, middleFrequencyCustomerLostCount, lowFrequencyCustomerLostCount);
        Integer maxLostCount = Collections.max(customerLostCountList);
        String maxCustomer = "高";
        if (maxLostCount.equals(highFrequencyCustomerLostCount)) {
            maxCustomer = "高";
        } else if (maxLostCount.equals(middleFrequencyCustomerLostCount)) {
            maxCustomer = "中";
        } else if (maxLostCount.equals(lowFrequencyCustomerLostCount)) {
            maxCustomer = "低";
        }
        sb.append(maxCustomer).append("质量流失客户比例较大！");
        return sb.toString();
    }

    /**
     * 客户流失分析
     * @return
     */
    @RequestMapping("lost")
    public String customerLost(Model model,@RequestParam(value = "tag", required = false)String tag) {
        Long shopId = UserUtils.getShopIdForSession(request);
        this._init(model);
        model.addAttribute("subModule", "analysis");

        //初始化流失客户总信息
        com.tqmall.core.common.entity.Result<CustomerAnalysisLostDTO> dubboResult = rpcCustomerAnalysisService.getLostAnalysis(shopId);
        if (dubboResult.isSuccess()) {
            CustomerAnalysisLostDTO analysisDTO =  dubboResult.getData();
            //客户总数
            model.addAttribute("totalCustomerCount", analysisDTO.getTotalCustomerCount());
            //已流失客户数
            model.addAttribute("lostCustomerCount", analysisDTO.getLostCustomerCount());
            //损失
            BigDecimal lostAmountOneYear = getLostAmountOneYear(
                    analysisDTO.getHighFrequencyCustomerLostCount(),
                    analysisDTO.getMiddleFrequencyCustomerLostCount(),
                    analysisDTO.getLowFrequencyCustomerLostCount());
            model.addAttribute("lostAmountOneYear", lostAmountOneYear);

            model.addAttribute("tag", tag);
            //高质量用户信息
            model.addAttribute("hightCustomerCount", analysisDTO.getHighFrequencyCustomerLostCount());
            model.addAttribute("hightCustomerValue", CustomerLostAnalysisService.CUSTOMER_HIGHT_VALUE);
            //中等质量用户信息
            model.addAttribute("middleCustomerCount", analysisDTO.getMiddleFrequencyCustomerLostCount());
            model.addAttribute("middleCustomerValue", CustomerLostAnalysisService.CUSTOMER_MIDDLE_VALUE);
            //低等用户质量
            model.addAttribute("lowCustomerCount", analysisDTO.getLowFrequencyCustomerLostCount());
            model.addAttribute("lowCustomerValue", CustomerLostAnalysisService.CUSTOMER_LOW_VALUE);
        }

        return "marketing/ng/analysis/lost";
    }

    /**
     * 高质量流失客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")})
    @RequestMapping("lost/high")
    @ResponseBody
    public Object lostHigh(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        return Result.wrapSuccessfulResult(lostAnalysisService.getLostHighCustomer(shopId,pageable));
    }
    /**
     * 中质量流失客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")})
    @RequestMapping("lost/middle")
    @ResponseBody
    public Object lostMiddle(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        return Result.wrapSuccessfulResult(lostAnalysisService.getLostMiddleCustomer(shopId, pageable));
    }
    /**
     * 低质量流失客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")})
    @RequestMapping("lost/low")
    @ResponseBody
    public Object lostLow(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        return Result.wrapSuccessfulResult(lostAnalysisService.getLostLowCustomer(shopId, pageable));
    }

    /**
     * 客户流失分析导出
     * @param type 1:高, 2:中, 3:低
     * @param request
     * @param response
     */
    @RequestMapping(value = "lost/export", method = RequestMethod.GET)
    public void exporLostCustomertExcel(@RequestParam(value = "type", defaultValue = "1") Integer type,
                                        @PageableDefault(page = 1, value = 500, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable,
                                        HttpServletRequest request, HttpServletResponse response) {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        String fileName = "";
        if (type == 1) {
            fileName = "高等质量流失客户";
        } else if (type == 2) {
            fileName = "中等质量流失客户";
        } else if (type == 3) {
            fileName = "低等质量流失客户";
        } else {
            return;
        }

        long startTime = System.currentTimeMillis();
        ExcelExportor exportor = null;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName + "-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——" + fileName;
            exportor.writeTitle(null, headline, CustomerAnalysisBO.class);

            while (true) {
                Page<CustomerInfo> page = null;
                if (type == 1) {
                    page = lostAnalysisService.getLostHighCustomer(shopId, pageable);
                } else if (type == 2) {
                    page = lostAnalysisService.getLostMiddleCustomer(shopId, pageable);
                } else if (type == 3) {
                    page = lostAnalysisService.getLostLowCustomer(shopId, pageable);
                }
                if (page == null) {
                    break;
                }
                List<CustomerAnalysisBO> customerAnalysisBOList = CustomerAnalysisConverter.convertList(page.getContent());
                if (CollectionUtils.isEmpty(customerAnalysisBOList)) {
                    break;
                }
                exportor.write(customerAnalysisBOList);
                totalSize += customerAnalysisBOList.size();
                if (totalSize >= page.getTotalElements()) {
                    break;
                }
                pageable = pageable.next();
            }
        } catch (Exception e) {
            log.error("客户流失分析导出异常，门店id：{}", shopId, e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog(fileName, userInfo, totalSize, endTime - startTime));
    }

    /**
     * 客户类型分析
     * @return
     */
    @RequestMapping("type")
    public String customerType(Model model,@RequestParam(value = "tag", required = false)String tag) {
        Long shopId = UserUtils.getShopIdForSession(request);

        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        Date sTime = null;
        if(params.get("sTime") != null){
            String sTimeStr = params.get("sTime").toString();
            sTime = DateUtil.convertStringToDateYMD(sTimeStr);
            model.addAttribute("search_sTime", params.get("sTime"));
        }
        Date eTime = null;
        if(params.get("eTime") != null){
            String eTimeStr = params.get("eTime").toString();
            eTime = DateUtil.convertStringToDateYMD(eTimeStr);
            eTime = DateUtil.addDate(eTime,1);
            model.addAttribute("search_eTime", params.get("eTime"));
        }
        this._init(model);
        model.addAttribute("subModule", "analysis");

        com.tqmall.core.common.entity.Result<CustomerAnalysisNewOldDTO> dubboResult = rpcCustomerAnalysisService.getNewOldAnalysis(shopId,sTime,eTime);
        if (dubboResult.isSuccess()) {
            CustomerAnalysisNewOldDTO analysisDTO =  dubboResult.getData();
            model.addAttribute("oldCustomerTypeCount", analysisDTO.getOldCustomerCount());
            model.addAttribute("newCustomerTypeCount", analysisDTO.getNewCustomerCount());
            model.addAttribute("oldCustomerTypeConsumeAmount", analysisDTO.getOldTotalAmount());
            model.addAttribute("newCustomerTypeConsumeAmount", analysisDTO.getNewTotalAmount());
        }
        model.addAttribute("tag",tag);
        //老用户消费金额

        return "marketing/ng/analysis/type";
    }

    /**
     * 客户类型分析
     * @return
     */
    @RequestMapping("type/activity")
    public String activityType(Model model, @RequestParam(value = "tag", required = false)String tag) {
        Long shopId = UserUtils.getShopIdForSession(request);
        this._init(model);
        model.addAttribute("subModule", "analysis");

        com.tqmall.core.common.entity.Result<CustomerAnalysisActivityDTO> dubboResult = rpcCustomerAnalysisService.getActivityAnalysis(shopId);
        if (dubboResult.isSuccess()) {
            CustomerAnalysisActivityDTO analysisDTO =  dubboResult.getData();
            model.addAttribute("sleepCustomerTypeCount", analysisDTO.getLazyCustomerCount());
            model.addAttribute("lostCustomerTypeCount", analysisDTO.getLostCustomerCount());
            model.addAttribute("activeCustomerTypeCount", analysisDTO.getActiveCustomerCount());
            //活跃用户消费金额
            model.addAttribute("activeCustomerTypeConsumeAmount", analysisDTO.getActiveTotalAmount());
            //休眠用户消费金额
            model.addAttribute("sleepCustomerTypeConsumeAmount", analysisDTO.getLazyTotalAmount());
            //流失用户消费金额
            model.addAttribute("lostCustomerTypeConsumeAmount", analysisDTO.getLostTotalAmount());
        }
        model.addAttribute("tag",tag);

        return "marketing/ng/analysis/activity";
    }

    /**
     * 新客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size"),@Param(name = "search_sTime", aliasName = "start_time"), @Param(name = "search_eTime", aliasName = "end_time")})
    @RequestMapping("type/new")
    @ResponseBody
    public Object typeNew(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        return Result.wrapSuccessfulResult(typeAnalysisService.getNewCustomer(params, pageable));
    }

    /**
     * 老客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size"),@Param(name = "search_sTime", aliasName = "start_time"), @Param(name = "search_eTime", aliasName = "end_time")})
    @RequestMapping("type/old")
    @ResponseBody
    public Object typeOld(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        return Result.wrapSuccessfulResult(typeAnalysisService.getOldCustomer(params, pageable));
    }

    /**
     * 新老客户分析导出
     * @param type 1:老, 2:新
     * @param request
     * @param response
     */
    @RequestMapping(value = "type/export", method = RequestMethod.GET)
    public void exportTypeCustomerExcel(@RequestParam(value = "type", defaultValue = "1") Integer type,
                                        @PageableDefault(page = 1, value = 500, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);

        String fileName = "";
        if (type == 1) {
            fileName = "老客户";
        } else if (type == 2) {
            fileName = "新客户";
        } else {
            return;
        }

        long startTime = System.currentTimeMillis();
        ExcelExportor exportor = null;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName + "-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——" + fileName;
            exportor.writeTitle(null, headline, CustomerAnalysisBO.class);

            while (true) {
                Page<CustomerInfo> page = null;
                if (type == 1) {
                    page = typeAnalysisService.getOldCustomer(params, pageable);
                } else if (type == 2) {
                    page = typeAnalysisService.getNewCustomer(params, pageable);
                }
                if (page == null) {
                    break;
                }
                List<CustomerAnalysisBO> customerAnalysisBOList = CustomerAnalysisConverter.convertList(page.getContent());
                if (CollectionUtils.isEmpty(customerAnalysisBOList)) {
                    break;
                }
                exportor.write(customerAnalysisBOList);
                totalSize += customerAnalysisBOList.size();
                if (totalSize >= page.getTotalElements()) {
                    break;
                }
                pageable = pageable.next();
            }
        } catch (Exception e) {
            log.error("新老客户分析导出异常，门店id：{}", shopId, e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog(fileName, userInfo, totalSize, endTime - startTime));
    }


    /**
     * 活跃客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")})
    @RequestMapping("type/activity/active")
    @ResponseBody
    public Object typeActive(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        return Result.wrapSuccessfulResult(typeAnalysisService.getActiveCustomer(shopId, pageable));
    }

    /**
     * 休眠客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")})
    @RequestMapping("type/activity/sleep")
    @ResponseBody
    public Object typeSleep(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        return Result.wrapSuccessfulResult(typeAnalysisService.getSleepCustomer(shopId, pageable));
    }

    /**
     * 流失客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")})
    @RequestMapping("type/activity/lost")
    @ResponseBody
    public Object typeLost(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        return Result.wrapSuccessfulResult(typeAnalysisService.getLostCustomer(shopId, pageable));
    }

    /**
     * 活跃度客户分析导出
     * @param type 1:活跃, 2:休眠, 3:流失
     * @param request
     * @param response
     */
    @RequestMapping(value = "type/activity/export", method = RequestMethod.GET)
    public void exportActivityCustomerExcel(@RequestParam(value = "type", defaultValue = "1") Integer type,
                                            @PageableDefault(page = 1, value = 500, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();

        String fileName = "";
        if (type == 1) {
            fileName = "活跃客户";
        } else if (type == 2) {
            fileName = "休眠客户";
        } else if (type == 3) {
            fileName = "流失客户";
        } else {
            return;
        }

        long startTime = System.currentTimeMillis();
        ExcelExportor exportor = null;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName + "-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——" + fileName;
            exportor.writeTitle(null, headline, CustomerAnalysisBO.class);

            while (true) {
                Page<CustomerInfo> page = null;
                if (type == 1) {
                    page = typeAnalysisService.getActiveCustomer(shopId, pageable);
                } else if (type == 2) {
                    page = typeAnalysisService.getSleepCustomer(shopId, pageable);
                } else if (type == 3) {
                    page = typeAnalysisService.getLostCustomer(shopId, pageable);
                }
                if (page == null) {
                    break;
                }
                List<CustomerAnalysisBO> customerAnalysisBOList = CustomerAnalysisConverter.convertList(page.getContent());
                if (CollectionUtils.isEmpty(customerAnalysisBOList)) {
                    break;
                }
                exportor.write(customerAnalysisBOList);
                totalSize += customerAnalysisBOList.size();
                if (totalSize >= page.getTotalElements()) {
                    break;
                }
                pageable = pageable.next();
            }
        } catch (Exception e) {
            log.error("活跃度客户分析导出异常，门店id：{}", shopId, e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog(fileName, userInfo, totalSize, endTime - startTime));
    }

    /**
     * 客户级别分析
     * @return
     */
    @RequestMapping("level")
    public String customerLevel(Model model, @RequestParam(value = "tag", required = false)String tag) {
        this._init(model);
        model.addAttribute("subModule", "analysis");

        Long shopId = UserUtils.getShopIdForSession(request);

        model.addAttribute("tag", tag);
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        Date sTime = null;
        Date eTime = null;
        if(params.get("sTime") != null){
            String sTimeStr = params.get("sTime").toString();
            sTime = DateUtil.convertStringToDateYMD(sTimeStr);
        }
        if(params.get("eTime") != null){
            String eTimeStr = params.get("eTime").toString();
            eTime = DateUtil.convertStringToDateYMD(eTimeStr);
            eTime = DateUtil.addDate(eTime,1);
        }
        if(params.get("sTime") != null){
            model.addAttribute("search_sTime", params.get("sTime"));
        }
        if(params.get("eTime") != null){
            model.addAttribute("search_eTime", params.get("eTime"));
        }
        /**
         * 2016-12-22 refactor点:
         *
         * 1.rpc建议try-catch,避免cascade
         *
         * TODO 2016-12-22 refactor点:
         *
         * 2.dubbo接口定义:对于get类型的接口,杜绝返回null
         * 3.dubbo接口定义:对于get类型的接口,杜绝抛出任何异常
         *
         */
        com.tqmall.core.common.entity.Result<CustomerAnalysisLevelDTO> dubboResult = null;
        log.error("[cube平台]dubbo获取等级分析数据 [入参] 门店:{} 时间范围:{}", shopId, sTime + " ~ " + eTime);
        try {
            dubboResult = rpcCustomerAnalysisService.getLevelAnalysis(shopId,sTime,eTime);
        } catch (Exception e) {
            log.error("[cube平台]dubbo获取等级分析数据异常,异常信息:{}", e);
            dubboResult = null;
        }
        if (dubboResult !=null && dubboResult.isSuccess()) {
            CustomerAnalysisLevelDTO analysisDTO =  dubboResult.getData();
            model.addAttribute("highCustomerTypeConsumeAmount", analysisDTO.getHighTotalAmount());
            model.addAttribute("middleCustomerTypeConsumeAmount", analysisDTO.getMiddleTotalAmount());
            model.addAttribute("lowCustomerTypeConsumeAmount", analysisDTO.getLowTotalAmount());
            model.addAttribute("highCustomerTypeCount", analysisDTO.getHighLevelCustomerCount());
            model.addAttribute("middleCustomerTypeCount", analysisDTO.getMiddleLevelCustomerCount());
            model.addAttribute("lowCustomerTypeCount", analysisDTO.getLowLevelCustomerCount());


        }
        return "marketing/ng/analysis/level";
    }

    /**
     * 高质量客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size"),@Param(name = "search_sTime", aliasName = "start_time"), @Param(name = "search_eTime", aliasName = "end_time")})
    @RequestMapping("level/high")
    @ResponseBody
    public Object levelHigh(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        params.put("carLevelTag", 3);
        return Result.wrapSuccessfulResult(levelAnalysisService.getCustomerWithTime(params, pageable));
    }

    /**
     * 中等质量客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size"),@Param(name = "search_sTime", aliasName = "start_time"), @Param(name = "search_eTime", aliasName = "end_time")})
    @RequestMapping("level/middle")
    @ResponseBody
    public Object levelMiddle(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        params.put("carLevelTag",2);
        return Result.wrapSuccessfulResult(levelAnalysisService.getCustomerWithTime(params, pageable));
    }

    /**
     * 低质量客户查询
     * @param model
     * @param pageable
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size"),@Param(name = "search_sTime", aliasName = "start_time"), @Param(name = "search_eTime", aliasName = "end_time")})
    @RequestMapping("level/low")
    @ResponseBody
    public Object levelLow(Model model, @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        params.put("carLevelTag",1);
        return Result.wrapSuccessfulResult(levelAnalysisService.getCustomerWithTime(params, pageable));
    }

    /**
     * 客户级别分析导出
     * @param type 1:高, 2:中, 3:低
     * @param request
     * @param response
     */
    @RequestMapping(value = "level/export", method = RequestMethod.GET)
    public void exportLevelCustomerExcel(@RequestParam(value = "type", defaultValue = "1") final Integer type,
                                         @PageableDefault(page = 1, value = 10, sort = "total_number", direction = Sort.Direction.DESC) Pageable pageable,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);

        String fileName = "";
        if (type == 3) {
            fileName = "高端客户";
            params.put("carLevelTag", 3);
        } else if (type == 2) {
            fileName = "中端客户";
            params.put("carLevelTag", 2);
        } else if (type == 1) {
            fileName = "低端客户";
            params.put("carLevelTag", 1);
        } else {
            return;
        }
        long startTime = System.currentTimeMillis();
        ExcelExportor exportor = null;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName + "-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——" + fileName;
            exportor.writeTitle(null, headline, CustomerAnalysisBO.class);

            while (true) {
                Page<CustomerInfo> page = levelAnalysisService.getCustomerWithTime(params, pageable);
                if (page == null) {
                    break;
                }
                List<CustomerAnalysisBO> customerAnalysisBOList = CustomerAnalysisConverter.convertList(page.getContent());
                if (CollectionUtils.isEmpty(customerAnalysisBOList)) {
                    break;
                }
                exportor.write(customerAnalysisBOList);
                totalSize += customerAnalysisBOList.size();
                if (totalSize >= page.getTotalElements()) {
                    break;
                }
                pageable = pageable.next();
            }
        } catch (Exception e) {
            log.error("客户级别分析导出异常，门店id：{}", shopId, e);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog(fileName, userInfo, totalSize, endTime - startTime));
    }

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                if (getValue() != null) {
                    return DateFormatUtils.toYMD((Date) getValue());
                }
                return null;
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text != null) {
                    try {
                        setValue(DateFormatUtils.parseYMD(text));
                    } catch (ParseException e) {
                        log.error("日期解析错误", e);
                    }
                }

            }
        });
    }

    /**
     * 门店推广
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "promotion", method = RequestMethod.GET)
    public String shopPromotion(Model model) {
        _init(model);
        model.addAttribute("subModule", "promotion");
        return "marketing/ng/analysis/promotion";
    }
}
