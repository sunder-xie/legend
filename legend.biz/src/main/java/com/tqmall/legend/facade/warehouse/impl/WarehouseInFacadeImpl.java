package com.tqmall.legend.facade.warehouse.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.result.supplier.SupplierDTO;
import com.tqmall.legend.billcenter.client.RpcPayService;
import com.tqmall.legend.billcenter.client.dto.PayBillDTO;
import com.tqmall.legend.billcenter.client.enums.PayTypeEnum;
import com.tqmall.legend.billcenter.client.param.PayInvalidParam;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.FinalInventoryService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.settlement.WarehouseInPaymentService;
import com.tqmall.legend.biz.shop.SupplierService;
import com.tqmall.legend.biz.warehousein.WarehouseInDetailService;
import com.tqmall.legend.biz.warehousein.WarehouseInService;
import com.tqmall.legend.entity.goods.FinalInventory;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.InvoiceTypeEnum;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Supplier;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.enums.settlement.PayStatusEnum;
import com.tqmall.legend.enums.warehouse.PayMethodEnum;
import com.tqmall.legend.enums.warehouse.WarehouseInStatusEnum;
import com.tqmall.legend.facade.supplier.SupplierFacade;
import com.tqmall.legend.facade.supplier.bo.SupplierBo;
import com.tqmall.legend.facade.warehouse.WarehouseInFacade;
import com.tqmall.legend.facade.warehouse.bo.WarehouseInDetailBo;
import com.tqmall.legend.facade.warehouse.vo.LegendWarehouseInDTOVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseInDetailVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseInVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.PageableResponseExtend;
import com.tqmall.search.dubbo.client.legend.warehousein.param.LegendWarehouseInParam;
import com.tqmall.search.dubbo.client.legend.warehousein.result.LegendWarehouseInDTO;
import com.tqmall.search.dubbo.client.legend.warehousein.result.LegendWarehouseInDetail;
import com.tqmall.search.dubbo.client.legend.warehousein.service.LegendWarehouseInService;
import com.tqmall.tqmallstall.domain.param.BaseQueryParam;
import com.tqmall.tqmallstall.domain.tc.OrderDetailDTO;
import com.tqmall.tqmallstall.domain.tc.OrderGoodsDTO;
import com.tqmall.tqmallstall.service.tc.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sven on 16/7/31.
 */
@Service
@Slf4j
public class WarehouseInFacadeImpl extends BaseServiceImpl implements WarehouseInFacade {
    @Resource
    private WarehouseInService warehouseInService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private WarehouseInDetailService warehouseInDetailService;
    @Resource
    private RpcPayService rpcPayService;
    @Resource
    private WarehouseInPaymentService paymentService;
    @Resource
    private FinalInventoryService finalInventoryService;
    @Resource
    private MaxSnService maxSnService;
    @Resource
    private ShopManagerService shopManagerService;
    @Resource
    private SupplierFacade supplierFacade;
    @Resource
    private OrderService orderService;
    @Resource
    private LegendWarehouseInService legendWarehouseInService;
    @Resource
    private SupplierService supplierService;

    @Override
    public String getSn(Long shopId, String prefix) {
        return maxSnService.getMaxSn(shopId, prefix);
    }

    @Override
    @Transactional
    public Long saveDraft(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo) {
        checkData(warehouseIn, warehouseInDetailList);
        warehouseIn.setStatus(WarehouseInStatusEnum.DRAFT.name());
        Long warehouseInId = saveWarehouseIn(warehouseIn, userInfo);
        saveWarehouseInDetail(warehouseIn, warehouseInDetailList);
        return warehouseInId;
    }

    /**
     * 入库单更新
     * 详情删除后重新插入
     *
     * @param warehouseIn
     * @param warehouseInDetailList
     */
    @Override
    @Transactional
    public void updateDraft(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo) {
        checkData(warehouseIn, warehouseInDetailList);
        warehouseIn.setModifier(userInfo.getUserId());
        warehouseIn.setShopId(userInfo.getShopId());
        warehouseIn.setPaymentStatus(PayStatusEnum.DDZF.name());
        warehouseIn.setAmountPayable(warehouseIn.getTotalAmount());
        if (BigDecimal.ZERO.compareTo(warehouseIn.getTotalAmount()) == 0) {
            warehouseIn.setPaymentStatus(PayStatusEnum.ZFWC.name());
        }
        //更新入库单草稿
        warehouseInService.updateById(warehouseIn);
        Map<String, Object> param = new HashMap<>();
        param.put("warehouseInId", warehouseIn.getId());
        param.put("shopId", userInfo.getShopId());
        param.put("status", WarehouseInStatusEnum.DRAFT.name());
        //删除对应的入库单详情
        warehouseInDetailService.delete(param);
        //获取完整的入库单信息
        WarehouseIn warehouse = warehouseInService.selectByIdAndShopId(warehouseIn.getId(), userInfo.getShopId());
        //插入新的入库单详情
        saveWarehouseInDetail(warehouse, warehouseInDetailList);
    }

    /**
     * 1.检验是否草稿状态
     *
     * @param id
     * @param userInfo
     */
    @Override
    @Transactional
    public void draftToStock(Long id, UserInfo userInfo, boolean push) {
        Long shopId = userInfo.getShopId();
        WarehouseIn warehouseIn = warehouseInService.selectByIdAndShopId(id, shopId);
        if (warehouseIn == null) {
            log.error("该草稿不存在,id:", id);
            throw new BizException("该草稿不存在,入库失败");
        }
        if (!WarehouseInStatusEnum.DRAFT.name().equals(warehouseIn.getStatus())) {
            log.error("该草稿已入库,id:", id);
            throw new BizException("该草稿已入库,请勿重复操作");
        }
        WarehouseIn warehouse = new WarehouseIn();
        warehouse.setId(warehouseIn.getId());
        warehouse.setShopId(shopId);
        warehouse.setStatus(WarehouseInStatusEnum.LZRK.name());
        warehouseInService.updateById(warehouse);
        Map<String, Object> param = new HashMap<>();
        param.put("warehouseInId", warehouseIn.getId());
        param.put("shopId", shopId);
        param.put("status", WarehouseInStatusEnum.LZRK.name());
        warehouseInDetailService.update(param);
        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("warehouseInId", warehouseIn.getId());
        searchParam.put("shopId", shopId);
        List<WarehouseInDetail> warehouseInDetailList = warehouseInDetailService.select(searchParam);
        if (CollectionUtils.isEmpty(warehouseInDetailList)) {
            log.error("草稿对应详情不存在,id:", id);
            throw new BizException("草稿对应详情不存在,入库失败");
        }
        for (WarehouseInDetail warehouseInDetail : warehouseInDetailList) {
            warehouseInDetail.setStatus(WarehouseInStatusEnum.LZRK.name());
        }
        calculatePriceAndAmount(warehouseInDetailList, userInfo, false);
        //推送到结算中心(编辑转入库数据还未存到数据库,不采用这个数据更新)
        if (push) {
            List<WarehouseIn> warehouseInList = Lists.newArrayList(warehouseIn);
            pushToBillCenter(warehouseInList);
        }

    }

