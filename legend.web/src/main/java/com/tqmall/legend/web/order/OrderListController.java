package com.tqmall.legend.web.order;

import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerFeedbackService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.facade.order.OrderServicesFacade;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

/**
 * 工单列表
 * Created by Sven.zhang on 2016/4/7.
 */
@Controller
@Slf4j
@RequestMapping("shop/order")
public class OrderListController extends BaseController {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private CustomerFeedbackService customerFeedbackService;
    @Autowired
    private OrderServicesFacade orderServicesFacade;
    @Autowired
    private ShopService shopService;
    @Autowired
    private WrapperOrderFacade wrapperOrderFacade;

    /**
     * 工单列表内容初始化
     *
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "order-list", method = RequestMethod.GET)
    public String initOrderList( @RequestParam(value = "tradeStatus", required = false) String tradeStatus,
                                 @RequestParam(value = "orderType", required = false) Integer orderType,
                                 Model model) {
        if (tradeStatus != null) {
            model.addAttribute("orderStatus", tradeStatus);
            model.addAttribute("orderStatusName", OrderNewStatusEnum.getOrderStatusNameByName(tradeStatus));
        }
        if (orderType != null) {
            model.addAttribute("orderType", orderType);
            model.addAttribute("orderTypeName", OrderCategoryEnum.getsNameByCode(orderType));
        }

        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        return "yqx/page/order/order-list";
    }

    /**
     *
     *  获取工单列表接口
     *
     * @return
     */
    @RequestMapping(value = "order-list/list", method = RequestMethod.GET)
    @ResponseBody
    public Object orderList(@PageableDefault(page = 1, value = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        // searchForm 条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        LegendOrderRequest orderRequest = new LegendOrderRequest();
        orderRequest.setShopId(shopId.toString());
        setOrderRequestParam(searchParams, orderRequest);

        int page = pageable.getPageNumber() - 1;
        PageableRequest pageableRequest = new PageableRequest(page,pageable.getPageSize(),Sort.Direction.DESC,new String[]{"id"});

        DefaultPage<OrderInfoVo> orderInfoVoPage = orderServicesFacade.getOrderListFromSearch(pageableRequest, orderRequest);

        return Result.wrapSuccessfulResult(orderInfoVoPage);
    }

    /**
     * 导出工单列表
     * @param pageable
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "order-list/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportOrderList(@PageableDefault(page = 0, value = 500, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception{
        UserInfo userInfo = UserUtils.getUserInfo(request);
        long sTime = System.currentTimeMillis();
        // searchForm 条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        LegendOrderRequest orderRequest = new LegendOrderRequest();
        orderRequest.setShopId(userInfo.getShopId().toString());
        setOrderRequestParam(searchParams, orderRequest);

        PageableRequest pageableRequest = new PageableRequest(pageable);

        String profile = "yunxiu";
        if ("true".equals(request.getAttribute(Constants.SESSION_SHOP_IS_TQMALL_VERSION))) {
            profile = "tqmall";
        }
        if("1".equals(request.getAttribute(Constants.SESSION_SHOP_JOIN_STATUS))) {
            profile = "banpen";
        }
        ExcelExportor exportor = null;

        try {
            String fileName = "工单信息-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——工单信息";
            exportor.writeTitle(profile, headline, OrderInfoVo.class);
            while(true) {
                DefaultPage<OrderInfoVo> orderInfoVoPage = orderServicesFacade.getOrderListFromSearch(pageableRequest, orderRequest);
                if (Langs.isNull(orderInfoVoPage) || Langs.isEmpty(orderInfoVoPage.getContent())) {
                    break;
                }
                List<OrderInfoVo> orderinfoVos = wrapperOrderFacade.wrapperOrderInfoVo(userInfo.getShopId(), orderInfoVoPage.getContent());
                recordSize += orderinfoVos.size();
                exportor.write(profile, orderinfoVos);
                if (recordSize >= orderInfoVoPage.getTotalElements()) {
                    break;
                }
                pageable = pageable.next();
                pageableRequest = new PageableRequest(pageable);
            }
            long exportTime = System.currentTimeMillis() - sTime;
            log.info(ExportLog.getExportLog("工单导出", userInfo, recordSize, exportTime));
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    private void setOrderRequestParam(Map<String, Object> searchParams, LegendOrderRequest orderRequest) {
        // 手机号码、车牌、姓名
        if (searchParams.containsKey("keyword")) {
            String keyword = (String)searchParams.get("keyword");
            orderRequest.setLikeKeyWords(keyword);
        }
        // 车型
        if (searchParams.containsKey("carLike")) {
            String carLike = (String)searchParams.get("carLike");
            orderRequest.setCarLike(carLike);
        }
        //工单编号
        if (searchParams.containsKey("orderSnLike")) {
            String orderSnLike = (String)searchParams.get("orderSnLike");
            orderRequest.setOrderSnLike(orderSnLike);
        }
        // 车牌
        if (searchParams.containsKey("licenseLike")) {
            String licenseLike = (String)searchParams.get("licenseLike");
            orderRequest.setCarLicenseLike(licenseLike);
        }
        // 姓名
        if (searchParams.containsKey("customerNameLike")) {
            String customerNameLike = (String)searchParams.get("customerNameLike");
            orderRequest.setCustomerNameLike(customerNameLike);
        }
        // 手机号码
        if (searchParams.containsKey("mobileLike")) {
            String mobileLike = (String)searchParams.get("mobileLike");
            orderRequest.setMobileLike(mobileLike);
        }
        // 开始时间
        if (searchParams.containsKey("startTime")) {
            orderRequest.setStartTime(searchParams.get("startTime") + " 00:00:00");
        }
        // 结束时间
        if (searchParams.containsKey("endTime")) {
            orderRequest.setEndTime(searchParams.get("endTime") + " 23:59:59");
        }
        // 订单状态
        if (searchParams.containsKey("orderStatuss")) {
            orderRequest.setOrderStatus(OrderNewStatusEnum.getOrderStatusByName(searchParams.get("orderStatuss").toString()));
            List<Integer> payStatus = Lists.newArrayList();
            Integer paystatus = OrderNewStatusEnum.getPayStatusByName(searchParams.get("orderStatuss").toString());
            if(null != paystatus){
                payStatus.add(paystatus);
            }
            orderRequest.setPayStatus(payStatus);
        }
        // 工单类别
        if (searchParams.containsKey("orderTag")) {
            List<Integer> orderTags = Lists.newArrayList();
            orderTags.add(Integer.parseInt((String)searchParams.get("orderTag")));
            orderRequest.setOrderTag(orderTags);
        }
        //业务类型
        if (searchParams.containsKey("orderType")){
            List<String> orderType = Lists.newArrayList();
            orderType.add((String)searchParams.get("orderType"));
            orderRequest.setOrderType(orderType);
        }
        // 渠道商
        if (searchParams.containsKey("channelNameLike")) {
            String channelNameLike = (String)searchParams.get("channelNameLike");
            orderRequest.setChannelNameLike(channelNameLike);
        }

        // 服务顾问id
        if (searchParams.containsKey("receiver")) {
            orderRequest.setReceiver((String)searchParams.get("receiver"));
        }
        // 委托状态
        if (searchParams.containsKey("proxyType")) {
            List<Integer> proxyType = Lists.newArrayList();
            proxyType.add(Integer.parseInt((String)searchParams.get("proxyType")));
            orderRequest.setProxyType(proxyType);
        }
        // 备注
        if (searchParams.containsKey("postscript")) {
            orderRequest.setPostscript((String)searchParams.get("postscript"));
        }
    }



    /**
     * 获取工单类型
     *
     * @return
     */
    @RequestMapping("order-tag-list")
    @ResponseBody
    public Result getOrderTag() {
        List<OrderTagBo> orderCategoryEnumList = new ArrayList<>();
        //档口版门店不展示综合维修单、引流活动单
        Integer shopLevel = UserUtils.getShopLevelForSession(request);
        if(shopLevel.equals(Constants.SHOP_LEVEL_TQMALL_VERSION)){
            TqmallOrderTagEnum[] tqmallOrderTagEnums = TqmallOrderTagEnum.getMessages();
            for (TqmallOrderTagEnum tqmallOrderTagEnum : tqmallOrderTagEnums) {
                OrderTagBo orderTagBo = new OrderTagBo();
                orderTagBo.setCode(tqmallOrderTagEnum.getCode());
                orderTagBo.setName(tqmallOrderTagEnum.getsName());
                orderCategoryEnumList.add(orderTagBo);
            }
        }else{
            OrderCategoryEnum[] orderCategoryEnums = OrderCategoryEnum.getMessages();
            for (OrderCategoryEnum orderCategoryEnum : orderCategoryEnums) {
                OrderTagBo orderTagBo = new OrderTagBo();
                orderTagBo.setCode(orderCategoryEnum.getCode());
                orderTagBo.setName(orderCategoryEnum.getsName());
                orderCategoryEnumList.add(orderTagBo);
            }
        }
        return Result.wrapSuccessfulResult(orderCategoryEnumList);
    }

    /**
     * 获取全部状态
     *
     * @return
     */

    @RequestMapping("order-status-list")
    @ResponseBody
    public Result getOrderStatus() {
        List<OrderStatusBo> orderNewStatusList = new ArrayList<>();
        OrderNewStatusEnum[] orderNewStatusEnum = OrderNewStatusEnum.values();
        for (OrderNewStatusEnum orderStatus : orderNewStatusEnum) {
            OrderStatusBo orderStatusBo = new OrderStatusBo();
            orderStatusBo.setKey(orderStatus.name());
            orderStatusBo.setName(orderStatus.getOrderStatusName());
            orderNewStatusList.add(orderStatusBo);
        }
        return Result.wrapSuccessfulResult(orderNewStatusList);
    }

    /**
     * 接待模块，客户详情-工单列表
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "customer", method = RequestMethod.GET)
    @ResponseBody
    public Object orderListForCustomer(
            @PageableDefault(page = 1, value = 4, sort = "create_time", direction = Sort.Direction.DESC) Pageable pageable) {

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        if (searchParams.containsKey("orderStatuss")) {
            String[] _str = String.class.cast(searchParams.get("orderStatuss")).split(",");
            searchParams.put("orderStatuss", _str);
        }
        if (searchParams.containsKey("startTime")) {
            searchParams.put("startTime", searchParams.get("startTime") + " 00:00:00");
        }
        if (searchParams.containsKey("endTime")) {
            searchParams.put("endTime", searchParams.get("endTime") + " 23:59:59");
        }
        DefaultPage<OrderInfo> page = null;

        page = (DefaultPage<OrderInfo>) orderInfoService.getOrderInfoPage(pageable, searchParams);
        for (OrderInfo orderInfo : page.getContent()) {
            Map<String, Object> serviceParams = new HashMap<String, Object>();
            serviceParams.put("shopId", searchParams.get("shopId"));
            serviceParams.put("orderId", orderInfo.getId());
            serviceParams.put("type", 1);
            // 获取服务项目
            List<OrderServices> list = orderServicesService.select(serviceParams);
            orderInfo.setServiceList(list);

            Map<String, Object> goodsParams = new HashMap<String, Object>();
            goodsParams.put("shopId", searchParams.get("shopId"));
            goodsParams.put("orderId", orderInfo.getId());
            goodsParams.put("tag", "gmt_modified");
            List<OrderGoods> good = orderGoodsService.select(goodsParams);
            orderInfo.setGoodsList(good);

            Map<String, Object> otherParams = new HashMap<String, Object>();
            otherParams.put("shopId", searchParams.get("shopId"));
            otherParams.put("orderId", orderInfo.getId());
            otherParams.put("type", 2);
            List<OrderServices> other = orderServicesService.select(otherParams);
            orderInfo.setOtherList(other);

            CustomerFeedback customerFeedback = customerFeedbackService.selectByOrderIdAndShopId(
                    orderInfo.getId(), orderInfo.getShopId());
            if (customerFeedback != null) {
                orderInfo.setTotalStar(customerFeedback.getTotalStar());
            } else {
                orderInfo.setTotalStar(0);
            }
        }
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));

        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 获取车辆挂账工单list
     *
     * @return
     */
    @RequestMapping("get-sign-order")
    @ResponseBody
    public Result getOrderInfoList(@RequestParam(value = "customerCarId",required = true) Long customerCarId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String, Object> searchMap = ServletUtils.getParametersMapStartWith(request);
        searchMap.put("customerCarId",customerCarId);
        searchMap.put("orderStatus",OrderNewStatusEnum.DDYFK.getOrderStatus());
        searchMap.put("payStatus",PayStatusEnum.SIGN.getCode());
        List<OrderInfo> orderInfoList = orderInfoService.select(searchMap);
        orderInfoList = orderInfoService.getOrderDebitAmountByBillcenter(shopId, orderInfoList);
        return Result.wrapSuccessfulResult(orderInfoList);
    }
}
