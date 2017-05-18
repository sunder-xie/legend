package com.tqmall.legend.web.settlement;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UpperNumbers;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.*;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.setting.ShopPrintConfigService;
import com.tqmall.legend.biz.setting.vo.ShopPrintConfigVO;
import com.tqmall.legend.biz.settlement.vo.DebitBillAndFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillVo;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.ShopPrintConfigUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopConfigureVO;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.enums.setting.PrintTemplateEnum;
import com.tqmall.legend.facade.order.OrderServicesFacade;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.facade.order.vo.DebitExcelBO;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.facade.print.PrintFacade;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.SettlementFacade;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Settlement Controller
 */
@Slf4j
@Controller("SettlementController")
@RequestMapping("shop/settlement")
public class SettlementController extends BaseController {

    @Autowired
    private WrapperOrderFacade wrapperOrderFacade;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private SettlementFacade settlementFacade;
    @Autowired
    private DebitFacade debitFacade;
    @Autowired
    private OrderServicesFacade orderServicesFacade;
    @Autowired
    private ShopPrintConfigService printConfigService;
    @Autowired
    private PrintFacade printFacade;

    /**
     * TODO 分开 列表展示和excel导入 入口
     * <p/>
     * 获取工单列表
     *
     * @param type
     * @param pageable
     * @param response
     * @return
     */
    @RequestMapping(value = "list/{type}")
    @ResponseBody
    public Object orderList(@PathVariable("type") String type,
                            @PageableDefault(page = 1, value = 20, sort = "gmt_modified",
                                    direction = Sort.Direction.DESC) Pageable pageable,
                            HttpServletResponse response) throws IOException, WheelException {
        long sTime = System.currentTimeMillis();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        // query param
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        // 设置Excel导出条数
        if ("export".equals(type)) {
            pageable = new PageRequest(1, Constants.MAX_PAGE_SIZE);
        }

        LegendOrderRequest orderRequest = getLegendOrderRequest(shopId, searchParams);

        int frist = pageable.getPageNumber() - 1;
        PageableRequest pageableRequest = new PageableRequest(frist,pageable.getPageSize(),Sort.Direction.DESC,new String[]{"gmtModified"});

        DefaultPage<OrderInfoVo> orderInfoVoPage = orderServicesFacade.getOrderListFromSearch(pageableRequest, orderRequest);


        if ("export".equals(type)) {
            String excelName = setExcelFileName(type);

            String profile = "yunxiu";
            if ("true".equals(request.getAttribute(Constants.SESSION_SHOP_IS_TQMALL_VERSION))) {
                profile = "tqmall";
            }
            if("1".equals(request.getAttribute(Constants.SESSION_SHOP_JOIN_STATUS))) {
                profile = "banpen";
            }
            ExcelExportor exportor = null;

            try {
                exportor = ExcelHelper.createDownloadExportor(response, excelName + "-"  + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
                Shop shop = shopService.selectById(userInfo.getShopId());
                String headline = shop.getName() + "——" + excelName;
                exportor.writeTitle(profile, headline, DebitExcelBO.class);
                int recordSize = 0;
                while(orderInfoVoPage != null && Langs.isNotEmpty(orderInfoVoPage.getContent())) {
                    List<OrderInfoVo> orderInfoVos = wrapperOrderFacade.wrapperOrderInfoVo(shopId, orderInfoVoPage.getContent());
                    List<DebitExcelBO> debitExcelBOs = BeanMapper.mapListIfPossible(orderInfoVos, DebitExcelBO.class);
                    recordSize += debitExcelBOs.size();
                    exportor.write(profile, debitExcelBOs);
                    if (recordSize >= orderInfoVoPage.getTotalElements()) {
                        break;
                    }
                    pageableRequest = new PageableRequest(pageableRequest.getPageNumber()+1,pageableRequest.getPageSize(),Sort.Direction.DESC,new String[]{"id"});
                    orderInfoVoPage = orderServicesFacade.getOrderListFromSearch(pageableRequest, orderRequest);
                }
                long exportTime = System.currentTimeMillis() - sTime;
                String exportLog = ExportLog.getExportLog(excelName, userInfo, recordSize, exportTime);
                log.info(exportLog);
            } finally {
                ExcelHelper.closeQuiet(exportor);
            }

            return null;
        }
        return Result.wrapSuccessfulResult(orderInfoVoPage);
    }

    /**
     * 根据工单id获取工单信息
     */
    @Deprecated
    @RequestMapping(value = "settlement_order")
    @ResponseBody
    public Result getOrderInfo(@RequestParam("id") Long orderId,
                               HttpServletRequest request) {
        return Result.wrapErrorResult("", "接口已废弃");
    }

    /**
     * 工单结算历史记录
     */
    @Deprecated
    @RequestMapping(value = "settlement_post")
    @ResponseBody
    public Result settlementPost(HttpServletRequest request, long id) {
        return Result.wrapErrorResult("", "接口已废弃");
    }

    /**
     * 获取淘汽优惠券
     *
     * @param orderId
     * @param taoqiCouponSn
     * @return
     */
    @RequestMapping("get_taoqi_coupon_amount")
    @ResponseBody
    public Object getTaoqiCouponAmount(Long orderId, String taoqiCouponSn) {
        Result result = settlementFacade.couponCheck(orderId, taoqiCouponSn);
        if(result.isSuccess()) {
            return result;
        }else{
            return Result.wrapErrorResult(result.getCode(),"淘汽优惠券不可用");
        }
    }

    @RequestMapping(value = "batch_pay_list")
    @ResponseBody
    public Object orderList(@PageableDefault(page = 1, value = 10) final Pageable pageable) {
        return new ApiTemplate<DefaultPage<com.tqmall.legend.facade.order.vo.OrderInfoVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected DefaultPage<OrderInfoVo> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
                LegendOrderRequest orderRequest = getLegendOrderRequest(shopId, searchParams);
                int frist = pageable.getPageNumber() - 1;
                PageableRequest pageableRequest = new PageableRequest(frist,pageable.getPageSize(),Sort.Direction.DESC,new String[]{"gmtModified"});
                DefaultPage<OrderInfoVo> orderInfoVos = orderServicesFacade.getOrderListFromSearch(pageableRequest, orderRequest);
                return orderInfoVos;
            }
        }.execute();

    }

    /**
     * 根据key获取日期
     *
     * @param key
     * @return
     */
    @RequestMapping("get_date")
    @ResponseBody
    public Result getDate(String key) {
        String startTime = "";
        String endTime = "";
        if (key != null && key.equals("this_week")) {
            startTime = getMondayOfThisWeek();
            endTime = DateUtil.convertDateToYMD(new Date());
        } else if (key != null && key.equals("last_week")) {
            startTime = getMondayOfLastWeek();
            endTime = getSundayOfLastWeek();
        } else if (key != null && key.equals("this_month")) {
            startTime = getFirstDayOfThisMonth();
            endTime = DateUtil.convertDateToYMD(new Date());
        } else if (key != null && key.equals("last_month")) {
            startTime = getFirstDayOfLastMonth();
            endTime = getLastDayOfLastMonth();
        }
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("startTime", startTime);
        searchMap.put("endTime", endTime);
        return Result.wrapSuccessfulResult(searchMap);
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    private static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        return df2.format(c.getTime());
    }

    /**
     * 得到上周周一
     *
     * @return yyyy-MM-dd
     */
    private static String getMondayOfLastWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week - 6);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        return df2.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    private static String getSundayOfLastWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        return df2.format(c.getTime());
    }

    /**
     * 得到本月第一个天
     *
     * @return yyyy-MM-dd
     */
    private static String getFirstDayOfThisMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        return df2.format(c.getTime());
    }

    /**
     * 得到上月第一个天
     *
     * @return yyyy-MM-dd
     */
    private static String getFirstDayOfLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        return df2.format(c.getTime());
    }

    /**
     * 得到上月最后1天
     *
     * @return yyyy-MM-dd
     */
    private static String getLastDayOfLastMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        return df2.format(c.getTime());
    }



    /**
     * 结算工单 打印
     *
     * @param model
     * @param orderId 工单ID
     * @param request
     * @return
     */
    @RequestMapping(value = "settle-print", method = RequestMethod.GET)
    public String prePayPrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (!printOrder(model, shopId, orderId, "结算单打印", null)) {
            //工单不存在跳到结算列表页，todo 页面后续迁移需要改url
            return "redirect:/shop/settlement/debit/order-list";
        }
        return "yqx/page/settlement/settle-print";
    }

    /**
     * 简化版结算单打印
     *
     * @param model
     * @param orderId      工单ID
     * @param request
     * @return
     */
    @RequestMapping(value = "simple-settle-print", method = RequestMethod.GET)
    public String prePaySimplePrint(Model model, Long orderId, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (!printOrder(model, shopId, orderId, "简化版结算单打印", null)) {
            return "redirect:/shop/settlement/debit/order-list";
        }
        return "yqx/page/settlement/simple-settle-print";
    }

    /**
     * 新版结算单打印
     * @param model
     * @param orderId
     * @return
     */
    @RequestMapping("shop-settle-print")
    public String settlePrint(Model model, Long orderId){
        Long startTime = System.currentTimeMillis();
        Long shopId = UserUtils.getShopIdForSession(request);
        //获取打印设置
        ShopPrintConfig printConfig = printConfigService.checkOpenStatus(PrintTemplateEnum.SETTLE.getCode(),request);
        if (null == printConfig){
            //没有可供打印的单据
            return "redirect:/shop/settlement/debit/order-list";
        }
        ShopPrintConfigVO printConfigVO = BdUtil.bo2do(printConfig, ShopPrintConfigVO.class);
        boolean flag = settlementFacade.settlePrint(model, shopId, orderId, PrintTemplateEnum.SETTLE.getName(),printConfigVO);
        if (!flag) {
            //工单不存在跳到结算列表页
            return "redirect:/shop/settlement/debit/order-list";
        }
        //打印自定义设置
        printFacade.printSelfConfig(model, shopId);

        log.info("[新版结算单打印耗时:]{}",System.currentTimeMillis() - startTime);
        return "yqx/page/settlement/print/settle-print";
    }
    /**
     * 新版销售结算单打印
     * @param model
     * @param orderId
     * @return
     */
    @RequestMapping("print/sale-print")
    public String saleSettlePrint(Model model, Long orderId){
        Long startTime = System.currentTimeMillis();
        Long shopId = UserUtils.getShopIdForSession(request);
        //获取打印设置
        ShopPrintConfig printConfig = printConfigService.checkOpenStatus(PrintTemplateEnum.SETTLE.getCode(),request);
        if (null == printConfig){
            //没有可供打印的单据
            return "redirect:/shop/settlement/debit/order-list";
        }
        ShopPrintConfigVO printConfigVO = new ShopPrintConfigVO();
        try {
            BeanUtils.copyProperties(printConfigVO,printConfig);
        } catch (Exception e){
            log.error("[打印转换vo错误]",e);
        }
        boolean flag = settlementFacade.settlePrint(model, shopId, orderId, PrintTemplateEnum.SETTLE.getName(),printConfigVO);
        if (!flag) {
            return "redirect:/shop/settlement/debit/order-list";
        }
        //打印自定义设置
        printFacade.printSelfConfig(model, shopId);

        log.info("[新版结算单打印耗时:]{}",System.currentTimeMillis() - startTime);
        return "yqx/page/settlement/print/sale-print";
    }

    /**
     * 新版小票打单打印
     * @param model
     * @param orderId
     * @return
     */
    @RequestMapping("shop-receipt-print")
    public String receiptPrint(Model model, Long orderId){
        Long startTime = System.currentTimeMillis();
        Long shopId = UserUtils.getShopIdForSession(request);
        //获取打印设置
        ShopPrintConfig printConfig = printConfigService.checkOpenStatus(PrintTemplateEnum.TICKET.getCode(),request);
        if (null == printConfig){
            //没有可供打印的单据
            return "redirect:/shop/settlement/debit/order-list";
        }
        ShopPrintConfigVO printConfigVO = new ShopPrintConfigVO();
        try {
            BeanUtils.copyProperties(printConfigVO,printConfig);
        } catch (Exception e){
            log.error("[打印转换vo错误]",e);
        }
        boolean flag = settlementFacade.settlePrint(model, shopId, orderId, PrintTemplateEnum.TICKET.getName(),printConfigVO);
        if (!flag) {
            //工单不存在跳到结算列表页，todo 页面后续迁移需要改url
            return "redirect:/shop/settlement/debit/order-list";
        }
        log.info("[小票打印耗时:]{}",System.currentTimeMillis() - startTime);
        return "yqx/page/settlement/print/receipt-print";
    }


    /**
     * 打印工单
     * @param model
     * @param shopId
     * @param orderId
     * @return
     */
    private boolean printOrder(Model model, Long shopId, Long orderId,String printType, ShopPrintConfigVO shopPrintConfigVO) {
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop", shop);
        String printLog = OrderOperationLog.getOrderPrintLog(printType, shop);
        log.info(printLog);
        // 获取门店配置
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.SETTLEPRINT, shopId);
        String shopConfigureContent = "1、本公司的检验不承担用户提供配件的维修和已建议的维修故障而拒修的质量责任；<br />" +
                "2、本维修工单内价格供参考，服务费用以实际结算为准。";
        if (shopConfigureOptional.isPresent()) {
            ShopConfigure shopConfigure = shopConfigureOptional.get();
            shopConfigureContent = shopConfigure.getConfValue();
        }
        ShopConfigureVO configureVO = new ShopConfigureVO();
        configureVO.setSettleComment(shopConfigureContent);
        model.addAttribute("conf", configureVO);

        // 工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return false;
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        //维修类别
        Long orderTypeId = orderInfo.getOrderType();
        if (orderTypeId > 0) {
            OrderType orderType = orderTypeService.selectById(orderTypeId);
            orderInfo.setOrderTypeName(orderType.getName());
        }

        // 获取车辆信息
        Optional<CustomerCar> customerCarOptional = customerCarService.getCustomerCar(orderInfo.getCustomerCarId());
        if (customerCarOptional.isPresent()) {
            CustomerCar customerCar = customerCarOptional.get();
            Customer customer = customerService.selectById(customerCar.getCustomerId());
            orderInfo = wrapperOrderFacade.wrapperOrderInfo(orderInfo, customerCar, customer);
        }
        // 工单信息
        model.addAttribute("orderInfo", orderInfo);

        // 工单物料
        Optional<List<OrderGoods>> orderGoodListOptional = orderGoodsService.getOrderGoodList(orderId, shopId);
        if (orderGoodListOptional.isPresent()) {
            model.addAttribute("orderGoodsList", orderGoodListOptional.get());
        }

        // 工单服务
        List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId);
        // 基本服务
        List<OrderServices> basicOrderServiceList = Lists.newArrayList();
        // 附加服务
        List<OrderServices> additionalOrderServiceList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                int serviceType = orderServices.getType();
                // 基本服务
                if (serviceType == OrderServiceTypeEnum.BASIC.getCode()) {
                    basicOrderServiceList.add(orderServices);
                }
                // 附加服务
                if (serviceType == OrderServiceTypeEnum.ANCILLARY.getCode()) {
                    additionalOrderServiceList.add(orderServices);
                }
            }
        }

        // 关联维修工名称
        List<OrderServicesVo> orderServicesVos = wrapperOrderFacade.orderServiceListReferWorderName(basicOrderServiceList);
        // 基本服务
        model.addAttribute("orderServicesList1", orderServicesVos);
        // 附加服务
        model.addAttribute("orderServicesList2", additionalOrderServiceList);

        model.addAttribute("orderAmountUpper", UpperNumbers.toChinese(orderInfo.getOrderAmount().toString()));

        // 查询收款和流水
        DebitBillAndFlowVo debitBillAndFlow = debitFacade.findDebitBillAndFlow(shopId, orderId);
        if (debitBillAndFlow != null) {
            DebitBillVo debitBillVo = debitBillAndFlow.getDebitBillVo();
            if (debitBillVo != null) {
                // 应收
                model.addAttribute("receivableAmountUpper", UpperNumbers.toChinese(debitBillVo.getReceivableAmount().toPlainString()));
                // 实收
                model.addAttribute("paidAmountUpper", UpperNumbers.toChinese(debitBillVo.getPaidAmount().toString()));
            }
            model.addAttribute("debitBill", debitBillVo);
            model.addAttribute("debitBillFlowList", debitBillAndFlow.getDebitBillFlowVoList());
        }

        //新版本打印
        if (null != shopPrintConfigVO && null != shopPrintConfigVO.getConfigFieldVO()){
            ShopPrintConfigUtil.wrapperField(shopPrintConfigVO.getConfigFieldVO(),orderInfo);
            model.addAttribute("printConfigVO",shopPrintConfigVO);
        }

        return true;
    }

    /**===================================================
     *
     *
     * ===================================================
     */

    private LegendOrderRequest getLegendOrderRequest(Long shopId, Map<String, Object> searchParams) {
        LegendOrderRequest orderRequest = new LegendOrderRequest();
        orderRequest.setShopId(shopId.toString());
        if(searchParams.containsKey("orderStatuss")){
            orderRequest.setOrderStatus(searchParams.get("orderStatuss").toString());
        }
        if(searchParams.containsKey("payStatus")){
            List<Integer> payStatus = Lists.newArrayList();
            Integer paystatus = Integer.parseInt(searchParams.get("payStatus").toString());
            if(paystatus == 3){
                payStatus.add(OrderNewStatusEnum.WXDD.getPayStatus());
                orderRequest.setOrderStatus(OrderNewStatusEnum.WXDD.getOrderStatus());
            }else {
                payStatus.add(paystatus);
            }
            orderRequest.setPayStatus(payStatus);
        }
        if(searchParams.containsKey("payStatusList")){
            List<Integer> payStatus = Lists.newArrayList();
            Integer paystatus = Integer.parseInt(searchParams.get("payStatusList").toString());
            payStatus.add(paystatus);
            orderRequest.setPayStatus(payStatus);
        }
        // 开单开始时间
        if (searchParams.containsKey("startTime")) {
            orderRequest.setStartTime(searchParams.get("startTime") + " 00:00:00");
        }
        // 开单结束时间
        if (searchParams.containsKey("endTime")) {
            orderRequest.setEndTime(searchParams.get("endTime") + " 23:59:59");
        }
        // 结算开始时间
        if (searchParams.containsKey("payStartTime")) {
            orderRequest.setPayStartTime(searchParams.get("payStartTime") + " 00:00:00");
        }
        // 结算结束时间
        if (searchParams.containsKey("payEndTime")) {
            orderRequest.setPayEndTime(searchParams.get("payEndTime") + " 23:59:59");
        }
        // 对账开始时间
        if (searchParams.containsKey("confirmStartTime")) {
            orderRequest.setConfirmStartTime(searchParams.get("confirmStartTime") + " 00:00:00");
        }
        // 对账结束时间
        if (searchParams.containsKey("confirmEndTime")) {
            orderRequest.setConfirmEndTime(searchParams.get("confirmEndTime") + " 23:59:59");
        }
        // 服务顾问id
        if (searchParams.containsKey("receiver")) {
            orderRequest.setReceiver(searchParams.get("receiver").toString());
        }
        // 工单编号
        if (searchParams.containsKey("orderSnLike")) {
            orderRequest.setOrderSnLike((String) searchParams.get("orderSnLike"));
        }
        // 查找车牌号、手机号、工单号、车主
        if (searchParams.containsKey("keyword")) {
            orderRequest.setLikeKeyWords((String) searchParams.get("keyword"));
        }
        // 车牌
        if (searchParams.containsKey("carLicenseLike")) {
            orderRequest.setCarLicenseLike((String) searchParams.get("carLicenseLike"));
        }
        // 单位
        if (searchParams.containsKey("companyLike")) {
            orderRequest.setCompany((String) searchParams.get("companyLike"));
        }
        //业务类型
        if (searchParams.containsKey("orderType")){
            List<String> orderType = Lists.newArrayList();
            orderType.add(searchParams.get("orderType").toString());
            orderRequest.setOrderType(orderType);
        }
        if(searchParams.containsKey("orderStatusSale")){
            orderRequest.setOrderStatusSale(Boolean.valueOf((String) searchParams.get("orderStatusSale")));
        }
        // 备注
        if (searchParams.containsKey("postscript")) {
            orderRequest.setPostscript((String) searchParams.get("postscript"));
        }
        return orderRequest;
    }

    private String setExcelFileName(String type) {
        String excelName = "";
        if ("export".equals(type) || "export_ng".equals(type)) {
            // 设置响应头
            String filename = "";
            String searchPayStatus = request.getParameter("search_payStatus");
            if ("2".equals(searchPayStatus)) {
                filename = "已结算工单信息";
            } else if ("0".equals(searchPayStatus)) {
                filename = "待结算工单信息";
            } else if ("1".equals(searchPayStatus)) {
                filename = "挂账工单信息";
            } else if ("2".equals(searchPayStatus)){
                filename = "已结清工单信息";
            } else if ("3".equals(searchPayStatus)){
                filename = "无效工单信息";
            }else{
                filename = "所有工单信息";
            }

            excelName = filename;
        }
        return excelName;
    }
}
