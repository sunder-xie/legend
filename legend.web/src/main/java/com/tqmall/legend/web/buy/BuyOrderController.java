package com.tqmall.legend.web.buy;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.warehousein.WarehouseInService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.buy.LegendTradeStatus;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsQueryParam;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.web.buy.vo.LegendOrderDTOVO;
import com.tqmall.legend.web.buy.vo.LegendOrderGoodsDTOVO;
import com.tqmall.legend.web.buy.vo.OrderGoodDTOVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.tqmallstall.domain.param.LegendQueryParam;
import com.tqmall.tqmallstall.domain.param.goods.GoodsListQueryRequest;
import com.tqmall.tqmallstall.domain.param.refund.RefundParam;
import com.tqmall.tqmallstall.domain.result.Legend.LegendOrderDTO;
import com.tqmall.tqmallstall.domain.result.Legend.LegendOrderGoodsDTO;
import com.tqmall.tqmallstall.domain.result.OrderGoodDTO;
import com.tqmall.tqmallstall.domain.result.goods.GoodsListForSearchBaseDTO;
import com.tqmall.tqmallstall.domain.result.goods.GoodsListForSearchDTO;
import com.tqmall.tqmallstall.service.OrderRemoteService;
import com.tqmall.tqmallstall.service.refund.ExchangeOrderService;
import com.tqmall.tqmallstall.service.tc.OrderService;
import com.tqmall.venus.domain.base.VenusRequestContext;
import com.tqmall.venus.service.lifecycle.OrderLifeCycleService;
import com.tqmall.venus.service.lifecycle.param.KhqsOrderRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.tqmall.wheel.lang.Langs.*;

/**
 * Created by lixiao on 14-11-20.
 */
@Controller
@RequestMapping("shop/buy")
public class BuyOrderController extends BaseController {

    Logger logger = LoggerFactory.getLogger(BuyOrderController.class);

    @Autowired
    private OrderRemoteService orderRemoteService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private WarehouseInService warehouseInService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private com.tqmall.tqmallstall.service.goods.GoodsService goodsInfoService;
    @Autowired
    private ExchangeOrderService exchangeOrderService;
    @Autowired
    private OrderLifeCycleService orderLifeCycleService;


