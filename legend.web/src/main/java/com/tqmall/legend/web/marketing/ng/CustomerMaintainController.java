package com.tqmall.legend.web.marketing.ng;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcCustomerMaintainService;
import com.tqmall.cube.shop.result.CustomerMaintainDTO;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.annotation.Param;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerFeedbackService;
import com.tqmall.legend.biz.customer.bo.CustomerFeedbackBO;
import com.tqmall.legend.biz.marketing.ng.NoteEffectService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.CustomerInfoService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServiceTypeEnum;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.shop.NoteInfoVO;
import com.tqmall.legend.entity.shop.OrderAndFeedbackInfoVO;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.pojo.shopnote.ShopNoteInfoVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 营销中心controller
 * Created by wjc on 3/9/16.
 */
@Slf4j
@Controller
@RequestMapping("/marketing/ng/maintain")
public class CustomerMaintainController extends BaseController {
    @Autowired
    private NoteEffectService noteEffectService;
    @Autowired
    private ShopNoteInfoService shopNoteInfoService;
    @Autowired
    private CustomerFeedbackService customerFeedbackService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private RpcCustomerMaintainService rpcCustomerMaintainService;
    @Autowired
    private ShopService shopService;


    @RequestMapping
    public String index(Model model) {
        _initModel(model);
        model.addAttribute("subModule", "maintain-center");

        Long shopId = UserUtils.getShopIdForSession(request);
        com.tqmall.core.common.entity.Result<CustomerMaintainDTO> dubboResult = rpcCustomerMaintainService.getMaintainSummery(shopId);
        if (dubboResult.isSuccess()) {
            CustomerMaintainDTO customerMaintainDTO = dubboResult.getData();
            model.addAttribute("customerMaintainDTO", customerMaintainDTO);
        }
        model.addAttribute("current_month", DateUtil.convertDateToStr(new Date(), "M"));
        model.addAttribute("current_month_data", marketingChart(0).getData());
        model.addAttribute("next_month", DateUtil.convertDateToStr(DateUtil.addMonth(new Date(), -1), "M"));
        model.addAttribute("next_month_data", marketingChart(-1).getData());
        model.addAttribute("last_month", DateUtil.convertDateToStr(DateUtil.addMonth(new Date(), -2), "M"));
        model.addAttribute("last_month_data", marketingChart(-2).getData());
        model.addAttribute("isYBD", UserUtils.isYBD(request));//是否样板店

        return "marketing/ng/maintain/index";
    }


    public Result marketingChart(Integer month) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Date start_month = DateUtil.getStartMonth(DateUtil.addMonth(new Date(), month));//月初
        Date end_month = DateUtil.getEndMonth(DateUtil.addMonth(new Date(), month));//月末
        Map param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("noteFlag", 1);
        Map resultMap = Maps.newConcurrentMap();
        param.put("sTime", start_month);
        param.put("eTime", end_month);
        Integer handle_count = shopNoteInfoService.getShopNoteInfoCount(param);

        param.remove("sTime");
        param.remove("eTime");
        param.put("sOperatorTime", start_month);
        param.put("eOperatorTime", end_month);
        Integer count = noteEffectService.selectCount(param);
        BigDecimal amount = noteEffectService.effectAmount(param);

        resultMap.put("customer", count);
        resultMap.put("amount", amount);
        resultMap.put("handleCount", handle_count);

