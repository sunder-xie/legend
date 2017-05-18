package com.tqmall.legend.web.home;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.inventory.InventoryService;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.biz.warehouse.IWarehouseOutDetailService;
import com.tqmall.legend.biz.warehousein.WarehouseInDetailService;
import com.tqmall.legend.common.NoteType;
import com.tqmall.legend.entity.base.CountOrderEntity;
import com.tqmall.legend.entity.customer.AppointVo;
import com.tqmall.legend.entity.goods.GoodsCar;
import com.tqmall.legend.entity.goods.GoodsOnsaleEnum;
import com.tqmall.legend.entity.warehouse.GoodsTqmallPriceBo;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.goods.GoodsFacade;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.facade.order.OrderServicesFacade;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.object.enums.warehouse.ZeroStockRangeEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.legend.web.vo.home.RemindCountVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendShortageGoodsRequest;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by ende on 16/5/9.
 */
@Slf4j
@Controller
@RequestMapping("guide")
public class GuideController extends BaseController {
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private ShopNoteInfoService shopNoteInfoService;
    @Autowired
    private AppointService appointService;
    @Autowired
    private PrechecksService prechecksService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private WarehouseInDetailService warehouseInDetailService;
    @Autowired
    private IWarehouseOutDetailService warehouseOutDetailService;
    @Autowired
    private OrderServicesFacade orderServicesFacade;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    GoodsFacade goodsFacade;

    /**
     * 登录后门店管理首页
     *
     * @param model
     * @return
     */
    @HttpRequestLog
    @RequestMapping("")
    public String guide(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("moduleUrl", ModuleUrlEnum.HOME.getModuleUrl());
        RemindCountVo remindCountVo = new RemindCountVo();
        try {
            //预约提醒数
            remindCountVo.setAppointNumber(getRemindAppointCount(shopId));
            //工单提醒数
            Result<Integer> orderResult = getRemindOrderCount(shopId);
            if (orderResult.isSuccess()) {
                remindCountVo.setOrderNumber(orderResult.getData());
            }
            //保养提醒数
            // 首次保养提醒
            Integer firstMaintainCount = shopNoteInfoService.getNoteInfoCount(shopId, NoteType.FIRST_MAINTAIN_NOTE_TYPE);
            // 保养二次提醒
            Integer secondMaintainCount = shopNoteInfoService.getNoteInfoCount(shopId, NoteType.SECOND_MAINTAIN_NOTE_TYPE);
            remindCountVo.setMaintainNumber(firstMaintainCount + secondMaintainCount);
            //保险提醒数
            int insuranceNum = shopNoteInfoService.getNoteInfoCount(shopId, NoteType.INSURANCE_NOTE_TYPE);
            remindCountVo.setInsuranceNumber(insuranceNum);
            //年检提醒数
            int auditingNum = shopNoteInfoService.getNoteInfoCount(shopId, NoteType.AUDITING_NOTE_TYPE);
            remindCountVo.setAuditingNumber(auditingNum);
            // 仓库预警数量
            int stockwarningNum =goodsService.statisticsWarningTotal(shopId);
            remindCountVo.setStockwarningNumber(stockwarningNum);
            model.addAttribute("remindCountVo", remindCountVo);
        } catch (Exception e) {
            log.error("[[门店首页]-初始化数据出错");
        }
        return "/yqx/page/guide";
    }