    @RequestMapping("")
    public String orderList(Model model, HttpServletRequest request) {
        //采购订单专享金   FINANCEHD
        //采购金订单    YXCGJHD
        //福斯机油账期订单   FUCHSHDYXDD
        String orderFlag = request.getParameter("orderFlag");
        model.addAttribute("moduleUrl", "buy");
        model.addAttribute("buyTab", "order_list");
        model.addAttribute("search_orderFlag", orderFlag);

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Shop shop = shopService.selectById(userInfo.getShopId());
        Long shopId = shop.getId();
        try {
            com.tqmall.core.common.entity.Result<Map<String, Integer>> result = orderRemoteService
                    .countOrderByTradeStatusByUserGlobalId(shop.getUserGlobalId(), orderFlag);
            if (result != null && result.isSuccess() && result.getData() != null) {
                Map<String, Integer> map = result.getData();
                logger.info("map参数:{}", map);
                logger.info("全部订单数据:{}", map.get("全部订单"));
                List<LegendTradeStatus> list = new ArrayList<>();
                list.add(new LegendTradeStatus("GXD", "刚下单", map.get("刚下单")));
                list.add(new LegendTradeStatus("PHZ", "配货中", map.get("配货中")));
                list.add(new LegendTradeStatus("YFH", "已发货", map.get("已发货")));
                list.add(new LegendTradeStatus("YQS", "已签收", map.get("已签收")));
                list.add(new LegendTradeStatus("YFK", "已付款", map.get("已付款")));
                list.add(new LegendTradeStatus("WXDD", "取消订单", map.get("无效工单")));
                list.add(new LegendTradeStatus("ALL", "全部订单", map.get("全部订单")));

                model.addAttribute("legendTradeStatusList", list);
            } else {
                logger.error("调用接口countOrderByTradeStatusByUserGlobalId失败,{}", LogUtils.funToString(shop, result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());

        }

        return "yqx/page/buy/order/order-list";

    }


    //获取批量入库
    @RequestMapping("batch_insert_list2")
    @ResponseBody
    public Result<List<OrderGoodDTOVO>> getBatchInsertList(String orderIds) {
        String flag = (String) request.getAttribute(Constants.BPSHARE);
        com.tqmall.core.common.entity.Result<List<OrderGoodDTO>> result = orderRemoteService.queryOrderGoodsList(orderIds);
        if (result.getData() == null) {
            return Result.wrapErrorResult("", "该订单无内容....");
        }

        List<OrderGoodDTO> data = result.getData();
        List<OrderGoodDTOVO> temp_list = new ArrayList<OrderGoodDTOVO>();
        for (OrderGoodDTO item : data) {
            OrderGoodDTOVO temp_item = new OrderGoodDTOVO();
            if (item.getGoodsThumb() == null) {
                item.setGoodsThumb("");
            }
            BeanUtils.copyProperties(item, temp_item);
            temp_item.setGoodsResult(this.getGoodsByTqmallGoodsId(item.getGoodsId().longValue(), request));
            if ((boolean) temp_item.getGoodsResult().getData() == false) {
                //获取brandName
                GoodsListQueryRequest requestParam = new GoodsListQueryRequest();
                requestParam.setGoodsId(item.getGoodsId().toString());
                com.tqmall.core.common.entity.Result<GoodsListForSearchDTO> goodsInfo = null;
                if ("true".equals(flag)) {
                    goodsInfo = goodsInfoService.queryGoods4Spray(requestParam);
                } else {
                    goodsInfo = goodsInfoService.queryGoodsListBySearch4Legend(requestParam);
                }

                GoodsListForSearchDTO goodsListForSearchDTO = goodsInfo.getData();
                if (goodsListForSearchDTO.getGoods() != null && goodsListForSearchDTO.getGoods().size() != 1) {
                    GoodsListForSearchBaseDTO.GoodsListDTO goodInfo = goodsListForSearchDTO.getGoods().get(0);
                    temp_item.setBrandName(goodInfo.getBrandName());
                }
                // GoodsListForSearchBaseDTO goodInfo= null;
                temp_list.add(temp_item);
            }
        }
        if (temp_list.size() >= 0)
            //说明有未入库的商品
            return Result.wrapSuccessfulResult(temp_list);
        else {
            //说明没有入库的商品
            return Result.wrapSuccessfulResult(null);
        }

    }


    //获取批量入库
    @RequestMapping("batch_insert_list")
    @ResponseBody
    public Result<List<GoodsListForSearchBaseDTO.GoodsListDTO>> getBatchInsertList2(String orderIds) {
        String flag = (String) request.getAttribute(Constants.BPSHARE);
        com.tqmall.core.common.entity.Result<List<OrderGoodDTO>> result = orderRemoteService.queryOrderGoodsList(orderIds);
        if (result == null || !result.isSuccess() || result.getData() == null) {

            logger.error("从档口获取商品信息失败,Param:{}, result:{}", LogUtils.objectToString(orderIds), LogUtils.wraperResult(result));
            return Result.wrapErrorResult("", "该订单无内容，或者iserver调用异常....");
        }
        List<OrderGoodDTO> data = result.getData();
        List<GoodsListForSearchBaseDTO.GoodsListDTO> temp_list = new ArrayList<GoodsListForSearchBaseDTO.GoodsListDTO>();
        for (OrderGoodDTO item : data) {
            //判断是否已经添加入库
            if ((boolean) this.getGoodsByTqmallGoodsId(item.getGoodsId().longValue(), request).getData() == false) {
                GoodsListQueryRequest requestParam = new GoodsListQueryRequest();
                requestParam.setGoodsId(item.getGoodsId().toString());
                requestParam.setSource("legend");
                requestParam.setOnSale("ALL");
                requestParam.setIsDelete("ALL");
                com.tqmall.core.common.entity.Result<GoodsListForSearchDTO> goodsInfo = null;
                if ("true".equals(flag)) {
                    goodsInfo = goodsInfoService.queryGoods4Spray(requestParam);
                } else {
                    goodsInfo = goodsInfoService.queryGoodsListBySearch4Legend(requestParam);
                }

                if (goodsInfo == null || !goodsInfo.isSuccess() || goodsInfo.getData() == null) {
                    logger.error("从档口获取商品信息失败接口是 queryGoodsListBySearch4Legend,Param:{}, result:{}", LogUtils.objectToString(requestParam), LogUtils.wraperResult(goodsInfo));
                    return Result.wrapErrorResult("", "没有从CRM获取到配件信息，或者iserver调用异常....");
                }
                GoodsListForSearchDTO goodsListForSearchDTO = goodsInfo.getData();

//    		 if(goodsListForSearchDTO == null)
//    		 {
//    		  return  Result.wrapErrorResult("", "");
//    		 }

                if (goodsListForSearchDTO.getGoods() != null && goodsListForSearchDTO.getGoods().size() == 1) {
                    GoodsListForSearchBaseDTO.GoodsListDTO goodInfo = goodsListForSearchDTO.getGoods().get(0);
                    temp_list.add(goodInfo);
                }

            }
        }

        //z组转返回的信息 先得到shopId
        Long shopId = UserUtils.getShopIdForSession(request);
        for (GoodsListForSearchBaseDTO.GoodsListDTO good : temp_list) {
            String[] goodsString = {good.getGoodsSn()};
            String goodsName = good.getGoodsName();
            String goodsFormat = good.getGoodsFormat();

            List<Goods> goodsL = goodsService.selectByTqmallGoods(goodsString, goodsName, goodsFormat, shopId);
            if (!goodsL.isEmpty()) {
                good.setPrice(goodsL.get(0).getPrice());
                if (!StringUtils.isBlank(goodsL.get(0).getDepot())) {
                    good.setCat2Name(goodsL.get(0).getDepot());
                }
                good.setGoodsId(Integer.valueOf(String.valueOf(goodsL.get(0).getId())));
            }
        }
        if (temp_list.size() >= 0) {
            /**
             * 说明有未入库的商品
             * 对temp_list去重
             */
            List<Integer> list = new ArrayList<>();
            Iterator<GoodsListForSearchBaseDTO.GoodsListDTO> ite = temp_list.iterator();
            while (ite.hasNext()) {
                GoodsListForSearchBaseDTO.GoodsListDTO temp = ite.next();
                Integer goodsId = temp.getGoodsId();
                if (list.contains(goodsId)) {
                    ite.remove();
                } else {
                    list.add(goodsId);
                }
            }
            return Result.wrapSuccessfulResult(temp_list);
        } else {
            //说明没有入库的商品
            return Result.wrapSuccessfulResult(null);
        }

    }


    //add by twg start 2015-10-20
    @RequestMapping("my_order")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<PagingResult<LegendOrderDTO>> myOrder(String status,
                                                                                      @RequestParam(value = "start", required = false) Integer start,
                                                                                      @RequestParam(value = "limit", required = false) Integer limit,
                                                                                      HttpServletRequest request, String orderFlag) {
        if (start == null) {
            start = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Shop shop = shopService.selectById(userInfo.getShopId());
        String mobile = shop.getMobile();
        // mobile = "15088618954";
        // status = "ALL";
        com.tqmall.core.common.entity.Result<PagingResult<LegendOrderDTO>> result = null;
        logger.info("淘汽采购获取订单数据,参数:userGlobalId={}, status={}, start={}, limit={}, orderFlag={}",
                shop.getUserGlobalId(), status, start, limit, orderFlag);
        Long sTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(orderFlag)) {
            result = orderRemoteService.getOrderListDetail(shop.getUserGlobalId(), status, start, limit);
            if (logger.isInfoEnabled()) {
                logger.info("orderRemoteService#getOrderListDetail耗时[{}]毫秒", System.currentTimeMillis() - sTime);
            }
        } else if ("FINANCEHD".equals(orderFlag)) {
            //金融订单
            LegendQueryParam legendQueryParam = new LegendQueryParam();
            legendQueryParam.setShopId(Integer.valueOf(shop.getUserGlobalId()));
            legendQueryParam.setStatus(status);
            legendQueryParam.setOrderFlags(orderFlag);
            legendQueryParam.setStart(start);
            legendQueryParam.setLimit(limit);
            result = orderRemoteService.getLegendOrderList(legendQueryParam);
            if (logger.isInfoEnabled()) {
                logger.info("orderRemoteService#getLegendOrderList耗时[{}]毫秒", System.currentTimeMillis() - sTime);
            }
        } else if ("HHDD".equals(orderFlag)) {
            //换货订单
            RefundParam refundParam = new RefundParam();
            refundParam.setShopId(Integer.valueOf(shop.getUserGlobalId()));
            refundParam.setTradeStatus(status);
            refundParam.setStart(start);
            refundParam.setLimit(limit);
            result = exchangeOrderService.getExchangeOrderList(refundParam);
            if (logger.isInfoEnabled()) {
                logger.info("exchangeOrderService#getExchangeOrderList耗时[{}]毫秒", System.currentTimeMillis() - sTime);
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append("userGlobalId:").append(shop.getUserGlobalId())
                .append(",status:").append(status)
                .append(",start:").append(start)
                .append(",limit:").append(limit)
                .append(",orderFlag").append(orderFlag);

        if (result != null && result.isSuccess() && result.getData() != null) {
            logger.info("获取淘汽采购获取订单数据成功,Param:{},返回{}条记录", sb.toString(), result.getData().getList().size());
            return result;
        } else {
            logger.error("获取采购订单列表失败.{}", LogUtils.funToString(sb, result));
        }
        return result;

    }

    /**
     * 3表联合查询的订单列表ajax方法 （原方法未提供分页方法，分页未实现
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping("my_order_info")
    @ResponseBody
    public Object myOrderNg(
            @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        String userGlobalId = userInfo.getUserGlobalId();

        Integer pageLimit = pageable.getPageSize();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        String orderFlag = isNull(searchParams.get("orderFlag")) ? EMPTY_STRING : searchParams.get("orderFlag").toString();
        String status = searchParams.get("status").toString();
        int start = (pageable.getPageNumber() - 1) * pageLimit;
        int limit = pageLimit;
        /**
         * 从档口获取订单列表信息
         */
        com.tqmall.core.common.entity.Result<PagingResult<LegendOrderDTO>> result
                = getOrderListFromTqmall(userGlobalId, orderFlag, status, start, limit);

        if (result == null || !result.isSuccess() || result.getData() == null) {
            logger.error("调用接口获取订单信息异常{}", LogUtils.wraperResult(result));
            return Result.wrapErrorResult("", "订单列表无内容。");
        }

        /**
         * 分页信息组装
         */
        Integer total = result.getData().getTotal();
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber() - 1, pageLimit);
        List<LegendOrderDTO> rawOrderList = result.getData().getList();

        List<LegendOrderDTOVO> orderList = Lists.newArrayList();

        if (isNotEmpty(rawOrderList)) {
            /**
             * 获取本查询结果的订单sn列表
             */
            List<String> purchaseSnList = FluentIterable.from(rawOrderList).transform(new Function<LegendOrderDTO, String>() {
                @Override
                public String apply(LegendOrderDTO input) {
                    return input.getOrderSn();
                }
            }).toList();
            List<WarehouseIn> warehouseIn = this.warehouseInService.selectByPurchaseSnList(userInfo.getShopId(), purchaseSnList);
            /**
             * 已入库的订单号
             */
            Set<String> hasInWarehouseSn = FluentIterable.from(warehouseIn).transform(new Function<WarehouseIn, String>() {
                @Override
                public String apply(WarehouseIn input) {
                    return input.getPurchaseSn();
                }
            }).toSet();


            /**
             * 查询本页涉及到的商品id集合
             */
            Set<Long> goodsIdSet = FluentIterable.from(rawOrderList).transformAndConcat(new Function<LegendOrderDTO, List<Long>>() {
                @Override
                public List<Long> apply(LegendOrderDTO input) {
                    return FluentIterable.from(input.getGoodsList()).transform(new Function<LegendOrderGoodsDTO, Long>() {
                        @Override
                        public Long apply(LegendOrderGoodsDTO input) {
                            return input.getGoodsId().longValue();
                        }
                    }).toList();
                }
            }).toSet();
            List<Goods> goodsList = this.goodsService.findByTqmallGoodsIds(userInfo.getShopId(), goodsIdSet);
            /**
             * 已经在配件库中的配件id集合
             */
            Set<Long> hasInGoodsId = FluentIterable.from(goodsList).transform(new Function<Goods, Long>() {
                @Override
                public Long apply(Goods input) {
                    return input.getTqmallGoodsId();
                }
            }).toSet();

            for (LegendOrderDTO rawOrder : rawOrderList) {
                LegendOrderDTOVO order = new LegendOrderDTOVO();
                orderList.add(order);
                BeanUtils.copyProperties(rawOrder, order);
                /**
                 * 是否已入库
                 */
                if (hasInWarehouseSn.contains(rawOrder.getOrderSn())) {
                    order.setWarehouseResult(Result.wrapSuccessfulResult(1));
                } else {
                    order.setWarehouseResult(Result.wrapSuccessfulResult(0));
                }

                boolean flag = false;
                List<LegendOrderGoodsDTO> rGoodsList = Lists.newArrayList();
                if (isNotEmpty(rawOrder.getGoodsList())) {
                    for (LegendOrderGoodsDTO rawGoods : rawOrder.getGoodsList()) {
                        LegendOrderGoodsDTOVO goods = new LegendOrderGoodsDTOVO();
                        rGoodsList.add(goods);
                        BeanUtils.copyProperties(rawGoods, goods);
                        if (hasInGoodsId.contains(Long.valueOf(rawGoods.getGoodsId().longValue()))) {
                            goods.setGoodsResult(Result.wrapSuccessfulResult(true));
                        } else {
                            goods.setGoodsResult(Result.wrapSuccessfulResult(false));
                            flag = true;
                        }

                    }
                }
                //取消订单不显示批量入库
                if ("QXDD".equals(order.getTradeStats())) {
                    flag = false;
                }
                order.setShowBatchAdd(flag);
                order.setGoodsList(rGoodsList);
            }
        }

        DefaultPage defaultPage = new DefaultPage(orderList, pageRequest, total);
        defaultPage.setPageUri(request.getRequestURI());
        defaultPage.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(defaultPage);
    }

    private com.tqmall.core.common.entity.Result<PagingResult<LegendOrderDTO>> getOrderListFromTqmall(String userGlobalId, String orderFlag, String status, int start, int limit) {
        com.tqmall.core.common.entity.Result<PagingResult<LegendOrderDTO>> result = null;
        logger.info("淘汽采购获取订单数据,参数:userGlobalId={}, status={}, start={}, limit={}, orderFlag={}", userGlobalId, status, start, limit, orderFlag);
        if (StringUtils.isEmpty(orderFlag)) {
            result = orderRemoteService.getOrderListDetail(userGlobalId, status, start, limit);
        } else if ("FINANCEHD".equals(orderFlag)) {
            //金融订单
            LegendQueryParam legendQueryParam = new LegendQueryParam();
            legendQueryParam.setShopId(Integer.valueOf(userGlobalId));
            legendQueryParam.setStatus(status);
            legendQueryParam.setOrderFlags(orderFlag);
            legendQueryParam.setStart(start);
            legendQueryParam.setLimit(limit);
            result = orderRemoteService.getLegendOrderList(legendQueryParam);
        } else if ("HHDD".equals(orderFlag)) {
            //换货订单
            RefundParam refundParam = new RefundParam();
            refundParam.setShopId(Integer.valueOf(userGlobalId));
            refundParam.setTradeStatus(status);
            refundParam.setStart(start);
            refundParam.setLimit(limit);
            result = exchangeOrderService.getExchangeOrderList(refundParam);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("userGlobalId:").append(userGlobalId)
                .append(",status:").append(status)
                .append(",start:").append(start)
                .append(",limit:").append(limit)
                .append(",orderFlag").append(orderFlag);

        if (result != null && result.isSuccess() && result.getData() != null) {
            logger.info("获取淘汽采购获取订单数据成功,Param:{},返回{}条记录", sb.toString(), result.getData().getList().size());
        } else {
            logger.error("获取采购订单列表失败.{}", LogUtils.funToString(sb, result));
            return null;
        }
        return result;
    }
//end

    /**
     * 根据淘汽档口的orderSn查询是否入库
     *
     * @param orderSn
     * @param request
     * @return
     */
    @RequestMapping("get_warehouse_in_by_purchase_sn")
    @ResponseBody
    public Result getWarehouseInByPurchaseSn(String orderSn, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Map map = new HashMap();
        map.put("purchaseSn", orderSn);
        map.put("shopId", shopId);
        map.put("isDeleted", "N");
        List<WarehouseIn> list = warehouseInService.select(map);
        if (!CollectionUtils.isEmpty(list)) {
            return Result.wrapSuccessfulResult(list.size());
        } else {
            return Result.wrapSuccessfulResult(0);
        }
    }

    @RequestMapping("cancel_order")
    @ResponseBody
    public Result cancelOrder(Integer orderId, Integer userId, String mobile,
                              HttpServletRequest request) {

        com.tqmall.core.common.entity.Result<Void> result = this.orderService.cancelOrder(orderId, userId);
        if (result != null && result.isSuccess() && result.getData() != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("取消下单成功,返回:{}", LogUtils.wraperResult(result));
            }
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            logger.error("调用取消下单接口失败,{}", LogUtils.wraperResult(result));
            return Result.wrapErrorResult("9999", "取消下单失败");
        }
    }

    /**
     * 确认收货接口
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("confirm_revice")
    @ResponseBody
    public Result confirmPurchaseOrderRecive(Integer orderId, String status, String orderFlags,
                                             HttpServletRequest request) {
        if (!status.equals("XEJHJS")) {
            return Result.wrapErrorResult("", "工单状态不是订单拣货完成,无法确认收货");
        }
        if (null == orderId || 0 == orderId) {
            return Result.wrapErrorResult("", "工单id不能为空");
        }
        if (StringUtils.isEmpty(orderFlags) || !"SPRAY".equals(orderFlags)) {
            return Result.wrapErrorResult("", "非钣喷订单无法确认收货");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        KhqsOrderRequest khqsOrderRequest = new KhqsOrderRequest();
        khqsOrderRequest.setOrderId(orderId);
        khqsOrderRequest.setTradeLog(true);
        khqsOrderRequest.setTradeStatus("KHQS");
        khqsOrderRequest.setModifyDate(new Date());
        List<KhqsOrderRequest> list = Lists.newArrayList();
        list.add(khqsOrderRequest);
        VenusRequestContext context = new VenusRequestContext();
        context.setOperatorId(userInfo.getUserId().intValue());
        context.setOperatorName(userInfo.getName());
        context.setSystemName("LEGEND");

        com.tqmall.core.common.entity.Result<Integer> result = null;
        try {
            result = this.orderLifeCycleService.doKhqsOrder(list, context);
        } catch (Exception e) {
            logger.error("订单签收接口调用失败,{}", e);
            return Result.wrapErrorResult("", "确认签收失败");
        }
        if (null != result && result.isSuccess() && result.getData() == 1) {
            if (logger.isDebugEnabled()) {
                logger.debug("确认签收成功,返回成功数量:{}", LogUtils.objectToString(result.getData()));
            }
            return Result.wrapSuccessfulResult("订单签收成功!");
        } else {
            logger.error("确认签收工单失败,orderId:{}", orderId);
            return Result.wrapErrorResult("", "确认签收失败");
        }
    }

    @RequestMapping("goods/getGoodsByTqmallGoodsId")
    @ResponseBody
    public Result getGoodsByTqmallGoodsId(Long tqmallGoodsId, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        GoodsQueryParam queryParam = new GoodsQueryParam();
        queryParam.setTqmallGoodsId(tqmallGoodsId);
        queryParam.setShopId(shopId);
        List<Goods> goodsList = goodsService.queryGoods(queryParam);
        if (!CollectionUtils.isEmpty(goodsList)) {
            return Result.wrapSuccessfulResult(true);
        } else {
            return Result.wrapSuccessfulResult(false);
        }
    }

}
