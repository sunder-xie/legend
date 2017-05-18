package com.tqmall.legend.server.order;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.cube.shop.provider.popularsort.RpcPopularSortService;
import com.tqmall.cube.shop.result.popularsort.PopularDataDTO;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.api.entity.OrderCountSearchVO;
import com.tqmall.legend.biz.api.IOrderApiService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.CustomerCarFileService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerFeedbackService;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarFile;
import com.tqmall.legend.entity.customer.CustomerFeedback;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.order.OrderServicesFacade;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.legend.log.ShopNoteInfoLog;
import com.tqmall.legend.object.param.order.CustomerFeedbackDTO;
import com.tqmall.legend.object.param.order.OrderSearchParam;
import com.tqmall.legend.object.param.order.SaveAndUpdateServiceWarnParam;
import com.tqmall.legend.object.param.order.speedily.CreateParam;
import com.tqmall.legend.object.param.order.speedily.GoodsParam;
import com.tqmall.legend.object.param.order.speedily.OrderInfoParam;
import com.tqmall.legend.object.param.order.speedily.ServiceParam;
import com.tqmall.legend.object.param.service.WashCarServiceParam;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.customer.CustomerRecordDTO;
import com.tqmall.legend.object.result.order.OrderGoodsDTO;
import com.tqmall.legend.object.result.order.OrderInfoDTO;
import com.tqmall.legend.object.result.order.OrderReceiverDTO;
import com.tqmall.legend.object.result.order.OrderServicesDTO;
import com.tqmall.legend.object.result.order.WorkerDTO;
import com.tqmall.legend.object.result.shop.ShopFeedBackDTO;
import com.tqmall.legend.service.order.RpcAppOrderService;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lifeilong on 2016/3/15.
 */
@Slf4j
@Service("rpcAppOrderService")
public class RpcAppOrderServiceImpl implements RpcAppOrderService {

    @Autowired
    private ShopManagerService shopManagerService;

    @Autowired
    private IOrderApiService orderApiService;

    @Autowired
    private AppointService appointService;

    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private NoteConfigureService noteConfigureService;

    @Autowired
    private CustomerCarFileService customerCarFileService;

    @Autowired
    private CustomerFeedbackService customerFeedbackService;

    @Autowired
    private ShopNoteInfoService shopNoteInfoService;
    @Autowired
    private RpcPopularSortService rpcPopularSortService;
    @Autowired
    private OrderServicesFacade orderServicesFacade;

    @Autowired
    private ShopServiceInfoService shopServiceInfoService;

    @Override
    public Result<List<OrderReceiverDTO>> getOrderReceiverList(Long shopId) {
        log.info("[服务顾问列表]Begin getOrderReceiverList().获取门店服务顾问列表,shopId={}", shopId);
        if (null == shopId) {
            log.error("[服务顾问列表] 店铺id错误,shopId==null");
            return LegendErrorCode.APP_SHOP_ID_ERROR.newResult();
        } else if (shopId < 1) {
            log.error("[服务顾问列表] 店铺id错误,shopId={}", shopId);
            return LegendErrorCode.APP_SHOP_ID_ERROR.newResult();
        }
        Result result = null;
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("shopId", shopId);
        List<ShopManager> shopManagerList = shopManagerService.selectWithoutCache(searchParams);
        if (CollectionUtils.isEmpty(shopManagerList)) {
            log.info("[服务顾问列表] 该门店服务顾问列表为空,shopId={}", shopId);
            return result;
        }
        List<OrderReceiverDTO> orderReceiverDTOList = new ArrayList<OrderReceiverDTO>();
        for (ShopManager shopManager : shopManagerList) {
            if (null == shopManager.getId()) {
                continue;
            } else {
                if (shopManager.getId() < 1 || StringUtils.isBlank(shopManager.getName())) {
                    continue;
                } else {
                    OrderReceiverDTO orderReceiverDTO = new OrderReceiverDTO();
                    orderReceiverDTO.setReceiver(shopManager.getId());
                    orderReceiverDTO.setReceiverName(shopManager.getName());
                    orderReceiverDTOList.add(orderReceiverDTO);
                }
            }
        }
        return Result.wrapSuccessfulResult(orderReceiverDTOList);
    }

    /**
     * 工单查询
     *
     * @param orderSearchParam
     *
     * @return
     *
     * @Since app3.0
     */
    @Override
    public Result<PageEntityDTO<OrderInfoDTO>> searchOrderInfo(OrderSearchParam orderSearchParam) {
        log.info("[工单查询] 搜索门店工单 [参数]orderSearchParam={}", orderSearchParam);
        Result<DefaultPage<OrderInfoVo>> result = orderApiService.getOrderInfoListFromSearchOrLocal(orderSearchParam);
        PageEntityDTO<OrderInfoDTO> resultEntity = new PageEntityDTO<OrderInfoDTO>();
        if (!result.isSuccess()) {
            log.error("[工单搜索] 获取工单信息失败.code={},errorMsg={}", result.getCode(), result.getMessage());
            return (Result<PageEntityDTO<OrderInfoDTO>>) LegendErrorCode.APP_ORDER_LIST_ERROR.newResult();
        }
        log.info("[工单查询] 搜索门店工单,isSuccess:{}", result.isSuccess());
        DefaultPage<OrderInfoVo> defaultPage = result.getData();
        if (null != defaultPage) {
            resultEntity.setTotalNum(Long.valueOf(defaultPage.getTotalElements()).intValue());
            resultEntity.setPageNum(defaultPage.getNumber());
            List<OrderInfoDTO> orderInfoDTOList = Lists.newArrayList();
            List<OrderInfoVo> orderInfoVos = defaultPage.getContent();
            //设置返回数据传输对象
            if (!CollectionUtils.isEmpty(orderInfoVos)) {
                for (OrderInfoVo orderInfoVo : orderInfoVos) {
                    OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
                    BeanUtils.copyProperties(orderInfoVo, orderInfoDTO);
                    setOrderInfDTO(orderInfoVo, orderInfoDTO);
                    orderInfoDTOList.add(orderInfoDTO);
                }
            }

            resultEntity.setRecordList(orderInfoDTOList);
        }
        return Result.wrapSuccessfulResult(resultEntity);
    }