    /**
     * 预约单提醒总数(包括待确认和待开单的预约单)
     *
     * @return
     */
    private int getRemindAppointCount(Long shopId) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        searchParams.put("shopId", shopId);
        searchParams.put("pushStatus", 1);//1表示不需要下推的或者已经下推的
        Set<Long> statusSet = new HashSet<>();
        statusSet.add(AppointStatusEnum.TO_CONFIRM.getIndex().longValue());//待确认的
        statusSet.add(AppointStatusEnum.APPOINT_SUCCESS.getIndex().longValue());//待开单的
        searchParams.put("statusList", statusSet);
        log.info("[门店首页-工单提醒]-预约单提醒总数查询条件:{}", searchParams);
        return appointService.selectCount(searchParams);
    }

    /**
     * 工单提醒数(包括待报价、已报价、已派工、已完工、已挂账的工单)
     *
     * @return
     */
    private Result<Integer> getRemindOrderCount(Long shopId) {
        CountOrderEntity countOrderEntity = new CountOrderEntity();
        countOrderEntity.setShopId(shopId);
        countOrderEntity.setOrderStatus("CJDD,DDBJ,FPDD,DDSG,DDWC,DDYFK");
        countOrderEntity.setPayStatus(new Integer[]{0, 1});
        // 调用搜索接口获取工单计数
        return qryOrderCount(countOrderEntity);
    }

    /**
     * 数量统计
     *
     * @param noteType 查询类型:1.车辆接待数 2.维修开单数 3.结算数 4.客户回访数 5.采购入库数 6.库存查询(预警数) 7.领料出库数
     * @return
     */
    @RequestMapping(value = "/count")
    @ResponseBody
    public Result count(@RequestParam(value = "noteType", required = true) Integer noteType) {
        Long shopId = UserUtils.getShopIdForSession(request);
        int count = 0;
        boolean isErrorType = false;
        switch (noteType) {
            case 1://1.车辆接待数
                count = getTodayPrecheckCarCount(shopId);
                break;
            case 2://2.维修开单数
                Result<Integer> countOrderRe = getTodayOrderCount(shopId);
                count = countOrderRe.getData();
                break;
            case 3://3.结算数
                Result<Integer> countSettledOrderRe = getTodaySettledOrderCount(shopId);
                count = countSettledOrderRe.getData();
                break;
            case 4://4.客户回访数
                int count1 = shopNoteInfoService.getNoteInfoCount(shopId, NoteType.VISIT_NOTE_TYPE);//noteType=4表示回访到期提醒
                int count2 = shopNoteInfoService.getNoteInfoCount(shopId, NoteType.NEXT_VISIT_NOTE_TYPE);//下次回访
                count = count1 + count2;
                break;
            case 5://5.采购入库数
                count = getTodayWarehosuInGoodsCount(shopId);
                break;
            case 6://6.库存查询(在售数)
                count = getOnsaleGoodsCount(shopId);
                break;
            case 7://7.领料出库数
                count = getTodayWarehouseOutGoodsCount(shopId);
                break;
            default:
                isErrorType = true;
                break;
        }
        if (isErrorType) {
            return Result.wrapErrorResult("-1", "未定义的查询类型");
        } else {
            return Result.wrapSuccessfulResult(count);
        }
    }

    /**
     * 今日预检车辆数
     *
     * @param shopId
     * @return
     */
    private int getTodayPrecheckCarCount(Long shopId) {
        Map<String, Object> precheckParams = Maps.newHashMap();
        String today = DateUtil.convertDateToYMD(new Date());
        String gmtCreateBegin = today + " 00:00:00";
        String gmtCreateEnd = today + " 23:59:59";
        precheckParams.put("shopId", shopId);
        precheckParams.put("gmtCreateGtoe", gmtCreateBegin);
        precheckParams.put("gmtCreateLtoe", gmtCreateEnd);
        return prechecksService.countPrecheckCar(precheckParams);
    }

    /**
     * 今日开单数
     *
     * @param shopId
     * @return
     */
    private Result<Integer> getTodayOrderCount(Long shopId) {
        CountOrderEntity countOrderEntity = new CountOrderEntity();
        countOrderEntity.setShopId(shopId);
        String today = DateUtil.convertDateToYMD(new Date());
        String startTime = today + " 00:00:00";
        String endTime = today + " 23:59:59";
        countOrderEntity.setStartTime(startTime);
        countOrderEntity.setEndTime(endTime);
        // 调用搜索接口获取工单计数
        return qryOrderCount(countOrderEntity);
    }

    /**
     * 今日结算工单数
     *
     * @param shopId
     * @return
     */
    private Result<Integer> getTodaySettledOrderCount(Long shopId) {
        CountOrderEntity countOrderEntity = new CountOrderEntity();
        countOrderEntity.setShopId(shopId);
        String today = DateUtil.convertDateToYMD(new Date());
        String startTime = today + " 00:00:00";
        String endTime = today + " 23:59:59";
        countOrderEntity.setPayStartTime(startTime);
        countOrderEntity.setPayEndTime(endTime);
        countOrderEntity.setPayStatus(new Integer[]{2});//TODO 待定
        // 调用搜索接口获取工单计数
        return qryOrderCount(countOrderEntity);
    }

    /**
     * 在售配件数
     *
     * @param shopId
     * @return
     */
    private int getOnsaleGoodsCount(Long shopId) {
        return goodsService.getGoods(shopId, GoodsOnsaleEnum.UPSHELF, ZeroStockRangeEnum.NONZEROSTOCK).size();
    }

    /**
     * 今日采购入库商品数
     *
     * @param shopId
     * @return
     */
    private int getTodayWarehosuInGoodsCount(Long shopId) {
        Map<String, Object> searchParams = Maps.newHashMap();
        String today = DateUtil.convertDateToYMD(new Date());
        String gmtCreateBegin = today + " 00:00:00";
        String gmtCreateEnd = today + " 23:59:59";
        searchParams.put("shopId", shopId);
        searchParams.put("startTime", gmtCreateBegin);
        searchParams.put("endTime", gmtCreateEnd);
        searchParams.put("status", "LZRK");
        return warehouseInDetailService.selectGoodsCount(searchParams);
    }

    /**
     * 今日领料出库商品数
     *
     * @param shopId
     * @return
     */
    private int getTodayWarehouseOutGoodsCount(Long shopId) {
        Map<String, Object> searchParams = Maps.newHashMap();
        String today = DateUtil.convertDateToYMD(new Date());
        String gmtCreateBegin = today + " 00:00:00";
        String gmtCreateEnd = today + " 23:59:59";
        searchParams.put("shopId", shopId);
        searchParams.put("startTime", gmtCreateBegin);
        searchParams.put("endTime", gmtCreateEnd);
        searchParams.put("status", "LZCK");
        return warehouseOutDetailService.selectGoodsCount(searchParams);
    }

    /**
     * 调用搜索接口查询工单计数
     *
     * @param countOrderEntity
     * @return
     */
    private Result<Integer> qryOrderCount(CountOrderEntity countOrderEntity) {
        countOrderEntity.setSymbol(1);
        Map<String,Long> result = orderServicesFacade.getOrderCountFromSearch(countOrderEntity);
        if(CollectionUtils.isEmpty(result)){
            return Result.wrapSuccessfulResult(0);
        }
        return Result.wrapSuccessfulResult(Integer.parseInt(result.get("1").toString()));
    }

    /**
     * 预约提醒明细
     * @return
     */
    @ResponseBody
    @RequestMapping("/appoint-list")
    public Result appointList(@PageableDefault(page = 1, value = 10) Pageable pageable) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        searchParams.put("offset", 0);
        searchParams.put("limit", pageable.getPageSize());//只查询前10条数据
        searchParams.put("shopId", shopId);
        searchParams.put("pushStatus", 1);//1表示不需要下推的或者已经下推的
        Set<Long> statusSet = new HashSet<>();
        statusSet.add(AppointStatusEnum.TO_CONFIRM.getIndex().longValue());//待确认的
        statusSet.add(AppointStatusEnum.APPOINT_SUCCESS.getIndex().longValue());//待开单的
        searchParams.put("statusList", statusSet);
        ArrayList<String> sorts = new ArrayList<>();
        sorts.add("status asc");//未确认的需要优先展示
        sorts.add("appoint_time desc");
        searchParams.put("sorts", sorts);
        log.info("[门店首页-工单提醒]-预约单提醒列表查询条件:{}", searchParams);
        List<AppointVo> appointVoList = appointFacade.getAppointVoList(searchParams);
        return Result.wrapSuccessfulResult(appointVoList);
    }

    @ResponseBody
    @RequestMapping("/order-list")
    public com.tqmall.core.common.entity.Result<DefaultPage<OrderInfoVo>> orderList(@PageableDefault(page = 1, value = 10) final Pageable pageable){
        return new ApiTemplate<DefaultPage<OrderInfoVo>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected DefaultPage<OrderInfoVo> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);

                LegendOrderRequest orderRequest = new LegendOrderRequest();
                orderRequest.setShopId(shopId.toString());
                orderRequest.setOrderStatus("CJDD,DDBJ,FPDD,DDSG,DDWC,DDYFK");
                List<Integer> payStatus = Lists.newArrayList();
                payStatus.add(0);
                payStatus.add(1);
                orderRequest.setPayStatus(payStatus);

                int page = pageable.getPageNumber() - 1;
                PageableRequest pageableRequest = new PageableRequest(page,pageable.getPageSize(), Sort.Direction.DESC,new String[]{"id"});

                DefaultPage<OrderInfoVo> orderInfoVos = orderServicesFacade.getOrderListFromSearch(pageableRequest, orderRequest);
                return orderInfoVos;
            }
        }.execute();

    }


    /**
     * 首页-库存预警列表数据
     * 库存预警列表数据 前10条数据 ID降序
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/stockwarning-list", method = RequestMethod.GET)
    public Result stockwarningList() {

        // 当前登录用户
        UserInfo currentLoginUser = UserUtils.getUserInfo(request);
        // 前10条 库存预警数据
        LegendShortageGoodsRequest goodsRequest = new LegendShortageGoodsRequest();
        goodsRequest.setShopId(currentLoginUser.getShopId());
        goodsRequest.setOnsaleStatus(GoodsOnsaleEnum.UPSHELF.getCode());
        goodsRequest.setGoodsType(0);
        PageableRequest pageableRequest = new PageableRequest(0, 10, Sort.Direction.DESC, new String[]{"id"});
        DefaultPage<SearchGoodsVo> page = goodsFacade.goodsPageSearchInStockWarning(goodsRequest, pageableRequest);

        // 配件数据包装
        List<SearchGoodsVo> goodses = page.getContent();
        if(CollectionUtils.isEmpty(goodses)){
            return Result.wrapSuccessfulResult(page);
        }
        Set<Long> goodsSet = new HashSet<Long>();
        Double stock = null;
        Integer shortageNumber = null;
        String measureUnit = "";
        for (SearchGoodsVo goods : goodses) {
            goodsSet.add(Long.parseLong(goods.getId()));
            // 单位
            measureUnit = goods.getMeasureUnit();
            // 显示库存数量 = 库存数量+单位
            stock = goods.getStock();
            stock = (stock == null) ? 0 : stock;
            goods.setStockForShow(stock + measureUnit);
            // 显示预警数量 = 预警库存+单位
            shortageNumber = goods.getShortageNumber();
            shortageNumber = (shortageNumber == null) ? 0 : shortageNumber;
            goods.setShortageNumberForShow(shortageNumber + measureUnit);
            // 适配车型(车型1|车型2|车型3)
            StringBuffer carInfoStr = new StringBuffer();
            List<GoodsCar> carInfoList = goods.getCarInfoList();
            if (!CollectionUtils.isEmpty(carInfoList)) {
                for (GoodsCar goodsCar : carInfoList) {
                    carInfoStr.append(goodsCar.getCarBrandName());
                    carInfoStr.append(goodsCar.getCarSeriesName());
                    carInfoStr.append(goodsCar.getCarAlias());
                    carInfoStr.append("|");
                }
            }
            goods.setCarInfoStr(carInfoStr.toString());
        }

        // batch 获取配件的采购价
        Long[] goodsIds = goodsSet.toArray(new Long[goodsSet.size()]);
        com.tqmall.legend.common.Result result = null;
        try {
            result = inventoryService.batchGetGoodsInventoryPrice(goodsIds, currentLoginUser);
        } catch (Exception e) {
            log.error("[门店首页-库存预警]批量查询配件云修采购价异常", e);
            return Result.wrapErrorResult("-1", "批量查询配件云修采购价异常");
        }
        List<GoodsTqmallPriceBo> goodsTqmallPriceBos = (List<GoodsTqmallPriceBo>) result.getData();
        Map<Long, String> goodsTqmallPriceMap = new HashMap<Long, String>(goodsTqmallPriceBos.size());
        for (GoodsTqmallPriceBo goodsTqmallPriceBo : goodsTqmallPriceBos) {
            goodsTqmallPriceMap.put(goodsTqmallPriceBo.getGoodsId(), goodsTqmallPriceBo.getTqmallPrice());
        }
        for (SearchGoodsVo goods : goodses) {
            String tqmallPrice = goodsTqmallPriceMap.get(goods.getId());
            tqmallPrice = (tqmallPrice == null) ? "" : tqmallPrice;
            goods.setCityPrice(tqmallPrice);
        }

        return Result.wrapSuccessfulResult(page);
    }

}