        return Result.wrapSuccessfulResult(resultMap);
    }

    private void _initModel(Model model) {
        model.addAttribute("moduleUrl", "marketing");
    }

    @HttpRequestLog
    @RequestMapping("center")
    public String center(Model model, String tag, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (log.isInfoEnabled()) {
            log.info("[提醒中心跳转上级url]:{}", request.getHeader("Referer"));
        }
        _initModel(model);
        model.addAttribute("subModule", "maintain-center");
        model.addAttribute("tag", tag);
        NoteInfoVO noteInfoVO = shopNoteInfoService.countUnHandleNoteInfo(shopId);
        model.addAttribute("noteInfo", noteInfoVO);
        return "marketing/ng/maintain/center";
    }

    /**
     * 查询门店提醒信息
     *
     * @param noteType
     * @return
     */
    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")})
    @RequestMapping("list")
    @ResponseBody
    public Result findShopNoteInfoByType(@RequestParam Integer noteType, @PageableDefault(page = 1, value = 10) Pageable pageable, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Page<ShopNoteInfoVO> page = shopNoteInfoService.getShopNoteInfo(shopId, noteType, pageable.getPageNumber(), pageable.getPageSize());
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 消费记录
     */
    @RequestMapping("consumeRecord")
    @ResponseBody
    public Object consumeRecord(@RequestParam(value = "customerCarId", required = true) Long customerCarId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("customerCarId", customerCarId);
        param.put("offset", 0);
        param.put("limit", 10);
        param.put("orderStatus", "DDYFK");
        param.put("sorts", new String[] { "gmt_create desc" });
        List<OrderInfo> orderInfoList = orderInfoService.select(param);
        List<OrderAndFeedbackInfoVO> orderAndFeedbackInfoVOList = Lists.newArrayList();
        for (OrderInfo orderInfo : orderInfoList) {
            OrderAndFeedbackInfoVO orderAndFeedbackInfoVO = new OrderAndFeedbackInfoVO();
            orderAndFeedbackInfoVO.setOrderId(orderInfo.getId());
            orderAndFeedbackInfoVO.setOrderSn(orderInfo.getOrderSn());
            orderAndFeedbackInfoVO.setOrderTime(DateUtil.convertDateToYMDHMS(orderInfo.getGmtCreate()));
            orderAndFeedbackInfoVO.setOrderServicesList(orderServicesService.queryOrderServiceList(orderInfo.getId(), orderInfo.getShopId(), OrderServiceTypeEnum.BASIC, param));
            //            orderAndFeedbackInfoVO.setOrderGoodsList(orderGoodsService.queryOrderGoodList(orderInfo.getId(), orderInfo.getShopId(), OrderGoodTypeEnum.ACTUAL));
            orderAndFeedbackInfoVOList.add(orderAndFeedbackInfoVO);
        }
        return Result.wrapSuccessfulResult(orderAndFeedbackInfoVOList);
    }


    /**
     * 回访记录
     */
    @RequestMapping("feedbackList")
    @ResponseBody
    public Object feedbackList(@RequestParam(value = "customerCarId", required = true) Long customerCarId) {
        if (customerCarId == null || customerCarId < 1) {
            return Result.wrapSuccessfulResult(Collections.emptyList());
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Map param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("customerCarId", customerCarId);
        param.put("visitTimeLt",new Date());
        param.put("offset", 0);
        param.put("limit", 10);
        //param.put("orderId", 0);
        param.put("sorts", new String[] { "gmt_create desc" });
        return Result.wrapSuccessfulResult(customerFeedbackService.select(param));
    }

    /**
     * 客户回访
     *
     * @param customerCarId 客户id
     * @param content       回访内容
     * @param nextVisitTime 下次回访时间
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "saveFeedback", method = RequestMethod.POST)
    @ResponseBody
    public Result saveFeedback(@RequestParam(value = "noteInfoId", required = false) Long noteInfoId, @RequestParam(value = "customerCarId") Long customerCarId, String content, String nextVisitTime) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        CustomerInfo customerInfo = null;
        if (customerCarId != null && customerCarId > 0) {
            customerInfo = customerInfoService.getCustomerInfo(shopId, customerCarId);
        }
        CustomerFeedback customerFeedback = new CustomerFeedback();
        customerFeedback.setShopId(shopId);
        customerFeedback.setCreator(userId);
        customerFeedback.setModifier(userId);
        customerFeedback.setCustomerCarId(customerCarId);
        customerFeedback.setCustomerFeedback(content);
        if (customerInfo != null) {
            customerFeedback.setCarLicense(customerInfo.getCarLicense());
            customerFeedback.setCarBrandId(customerInfo.getCarBrandId());
            customerFeedback.setCarBrandName(customerInfo.getCarBrand());
            customerFeedback.setCarSeriesId(customerInfo.getCarSeriesId());
            customerFeedback.setCarSeriesName(customerInfo.getCarSeries());
            customerFeedback.setMobile(customerInfo.getMobile());
            customerFeedback.setCustomerId(customerInfo.getCustomerId());
            customerFeedback.setCustomerName(customerInfo.getCustomerName());
        }
        Long orderId = 0L;
        if (noteInfoId != null && noteInfoId > 0) {
            NoteInfo noteInfo = shopNoteInfoService.getById(shopId, noteInfoId);
            if (noteInfo != null) {
                if (noteInfo.getNoteType().equals(NoteType.VISIT_NOTE_TYPE)) {
                    orderId = noteInfo.getRelId();
                    if (orderId != null && orderId > 0) {
                        OrderInfo orderInfo = orderInfoService.selectById(orderId);
                        if (orderInfo != null) {
                            customerFeedback.setFinishTime(orderInfo.getFinishTime());
                        }
                        // 设置orderInfo已回访
                        orderInfoService.updateOrderVisit(shopId, orderId);
                    }
                }
                customerFeedback.setNoteInfoId(noteInfoId);
                customerFeedback.setNoteType(noteInfo.getNoteType());
            }
        }
        customerFeedback.setOrderId(orderId);
        customerFeedback.setVisitorName(userInfo.getName());
        customerFeedback.setVisitMethod("电话回访");
        customerFeedback.setVisitTime(new Date());
        if (StringUtils.isNotBlank(nextVisitTime)) {
            customerFeedback.setNextVisitTime(DateUtil.convertStringToDateYMD(nextVisitTime));
        }
        Result result = customerFeedbackService.addAndUpdateNoteInfo(customerFeedback, noteInfoId, userInfo);
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(customerFeedback);
        } else {
            return Result.wrapErrorResult("", "回访记录保存失败");
        }
    }


    @RequestMapping("effect")
    public String effect(Model model) {
        _initModel(model);
        model.addAttribute("subModule", "maintain-effect");
        return "marketing/ng/maintain/effect";
    }

    /**
     * 获取收益列表
     *
     * @return
     */
    @HttpRequestLog
    @RequestMapping("effect/list")
    @ResponseBody
    public Object effectList() {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        return Result.wrapSuccessfulResult(noteEffectService.selectCountWithWay(params));
    }

    @RequestMapping("detail")
    public String detail(Model model) {
        _initModel(model);
        model.addAttribute("subModule", "maintain-center");

        return "marketing/ng/maintain/detail";
    }

    /**
     * 客情明细——工作记录列表
     *
     * @param pageable
     * @return
     */
    @HttpRequestLog
    @RequestMapping("detail/list")
    @ResponseBody
    public Result<DefaultPage<CustomerFeedbackBO>> detailList(@PageableDefault(page = 1, value = 10) Pageable pageable,
                                                              @RequestParam(value = "search_sTime", required = false) String sTime,
                                                              @RequestParam(value = "search_eTime", required = false) String eTime,
                                                              HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber() - 1, pageable.getPageSize());
        String visitMethod = "电话回访";
        int total = customerFeedbackService.countCustomerFeedback(shopId, sTime, eTime, visitMethod);
        List<CustomerFeedbackBO> customerFeedbackList = customerFeedbackService.getCustomerFeedbackList(shopId, sTime, eTime, visitMethod, pageable.getPageNumber(), pageable.getPageSize());
        DefaultPage<CustomerFeedbackBO> page = new DefaultPage<>(customerFeedbackList, pageRequest, total);
        page.setPageUri(request.getRequestURI());
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 导出工作记录列表
     * @param sTime
     * @param eTime
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "detail/list/export", method = RequestMethod.GET)
    public void exportCustomerFeedbackExcel(@RequestParam(value = "search_sTime", required = false) final String sTime,
                                            @RequestParam(value = "search_eTime", required = false) final String eTime,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws IOException, WheelException {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        String fileName = "工作记录-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        String visitMethod = "电话回访";

        try {
            long sTime1 = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            int recordSize = 0;
            int page = 1;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——工作记录";
            exportor.writeTitle(null, headline, CustomerFeedbackBO.class);
            do {
                List<CustomerFeedbackBO> customerFeedbackList = customerFeedbackService.getCustomerFeedbackList(shopId, sTime, eTime, visitMethod, page, 500);
                if (CollectionUtils.isEmpty(customerFeedbackList)) {
                    break;
                }
                recordSize += customerFeedbackList.size();
                exportor.write(customerFeedbackList);
                page++;
            }while (true);
            long exportTime = System.currentTimeMillis() - sTime1;
            String exportLog = ExportLog.getExportLog("工作记录", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }



    /**
     * 客情明细——收益记录列表
     *
     * @param pageable
     * @return
     */
    @HttpRequestLog
    @RequestMapping("detail/effectList")
    @ResponseBody
    public Object effectDetail(@PageableDefault(page = 1, value = 10, sort = "gmt_modified", direction = Sort.Direction.DESC) Pageable pageable) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        return Result.wrapSuccessfulResult(noteEffectService.selectList(pageable, params));
    }

    @RequestMapping("type_analysis")
    public String typeAnalysis(Model model) {
        _initModel(model);
        model.addAttribute("subModule", "maintain-analysis");
        return "marketing/ng/maintain/type_analysis";
    }

    /**
     * 提醒类别分析
     *
     * @return
     */
    @HttpRequestLog
    @RequestMapping("type_analysis/list")
    @ResponseBody
    public Object typeAnalysisList() {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        return Result.wrapSuccessfulResult(noteEffectService.selectCountWithType(params));
    }
}
