package com.tqmall.legend.web.warehouse;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UpperNumbers;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.setting.ShopPrintConfigService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.common.GoodsUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.OrderFormEntityBo;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServiceTypeEnum;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.warehouseout.WarehouseOut;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.setting.PrintTemplateEnum;
import com.tqmall.legend.enums.warehouse.WarehouseOutStatusEnum;
import com.tqmall.legend.enums.warehouse.WarehouseOutTypeEnum;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.facade.order.vo.SearchOrderVo;
import com.tqmall.legend.facade.print.PrintFacade;
import com.tqmall.legend.facade.warehouse.WarehouseOutFacade;
import com.tqmall.legend.facade.warehouse.vo.LegendWarehouseOutDTOVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutDetailVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutRefundVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ExcelExportUtil;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import com.tqmall.search.dubbo.client.legend.warehouseout.param.LegendWarehouseOutRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/8/24.
 */
@Controller
@RequestMapping("shop/warehouse/out")
@Slf4j
public class WarehouseOutController extends BaseController {
    @Resource
    private WarehouseOutFacade warehouseOutFacade;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private ShopPrintConfigService printConfigService;
    @Autowired
    private WrapperOrderFacade wrapperOrderFacade;
    @Autowired
    private WarehouseOutService warehouseOutService;
    @Autowired
    private PrintFacade printFacade;

    /**
     * 获取出库类型
     *
     * @param action 是否剔除工单出库类型
     * @return
     */
    @RequestMapping(value = "get_out_type", method = RequestMethod.GET)
    @ResponseBody
    public Result getOutType(@RequestParam(value = "action", required = false) Boolean action) {
        List<BaseEnumBo> outTypeList = new ArrayList<>(WarehouseOutTypeEnum.values().length);
        for (WarehouseOutTypeEnum typeEnum : WarehouseOutTypeEnum.values()) {
            BaseEnumBo bo = new BaseEnumBo();
            bo.setMessage(typeEnum.getMessage());
            bo.setName(typeEnum.name());
            outTypeList.add(bo);
        }
        if (action != null && action) {
            Iterator it = outTypeList.iterator();
            while (it.hasNext()) {
                BaseEnumBo bo = (BaseEnumBo) it.next();
                if (WarehouseOutTypeEnum.WBXS.name().equals(bo.getName()) || WarehouseOutTypeEnum.GDCK.name().equals(bo.getName())) {
                    it.remove();
                }
            }
        }
        return Result.wrapSuccessfulResult(outTypeList);
    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return ModuleUrlEnum.WAREHOUSE.getModuleUrl();
    }