    private void setOrderInfDTO(OrderInfoVo orderInfoVo, OrderInfoDTO orderInfoDTO) {
        orderInfoDTO.setGmtCreateStr(orderInfoVo.getCreateTimeStr());
        orderInfoDTO.setShopId(Long.parseLong(orderInfoVo.getShopId().toString()));
        if (StringUtils.isNotBlank(orderInfoVo.getId())) {
            orderInfoDTO.setId(Long.parseLong(orderInfoVo.getId()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getGmtCreate())) {
            orderInfoDTO.setGmtCreate(DateUtil.convertStringToDate(orderInfoVo.getGmtCreate()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getGmtModified())) {
            orderInfoDTO.setGmtModified(DateUtil.convertStringToDate(orderInfoVo.getGmtModified()));
        }
        if (orderInfoVo.getOrderType() != null) {
            orderInfoDTO.setOrderType(Long.parseLong(orderInfoVo.getOrderType().toString()));
        }
        if (orderInfoVo.getCustomerId() != null) {
            orderInfoDTO.setCustomerId(Long.parseLong(orderInfoVo.getCustomerId().toString()));
        }
        if (orderInfoVo.getCustomerCarId() != null) {
            orderInfoDTO.setCustomerCarId(Long.parseLong(orderInfoVo.getCustomerCarId().toString()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getExpectedTime())) {
            orderInfoDTO.setExpectedTime(DateUtil.convertStringToDate(orderInfoVo.getExpectedTime()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getBuyTime())) {
            orderInfoDTO.setBuyTime(DateUtil.convertStringToDate(orderInfoVo.getBuyTime()));
        }
        if (orderInfoVo.getCarBrandId() != null) {
            orderInfoDTO.setCarBrandId(Long.parseLong(orderInfoVo.getCarBrandId().toString()));
        }
        if (orderInfoVo.getCarModelsId() != null) {
            orderInfoDTO.setCarModelsId(Long.parseLong(orderInfoVo.getCarModelsId().toString()));
        }
        if (orderInfoVo.getCarPowerId() != null) {
            orderInfoDTO.setCarPowerId(Long.parseLong(orderInfoVo.getCarPowerId().toString()));
        }
        if (orderInfoVo.getCarSeriesId() != null) {
            orderInfoDTO.setCarSeriesId(Long.parseLong(orderInfoVo.getCarSeriesId().toString()));
        }
        if (orderInfoVo.getCarYearId() != null) {
            orderInfoDTO.setCarYearId(Long.parseLong(orderInfoVo.getCarYearId().toString()));
        }
        if (orderInfoVo.getDiscount() != null) {
            orderInfoDTO.setDiscount(new BigDecimal(orderInfoVo.getDiscount()));
        }
        if (orderInfoVo.getFeeAmount() != null) {
            orderInfoDTO.setFeeAmount(new BigDecimal(orderInfoVo.getFeeAmount()));
        }
        if (orderInfoVo.getFeeDiscount() != null) {
            orderInfoDTO.setFeeDiscount(new BigDecimal(orderInfoVo.getFeeDiscount()));
        }
        if (orderInfoVo.getGoodsAmount() != null) {
            orderInfoDTO.setGoodsAmount(new BigDecimal(orderInfoVo.getGoodsAmount()));
        }
        if (orderInfoVo.getGoodsDiscount() != null) {
            orderInfoDTO.setGoodsDiscount(new BigDecimal(orderInfoVo.getGoodsDiscount()));
        }
        if (orderInfoVo.getGoodsCount() != null) {
            orderInfoDTO.setGoodsCount(Long.parseLong(orderInfoVo.getGoodsCount().toString()));
        }
        if (orderInfoVo.getServiceAmount() != null) {
            orderInfoDTO.setServiceAmount(new BigDecimal(orderInfoVo.getServiceAmount()));
        }
        if (orderInfoVo.getTaxAmount() != null) {
            orderInfoDTO.setTaxAmount(new BigDecimal(orderInfoVo.getTaxAmount()));
        }
        if (orderInfoVo.getTotalAmount() != null) {
            orderInfoDTO.setTotalAmount(new BigDecimal(orderInfoVo.getTotalAmount()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getOrderAmount())) {
            orderInfoDTO.setOrderAmount(new BigDecimal(orderInfoVo.getOrderAmount()));
        }
        if (orderInfoVo.getPreCouponAmount() != null) {
            orderInfoDTO.setPreCouponAmount(new BigDecimal(orderInfoVo.getPreCouponAmount()));
        }
        if (orderInfoVo.getPayAmount() != null) {
            orderInfoDTO.setPayAmount(new BigDecimal(orderInfoVo.getPayAmount()));
        }
        if (orderInfoVo.getPreDiscountRate() != null) {
            orderInfoDTO.setPreDiscountRate(new BigDecimal(orderInfoVo.getPreDiscountRate()));
        }
        if (orderInfoVo.getPrePreferentiaAmount() != null) {
            orderInfoDTO.setPrePreferentiaAmount(new BigDecimal(orderInfoVo.getPrePreferentiaAmount()));
        }
        if (orderInfoVo.getPreTaxAmount() != null) {
            orderInfoDTO.setPreTaxAmount(new BigDecimal(orderInfoVo.getPreTaxAmount()));
        }
        if (orderInfoVo.getPreTotalAmount() != null) {
            orderInfoDTO.setPreTotalAmount(new BigDecimal(orderInfoVo.getPreTotalAmount()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getFinishTime())) {
            orderInfoDTO.setFinishTime(DateUtil.convertStringToDate(orderInfoVo.getFinishTime()));
        }
        if (orderInfoVo.getInsuranceCompanyId() != null) {
            orderInfoDTO.setInsuranceCompanyId(Long.parseLong(orderInfoVo.getInsuranceCompanyId().toString()));
        }
        if (orderInfoVo.getInvoiceType() != null) {
            orderInfoDTO.setInvoiceType(Long.parseLong(orderInfoVo.getInvoiceType().toString()));
        }
        if (orderInfoVo.getReceiver() != null) {
            orderInfoDTO.setReceiver(Long.parseLong(orderInfoVo.getReceiver().toString()));
        }
        if (orderInfoVo.getServiceCount() != null) {
            orderInfoDTO.setServiceCount(Long.parseLong(orderInfoVo.getServiceCount().toString()));
        }
        if (orderInfoVo.getSignAmount() != null) {
            orderInfoDTO.setSignAmount(new BigDecimal(orderInfoVo.getSignAmount()));
        }
        if (orderInfoVo.getServiceDiscount() != null) {
            orderInfoDTO.setServiceDiscount(new BigDecimal(orderInfoVo.getServiceDiscount()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getPayTime())) {
            orderInfoDTO.setPayTime(DateUtil.convertStringToDate(orderInfoVo.getPayTime()));
        }
        if (StringUtils.isNotBlank(orderInfoVo.getCreateTime())) {
            orderInfoDTO.setCreateTime(DateUtil.convertStringToDate(orderInfoVo.getCreateTime()));
        }
    }

    /**
     * 获取工单详情
     *
     * @param shopId  店铺id
     * @param orderId 工单id
     */
    @Override
    public Result<OrderInfoDTO> getOrderInfoDetail(Long shopId, Long orderId) {


        if (shopId == null || shopId < 1) {
            log.error("[工单详情] 获取工单详情.店铺信息错误. shopId:{}", shopId);
            return (Result<OrderInfoDTO>) LegendErrorCode.APP_SHOP_ID_ERROR.newResult(shopId);
        }
        if (orderId == null || orderId < 1) {
            log.error("[工单详情] 获取工单详情.店铺信息错误. orderId:{}", orderId);
            return (Result<OrderInfoDTO>) LegendErrorCode.APP_ORDER_ID_ERROR.newResult();
        }
        Result result = orderApiService.getOrder(orderId, shopId);
        if (!result.isSuccess()) {
            log.error("[工单详情] 获取工单详情失败. code:{},msg:{}", result.getCode(), result.getMessage());
            return (Result<OrderInfoDTO>) LegendErrorCode.APP_ORDERINFO_ERROR.newResult();
        }
        OrderInfo orderInfo = (OrderInfo) result.getData();
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        BeanUtils.copyProperties(orderInfo, orderInfoDTO);
        //设置车型信息
        orderInfoDTO.setCarInfo(orderInfo.getCarInfo());
        //设置开单时间
        orderInfoDTO.setGmtCreateStr(DateUtil.convertDateToYMDHHmm(orderInfo.getCreateTime()));
        //订单状态clientName
        if (orderInfo.getPayStatus() != null && orderInfo.getPayStatus() == 1) {
            orderInfoDTO.setOrderStatusClientName("已挂账");
        } else {
            if (orderInfoDTO.getOrderStatus().equals("DDYFK")) {
                orderInfoDTO.setOrderStatusClientName("已结清");
            } else {
                orderInfoDTO.setOrderStatusClientName(OrderStatusEnum.getorderStatusClientNameByKey(orderInfoDTO.getOrderStatus()));
            }
        }
        //设置物料
        List<OrderGoods> orderGoodsList = orderInfo.getGoodsList();
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
                BeanUtils.copyProperties(orderGoods, orderGoodsDTO);
                orderGoodsDTOList.add(orderGoodsDTO);
            }
        }
        orderInfoDTO.setOrderGoodsDTOList(orderGoodsDTOList);
        //设置虚开物料
        List<OrderGoods> orderInfoVirtualGoodsList = orderInfo.getVirtualGoodsList();
        List<OrderGoodsDTO> orderVirtualGoodsDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderInfoVirtualGoodsList)) {
            for (OrderGoods orderGoods : orderInfoVirtualGoodsList) {
                OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
                BeanUtils.copyProperties(orderGoods, orderGoodsDTO);
                orderVirtualGoodsDTOList.add(orderGoodsDTO);
            }
        }
        orderInfoDTO.setOrderVirtualGoodsDTOList(orderVirtualGoodsDTOList);
        //设置服务
        List<OrderServices> orderServicesList = orderInfo.getServiceList();
        List<OrderServicesDTO> orderServicesDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                OrderServicesDTO orderServicesDTO = new OrderServicesDTO();
                BeanUtils.copyProperties(orderServices, orderServicesDTO);
                orderServicesDTOList.add(orderServicesDTO);
            }
        }
        orderInfoDTO.setOrderServicesDTOList(orderServicesDTOList);
        //设置其他服务
        List<OrderServices> additionalOrderServicesList = orderInfo.getAdditionalServiceList();
        List<OrderServicesDTO> additionalOrderServicesDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(additionalOrderServicesList)) {
            for (OrderServices orderServices : additionalOrderServicesList) {
                OrderServicesDTO orderServicesDTO = new OrderServicesDTO();
                BeanUtils.copyProperties(orderServices, orderServicesDTO);
                additionalOrderServicesDTOList.add(orderServicesDTO);
            }
        }
        orderInfoDTO.setOtherOrderServicesDTOList(additionalOrderServicesDTOList);
        //设置图片信息
        if (StringUtils.isBlank(orderInfoDTO.getImgUrl())) {
            if (orderInfo.getCustomerCar() != null) {
                  /*车辆文件信息*/
                List<CustomerCarFile> carFiles = null;
                //CustomerCarFile[] carFilesArray = new CustomerCarFile[] {};
                Map<String, Object> carFileParam = new HashMap<>();
                carFileParam.put("shopId", shopId);
                carFileParam.put("relId", orderInfo.getCustomerCar().getId());
                carFileParam.put("relType", 1);
                carFileParam.put("limit", 4);
                carFileParam.put("offset", 0);
                List<String> sorts = new ArrayList<>();
                sorts.add("id desc");
                carFileParam.put("sorts", sorts);
                carFiles = customerCarFileService.select(carFileParam);
                if (CollectionUtils.isEmpty(carFiles)) {
                    orderInfoDTO.setImgUrl(null);
                } else {
                    StringBuffer str = new StringBuffer("");
                    for (int j = 0; j < carFiles.size(); j++) {
                        str.append(carFiles.get(j).getFilePath());
                        if (j != carFiles.size() - 1) {
                            str.append(";");
                        }
                    }
                    orderInfoDTO.setImgUrl(carFiles.get(0).getFilePath());
                }
            }
        }

        return Result.wrapSuccessfulResult(orderInfoDTO);
    }