    @Override
    @Transactional
    public void draftToStock(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo) {
        updateDraft(warehouseIn, warehouseInDetailList, userInfo);
        draftToStock(warehouseIn.getId(), userInfo, false);
        warehouseIn.setShopId(userInfo.getShopId());
        warehouseIn.setCreator(userInfo.getUserId());
        warehouseIn.setStatus(WarehouseInStatusEnum.LZRK.name());
        List<WarehouseIn> warehouseInList = Lists.newArrayList(warehouseIn);
        pushToBillCenter(warehouseInList);
    }

    @Override
    @Transactional
    public boolean delete(Long id, Long shopId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("shopId", shopId);
        WarehouseIn warehouseIn = warehouseInService.selectByIdAndShopId(id, shopId);
        if (warehouseIn == null) {
            return false;
        }
        if (WarehouseInStatusEnum.HZRK.name().equals(warehouseIn.getStatus()) ||
                WarehouseInStatusEnum.LZRK.name().equals(warehouseIn.getStatus())) {
            throw new BizException("入库、退货记录不允许删除");
        }
        int count = warehouseInService.delete(map);
        Map<String, Object> param = new HashMap<>();
        param.put("warehouseInId", id);
        param.put("shopId", shopId);
        int total = warehouseInDetailService.delete(param);
        if (count > 0 && total > 0) {
            return true;
        }
        return false;
    }

    /**
     * 1.检查数量是否大于蓝字入库对应的数量
     * 2.保存红字入库单
     * 3.保存详情
     * 4.更新对应蓝字入库详情
     * 5.更新单价和库存
     *
     * @param warehouseIn
     * @param warehouseInDetailList
     * @param userInfo
     * @return
     */
    @Override
    @Transactional
    public Long stockRefund(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo) {
        checkData(warehouseIn, warehouseInDetailList);
        warehouseIn.setStatus(WarehouseInStatusEnum.HZRK.name());
        Long warehouseInId = saveWarehouseIn(warehouseIn, userInfo);
        //检查数量并更新蓝字入库详情
        checkAmountAndUpdate(warehouseIn, warehouseInDetailList);
        warehouseInDetailList = saveWarehouseInDetail(warehouseIn, warehouseInDetailList);
        //更新价格及库存
        calculatePriceAndAmount(warehouseInDetailList, userInfo, false);
        //推送到结算中心
        List<WarehouseIn> warehouseInList = Lists.newArrayList(warehouseIn);
        pushToBillCenter(warehouseInList);
        return warehouseInId;
    }

    @Override
    @Transactional
    public Long stock(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList, UserInfo userInfo) {
        checkTqmallPurchase(warehouseIn.getPurchaseSn());
        warehouseIn.setStatus(WarehouseInStatusEnum.LZRK.name());
        Long id = saveWarehouseIn(warehouseIn, userInfo);
        warehouseInDetailList = saveWarehouseInDetail(warehouseIn, warehouseInDetailList);
        calculatePriceAndAmount(warehouseInDetailList, userInfo, false);
        //推送到结算中心
        List<WarehouseIn> warehouseInList = Lists.newArrayList(warehouseIn);
        pushToBillCenter(warehouseInList);
        return id;
    }