    /**
     * 根据车辆查询对应的退货记录
     *
     * @param customerCarId
     * @return
     */
    @RequestMapping(value = "out-refund/info", method = RequestMethod.GET)
    @ResponseBody
    public Result outRefundInfo(@RequestParam("customerCarId") Long customerCarId,
                                @PageableDefault(value = 5, page = 1) Pageable pageable) {
        Long shopId = UserUtils.getShopIdForSession(request);
        PageRequest pageRequest = pageTranslate(pageable, null);
        DefaultPage<WarehouseOutRefundVo> defaultPage = warehouseOutFacade.selectOutRefund(customerCarId, shopId, pageRequest);
        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * 删除作废出库单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestParam("id") Long id) {
        Long shopId = UserUtils.getShopIdForSession(request);
        warehouseOutFacade.delete(shopId, id);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 出库详情页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/out-detail", method = RequestMethod.GET)
    public String detail(@RequestParam(value = "id") Long id, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (id == null) {
            return "common/error";
        }
        WarehouseOutVo warehouseOutVo = warehouseOutFacade.select(id, shopId);
        model.addAttribute("warehouseOut", warehouseOutVo);
        return "yqx/page/warehouse/out/out-detail";
    }

    /**
     * 其他出库页面初始化
     *
     * @param model
     * @param goodsIds 商品ids
     * @return
     */
    @RequestMapping(value = "/out-other", method = RequestMethod.GET)
    public String outOther(Model model, @RequestParam(value = "goodsIds", required = false) String goodsIds) {
        List<Long> ids = GoodsUtils.idsToList(goodsIds);
        if (!CollectionUtils.isEmpty(ids)) {
            List<Goods> goodsList = goodsService.selectByIds(ids.toArray(new Long[ids.size()]));
            model.addAttribute(goodsList);
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        String sn = warehouseOutFacade.getSn(shopId, "LC");
        model.addAttribute("warehouseOutSn", sn);
        model.addAttribute("operatorName", userInfo.getName());
        return "yqx/page/warehouse/out/out-other";
    }

    /**
     * 保存出库
     *
     * @param warehouseOutBo           出库单
     * @param warehouseOutDetailBoList 出库单详情
     * @return
     */
    @RequestMapping(value = "/stock/out", method = RequestMethod.POST)
    @ResponseBody
    public Result stockOut(@RequestParam("warehouseOutBo") String warehouseOutBo, @RequestParam("warehouseOutDetailBoList") String warehouseOutDetailBoList) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseOut warehouseOut = translateWarehouseOut(warehouseOutBo);
        List<WarehouseOutDetail> detailList = translateDetail(warehouseOutDetailBoList);
        String info = checkData(warehouseOut, detailList);
        if (info != null) {
            return Result.wrapErrorResult("", info);
        }
        Long id = warehouseOutFacade.stockOut(warehouseOut, detailList, userInfo);
        return Result.wrapSuccessfulResult(id);
    }

    /**
     * 出库单列表页面跳转
     *
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "/out-list", method = RequestMethod.GET)
    public String outList() {
        return "yqx/page/warehouse/out/out-list";
    }


    /**
     * 出库单列表数据初始化
     *
     * @param type     export 导出(暂无此功能) list 数据
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/out-list/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object outList(@PathVariable("type") String type, Model model, HttpServletResponse response,
                          @PageableDefault(value = 1, size = 6, page = 1, sort = "id desc", direction = Sort.Direction.DESC) Pageable pageable) {
        long time = System.currentTimeMillis();
        Map<String, Object> searchParam = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        //处理查询条件
        LegendWarehouseOutRequest param = makeParam(searchParam, userInfo.getShopId());
        //处理分页
        PageableRequest page = pageTranslate(pageable, type);
        DefaultPage<LegendWarehouseOutDTOVo> defaultPage;
        defaultPage = warehouseOutFacade.select(param, page, userInfo.getShopId());
        //导出
        if ("export".equals(type)) {
            String path = "yqx/page/warehouse/out/out-list-export";
            String fileName = "出库单";
            //完善导出信息
            model.addAttribute("startTime", searchParam.get("startTime"));
            model.addAttribute("endTime", searchParam.get("endTime"));
            return ExcelExportUtil.export(defaultPage.getContent(), response, path, fileName, time);
        }
        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * 出库退货页面初始化
     *
     * @return
     */
    @RequestMapping(value = "/out-refund", method = RequestMethod.GET)
    public String refund(@RequestParam("id") Long id, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (id == null) {
            return "common/error";
        }
        WarehouseOutVo warehouseOutVo = warehouseOutFacade.select(id, userInfo.getShopId());
        if (!WarehouseOutStatusEnum.LZCK.name().equals(warehouseOutVo.getStatus())) {
            return "common/error";
        }
        model.addAttribute("warehouseOutSn", warehouseOutFacade.getSn(userInfo.getShopId(), "HC"));
        model.addAttribute("warehouseOut", warehouseOutVo);
        return "yqx/page/warehouse/out/out-refund";
    }

    /**
     * 出库退货保存
     *
     * @return
     */
    @RequestMapping(value = "/out-refund/save", method = RequestMethod.POST)
    @ResponseBody
    public Result refund(@RequestParam("warehouseOutBo") String
                                 warehouseOutBo, @RequestParam("warehouseOutDetailBoList") String warehouseOutDetailBoList, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WarehouseOut warehouseOut = translateWarehouseOut(warehouseOutBo);
        List<WarehouseOutDetail> detailList = translateDetail(warehouseOutDetailBoList);
        //校验数据
        String info = checkData(warehouseOut, detailList);
        if (info != null) {
            return Result.wrapErrorResult("", info);
        }
        try {
            Long id = warehouseOutFacade.stockOutRefund(warehouseOut, detailList, userInfo);
            return Result.wrapSuccessfulResult(id);
        } catch (BizException e) {
            log.error("退货错误原因:", e);
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e1) {
            log.error("退货错误原因:", e1);
            return Result.wrapErrorResult("", "退货失败");
        }
    }

    /**
     * 批量更新出库单(暂时只是更新销售价)
     *
     * @param detailList
     * @return
     */
    @RequestMapping(value = "/out-detail/update", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> updateDetailPrice(@RequestBody List<WarehouseOutDetail> detailList) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        warehouseOutFacade.batchUpdateWarehouseDetail(detailList, userInfo);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 出库单作废
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "abolish", method = RequestMethod.POST)
    public Result abolish(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        warehouseOutFacade.abolishStockOut(id, userInfo);
        return Result.wrapSuccessfulResult(id);
    }

    /**
     * 出库单打印
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/out-print", method = RequestMethod.GET)
    public String print(@RequestParam("id") Long id, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Shop shop = shopService.selectById(userInfo.getShopId());
        model.addAttribute("shop", shop);
        WarehouseOutVo warehouseOutVo = warehouseOutFacade.select(id, userInfo.getShopId());
        if (warehouseOutVo == null) {
            return "common/error";
        }
        BigDecimal totalCount = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(warehouseOutVo.getDetailVoList())) {
            return "common/error";
        }
        for (WarehouseOutDetailVo detailVO : warehouseOutVo.getDetailVoList()) {
            totalCount = totalCount.add(detailVO.getGoodsCount());
        }
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("saleAmount", UpperNumbers.toChinese(warehouseOutVo.getSaleAmount().doubleValue() + ""));
        model.addAttribute("warehouseOut", warehouseOutVo);

        return "yqx/page/warehouse/out/out-print";
    }


    /**
     * 工单报价列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/order-quote-list", method = RequestMethod.GET)
    public String orderQuoteList() {
        return "yqx/page/warehouse/out/order-quote-list";
    }

    /**
     * 工单报价列表初始化
     *
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/order-quote-list/list", method = RequestMethod.GET)
    @ResponseBody
    public Result orderQuoteListData(HttpServletRequest request,
                                     @PageableDefault(page = 0, value = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        LegendOrderRequest orderRequest = makeOrderParam(searchParams);
        PageableRequest pageableRequest = pageTranslate(pageable, null);
        DefaultPage<SearchOrderVo> searchPage = warehouseOutFacade.selectOrderQuoteList(orderRequest, pageableRequest);
        searchPage.setPageUri(request.getRequestURI());
        searchPage.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(searchPage);
    }

    /**
     * 工单报价详情页面
     *
     * @param model
     * @param orderId 工单ID
     * @param refer   来源 {quote:报价列表;outbound:工单出库列表}
     * @return
     */
    @RequestMapping(value = "/order-quote-detail", method = RequestMethod.GET)
    public String orderQuoteDetail(@RequestParam(value = "orderId", required = true) Long orderId, Model model,
                                   @RequestParam(value = "refer", required = false) String refer) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return "common/error";
        }
        model.addAttribute("orderInfo", orderInfoOptional.get());
        //该工单服务项目
        List<OrderServices> orderServiceList =
                orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC);
        // 该工单实开物料
        List<OrderGoods> allOrderGoodList =
                orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        //该工单已出库物料
        List<WarehouseOutDetail> detailList = warehouseOutFacade.selectByOrderIdAndShopId(orderId, shopId);
        if (!CollectionUtils.isEmpty(detailList)) {
            Map<Long, BigDecimal> map = new HashMap<>();
            for (WarehouseOutDetail detail : detailList) {
                map.put(detail.getOrderGoodsId(), detail.getGoodsRealCount());
            }
            for (OrderGoods orderGoods : allOrderGoodList) {
                if (map.containsKey(orderGoods.getId())) {
                    orderGoods.setOutNumber(map.get(orderGoods.getId()));
                }
            }
        }

        for (OrderServices orderServices : orderServiceList) {
            String workerIdsStr = orderServices.getWorkerIds();
            String workName = getWorkName(workerIdsStr);
            orderServices.setWorkerName(workName);
        }

        model.addAttribute("orderServicesList", orderServiceList);
        model.addAttribute("orderGoodList", allOrderGoodList);
        model.addAttribute("refer", refer);
        return "yqx/page/warehouse/out/order-quote-detail";
    }

    /**
     * update orderInfo
     *
     * @param orderFormEntityBo 工单实体
     * @return
     * @see OrderFormEntityBo
     */
    @RequestMapping(value = "order-quote-detail/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(OrderFormEntityBo orderFormEntityBo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        if (orderInfo == null || orderInfo.getId() == null) {
            return Result.wrapErrorResult("", "工单信息有误,保存失败");
        }
        if (StringUtils.isBlank(orderInfo.getCarLicense())) {
            return Result.wrapErrorResult("", "车牌不能为空");
        }

        // 校验工单状态
        String orderStatus = orderInfo.getOrderStatus();
        if (StringUtils.isNotEmpty(orderStatus)) {
            OrderStatusEnum statusEnumOptional = OrderStatusEnum.getOrderStatusEnum(orderStatus);
            if (statusEnumOptional == null) {
                return Result.wrapErrorResult("", "非法的工单状态");
            }
        }
        try {
            return orderService.updateOrderOfWareHouse(orderFormEntityBo, userInfo);
        } catch (BusinessCheckedException e1) {
            log.error("编辑工单失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "编辑工单失败!<br/>" + e1.getMessage());

        } catch (RuntimeException e2) {
            log.error("编辑工单异常", e2);
            return Result.wrapErrorResult("", "编辑工单失败!");
        }
    }

    /**
     * 新版打印报价单
     *
     * @param model
     * @param orderId 工单ID
     * @param request
     * @return
     */
    @RequestMapping(value = "shop_pricing_print", method = RequestMethod.GET)
    public String shopPricingPrint(@RequestParam(value = "orderId", required = true) Long orderId,
                                   Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        //获取打印设置
        ShopPrintConfig printConfig = printConfigService.checkOpenStatus(PrintTemplateEnum.QUOTE.getCode(), request);
        //没有可供打印的单据
        if (null == printConfig) {
            return "redirect:/shop/order/order-list";
        }
        //设置大小字体
        model.addAttribute("fontStyle", printConfig.getFontStyle());
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            log.error("[报价单打印]工单报价单打印失败 [原因]对应的店铺不存在 店铺ID:{}", shopId);
            return "common/error";
        }
        String printLog = OrderOperationLog.getOrderPrintLog("报价单打印", shop);
        log.info(printLog);
        model.addAttribute("shop", shop);

        // 工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            log.error("[报价单打印]工单报价单打印失败 [原因]对应的工单不存在 工单ID:{}", orderId);
            return "yqx/page/warehouse/out/pricing_print";
        }
        OrderInfo orderInfo = orderInfoOptional.get();

        // 判断是否跨门店访问
        if (!shopId.equals(orderInfo.getShopId())) {
            log.error("[报价单打印]工单报价单打印失败 [原因]跨店访问 店铺ID:{}", shopId);
            return "common/error";
        }
        //set orderType
        Optional<OrderType> orderTypeOpt = orderTypeService.getOrderType(orderInfo.getOrderType());
        if (orderTypeOpt.isPresent()) {
            orderInfo.setOrderTypeName(orderTypeOpt.get().getName());
        }
        //set company
        Customer customer = customerService.selectById(orderInfo.getCustomerId());
        if (customer != null) {
            orderInfo.setCompany(customer.getCompany());
        }
        // 获取实开物料
        List<OrderGoods> realOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
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

        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("orderServicesList1", orderServicesVos);
        model.addAttribute("orderServicesList2", additionalOrderServiceList);
        model.addAttribute("orderGoodsList", realOrderGoodsList);

        //打印自定义设置
        printFacade.printSelfConfig(model, shopId);

        return "yqx/page/warehouse/out/print/pricing_print";
    }


    /**
     * 打印报价单
     *
     * @param model
     * @param orderId 工单ID
     * @param request
     * @return
     */
    @RequestMapping(value = "pricing_print", method = RequestMethod.GET)
    public String quotationPrint(@RequestParam(value = "id") Long orderId,
                                 Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            log.error("[报价单打印]工单报价单打印失败 [原因]对应的店铺不存在 店铺ID:{}", shopId);
            return "common/error";
        }
        model.addAttribute("shop", shop);

        // 工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            log.error("[报价单打印]工单报价单打印失败 [原因]对应的工单不存在 工单ID:{}", orderId);
            return "yqx/page/warehouse/out/pricing_print";
        }
        OrderInfo orderInfo = orderInfoOptional.get();

        // 判断是否跨门店访问
        if (!shopId.equals(orderInfo.getShopId())) {
            log.error("[报价单打印]工单报价单打印失败 [原因]跨店访问 店铺ID:{}", shopId);
            return "common/error";
        }
        //获取工单类型名称
        OrderType orderType = orderTypeService.selectByIdAndShopId(shopId, orderInfo.getOrderType());
        if (orderType != null) {
            orderInfo.setOrderTypeName(orderType.getName());
        }
        // 获取实开物料
        List<OrderGoods> realOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        // 物料总金额
        BigDecimal realGoodsAmount = BigDecimal.ZERO;
        //折扣总金额
        BigDecimal realGoodsDiscount = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(realOrderGoodsList)) {
            for (OrderGoods realOrderGoods : realOrderGoodsList) {
                BigDecimal goodsAmount = realOrderGoods.getGoodsAmount();
                goodsAmount = (goodsAmount == null) ? BigDecimal.ZERO : goodsAmount;
                BigDecimal discount = realOrderGoods.getDiscount();
                discount = (discount == null) ? BigDecimal.ZERO : discount;
                realGoodsAmount = realGoodsAmount.add(goodsAmount);
                realGoodsDiscount = realGoodsDiscount.add(discount);
            }
        }
        orderInfo.setGoodsAmount(realGoodsAmount);
        orderInfo.setGoodsDiscount(realGoodsDiscount);

        // 获取实开服务
        List<OrderServices> basicOrderServiceList =
                orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC);
        for (OrderServices orderServices : basicOrderServiceList) {
            String workerIds = orderServices.getWorkerIds();
            String workName = getWorkName(workerIds);
            orderServices.setWorkerName(workName);
        }
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("orderServicesList1", basicOrderServiceList);
        model.addAttribute("orderGoodsList", realOrderGoodsList);
        return "yqx/page/warehouse/out/pricing_print";
    }

    /**
     * 出库工单列表
     *
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "/order-out-list", method = RequestMethod.GET)
    public String orderOutList() {
        return "yqx/page/warehouse/out/order-out-list";
    }

    /**
     * 工单出库列表数据初始化
     *
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping(value = "order-out-list/list", method = RequestMethod.GET)
    @ResponseBody
    public Result orderOutListData(HttpServletRequest request,
                                   @PageableDefault(page = 0, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        LegendOrderRequest orderRequest = makeOrderParam(searchParams);
        PageableRequest pageableRequest = pageTranslate(pageable, null);
        DefaultPage<SearchOrderVo> searchPage = warehouseOutFacade.selectOrderOutList(orderRequest, pageableRequest);
        searchPage.setPageUri(request.getRequestURI());
        searchPage.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(searchPage);
    }

    /**
     * 工单出库单详情页
     *
     * @param model
     * @param orderId 工单ID
     * @return
     */
    @RequestMapping(value = "/order-out-detail", method = RequestMethod.GET)
    public String outbound(@RequestParam(value = "orderId", required = true) Long orderId, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();

        // get orderInfo
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return "common/error";
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        if (!orderInfo.getShopId().equals(shopId)) {
            return "common/error";
        }
        String orderStatus = orderInfo.getOrderStatus();
        if (!(StringUtils.equals(orderStatus, OrderStatusEnum.DDWC.getKey())
                || StringUtils.equals(orderStatus, OrderStatusEnum.DDYFK.getKey()))) {
            model.addAttribute("orderInfoFlag", true);
        }
        List<OrderGoods> orderGoodsList = warehouseOutFacade.selectTraceOrderGoods(orderId, shopId);

        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("orderGoodsList", orderGoodsList);
        Optional<Customer> customerOptional = customerService.getCustomer(orderInfo.getCustomerCarId(), shopId);
        if (customerOptional.isPresent()) {
            model.addAttribute("customer", customerOptional.get());
        }
        model.addAttribute("warehouseOutSn", warehouseOutFacade.getSn(shopId, Constants.BLUE_OUT_WARHOUSE));
        return "yqx/page/warehouse/out/order-out-detail";
    }

    /**
     * 工单物料是否缺货
     *
     * @param request
     * @param orderId
     * @return {true:缺货;false：不缺货}
     */
    @RequestMapping(value = "/isstockout", method = RequestMethod.GET)
    @ResponseBody
    public Result isStockOut(HttpServletRequest request,
                             @RequestParam(value = "orderid") Long orderId) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        // 是否缺货
        boolean isStockOut;
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            log.error("[校验工单库存]检验工单库存失败，工单不存在，工单ID:{}", orderId);
            return Result.wrapErrorResult("", "检验工单库存失败，工单不存在");
        }
        OrderInfo orderInfo = orderInfoOptional.get();

        // checke is stockout
        isStockOut = warehouseOutService.isStockOut(orderInfo, userInfo);

        return Result.wrapSuccessfulResult(isStockOut);
    }
    //转换出库单

    private WarehouseOut translateWarehouseOut(String warehouseOut) {
        WarehouseOut warehouse = new Gson().fromJson(warehouseOut, new TypeToken<WarehouseOut>() {
        }.getType());
        warehouse.setGmtCreate(DateUtil.convertStringToDate1(warehouse.getGmtCreateStr()));
        return warehouse;
    }

    //转换入库单详情
    private List<WarehouseOutDetail> translateDetail(String detail) {
        List<WarehouseOutDetail> detailList = new Gson().fromJson(detail, new TypeToken<List<WarehouseOutDetail>>() {
        }.getType());
        return detailList;
    }

    //保存数据校验
    private String checkData(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList) {
        if (CollectionUtils.isEmpty(detailList)) {
            return "物料不存在,操作失败";
        }
        if (warehouseOut == null) {
            return "参数不能为空";
        }

        if (warehouseOut.getGoodsReceiver() == null) {
            return "领料人不能为空";
        }
        if (!StringUtil.isNull(warehouseOut.getCustomerMobile()) && !StringUtil.isPhone(warehouseOut.getCustomerMobile()) && !StringUtil.isMobileNO(warehouseOut.getCustomerMobile())) {
            return "号码格式有误";
        }
        return null;
    }

    /**
     * 组装出库单查询条件
     *
     * @param searchParam
     * @param shopId
     * @return
     */
    private LegendWarehouseOutRequest makeParam(Map<String, Object> searchParam, Long shopId) {
        LegendWarehouseOutRequest param = new LegendWarehouseOutRequest();
        param.setShopId(shopId.intValue());
        if (searchParam.containsKey("endTime")) {
            param.setEndTime(searchParam.get("endTime") + " 23:59:59");
        }
        if (searchParam.containsKey("startTime")) {
            param.setStartTime(searchParam.get("startTime") + " 00:00:00");
        }
        if (searchParam.containsKey("warehouseOutSn")) {
            param.setWarehouseOutSn(searchParam.get("warehouseOutSn").toString());
        }
        if (searchParam.containsKey("conditionLike")) {
            param.setGoodsLike(searchParam.get("conditionLike").toString());
        }
        if (searchParam.containsKey("keyword")) {
            param.setNumLike(searchParam.get("keyword").toString());
        }
        if (searchParam.containsKey("goodsReceiver")) {
            param.setGoodsReceiver(searchParam.get("goodsReceiver").toString());
        }
        List<String> statusList = Lists.newArrayList();
        if (searchParam.containsKey("status")) {
            statusList.add(searchParam.get("status").toString());
        }
        param.setStatusList(statusList);
        if (searchParam.containsKey("carInfo")) {
            param.setCarInfo(searchParam.get("carInfo").toString());
        }
        return param;
    }

    /**
     * 组装工单查询条件
     *
     * @param searchParams
     * @return
     */
    private LegendOrderRequest makeOrderParam(Map<String, Object> searchParams) {
        LegendOrderRequest orderRequest = new LegendOrderRequest();
        orderRequest.setShopId(searchParams.get("shopId").toString());
        if (searchParams.containsKey("startTime")) {
            orderRequest.setStartTime(searchParams.get("startTime") + " 00:00:00");
        }
        if (searchParams.containsKey("endTime")) {
            orderRequest.setEndTime(searchParams.get("endTime") + " 23:59:59");
        }
        // 车牌|车主电话|工单编号
        if (searchParams.containsKey("keyword")) {
            orderRequest.setLikeKeyWords(searchParams.get("keyword") + "");
        }
        // 服务顾问
        if (searchParams.containsKey("receiver")) {
            orderRequest.setReceiver(searchParams.get("receiver") + "");
        }
        // 工单类别
        if (searchParams.containsKey("orderTag")) {
            Object orderTag = searchParams.get("orderTag");
            orderRequest.setOrderTag(Arrays.asList(Integer.valueOf(orderTag + "")));
        }
        // hasOut{1:表示已领料;0:未领料}
        if (searchParams.containsKey("hasOut")) {
            orderRequest.setHasOut(Integer.valueOf(searchParams.get("hasOut") + ""));
        }
        return orderRequest;
    }

    /**
     * 出入库列表分页
     */
    public PageableRequest pageTranslate(Pageable pageable, String type) {
        int pageNumber = pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber() - 1;
        PageableRequest page = new PageableRequest(pageNumber, pageable.getPageSize(), Sort.Direction.DESC, "id");
        if ("export".equals(type)) {
            page = new PageableRequest(0, 1000, Sort.Direction.DESC, "id");
        }

        return page;
    }

    /**
     * 获取多个维修工
     *
     * @param workerIdsStr
     * @return
     */
    private String getWorkName(String workerIdsStr) {
        StringBuffer workName = new StringBuffer("");
        if (StringUtils.isNotBlank(workerIdsStr) && !"0".equals(workerIdsStr)) {
            String[] workIdArr = workerIdsStr.split(",");
            if (ArrayUtils.isNotEmpty(workIdArr)) {
                Long[] workerIds = new Long[workIdArr.length];
                for (int i = 0; i < workerIds.length; i++) {
                    workerIds[i] = Long.valueOf(workIdArr[i].trim());
                }
                List<ShopManager> shopManagerList = shopManagerService.selectByIds(workerIds);
                if (!CollectionUtils.isEmpty(shopManagerList)) {
                    for (ShopManager shopManager : shopManagerList) {
                        workName.append(shopManager.getName() + ",");
                    }
                    return workName.substring(0, workName.length() - 1);
                }
            }
        }
        return workName.toString();
    }

}