    /**
     * 工单无效
     *
     * @param userId
     * @param orderId
     *
     * @return
     */
    @Override
    public Result invalidOrder(Long orderId, Long userId) {
        log.info("[工单无效] 工单无效. userId:{},orderId:{}", userId, orderId);
        Result result = orderApiService.invalidOrder(orderId, userId);
        log.info("[工单无效] 工单无效结果. userId:{},orderId:{},result:{}", userId, orderId, JSONUtil.object2Json(result));
        return result;
    }

    /**
     * 店铺待办工单数统计(1预约单,2待报价,3待结算,4待回访,5已挂账)
     *
     * @param shopId
     *
     * @return
     */
    @Override
    public Result<Map<String, Object>> shopOrderCount(Long shopId, String ver) {

        //获取店铺预约单配置列表
        int appointInvalidDate = 0;
        int appointValidDate = 0;
        NoteShopConfig appointShopConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE);
        if (null != appointShopConfig) {
            appointValidDate = appointShopConfig.getFirstValue();
            appointInvalidDate = appointShopConfig.getInvalidValue();
        }

        Map<String, Object> resultMap = new HashMap<>();
        /*统计预约单*/
        Map<String, Object> appointParam = new HashMap<>();
        appointParam.put("shopId", shopId);
        appointParam.put("isDeleted", "N");
        appointParam.put("pushStatus", 1);
        appointParam.put("appointTimeGt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -appointInvalidDate)));
        appointParam.put("appointTimeLt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), appointValidDate)));
        //待确认，已确认
        appointParam.put("statusList", new Integer[]{0, 1});
        Integer totalCount = appointService.selectCount(appointParam);
        resultMap.put("YYD", totalCount);

        /**
         * 调用搜索参数整理
         * */
        List<OrderCountSearchVO> paramList = new ArrayList<>();
        OrderCountSearchVO paramDBJ = new OrderCountSearchVO();
        paramDBJ.setSymbol(2);
        paramDBJ.setOrderTag(new Integer[]{1, 2, 3});
        paramDBJ.setShopId(shopId);
        paramDBJ.setOrderStatus("CJDD");
        paramDBJ.setProxyType(new Integer[]{0, 1});
        paramList.add(paramDBJ);

        OrderCountSearchVO paramDJSOne = new OrderCountSearchVO();
        paramDJSOne.setSymbol(3);
        paramDJSOne.setShopId(shopId);

        paramDJSOne.setOrderTag(new Integer[]{1});
        paramDJSOne.setPayStatus(new Integer[]{0});
        paramDJSOne.setOrderStatus("DDWC");
        paramDJSOne.setProxyType(new Integer[]{0, 1});
        paramList.add(paramDJSOne);

        OrderCountSearchVO paramDJSTwo = new OrderCountSearchVO();
        paramDJSTwo.setSymbol(5);
        paramDJSTwo.setOrderTag(new Integer[]{3});
        paramDJSTwo.setPayStatus(new Integer[]{0});
        paramDJSTwo.setShopId(shopId);
        paramDJSTwo.setOrderStatus("CJDD");
        paramDJSTwo.setProxyType(new Integer[]{0, 1});
        paramList.add(paramDJSTwo);

        //回访记录改造,跟PC提醒中心统一逻辑
        DefaultPage<NoteInfo> defaultPage = (DefaultPage<NoteInfo>) shopNoteInfoService.getVisitNoteInfo(shopId, null, false, 1, 1);
        if (null != defaultPage) {
            resultMap.put("DHF", Long.valueOf(defaultPage.getTotalElements()).intValue());
        }

        OrderCountSearchVO paramYGZ = new OrderCountSearchVO();
        paramYGZ.setSymbol(6);
        paramYGZ.setOrderTag(new Integer[]{1, 2, 3});
        paramYGZ.setPayStatus(new Integer[]{1});
        paramYGZ.setShopId(shopId);
        paramYGZ.setProxyType(new Integer[]{0, 1});
        paramYGZ.setOrderStatus("DDYFK");
        paramList.add(paramYGZ);

        com.tqmall.zenith.errorcode.support.Result<Map<String, Long>> result = orderApiService.getOrderCount(paramList);
        if (null == result || !result.isSuccess()) {
            log.error("[店铺待办事项数量统计] 调用搜索获取数量失败.{}", LogUtils.funToString(paramList, result));
            return LegendErrorCode.APP_ORDERCOUNT_ERROR.newResult();
        } else {
            resultMap.put("DBJ", result.getData().get("2"));
            resultMap.put("WJS", result.getData().get("3") + result.getData().get("5"));
            //resultMap.put("DHF", result.getData().get("4"));
            resultMap.put("YGZ", result.getData().get("6"));
        }
        return Result.wrapSuccessfulResult(resultMap);
    }

    /**
     * 车辆待办工单数统计(预约单,待报价,待结算,待回访)
     *
     * @param shopId
     * @param customerCarId
     *
     * @return
     */
    @Override
    public Result<Map<String, Object>> customerOrderCount(Long shopId, Long customerCarId) {
        int invalidDate = 0;
        int validDate = 0;
        //获取门店回访单配置列表
        NoteShopConfig noteShopConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_VISIT_CONF_TYPE);
        if (null != noteShopConfig) {
            validDate = noteShopConfig.getFirstValue();
            invalidDate = noteShopConfig.getInvalidValue();
        }
        //获取店铺预约单配置列表
        int appointInvalidDate = 0;
        int appointValidDate = 0;
        NoteShopConfig appointShopConfig = noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE);
        if (null != appointShopConfig) {
            appointValidDate = appointShopConfig.getFirstValue();
            appointInvalidDate = appointShopConfig.getInvalidValue();
        }