    @Override
    public DefaultPage<LegendWarehouseInDTOVo> select(LegendWarehouseInParam param, PageableRequest pageableRequest) {
        DefaultPage<LegendWarehouseInDTOVo> defaultPage = new DefaultPage<>(new ArrayList<LegendWarehouseInDTOVo>());
        log.info("[DUBBO]调用搜索入库单查询接口,入参:{}", LogUtils.objectToString(param));
        com.tqmall.search.common.result.Result<PageableResponseExtend<LegendWarehouseInDTO>> result = legendWarehouseInService.queryWithAggs(param, pageableRequest);
        if (!result.isSuccess()) {
            log.error("[DUBBO] 调用入库单查询搜索接口失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("列表数据初始化错误");
        }
        if (result.getData() == null || CollectionUtils.isEmpty(result.getData().getContent())) {
            return defaultPage;
        }
        List<LegendWarehouseInDTO> legendWarehouseInDTOs = result.getData().getContent();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        for (LegendWarehouseInDTO legendWarehouseInDTO : legendWarehouseInDTOs) {
            BigDecimal total = new BigDecimal(legendWarehouseInDTO.getTotalAmount());
            legendWarehouseInDTO.setTotalAmount(df.format(total));
            String time = legendWarehouseInDTO.getInTime();
            time = DateUtil.convertDateToYMDHHmm(DateUtil.convertStringToDate(time));
            legendWarehouseInDTO.setInTime(time);
        }
        List<LegendWarehouseInDTOVo> voList = new ArrayList<>();
        for (LegendWarehouseInDTO dto : legendWarehouseInDTOs) {
            LegendWarehouseInDTOVo vo = new LegendWarehouseInDTOVo();
            BeanUtils.copyProperties(dto, vo);
            voList.add(vo);
        }
        Long shopId = Long.parseLong(param.getShopId());
        voList = perfectDetail(voList, shopId);
        defaultPage = new DefaultPage<>(voList, pageableRequest, result.getData().getTotalElements());
        defaultPage.setOtherData(result.getData().getExtend());
        return defaultPage;
    }

    @Override
    public WarehouseInVo select(Long id, Long shopId) {
        if (id == null || shopId == null) {
            return null;
        }
        WarehouseInVo warehouseInVo = new WarehouseInVo();
        WarehouseIn warehouseIn = warehouseInService.selectByIdAndShopId(id, shopId);
        if (warehouseIn == null) {
            return null;
        }
        // 设置红字入库对应蓝字relSn
        if (WarehouseInStatusEnum.HZRK.name().equals(warehouseIn.getStatus()) || WarehouseInStatusEnum.HZZF.name().equals(warehouseIn.getStatus())) {
            WarehouseIn warehouse = warehouseInService.selectByIdAndShopId(warehouseIn.getRelId(), shopId);
            if (warehouse != null) {
                warehouseInVo.setRelSn(warehouse.getWarehouseInSn());
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("warehouseInId", id);
        List<WarehouseInDetail> detailList = warehouseInDetailService.select(param);
        BeanUtils.copyProperties(warehouseIn, warehouseInVo);
        //若入库时间不存在则 将创建时间作为入库时间
        if (warehouseIn.getInTime() == null) {
            warehouseInVo.setInTime(warehouseInVo.getGmtCreate());
        }
        //设置库存
        List<WarehouseInDetailVo> detailVoList = getStock(detailList);
        warehouseInVo.setDetailList(detailVoList);
        //设置开单人
        ShopManager shopManager = shopManagerService.selectById(warehouseIn.getCreator());
        if (shopManager != null) {
            warehouseInVo.setOperatorName(shopManager.getName());
        }
        return warehouseInVo;
    }

    @Override
    @Transactional
    public void abolishStock(Long id, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        WarehouseIn warehouseIn = warehouseInService.selectByIdAndShopId(id, shopId);
        if (warehouseIn == null) {
            log.error("该入库单不存在,id:", id);
            throw new BizException("该入库单不存在,作废失败");
        }
        //判断是否已作废
        if (WarehouseInStatusEnum.LZZF.name().equals(warehouseIn.getStatus()) ||
                WarehouseInStatusEnum.HZZF.name().equals(warehouseIn.getStatus())) {
            log.error("该入库单已作废,id:", id);
            throw new BizException("该入库单已作废,不能重复作废");
        }
        //蓝字入库验证是否有退货记录
        if (WarehouseInStatusEnum.LZRK.name().equals(warehouseIn.getStatus())) {
            Map<String, Object> param = new HashMap<>();
            param.put("relId", id);
            param.put("shopId", shopId);
            param.put("status", WarehouseInStatusEnum.HZRK.name());
            List<WarehouseIn> warehouseInList = warehouseInService.select(param);
            if (!CollectionUtils.isEmpty(warehouseInList)) {
                for (WarehouseIn warehouse : warehouseInList) {
                    abolishStock(warehouse.getId(), userInfo);
                }
            }
        }

        //更新入库单
        WarehouseIn newWarehouseIn = new WarehouseIn();
        newWarehouseIn.setId(id);
        newWarehouseIn.setShopId(shopId);
        newWarehouseIn.setModifier(userInfo.getUserId());
        newWarehouseIn.setStatus(WarehouseInStatusEnum.LZZF.name());
        if (WarehouseInStatusEnum.HZRK.name().equals(warehouseIn.getStatus())) {
            newWarehouseIn.setStatus(WarehouseInStatusEnum.HZZF.name());
        }
        warehouseInService.updateById(newWarehouseIn);
        //更新入库详情
        Map<String, Object> detailParam = new HashMap<>();
        detailParam.put("warehouseInId", id);
        detailParam.put("shopId", shopId);
        detailParam.put("status", WarehouseInStatusEnum.LZRK.name());
        List<WarehouseInDetail> details = null;
        if (WarehouseInStatusEnum.HZRK.name().equals(warehouseIn.getStatus())) {
            detailParam.put("status", WarehouseInStatusEnum.HZRK.name());
            //红字作废查询出对应蓝字入库详情
            Map<String, Object> map = new HashMap<>();
            map.put("shopId", shopId);
            map.put("warehouseInId", warehouseIn.getRelId());
            details = warehouseInDetailService.select(map);
            if (CollectionUtils.isEmpty(details)) {
                log.error("入库详情不存在,warehouseId:", warehouseIn.getRelId());
                throw new BizException("入库详情不存在,作废失败");
            }
        }
        List<WarehouseInDetail> detailList = warehouseInDetailService.select(detailParam);
        if (CollectionUtils.isEmpty(detailList)) {
            log.error("入库详情不存在,id:", id);
            throw new BizException("入库单详情不存在,作废失败");
        }
        //更新入库单详情
        for (WarehouseInDetail detail : detailList) {
            WarehouseInDetail warehouseInDetail = new WarehouseInDetail();
            warehouseInDetail.setId(detail.getId());
            warehouseInDetail.setShopId(shopId);
            warehouseInDetail.setGoodsRealCount(BigDecimal.ZERO);
            warehouseInDetail.setStatus(WarehouseInStatusEnum.LZZF.name());
            //红字作废将数量加到对应的蓝字入库详情
            if (WarehouseInStatusEnum.HZRK.name().equals(detail.getStatus())) {
                warehouseInDetail.setStatus(WarehouseInStatusEnum.HZZF.name());
                for (WarehouseInDetail inDetail : details) {
                    if (detail.getGoodsId().equals(inDetail.getGoodsId())) {
                        WarehouseInDetail detailTemp = new WarehouseInDetail();
                        detailTemp.setGoodsRealCount(inDetail.getGoodsRealCount().add(detail.getGoodsCount().abs()));
                        detailTemp.setId(inDetail.getId());
                        detailTemp.setShopId(inDetail.getShopId());
                        warehouseInDetailService.updateById(detailTemp);
                        break;
                    }
                }
            }
            warehouseInDetailService.updateById(warehouseInDetail);
        }
        //更新单价,数量
        Map<String, Object> params = new HashMap<>();
        params.put("warehouseInId", id);
        params.put("shopId", shopId);
        List<WarehouseInDetail> warehouseInDetailList = warehouseInDetailService.select(params);
        calculatePriceAndAmount(warehouseInDetailList, userInfo, true);

        //删除流水记录
        Map<String, Object> param = new HashMap<>();
        param.put("warehouseInId", id);
        param.put("shopId", shopId);
        paymentService.delete(param);
        //推送到结算中心
        try {
            abolishToBillCenter(warehouseIn.getId(), userInfo);
        } catch (Exception e) {
            log.error("[DUBBO] 调用结算中心接口添加付款单失败,错误原因:{}", e);
            throw new RuntimeException("[DUBBO] 调用结算中心接口添加付款单失败", e);
        }
    }

    @Override
    public WarehouseInVo getTqmallStockInfo(String orderSn, Long uid, UserInfo userInfo) {
        WarehouseInVo warehouseInVo = new WarehouseInVo();
        BaseQueryParam param = new BaseQueryParam();
        param.setUid(uid.intValue());
        log.info("[DUBBO] 调用淘汽档口获取淘汽采购商品详情接口 入参orderSn:{},uid:{}", orderSn, uid);
        try {
            Result<OrderDetailDTO> result = orderService.details(null, orderSn, param);
            if (!result.isSuccess()) {
                log.error("[DUBBO] 调用淘汽档口获取淘汽采购商品详情接口失败,错误原因:{}", JSONUtil.object2Json(result));
            }
            OrderDetailDTO orderDetailDTO = result.getData();
            if (orderDetailDTO == null) {
                log.error("该采购单不存在,orderSn:{},uid:{}", orderSn, uid);
                throw new BizException("该采购单不存在");
            }
            warehouseInVo.setOperatorName(userInfo.getName());
            warehouseInVo.setInTime(new Date());
            warehouseInVo.setGoodsAmount(new BigDecimal(orderDetailDTO.getGoodsAmount()));
            if (orderDetailDTO.getDiscount() != null) {
                warehouseInVo.setGoodsAmount(new BigDecimal(orderDetailDTO.getGoodsAmount()).subtract(new BigDecimal(orderDetailDTO.getDiscount())));
            }
            if (orderDetailDTO.getDeductAmount() != null) {
                warehouseInVo.setTotalAmount(new BigDecimal(orderDetailDTO.getOrderPrice()).add(orderDetailDTO.getDeductAmount()));
            }
            warehouseInVo.setInvoiceTypeName(orderDetailDTO.getInvType());
            warehouseInVo.setInvoiceType(InvoiceTypeEnum.getTypeByValue(orderDetailDTO.getInvType()));

            //获取淘汽档口供应商
            Map<String,Object> supplierMap = new HashMap<>();
            supplierMap.put("shopId", userInfo.getShopId());
            supplierMap.put("supplierName", "淘汽档口");
            List<Supplier> supplierList = supplierService.select(supplierMap);
            if(!CollectionUtils.isEmpty(supplierList)) {
                Supplier supplier = supplierList.get(0);
                warehouseInVo.setSupplierId(supplier.getId());
                warehouseInVo.setSupplierName(supplier.getSupplierName());
            }

            warehouseInVo.setFreight(new BigDecimal(orderDetailDTO.getShippingFee()));
            warehouseInVo.setPurchaseSn(orderDetailDTO.getOrderSn());
            warehouseInVo.setContact(orderDetailDTO.getConsignee());
            warehouseInVo.setContactMobile(orderDetailDTO.getMobile());
            List<OrderGoodsDTO> goodsList = result.getData().getGoods();
            if (!CollectionUtils.isEmpty(goodsList)) {
                List<WarehouseInDetail> warehouseInDetails = new ArrayList<>();
                for (OrderGoodsDTO goods : goodsList) {
                    WarehouseInDetail detail = new WarehouseInDetailVo();
                    detail.setTqmallGoodsId(goods.getGoodsId().longValue());
                    detail.setTqmallGoodsSn(goods.getGoodsSn());
                    BigDecimal goodsCount = BigDecimal.ZERO;
                    BigDecimal conversionValue = new BigDecimal(goods.getConversionValue());
                    if (goods.getGoodsNumber() != null) {
                        goodsCount = new BigDecimal(goods.getGoodsNumber()).multiply(conversionValue);
                    }
                    detail.setGoodsCount(goodsCount);
                    BigDecimal purchasePrice = BigDecimal.ZERO;

                    if (goods.getSoldPrice() != null) {
                        purchasePrice = new BigDecimal(goods.getSoldPrice());
                    }
                    purchasePrice = purchasePrice.divide(conversionValue, 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal purchaseAmount = goodsCount.multiply(purchasePrice);
                    detail.setPurchasePrice(purchasePrice);
                    detail.setPurchaseAmount(purchaseAmount);
                    detail.setGoodsName(goods.getGoodsName());
                    detail.setRelSn(orderSn);
                    detail.setMeasureUnit(goods.getMinMeasureUnit());
                    warehouseInDetails.add(detail);
                }
                //获取库存
                List<WarehouseInDetailVo> warehouseInDetailVos = getStock(warehouseInDetails, userInfo.getShopId());
                warehouseInVo.setDetailList(warehouseInDetailVos);
            }
        } catch (Exception e) {
            log.error("[DUBBO] 调用淘汽档口获取淘汽采购商品详情接口失败,错误原因:", e);
            throw new BizException("淘汽采购商品入库失败");
        }

        return warehouseInVo;
    }

    @Override
    public void batchStock(List<WarehouseInDetailBo> warehouseInDetailBoList, UserInfo userInfo) {
        List<Long> supplierIds = new ArrayList<>();
        for (WarehouseInDetailBo warehouseInDetailBo : warehouseInDetailBoList) {
            if (warehouseInDetailBo.getSupplierId() == null) {
                throw new BizException("供应商不能为空");
            }
            supplierIds.add(warehouseInDetailBo.getSupplierId());
        }
        List<SupplierDTO> supplierList = supplierFacade.selectByShopIdAndIds(userInfo.getShopId(), supplierIds);
        /**
         * 根据supplierId分组
         */
        Multimap<Long, WarehouseInDetailBo> supplierMultimap = Multimaps.index(warehouseInDetailBoList, new Function<WarehouseInDetailBo, Long>() {
            @Override
            public Long apply(WarehouseInDetailBo warehouseInDetailBo) {
                return warehouseInDetailBo.getSupplierId();
            }
        });
        //相同供应商一张入库单
        for (SupplierDTO supplier : supplierList) {
            WarehouseIn warehouseIn = new WarehouseIn();
            warehouseIn.setWarehouseInSn(maxSnService.getMaxSn(userInfo.getShopId(), "LR"));
            warehouseIn.setSupplierId(supplier.getId());
            warehouseIn.setSupplierName(supplier.getSupplierName());
            warehouseIn.setPaymentMode(PayMethodEnum.getMessageByCode(supplier.getPayMethod()));
            warehouseIn.setInTime(new Date());
            warehouseIn.setContact(supplier.getContact());
            if (supplier.getInvoiceType() == null) {
                supplier.setInvoiceType(0);
            }
            warehouseIn.setInvoiceType(supplier.getInvoiceType());
            warehouseIn.setInvoiceTypeName(InvoiceTypeEnum.getValueByType(supplier.getInvoiceType()));
            warehouseIn.setContactMobile(supplier.getMobile());
            warehouseIn.setPurchaseAgentName(userInfo.getName());
            warehouseIn.setPurchaseAgent(userInfo.getUserId());
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<WarehouseInDetail> detailList = new ArrayList<>();
            for (WarehouseInDetailBo warehouseInDetailBo : supplierMultimap.get(supplier.getId())) {
                BigDecimal purchaseAmount = warehouseInDetailBo.getPurchasePrice().multiply(warehouseInDetailBo.getGoodsCount());
                totalAmount = totalAmount.add(purchaseAmount);
                WarehouseInDetail detail = new WarehouseInDetail();
                detail.setGoodsId(warehouseInDetailBo.getGoodsId());
                detail.setPurchaseAmount(purchaseAmount);
                detail.setPurchasePrice(warehouseInDetailBo.getPurchasePrice());
                detail.setGoodsCount(warehouseInDetailBo.getGoodsCount());
                detailList.add(detail);
            }
            warehouseIn.setGoodsAmount(totalAmount);
            warehouseIn.setTotalAmount(totalAmount);
            //入库
            stock(warehouseIn, detailList, userInfo);
        }
    }

    @Override
    public void updateSupplier(SupplierBo supplierBo) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("supplierId", supplierBo.getReqSupplierId());
        param.put("shopId", supplierBo.getShopId());
        List<WarehouseIn> warehouseIns = warehouseInService.select(param);
        List<Long> ids = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(warehouseIns)) {
            for (WarehouseIn warehouseIn : warehouseIns) {
                ids.add(warehouseIn.getId());
            }
            log.info("入库记录更新供应商,入库单记录id:{}", LogUtils.objectToString(ids));
        }
        warehouseInService.updateSupplier(supplierBo);
    }

    @Override
    public boolean isSupplierExist(Long shopId, Long supplierId) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("supplierId", supplierId);
        int count = warehouseInService.selectCount(param);
        if (count > 0) {
            return true;
        }
        return false;
    }

    //计算结存价格,库存
    private void calculatePriceAndAmount(List<WarehouseInDetail> detailList, UserInfo userInfo, boolean abolish) {
        if (CollectionUtils.isEmpty(detailList)) {
            throw new BizException("入库单详情不存在");
        }
        List<Long> goodsIds = new ArrayList<>();
        for (WarehouseInDetail temp : detailList) {
            goodsIds.add(temp.getGoodsId());
        }
        List<Goods> goodsList = goodsService.selectByIds(goodsIds.toArray(new Long[goodsIds.size()]));
        if (CollectionUtils.isEmpty(goodsList)) {
            log.error("物料不存在,ids", JSONUtil.object2Json(goodsIds));
            throw new BizException("物料不存在");
        }
        Multimap<Long, Goods> goodsMap = Multimaps.index(goodsList, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods goods) {
                return goods.getId();
            }
        });
        List<FinalInventory> finalInventoryList = new ArrayList<>();
        for (WarehouseInDetail temp : detailList) {
            for (Goods goods : goodsMap.get(temp.getGoodsId())) {
                Goods good = new Goods();
                good.setShopId(userInfo.getShopId());
                good.setId(goods.getId());
                good.setModifier(userInfo.getUserId());
                if (goods.getStock() == null) {
                    goods.setStock(BigDecimal.ZERO);
                }
                if (temp.getGoodsCount() == null) {
                    temp.setGoodsCount(BigDecimal.ZERO);
                }
                BigDecimal stockTotal = goods.getStock().multiply(goods.getInventoryPrice());
                if (temp.getPurchasePrice() == null) {
                    temp.setPurchasePrice(BigDecimal.ZERO);
                }
                BigDecimal detailTotal = temp.getGoodsCount().multiply(temp.getPurchasePrice()).abs();

                if (WarehouseInStatusEnum.LZRK.name().equals(temp.getStatus()) || WarehouseInStatusEnum.HZZF.name().equals(temp.getStatus())) {
                    good.setStock(goods.getStock().add(temp.getGoodsCount().abs()));
                    good.setInventoryPrice(stockTotal.add(detailTotal).divide(good.getStock(), 8, BigDecimal.ROUND_HALF_UP));
                } else if (WarehouseInStatusEnum.HZRK.name().equals(temp.getStatus()) || WarehouseInStatusEnum.LZZF.name().equals(temp.getStatus())) {
                    good.setStock(goods.getStock().subtract(temp.getGoodsCount().abs()));
                    if (BigDecimal.ZERO.compareTo(good.getStock()) == 0) {
                        good.setInventoryPrice(goods.getInventoryPrice().add(temp.getPurchasePrice()).divide(new BigDecimal("2")));
                    } else {
                        good.setInventoryPrice(stockTotal.subtract(detailTotal).divide(good.getStock(), 8, BigDecimal.ROUND_HALF_UP));
                    }
                } else {
                    throw new BizException("入库类型:" + temp.getStatus() + "不存在,操作失败");
                }
                if (BigDecimal.ZERO.compareTo(good.getStock()) > 0) {
                    log.error("库存不足,id:{}", good.getId());
                    throw new BizException("库存不足,操作失败");
                }
                if (BigDecimal.ZERO.compareTo(good.getInventoryPrice()) > 0) {
                    log.error("数据有误,id:{}", good.getId());
                    throw new BizException("商品结存单价小于零,操作失败");
                }
                if (!abolish) {
                    good.setLastInPrice(temp.getPurchasePrice());
                    good.setLastInTime(new Date());
                }
                goodsService.updateById(good);
                FinalInventory finalInventory = new FinalInventory();
                finalInventory.setShopId(userInfo.getShopId());
                finalInventory.setCreator(userInfo.getUserId());
                finalInventory.setFinalDate(new Date());
                finalInventory.setGoodsId(good.getId());
                finalInventory.setOrderId(temp.getWarehouseInId());
                finalInventory.setGoodsCount(good.getStock());
                finalInventory.setGoodsFinalPrice(good.getInventoryPrice());
                finalInventory.setInventoryType(temp.getStatus());
                finalInventoryList.add(finalInventory);
            }
        }
        finalInventoryService.batchInsert(finalInventoryList);

    }

    //作废付款单
    private void abolishToBillCenter(Long id, UserInfo userInfo) {
        if (id == null) {
            log.error("入库单作废,入库单Id不存在");
            throw new BizException("入库单作废失败");
        }
        List<Long> relIds = Lists.newArrayList(id);
        Long shopId = userInfo.getShopId();
        log.info("获取付款单项信息,操作人:{}", userInfo.getName());
        Result<List<PayBillDTO>> result = rpcPayService.selectPayBill(relIds, PayTypeEnum.SUPPLIER.getCode(), shopId);
        List<PayBillDTO> payBillDTOs = result.getData();
        if (!result.isSuccess() || CollectionUtils.isEmpty(payBillDTOs) || payBillDTOs.get(0) == null) {
            log.error("入库单作废,获取付款单信息失败,原因:", JSONUtil.object2Json(result));
            throw new BizException("未获取到对应付款单信息,作废失败");
        }
        PayInvalidParam param = new PayInvalidParam();
        param.setShopId(shopId);
        param.setOperatorName(userInfo.getName());
        param.setCreator(userInfo.getUserId());
        param.setBillId(payBillDTOs.get(0).getId());
        log.info("[DUBBO] 调用结算中心无效接口,入参:{}", JSONUtil.object2Json(param));
        try {
            Result<Integer> paramResult = rpcPayService.invalidPayBill(param);
            if (!paramResult.isSuccess()) {
                log.error("入库单作废失败,错误原因:{}", JSONUtil.object2Json(paramResult));
                throw new BizException("入库单作废失败");
            }
        } catch (Exception e) {
            log.error("入库单作废失败,错误原因:", e);
            throw new BizException("入库单作废失败");
        }
        log.info("入库单作废成功,id:{}", id);
    }

    /**
     * 保存入库单
     *
     * @param warehouseIn
     * @param userInfo
     * @param
     */
    private Long saveWarehouseIn(WarehouseIn warehouseIn, UserInfo userInfo) {
        warehouseIn.setShopId(userInfo.getShopId());
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", userInfo.getShopId());
        param.put("warehouseInSn", warehouseIn.getWarehouseInSn());
        int count = warehouseInService.selectCount(param);
        if (count > 0) {
            log.error("该单号已存在,warehouseInSn:", warehouseIn.getWarehouseInSn());
            throw new BizException("该单号已存在,无法入库");
        }
        if (warehouseIn.getInTime() == null) {
            warehouseIn.setInTime(new Date());
        }
        warehouseIn.setCreator(userInfo.getUserId());
        warehouseIn.setAmountPaid(BigDecimal.ZERO);
        warehouseIn.setAmountPayable(warehouseIn.getTotalAmount());
        warehouseIn.setPaymentStatus(PayStatusEnum.DDZF.name());
        if (BigDecimal.ZERO.compareTo(warehouseIn.getTotalAmount()) == 0) {
            warehouseIn.setPaymentStatus(PayStatusEnum.ZFWC.name());
        }
        warehouseInService.insert(warehouseIn);
        return warehouseIn.getId();
    }

    /**
     * 保存详情
     *
     * @param warehouseIn
     * @param warehouseInDetails
     */
    private List<WarehouseInDetail> saveWarehouseInDetail(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetails) {
        String relSn = "";
        if (WarehouseInStatusEnum.HZRK.name().equals(warehouseIn.getStatus())) {
            WarehouseIn warehouse = warehouseInService.selectByIdAndShopId(warehouseIn.getRelId(), warehouseIn.getShopId());
            if (warehouse == null) {
                log.error("对应蓝字入库数据有误,id:{}", warehouseIn.getId());
                throw new BizException("对应蓝字入库数据有误");
            }
            relSn = warehouse.getWarehouseInSn();
        }
        //将重复的商品合并为一条
        warehouseInDetails = deduplication(warehouseInDetails);
        for (WarehouseInDetail detail : warehouseInDetails) {
            detail.setShopId(warehouseIn.getShopId());
            detail.setWarehouseInId(warehouseIn.getId());
            detail.setCreator(warehouseIn.getCreator());
            detail.setPurchaseAmount(detail.getPurchasePrice().multiply(detail.getGoodsCount()));
            detail.setStatus(warehouseIn.getStatus());
            detail.setGoodsRealCount(detail.getGoodsCount().abs());
            detail.setWarehouseInSn(warehouseIn.getWarehouseInSn());
            detail.setRelSn(warehouseIn.getPurchaseSn());
            if (WarehouseInStatusEnum.HZRK.name().equals(warehouseIn.getStatus())) {
                detail.setRelSn(relSn);
            }
        }
        //完善入库详情信息
        perfectDetail(warehouseInDetails);
        warehouseInDetailService.batchInsert(warehouseInDetails);
        return warehouseInDetails;
    }

    //将重复的商品合并为一条
    private List<WarehouseInDetail> deduplication(List<WarehouseInDetail> warehouseInDetails) {
        Multimap<Long, WarehouseInDetail> multimap = Multimaps.index(warehouseInDetails, new Function<WarehouseInDetail, Long>() {
            @Override
            public Long apply(WarehouseInDetail warehouseInDetail) {
                return warehouseInDetail.getGoodsId();
            }
        });
        Set<Long> goodsIdSet = new HashSet<>();
        List<WarehouseInDetail> warehouseInDetailList = new ArrayList<>();
        for (WarehouseInDetail detail : warehouseInDetails) {
            goodsIdSet.add(detail.getGoodsId());
        }
        if (goodsIdSet.size() == warehouseInDetails.size()) {
            return warehouseInDetails;
        }
        for (Long goodsId : goodsIdSet) {
            WarehouseInDetail warehouseInDetail = new WarehouseInDetail();
            BigDecimal goodsCount = BigDecimal.ZERO;
            BigDecimal purchaseAmount = BigDecimal.ZERO;
            for (WarehouseInDetail detail : multimap.get(goodsId)) {
                goodsCount = goodsCount.add(detail.getGoodsCount());
                purchaseAmount = purchaseAmount.add(detail.getPurchaseAmount());
            }
            warehouseInDetail.setGoodsId(goodsId);
            warehouseInDetail.setGoodsCount(goodsCount);
            warehouseInDetail.setPurchaseAmount(purchaseAmount);
            if (BigDecimal.ZERO.compareTo(goodsCount) == 0) {
                log.error("商品入库数量为0,goodsId:", goodsId);
                throw new BizException("商品入库数量不能为0,入库失败");
            }
            BigDecimal purchasePrice = purchaseAmount.divide(goodsCount, 8, BigDecimal.ROUND_HALF_UP);
            warehouseInDetail.setPurchasePrice(purchasePrice);
            warehouseInDetailList.add(warehouseInDetail);
        }
        return warehouseInDetailList;
    }

    /**
     * 校验淘汽采购是否已经入库
     * @param purchaseSn
     */
    private void checkTqmallPurchase(String purchaseSn) {
        if (StringUtils.isBlank(purchaseSn)) {
            return;
        }
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put("purchaseSn", purchaseSn);
        map.put("status",WarehouseInStatusEnum.LZRK.name());
        List<WarehouseIn> warehouseIns = warehouseInService.select(map);
        if (!CollectionUtils.isEmpty(warehouseIns)) {
            throw new BizException("该淘汽采购商品已入库,无需重复入库");
        }

    }

    //检查数据是否完整
    private void checkData(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList) {

        if (warehouseIn == null) {
            throw new BizException("入库单不存在");
        }
        if (CollectionUtils.isEmpty(warehouseInDetailList)) {
            throw new BizException("入库单详情不存在");
        }
        for (WarehouseInDetail warehouseInDetail : warehouseInDetailList) {
            if (BigDecimal.ZERO.compareTo(warehouseInDetail.getGoodsCount()) == 0) {
                log.error("商品入库数量不能为0,goodsId:", warehouseInDetail.getGoodsId());
                throw new BizException("商品入库数量不能为0");
            }
        }
    }

    /**
     * 检查数量是否充足,并更新对应蓝字
     *
     * @param warehouseIn
     * @param warehouseInDetailList
     * @return
     */
    private List<WarehouseInDetail> checkAmountAndUpdate(WarehouseIn warehouseIn, List<WarehouseInDetail> warehouseInDetailList) {
        List<Goods> goodsList = getGoodsList(warehouseInDetailList);
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", warehouseIn.getShopId());
        param.put("warehouseInId", warehouseIn.getRelId());
        param.put("status", WarehouseInStatusEnum.LZRK.name());
        //对应蓝字入库详情
        List<WarehouseInDetail> warehouseInDetails = warehouseInDetailService.select(param);
        if (CollectionUtils.isEmpty(warehouseInDetails)) {
            throw new BizException("数据有误");
        }
        for (WarehouseInDetail warehouseInDetail : warehouseInDetailList) {
            if (warehouseInDetail.getGoodsId() == null) {
                throw new BizException("商品ID不存在");
            }
            for (Goods goods : goodsList) {
                if (warehouseInDetail.getGoodsId().equals(goods.getId())) {
                    if (goods.getStock() == null) {
                        goods.setStock(BigDecimal.ZERO);
                    }
                    if (goods.getStock().compareTo(warehouseInDetail.getGoodsCount().abs()) < 0) {
                        log.error("库存不足,goodsId:", goods.getId());
                        throw new BizException("库存不足,红字入库失败");
                    }
                    break;
                }
            }
            for (WarehouseInDetail detail : warehouseInDetails) {
                if (warehouseInDetail.getGoodsId() == null) {
                    log.error("详情对应商品ID不存在,详情id:", detail.getId());
                    throw new BizException("数据有误");
                }
                if (warehouseInDetail.getId().equals(detail.getId())) {
                    if (detail.getGoodsCount() == null && BigDecimal.ZERO.compareTo(detail.getGoodsCount()) < 0) {
                        log.error("详情对应商品数量数据有误,详情id:", detail.getId());
                        throw new BizException("数据有误");
                    }
                    if (detail.getGoodsRealCount().compareTo(warehouseInDetail.getGoodsCount().abs()) < 0) {
                        log.error("可退数量小于退货数量,goodsId:", detail.getGoodsId());
                        throw new BizException("可退数量小于退货数量");
                    }
                    WarehouseInDetail ware = new WarehouseInDetail();
                    ware.setId(detail.getId());
                    ware.setShopId(detail.getShopId());
                    ware.setGoodsRealCount(detail.getGoodsRealCount().add(warehouseInDetail.getGoodsCount()));
                    warehouseInDetailService.updateById(ware);
                    break;
                }
            }

        }
        return warehouseInDetails;
    }

    //该详情对应的所有配件信息
    private List<Goods> getGoodsList(List<WarehouseInDetail> warehouseInDetailList) {
        if (CollectionUtils.isEmpty(warehouseInDetailList)) {
            return new ArrayList<>();
        }
        List<Long> goodsIds = new ArrayList<>();
        for (WarehouseInDetail warehouseInDetail : warehouseInDetailList) {
            goodsIds.add(warehouseInDetail.getGoodsId());
        }
        List<Goods> goodsList = goodsService.selectByIds(goodsIds.toArray(new Long[goodsIds.size()]));
        return goodsList;
    }

    //该详情对应的所有配件信息
    private List<Goods> getGoodsListByTqmallIds(List<WarehouseInDetail> warehouseInDetailList, Long shopId) {
        List<Long> tqmallGoodsIdList = new ArrayList<>();
        for (WarehouseInDetail warehouseInDetail : warehouseInDetailList) {
            tqmallGoodsIdList.add(warehouseInDetail.getTqmallGoodsId());
        }
        Long[] tqmallGoodsIdArray = tqmallGoodsIdList.toArray(new Long[tqmallGoodsIdList.size()]);
        return goodsService.selectByTqmallIds(tqmallGoodsIdArray, shopId);
    }

    //获取库存数量(普通)
    private List<WarehouseInDetailVo> getStock(List<WarehouseInDetail> detailList) {
        List<Goods> goodsList = getGoodsList(detailList);
        Multimap<Long, Goods> map = Multimaps.index(goodsList, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods goods) {
                return goods.getId();
            }
        });
        List<WarehouseInDetailVo> warehouseInDetailVoList = new ArrayList<>();
        for (WarehouseInDetail detail : detailList) {
            for (Goods goods : map.get(detail.getGoodsId())) {
                WarehouseInDetailVo warehouseInDetailVo = new WarehouseInDetailVo();
                BeanUtils.copyProperties(detail, warehouseInDetailVo);
                warehouseInDetailVo.setStock(goods.getStock());
                warehouseInDetailVo.setDepot(goods.getDepot());
                warehouseInDetailVo.setCatName(goods.getCat2Name());
                warehouseInDetailVo.setCarInfo(goods.getCarInfo());
                warehouseInDetailVoList.add(warehouseInDetailVo);
            }
        }
        return warehouseInDetailVoList;
    }

    //获取库存数量(淘汽采购)
    private List<WarehouseInDetailVo> getStock(List<WarehouseInDetail> detailList, Long shopId) {
        List<Goods> goodsList = getGoodsListByTqmallIds(detailList, shopId);
        Multimap<Long, Goods> map = Multimaps.index(goodsList, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods goods) {
                return goods.getTqmallGoodsId();
            }
        });
        List<WarehouseInDetailVo> warehouseInDetailVoList = new ArrayList<>();
        for (WarehouseInDetail detail : detailList) {
            for (Goods goods : map.get(detail.getTqmallGoodsId())) {
                WarehouseInDetailVo warehouseInDetailVo = new WarehouseInDetailVo();
                BeanUtils.copyProperties(detail, warehouseInDetailVo);
                warehouseInDetailVo.setGoodsId(goods.getId());
                warehouseInDetailVo.setGoodsFormat(goods.getFormat());
                warehouseInDetailVo.setStock(goods.getStock());
                warehouseInDetailVo.setDepot(goods.getDepot());
                warehouseInDetailVo.setCatName(goods.getCat2Name());
                warehouseInDetailVo.setCarInfo(goods.getCarInfo());
                warehouseInDetailVoList.add(warehouseInDetailVo);
            }
        }
        return warehouseInDetailVoList;
    }

    /**
     * 完善入库单详请信息
     *
     * @param detailList
     * @return
     */
    private List<WarehouseInDetail> perfectDetail(List<WarehouseInDetail> detailList) {
        List<Goods> goodsList = getGoodsList(detailList);
        Multimap<Long, Goods> goodsMap = Multimaps.index(goodsList, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods goods) {
                return goods.getId();
            }
        });
        for (WarehouseInDetail detail : detailList) {
            for (Goods goods : goodsMap.get(detail.getGoodsId())) {
                detail.setGoodsSn(goods.getGoodsSn());
                detail.setGoodsFormat(goods.getFormat());
                detail.setGoodsName(goods.getName());
                detail.setMeasureUnit(goods.getMeasureUnit());
                detail.setCarInfo(goods.getCarInfo());
                detail.setTqmallGoodsId(goods.getTqmallGoodsId());
                detail.setDepot(goods.getDepot());
                detail.setTqmallGoodsSn(goods.getTqmallGoodsSn());
            }
        }
        return detailList;
    }

    //完善入库信息
    private List<LegendWarehouseInDTOVo> perfectDetail(List<LegendWarehouseInDTOVo> voList, Long shopId) {
        List<Long> idList = Lists.newArrayList();
        for (LegendWarehouseInDTOVo vo : voList) {
            if (!CollectionUtils.isEmpty(vo.getDetailList())) {
                for (LegendWarehouseInDetail detailVo : vo.getDetailList()) {
                    idList.add(detailVo.getId());
                }
            }
        }
        if (CollectionUtils.isEmpty(idList)) {
            return voList;
        }
        Map<String, Object> detailParam = new HashMap<>();
        detailParam.put("shopId", shopId);
        detailParam.put("warehouseInDetailIds", idList);
        List<WarehouseInDetail> detailList = warehouseInDetailService.select(detailParam);
        List<WarehouseInDetailVo> detailVoList = getStock(detailList);
        //开单人
        List<ShopManager> managers = shopManagerService.selectByShopId(shopId);
        Map<Long, String> managerMap = new HashMap<>();
        for (ShopManager manager : managers) {
            managerMap.put(manager.getId(), manager.getName());
        }

        Multimap<Long, WarehouseInDetailVo> detailMultimap = Multimaps.index(detailVoList, new Function<WarehouseInDetailVo, Long>() {
            @Override
            public Long apply(WarehouseInDetailVo WarehouseInDetailVo) {
                return WarehouseInDetailVo.getWarehouseInId();
            }
        });
        for (LegendWarehouseInDTOVo vo : voList) {
            List<WarehouseInDetailVo> warehouseInDetailVoList = Lists.newArrayList();
            for (WarehouseInDetailVo detailVo : detailMultimap.get(vo.getId().longValue())) {
                warehouseInDetailVoList.add(detailVo);
                //设置开单人
                if (vo.getOperatorName() == null) {
                    vo.setOperatorName(managerMap.get(detailVo.getCreator()));
                }
                vo.setDetailVoList(warehouseInDetailVoList);
            }
        }
        return voList;
    }
    //组装数据提交到结算中心

    private void pushToBillCenter(List<WarehouseIn> warehouseInList) {
        List<PayBillDTO> payBillDTOs = makePayBillDTO(warehouseInList);
        log.info("[DUBBO] 调用结算中心接口添加付款单,shopId:{},操作:{}", payBillDTOs.get(0).getShopId(), payBillDTOs.get(0).getRemark());
        Result result = rpcPayService.batchSaveBill(payBillDTOs);
        if (!result.isSuccess()) {
            log.error("[DUBBO] 调用结算中心接口添加付款单失败,错误原因:{}", JSONUtil.object2Json(result));
            throw new BizException("[DUBBO] 调用结算中心接口添加付款单失败");
        }
    }

    //组装结算单数据
    private List<PayBillDTO> makePayBillDTO(List<WarehouseIn> warehouseInList) {
        List<PayBillDTO> payBillDTOs = new ArrayList<>();
        for (WarehouseIn warehouseIn : warehouseInList) {
            PayBillDTO payBill = new PayBillDTO();
            payBill.setBillName(warehouseIn.getWarehouseInSn());
            payBill.setCreator(warehouseIn.getCreator());
            if (warehouseIn.getAmountPaid() == null) {
                warehouseIn.setAmountPaid(BigDecimal.ZERO);
            }
            payBill.setPaidAmount(warehouseIn.getAmountPaid());
            if (warehouseIn.getAmountPayable() == null) {
                warehouseIn.setAmountPayable(warehouseIn.getTotalAmount());
            }
            payBill.setUnpaidAmount(warehouseIn.getAmountPayable());
            payBill.setTotalAmount(warehouseIn.getTotalAmount());
            payBill.setOperatorName(warehouseIn.getPurchaseAgentName());
            payBill.setPayeeName(warehouseIn.getSupplierName());
            payBill.setPayTypeId(PayTypeEnum.SUPPLIER.getCode());
            payBill.setRelId(warehouseIn.getId());
            payBill.setShopId(warehouseIn.getShopId());
            payBill.setRemark(WarehouseInStatusEnum.getMessageByName(warehouseIn.getStatus()));
            payBillDTOs.add(payBill);
        }
        return payBillDTOs;
    }

}