        Map<String, Object> resultMap = new HashMap<>();
        /*统计预约单*/
        Map<String, Object> appointParam = new HashMap<>();
        appointParam.put("shopId", shopId);
        appointParam.put("isDeleted", "N");
        appointParam.put("pushStatus", 1);
        appointParam.put("appointTimeGt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -appointInvalidDate)));
        appointParam.put("appointTimeLt", DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), appointValidDate)));
        appointParam.put("customerCarId", customerCarId);
        //待确认，已确认
        appointParam.put("statusList", new Integer[]{0, 1});
        Integer totalCount = appointService.selectCount(appointParam);
        resultMap.put("YYD", totalCount);

        /**
         * 调用搜索参数整理
         * */
        List<OrderCountSearchVO> paramList = new ArrayList<>();
        OrderCountSearchVO paramDBJ = new OrderCountSearchVO();
        paramDBJ.setSymbol(2);
        paramDBJ.setOrderTag(new Integer[]{1, 2, 3});
        paramDBJ.setShopId(shopId);
        paramDBJ.setOrderStatus("CJDD");
        paramDBJ.setCustomerCarId(customerCarId + "");
        paramDBJ.setProxyType(new Integer[]{0, 1});
        paramList.add(paramDBJ);

        OrderCountSearchVO paramDJSOne = new OrderCountSearchVO();
        paramDJSOne.setSymbol(3);
        paramDJSOne.setOrderTag(new Integer[]{1, 2, 3});
        paramDJSOne.setPayStatus(new Integer[]{0, 1});
        paramDJSOne.setShopId(shopId);
        paramDJSOne.setOrderStatus("DDWC,DDYFK");
        paramDJSOne.setCustomerCarId(customerCarId + "");
        paramDJSOne.setProxyType(new Integer[]{0, 1});
        paramList.add(paramDJSOne);

        OrderCountSearchVO paramDJSTwo = new OrderCountSearchVO();
        paramDJSTwo.setSymbol(5);
        paramDJSTwo.setOrderTag(new Integer[]{3});
        paramDJSTwo.setPayStatus(new Integer[]{0});
        paramDJSTwo.setShopId(shopId);
        paramDJSTwo.setOrderStatus("CJDD");
        paramDJSTwo.setCustomerCarId(customerCarId + "");
        paramDJSTwo.setProxyType(new Integer[]{0, 1});
        paramList.add(paramDJSTwo);

        OrderCountSearchVO paramDHF = new OrderCountSearchVO();
        paramDHF.setSymbol(4);
        paramDHF.setOrderTag(new Integer[]{1});
        paramDHF.setPayStatus(new Integer[]{2});
        paramDHF.setShopId(shopId);
        paramDHF.setOrderStatus("DDYFK");
        paramDHF.setProxyType(new Integer[]{0, 1});
        paramDHF.setCustomerCarId(customerCarId + "");
        paramDHF.setIsVisit(0);
        paramDHF.setPayStartTime(DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -(validDate + invalidDate))));
        paramDHF.setPayEndTime(DateUtil.convertDateToYMDHMS(DateUtil.getDateBy(new Date(), -validDate)));
        paramList.add(paramDHF);

        log.info("[车辆待办事项数量统计] paramList:{}", paramList);
        com.tqmall.zenith.errorcode.support.Result<Map<String, Long>> result = orderApiService.getOrderCount(paramList);
        log.info("[车辆待办事项数量统计] paramList:{},result:{}", paramList, result);
        if (null == result || !result.isSuccess()) {
            log.error("[车辆待办事项数量统计] 调用搜索获取数量失败. paramList:{}", paramList);
            return LegendErrorCode.APP_CUSTOMERORDERCOUNT_ERROR.newResult();
        } else {
            resultMap.put("DBJ", result.getData().get("2"));
            resultMap.put("WJS", result.getData().get("3") + result.getData().get("5"));
            resultMap.put("DHF", result.getData().get("4"));
        }
        return Result.wrapSuccessfulResult(resultMap);
    }

    /**
     * 获取店铺的待报价工单列表
     *
     * @param shopId
     * @param page
     * @param size
     * @param customerId
     *
     * @return
     */
    @Override
    public Result<PageEntityDTO<OrderInfoDTO>> getShopDBJOrderList(Long customerId, Long shopId, Integer page, Integer size) {
        OrderSearchParam orderSearchParam = new OrderSearchParam();
        orderSearchParam.setShopId(shopId);
        orderSearchParam.setOrderTag(new Integer[]{1, 3});
        orderSearchParam.setPageNum(page);
        orderSearchParam.setPageSize(size);
        orderSearchParam.setProxyType(new Integer[]{0, 1});
        orderSearchParam.setOrderStatus(new String[]{"CJDD"});
        orderSearchParam.setSortBy("createTime");
        orderSearchParam.setSortType("desc");
        if (customerId != null && customerId > 0) {
            orderSearchParam.setCustomerIds(new Long[]{customerId});
        }
        return searchOrderInfo(orderSearchParam);
    }

    /**
     * 获取店铺的未结算工单列表
     *
     * @param customerId
     * @param shopId
     * @param page
     * @param size
     *
     * @return
     */
    @Override
    public Result<PageEntityDTO<OrderInfoDTO>> getShopWJSOrderList(Long customerId, Long shopId, Integer page, Integer size, String ver) {
        if (null == page) {
            page = 1;
        }
        if (null == size || size == 0) {
            size = 10;
        }
        //最后的返回数据
        List<OrderInfoDTO> tempList = new ArrayList<>();
        List<OrderInfoDTO> resultList = new ArrayList<>();
        //总数量
        Integer totalNum = 0;

        OrderSearchParam orderSearchParam = new OrderSearchParam();
        orderSearchParam.setShopId(shopId);
        orderSearchParam.setOrderTag(new Integer[]{1});
        orderSearchParam.setPayStatus(new Integer[]{0});
        orderSearchParam.setOrderStatus(new String[]{"DDWC"});
        orderSearchParam.setPageNum(1);
        orderSearchParam.setPageSize(size * page);
        orderSearchParam.setSortBy("createTime");
        orderSearchParam.setSortType("desc");
        orderSearchParam.setProxyType(new Integer[]{0, 1});
        if (customerId != null && customerId > 0) {
            orderSearchParam.setCustomerIds(new Long[]{customerId});
        }
        Result<PageEntityDTO<OrderInfoDTO>> result = searchOrderInfo(orderSearchParam);
        if (result == null || !result.isSuccess()) {
            return result;
        } else {
            for (OrderInfoDTO orderInfoDTO : result.getData().getRecordList()) {
                tempList.add(orderInfoDTO);
            }
            totalNum += result.getData().getTotalNum();
        }

        //cjdd状态的快修快保单
        OrderSearchParam orderSearchParamOne = new OrderSearchParam();
        BeanUtils.copyProperties(orderSearchParam, orderSearchParamOne);
        orderSearchParamOne.setOrderTag(new Integer[]{3});
        orderSearchParamOne.setPayStatus(new Integer[]{0});
        orderSearchParamOne.setOrderStatus(new String[]{"CJDD"});
        orderSearchParamOne.setProxyType(new Integer[]{0, 1});
        Result<PageEntityDTO<OrderInfoDTO>> resultOne = searchOrderInfo(orderSearchParamOne);
        if (resultOne == null || !resultOne.isSuccess()) {
            return result;
        } else {
            for (OrderInfoDTO orderInfoDTO : resultOne.getData().getRecordList()) {
                tempList.add(orderInfoDTO);
            }
            totalNum += resultOne.getData().getTotalNum();
        }

        //数据排序
        Collections.sort(tempList, new Comparator<OrderInfoDTO>() {
            @Override
            public int compare(OrderInfoDTO o1, OrderInfoDTO o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (null != o1.getCreateTime() && null != o2.getCreateTime()) {
                    if (o1.getCreateTime().before(o2.getCreateTime())) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    if (null != o1.getCreateTime()) {
                        return -1;
                    } else if (null != o2.getCreateTime()) {
                        return 1;
                    }
                }
                return 0;
            }
        });
        for (int i = (page - 1) * size; i < page * size; i++) {
            if (i < tempList.size()) {
                resultList.add(tempList.get(i));
            }
        }

        PageEntityDTO<OrderInfoDTO> resultPage = new PageEntityDTO<>();
        resultPage.setTotalNum(totalNum);
        resultPage.setPageNum(page);
        resultPage.setRecordList(resultList);
        return Result.wrapSuccessfulResult(resultPage);
    }

    /**
     * 获取店铺的待回访工单列表
     *
     * @param customerId
     * @param shopId
     * @param page
     * @param size
     *
     * @return
     */
    @Override
    public Result<PageEntityDTO<ShopFeedBackDTO>> getShopDHFOrderList(Long customerId, Long shopId, Integer page, Integer size) {
        log.info("获取店铺待回访工单列表, customerId={},shopId={},page={},size={}", customerId, shopId, page, size);
        PageEntityDTO<ShopFeedBackDTO> pageEntityDTO = new PageEntityDTO<>();
        DefaultPage<NoteInfo> defaultPage = (DefaultPage<NoteInfo>) shopNoteInfoService.getVisitNoteInfo(shopId, null, false, page, size);
        if (null != defaultPage && !CollectionUtils.isEmpty(defaultPage.getContent())) {

            List<NoteInfo> noteInfos = defaultPage.getContent();
            List<ShopFeedBackDTO> shopFeedBackDTOs = orderInfoAssemble(shopId, noteInfos);
            pageEntityDTO.setPageNum(page);
            pageEntityDTO.setRecordList(shopFeedBackDTOs);
            pageEntityDTO.setTotalNum(new Long(defaultPage.getTotalElements()).intValue());

        }

        return Result.wrapSuccessfulResult(pageEntityDTO);
    }

    /**
     * 根据提醒记录获取对应的工单信息
     * 注:通用处理方法,修改注意
     */
    private List<ShopFeedBackDTO> orderInfoAssemble(Long shopId, List<NoteInfo> noteInfos) {
        List<ShopFeedBackDTO> shopFeedBackDTOs = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();
        for (NoteInfo noteInfo : noteInfos) {
            if (!orderIds.contains(noteInfo.getRelId())) {
                orderIds.add(noteInfo.getRelId());
            }
        }
        List<OrderInfo> orderInfos = orderInfoService.selectByIdsAndShopId(shopId, orderIds);
        Map<Long, OrderInfo> orderInfoMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderInfos)) {
            for (OrderInfo orderInfo : orderInfos) {
                orderInfoMap.put(orderInfo.getId(), orderInfo);
            }
        }
        for (NoteInfo noteInfo : noteInfos) {
            if (orderInfoMap.containsKey(noteInfo.getRelId())) {
                ShopFeedBackDTO shopFeedBackDTO = new ShopFeedBackDTO();
                shopFeedBackDTO.setShopId(shopId);
                shopFeedBackDTO.setOrderId(noteInfo.getRelId());
                shopFeedBackDTO.setMobile(noteInfo.getMobile());
                shopFeedBackDTO.setContactMobile(noteInfo.getContactMobile());
                shopFeedBackDTO.setContactName(noteInfo.getContactName());
                shopFeedBackDTO.setNoteTime(noteInfo.getNoteTime());
                shopFeedBackDTO.setNoteFlag(noteInfo.getNoteFlag());
                OrderInfo orderInfo = orderInfoMap.get(noteInfo.getRelId());
                shopFeedBackDTO.setCarLicense(orderInfo.getCarLicense());
                shopFeedBackDTO.setCustomerCarId(orderInfo.getCustomerCarId());
                shopFeedBackDTO.setOrderTag(orderInfo.getOrderTag());
                shopFeedBackDTO.setOrderStatus(orderInfo.getOrderStatus());
                shopFeedBackDTO.setCarInfo(orderInfo.getCarInfo());
                shopFeedBackDTOs.add(shopFeedBackDTO);
            }
        }
        return shopFeedBackDTOs;
    }

    /**
     * 获取客户的待处理工单(1:待报价 2：未结算 4：已挂账)
     * 注:3：'待回访' 改为 getCustomerDHFList 获取
     *
     * @param flag          1:待报价2:未结算  4：已挂账
     * @param customerCarId 车辆id
     * @param shopId        店铺id
     * @param page
     * @param size
     *
     * @return
     */
    @Override
    public Result<PageEntityDTO<OrderInfoDTO>> getCustomerDCLOrderList(Integer flag, Long customerCarId, Long shopId, Integer page, Integer size, String ver) {
        log.info("[客户待处理信息] 获取客户待处理信息. flag:{},customerCarId:{},shopId:{},page:{},size:{}", flag, customerCarId, shopId, page, size);
        if (flag == null || flag.compareTo(4) > 0 || flag.compareTo(1) < 0) {
            log.error("[客户待处理信息] flag error. flag:{}", flag);
            return (Result<PageEntityDTO<OrderInfoDTO>>) LegendErrorCode.APP_TEMPLATE_ERROR.newResult("获取失败");
        }
        Result<PageEntityDTO<OrderInfoDTO>> lastResult = Result.wrapSuccessfulResult(new PageEntityDTO<OrderInfoDTO>());
        //待报价
        if (flag.compareTo(1) == 0) {
            OrderSearchParam orderSearchParam = new OrderSearchParam();
            orderSearchParam.setShopId(shopId);
            orderSearchParam.setOrderTag(new Integer[]{1, 3});
            orderSearchParam.setPageNum(page);
            orderSearchParam.setPageSize(size);
            orderSearchParam.setProxyType(new Integer[]{0, 1});
            orderSearchParam.setOrderStatus(new String[]{"CJDD"});
            orderSearchParam.setSortBy("createTime");
            orderSearchParam.setSortType("desc");
            if (customerCarId != null && customerCarId > 0) {
                orderSearchParam.setCustomerCarIds(new Long[]{customerCarId});
            }
            lastResult = searchOrderInfo(orderSearchParam);
        } else if (flag.compareTo(2) == 0) {
            if (null == page) {
                page = 1;
            }
            if (null == size || size == 0) {
                size = 10;
            }
            //最后的返回数据
            List<OrderInfoDTO> tempList = new ArrayList<>();
            List<OrderInfoDTO> resultList = new ArrayList<>();
            //总数量
            Integer totalNum = 0;

            OrderSearchParam orderSearchParam = new OrderSearchParam();
            orderSearchParam.setShopId(shopId);
            //app3.1及以上版本待结算的的综合维修单不包括"挂账"状态的
            if (null != ver && ver.compareTo("3.1") < 0) {
                orderSearchParam.setOrderTag(new Integer[]{1, 2, 3});
                orderSearchParam.setPayStatus(new Integer[]{0, 1});
                orderSearchParam.setOrderStatus(new String[]{"DDWC", "DDYFK"});
            } else {
                orderSearchParam.setOrderTag(new Integer[]{1});
                orderSearchParam.setPayStatus(new Integer[]{0});
                orderSearchParam.setOrderStatus(new String[]{"DDWC"});
            }
            orderSearchParam.setPageNum(1);
            orderSearchParam.setProxyType(new Integer[]{0, 1});
            orderSearchParam.setPageSize(size * page);
            orderSearchParam.setSortBy("createTime");
            orderSearchParam.setSortType("desc");
            if (customerCarId != null && customerCarId > 0) {
                orderSearchParam.setCustomerCarIds(new Long[]{customerCarId});
            }
            Result<PageEntityDTO<OrderInfoDTO>> result = searchOrderInfo(orderSearchParam);
            if (result == null || !result.isSuccess()) {
                return result;
            } else {
                for (OrderInfoDTO orderInfoDTO : result.getData().getRecordList()) {
                    tempList.add(orderInfoDTO);
                }
                totalNum += result.getData().getTotalNum();
            }

            //cjdd状态的快修快保单
            OrderSearchParam orderSearchParamOne = new OrderSearchParam();
            BeanUtils.copyProperties(orderSearchParam, orderSearchParamOne);
            orderSearchParamOne.setOrderTag(new Integer[]{3});
            orderSearchParamOne.setPayStatus(new Integer[]{0});
            orderSearchParamOne.setOrderStatus(new String[]{"CJDD"});
            orderSearchParamOne.setProxyType(new Integer[]{0, 1});
            Result<PageEntityDTO<OrderInfoDTO>> resultOne = searchOrderInfo(orderSearchParamOne);
            if (resultOne == null || !resultOne.isSuccess()) {
                return result;
            } else {
                for (OrderInfoDTO orderInfoDTO : resultOne.getData().getRecordList()) {
                    tempList.add(orderInfoDTO);
                }
                totalNum += resultOne.getData().getTotalNum();
            }

            //数据排序
            Collections.sort(tempList, new Comparator<OrderInfoDTO>() {
                @Override
                public int compare(OrderInfoDTO o1, OrderInfoDTO o2) {
                    if (o1 == o2) {
                        return 0;
                    }
                    if (null != o1.getCreateTime() && null != o2.getCreateTime()) {
                        if (o1.getCreateTime().before(o2.getCreateTime())) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (null != o1.getCreateTime()) {
                            return -1;
                        } else if (null != o2.getCreateTime()) {
                            return 1;
                        }
                    }
                    return 0;
                }
            });
            for (int i = (page - 1) * size; i < page * size; i++) {
                if (i < tempList.size()) {
                    resultList.add(tempList.get(i));
                }
            }
            PageEntityDTO<OrderInfoDTO> resultPage = new PageEntityDTO<>();
            resultPage.setTotalNum(totalNum);
            resultPage.setPageNum(page);
            resultPage.setRecordList(resultList);
            lastResult = Result.wrapSuccessfulResult(resultPage);
        } else if (flag.compareTo(4) == 0) {
            OrderSearchParam orderSearchParam = new OrderSearchParam();
            orderSearchParam.setShopId(shopId);
            orderSearchParam.setOrderTag(new Integer[]{1, 2, 3});
            orderSearchParam.setPageNum(page);
            orderSearchParam.setPageSize(size);
            orderSearchParam.setOrderStatus(new String[]{"DDYFK"});
            orderSearchParam.setPayStatus(new Integer[]{1});
            orderSearchParam.setSortBy("id");
            orderSearchParam.setSortType("desc");
            orderSearchParam.setProxyType(new Integer[]{0, 1});
            if (customerCarId != null && customerCarId > 0) {
                orderSearchParam.setCustomerCarIds(new Long[]{customerCarId});
            }
            lastResult = searchOrderInfo(orderSearchParam);
        }
        if (null != lastResult && !lastResult.isSuccess()) {
            log.error("[客户待处理信息] 获取客户待处理信息失败. code:{},message:{}", lastResult.getCode(), lastResult.getMessage());

        }
        return lastResult;
    }

    /**
     * 客户档案-维修工单-获取客户工单列表
     *
     * @Since app3.0
     */
    @Override
    public Result<CustomerRecordDTO> getCustomerOrderInfoToApp(Long shopId, Long customerCarId, Integer page, Integer size) {
        log.info("[dubbo][客户档案] Begin getCustomerOrderRecord().传参:shopId={},customerCarId={}, page={}, size={}", shopId, customerCarId, page, size);
        if (null == shopId) {
            log.error("[dubbo][客户档案] 传入参数错误,shopId=null");
            return LegendErrorCode.APP_PARAM_ERROR.newResult();
        }
        if (null == customerCarId) {
            log.error("[dubbo][客户档案] 传入参数错误,customerCarId=null");
            return LegendErrorCode.APP_PARAM_ERROR.newResult();
        }
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("shopId", shopId);
        searchParams.put("customerCarId", customerCarId);
        if (null == page || page < 1) {
            page = 1;
        }
        if (null == size || size < 1) {
            size = 10;
        }
        searchParams.put("offset", (page - 1) * size);
        searchParams.put("limit", size);
        searchParams.put("sorts", new String[]{"create_time desc"});
        //不搜索无效工单
        String[] orderStatuss = new String[]{"CJDD", "DDBJ", "FPDD", "DDSG", "DDWC", "DDYFK"};
        searchParams.put("orderStatuss", orderStatuss);
        //只查找洗车单、综合维修单、快修快保
        Integer[] orderTags = new Integer[]{1, 2, 3};
        searchParams.put("orderTags", orderTags);
        searchParams.put("proxyTypes", new Integer[]{0, 1});

        //[客户档案]总对象
        CustomerRecordDTO customerRecordDTO = new CustomerRecordDTO();

        Map<String, Object> carParams = new HashMap<>();
        carParams.put("shopId", shopId);
        carParams.put("id", customerCarId);
        //查找车辆的信息
        List<CustomerCar> customerCarList = customerCarService.select(carParams);
        Integer totalNum = orderInfoService.selectCount(searchParams);
        List<OrderInfo> orderInfoList = orderInfoService.select(searchParams);

        List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderInfoList)) {
            //通用的车辆id、车型信息设置
            if (CollectionUtils.isEmpty(customerCarList)) {
                log.error("[dubbo][客户档案] 没有从客户车辆表找到该车辆信息,转为从工单表设置车辆信息，shopId={},customerCarId={}", shopId, customerCarId);
                OrderInfo ord = orderInfoList.get(0);
                customerRecordDTO.setCustomerCarId(ord.getCustomerCarId());
                customerRecordDTO.setLicense(ord.getCarLicense());
                customerRecordDTO.setCarInfo(ord.getCarInfo());
            } else {
                CustomerCar customerCar = customerCarList.get(0);
                customerRecordDTO.setCustomerCarId(customerCar.getId());
                customerRecordDTO.setLicense(customerCar.getLicense());
                customerRecordDTO.setCarInfo(customerCar.getCarInfo());
            }
            //设置返回数据传输对象，工单信息
            for (OrderInfo orderInfo : orderInfoList) {
                OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
                BeanUtils.copyProperties(orderInfo, orderInfoDTO);
                orderInfoDTOList.add(orderInfoDTO);
            }
        }

        customerRecordDTO.setOrderInfoDTOList(orderInfoDTOList);
        customerRecordDTO.setTotalNum(totalNum);
        return Result.wrapSuccessfulResult(customerRecordDTO);
    }

    /**
     * 客户档案- 回访记录
     *
     * @Since app3.0
     */
    @Override
    public Result<PageEntityDTO<ShopFeedBackDTO>> getCustomerFeedbackToApp(Long shopId, Long customerCarId, Integer page, Integer size) {
        log.info("[dubbo][客户档案][回访记录] shopId={},customerCarId={},page={},size={}", shopId, customerCarId, page, size);
        if (null == shopId) {
            log.error("[dubbo][客户档案][回访记录] 传入参数错误,shopId=null");
            return LegendErrorCode.APP_SHOP_ID_ERROR.newResult();
        }
        if (null == customerCarId) {
            log.error("[dubbo][客户档案][回访记录] 传入参数错误,customerCarId=null");
            return LegendErrorCode.APP_PARAM_ERROR.newResult();
        }
        if (null == page || page < 1) {
            page = 1;
        }
        if (null == size || size < 1) {
            size = 10;
        }
        PageEntityDTO<ShopFeedBackDTO> pageEntityDTO = new PageEntityDTO<>();
        DefaultPage<NoteInfo> defaultPage = (DefaultPage) shopNoteInfoService.getVisitNoteInfo(shopId, customerCarId, null, page, size);
        if (null != defaultPage && !CollectionUtils.isEmpty(defaultPage.getContent())) {
            List<NoteInfo> noteInfos = defaultPage.getContent();
            List<ShopFeedBackDTO> shopFeedBackDTOs = orderInfoAssemble(shopId, noteInfos);
            pageEntityDTO.setPageNum(page);
            pageEntityDTO.setRecordList(shopFeedBackDTOs);
            pageEntityDTO.setTotalNum(new Long(defaultPage.getTotalElements()).intValue());

        }

        return Result.wrapSuccessfulResult(pageEntityDTO);
    }

    @Override
    public Result<PageEntityDTO<OrderInfoDTO>> getShopYGZOrderList(Long customerId, Long shopId, Integer page, Integer size) {
        if (null == page) {
            page = 1;
        }
        if (null == size || size == 0) {
            size = 10;
        }
        log.info("[店铺代办事项] 店铺已挂账工单列表,customerId={}, shopId={}, page={}, size={}", customerId, shopId, page, size);
        OrderSearchParam orderSearchParam = new OrderSearchParam();
        orderSearchParam.setShopId(shopId);
        orderSearchParam.setPageNum(page);
        orderSearchParam.setPageSize(size);
        orderSearchParam.setPayStatus(new Integer[]{1});
        orderSearchParam.setOrderStatus(new String[]{"DDYFK"});
        orderSearchParam.setOrderTag(new Integer[]{1, 2, 3});
        orderSearchParam.setProxyType(new Integer[]{0, 1});
        orderSearchParam.setSortBy("createTime");
        orderSearchParam.setSortType("desc");
        if (customerId != null && customerId > 0) {
            orderSearchParam.setCustomerIds(new Long[]{customerId});
        }
        return searchOrderInfo(orderSearchParam);
    }

    /**
     * 创建回访单
     *
     * @param customerFeedbackDTO
     *
     * @return
     */
    @Override
    @Transactional
    public Result<Boolean> saveCustomerFeedback(CustomerFeedbackDTO customerFeedbackDTO) {
        Long shopId = customerFeedbackDTO.getShopId();
        Long orderId = customerFeedbackDTO.getOrderId();
        if (shopId == null || shopId < 1) {
            log.error("[dubbo][App创建工单回访单] 传入参数错误, shopId={}", shopId);
            return LegendErrorCode.APP_SHOP_ID_ERROR.newResult();
        }

        if (orderId == null || orderId < 1) {
            log.error("[dubbo][App创建工单回访单] 传入参数错误, orderId={}", orderId);
            return LegendErrorCode.APP_ORDER_ID_ERROR.newResult();
        }

        CustomerFeedback customerFeedback = new CustomerFeedback();
        customerFeedback.setShopId(shopId);
        customerFeedback.setOrderId(orderId);

        customerFeedback.setReceptionStar(customerFeedbackDTO.getReceptionStar());
        customerFeedback.setRepairStar(customerFeedbackDTO.getRepairStar());
        customerFeedback.setSendcarStar(customerFeedbackDTO.getSendcarStar());
        customerFeedback.setTotalStar(customerFeedbackDTO.getTotalStar());
        customerFeedback.setCustomerFeedback(customerFeedbackDTO.getCustomerFeedback());
        customerFeedback.setVisitorId(customerFeedbackDTO.getUserId());
        String userName = "";
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerId(shopId, customerFeedbackDTO.getUserId());
        if (shopManager != null) {
            userName = shopManager.getName();
        }
        customerFeedback.setVisitorName(userName);

        customerFeedback.setVisitMethod(customerFeedbackDTO.getVisitMethod());
        customerFeedback.setVisitTime(new Date());
        customerFeedback.setRefer(customerFeedbackDTO.getRefer());
        customerFeedback.setVer(customerFeedbackDTO.getVer());

        // 设置noteInfo已处理
        NoteInfo noteInfo = shopNoteInfoService.getVisitNoteInfoByOrderId(shopId, orderId);
        if (noteInfo != null) {
            customerFeedback.setNoteInfoId(noteInfo.getId());
            customerFeedback.setNoteType(noteInfo.getNoteType());

            NoteInfo updateNoteInfo = new NoteInfo();
            updateNoteInfo.setId(noteInfo.getId());
            updateNoteInfo.setNoteFlag(1);
            updateNoteInfo.setNoteWay(1);
            updateNoteInfo.setOperator(userName);
            updateNoteInfo.setOperatorTime(new Date());
            int updateResult = shopNoteInfoService.updateById(updateNoteInfo);
            if (updateResult == 1) {
                // 打印日志
                log.info(ShopNoteInfoLog.handleNoteLog(shopId));
            }
        }

        com.tqmall.legend.common.Result result = customerFeedbackService.feedback(customerFeedback);
        if (result.isSuccess()) {
            // 设置orderInfo已回访
            orderInfoService.updateOrderVisit(shopId, orderId);
            return Result.wrapSuccessfulResult(true);
        } else {
            return LegendErrorCode.APP_SAVE_CUSTOMERFEEDBACK_ERROR.newResult();
        }
    }

    /**
     * 客户待回访记录列表
     *
     * @param shopId        门店id
     * @param customerCarId 车辆id
     * @param page
     * @param size
     *
     * @return
     */
    @Override
    public Result<PageEntityDTO<ShopFeedBackDTO>> getCustomerDHFList(Long shopId, Long customerCarId, Integer page, Integer size) {
        log.info("[客户待回访工单] 获取店铺待回访工单列表, shopId:{}, customerCarId:{}, page:{}, size:{}", shopId, customerCarId, page, size);
        PageEntityDTO<ShopFeedBackDTO> pageEntityDTO = new PageEntityDTO<>();
        DefaultPage<NoteInfo> defaultPage = (DefaultPage) shopNoteInfoService.getVisitNoteInfo(shopId, customerCarId, false, page, size);
        if (null != defaultPage && !CollectionUtils.isEmpty(defaultPage.getContent())) {
            List<NoteInfo> noteInfos = defaultPage.getContent();
            List<ShopFeedBackDTO> shopFeedBackDTOs = orderInfoAssemble(shopId, noteInfos);
            pageEntityDTO.setPageNum(page);
            pageEntityDTO.setRecordList(shopFeedBackDTOs);
            pageEntityDTO.setTotalNum(new Long(defaultPage.getTotalElements()).intValue());

        }

        return Result.wrapSuccessfulResult(pageEntityDTO);
    }

    /**
     * 根据工单ID判断工单是否有预付定金
     *
     * @param orderId
     *
     * @return
     */
    @Override
    public Result<Boolean> hasDownPayment(Long orderId) {
        OrderInfo orderInfo = orderInfoService.selectById(orderId);
        if (null == orderInfo.getDownPayment()) {
            return Result.wrapSuccessfulResult(false);
        }
        if (orderInfo.getDownPayment().compareTo(BigDecimal.ZERO) > 0) {
            return Result.wrapSuccessfulResult(true);
        }
        return Result.wrapSuccessfulResult(false);
    }

    /**
     * 根据门店id获取排序后的洗车工列表
     *
     * @param shopId
     *
     * @return
     */
    @Override
    public com.tqmall.core.common.entity.Result<List<WorkerDTO>> getCarWashWorkerList(final Long shopId) {
        return new ApiTemplate<List<WorkerDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<WorkerDTO> process() throws BizException {
                List<WorkerDTO> workerDTOList = Lists.newArrayList();
                com.tqmall.core.common.entity.Result<List<PopularDataDTO>> carWashWorkerListResult = null;
                try {
                    carWashWorkerListResult = rpcPopularSortService.getCarWashWorkerList(shopId);
                } catch (Exception e) {
                    log.error("调用cube获取洗车工列表异常", e);
                    return workerDTOList;
                }
                if (!carWashWorkerListResult.isSuccess() && CollectionUtils.isEmpty(carWashWorkerListResult.getData())) {
                    return workerDTOList;
                }
                List<PopularDataDTO> popularDataDTOList = carWashWorkerListResult.getData();
                for (PopularDataDTO popularDataDTO : popularDataDTOList) {
                    WorkerDTO workerDTO = new WorkerDTO();
                    workerDTO.setId(popularDataDTO.getId());
                    workerDTO.setName(popularDataDTO.getName());
                    workerDTOList.add(workerDTO);
                }
                return workerDTOList;
            }
        }.execute();
    }

    @Override
    public com.tqmall.core.common.entity.Result<Long> createSpeedilyOrder(final CreateParam createParam) {
        return new ApiTemplate<Long>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(createParam, "创建订单实体不能为空");
                Assert.notNull(createParam.getOrderInfoParam(), "订单信息不能为空");
            }

            @Override
            protected Long process() throws BizException {
                OrderFormEntityBo orderFormEntityBo = convertToBO(createParam);
                return orderServicesFacade.createSpeedilyOrderForApp(orderFormEntityBo);
            }
        }.execute();
    }

    @Override
    public com.tqmall.core.common.entity.Result<Long> updateSpeedilyOrder(final CreateParam createParam) {
        return new ApiTemplate<Long>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(createParam, "创建订单实体不能为空");
                Assert.notNull(createParam.getOrderInfoParam(), "订单信息不能为空");
            }

            @Override
            protected Long process() throws BizException {
                OrderFormEntityBo orderFormEntityBo = convertToBO(createParam);
                return orderServicesFacade.updateSpeedilyOrderForApp(orderFormEntityBo);
            }
        }.execute();
    }

    /**
     * app预检单 加入保养提醒表
     *
     * @param saveAndUpdateServiceWarnParam
     *
     * @return
     */
    @Override
    public com.tqmall.core.common.entity.Result<String> saveAndUpdateServiceWarns(SaveAndUpdateServiceWarnParam saveAndUpdateServiceWarnParam) {
        CustomerCar customerCar = new CustomerCar();
        BeanUtils.copyProperties(saveAndUpdateServiceWarnParam, customerCar);
        return com.tqmall.core.common.entity.Result.wrapSuccessfulResult("保存或者更新保养提醒信息成功");
    }

    @Override
    public com.tqmall.core.common.entity.Result<Boolean> saveWashCarService(final WashCarServiceParam washCarServiceParam) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(washCarServiceParam, "洗车服务价格实体不能为空");
                Assert.notNull(washCarServiceParam.getShopId(), "门店ID不能为空");
                Assert.isTrue(washCarServiceParam.getShopId().longValue() > 0, "门店ID错误");
                Assert.isTrue(!CollectionUtils.isEmpty(washCarServiceParam.getServicePriceParams()), "服务列表不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                List<ShopServiceInfo> shopServiceInfos = BdUtil.bo2do4List(washCarServiceParam.getServicePriceParams(), ShopServiceInfo.class);
                com.tqmall.legend.common.Result result = shopServiceInfoService.saveWashCarServiceList(shopServiceInfos, washCarServiceParam.getShopId());
                if (!result.isSuccess()) {
                    throw new BizException(result.getErrorMsg());
                }
                return Boolean.TRUE;
            }
        }.execute();
    }

    /**
     * 快修快保单创建实体对象转换 param -> bo
     *
     * @param createParam
     *
     * @return
     */
    private OrderFormEntityBo convertToBO(CreateParam createParam) {
        OrderInfoParam orderInfoParam = createParam.getOrderInfoParam();
        List<ServiceParam> serviceParams = createParam.getServiceParams();
        List<GoodsParam> goodsParams = createParam.getGoodsParams();
        OrderFormEntityBo orderFormEntityBo = new OrderFormEntityBo();
        orderFormEntityBo.setShopId(createParam.getShopId());
        orderFormEntityBo.setUserId(createParam.getUserId());
        orderFormEntityBo.setVirtualOrderId(createParam.getVirtualOrderId());
        orderFormEntityBo.setImgUrl(createParam.getImgUrl());
        orderFormEntityBo.setUserName(createParam.getUserName());
        orderFormEntityBo.setVer(createParam.getVer());
        orderFormEntityBo.setRefer(createParam.getRefer());
        OrderInfo orderInfo = BdUtil.bo2do(orderInfoParam, OrderInfo.class);
        //车牌处理start
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.isBlank(carLicense)) {
            throw new BizException("车牌为空");
        }
        carLicense = carLicense.replace(" ", "").toUpperCase();
        orderInfo.setCarLicense(carLicense);
        //车牌处理end
        List<OrderServices> orderServices = BdUtil.bo2do4List(serviceParams, OrderServices.class);
        List<OrderGoods> orderGoodss = null;
        if (!CollectionUtils.isEmpty(goodsParams)) {
            orderGoodss = new ArrayList<>();
            for (GoodsParam goodsParam : goodsParams) {
                OrderGoods orderGoods = new OrderGoods();
                BeanUtils.copyProperties(goodsParam, orderGoods);
                if (null != goodsParam.getGoodsNumber()) {
                    orderGoods.setGoodsNumber(BigDecimal.valueOf(goodsParam.getGoodsNumber().longValue()));
                }
                orderGoodss.add(orderGoods);
            }
        }

        Gson gson = new Gson();
        String orderServicesJson = gson.toJson(orderServices);
        String orderGoodssJson = gson.toJson(orderGoodss);
        orderFormEntityBo.setOrderInfo(orderInfo);
        orderFormEntityBo.setOrderServiceJson(orderServicesJson);
        orderFormEntityBo.setOrderGoodJson(orderGoodssJson);
        return orderFormEntityBo;
    }
}
