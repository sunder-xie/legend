package com.tqmall.legend.facade.warehouse.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.Gson;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.goods.FinalInventoryService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderTrackService;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.biz.warehouse.IWarehouseOutDetailService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.goods.FinalInventory;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.OrderCategoryEnum;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.order.OrderTrack;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.warehouseout.WarehouseOut;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.legend.enums.warehouse.WarehouseOutStatusEnum;
import com.tqmall.legend.facade.order.vo.SearchOrderVo;
import com.tqmall.legend.facade.warehouse.WarehouseOutFacade;
import com.tqmall.legend.facade.warehouse.vo.LegendWarehouseOutDTOVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutRefundVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutDetailVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseOutVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.common.result.Result;
import com.tqmall.search.dubbo.client.PageableResponseExtend;
import com.tqmall.search.dubbo.client.legend.order.param.LegendOrderRequest;
import com.tqmall.search.dubbo.client.legend.order.result.LegendOrderInfoDTO;
import com.tqmall.search.dubbo.client.legend.order.service.LegendOrderService;
import com.tqmall.search.dubbo.client.legend.warehouseout.param.LegendWarehouseOutRequest;
import com.tqmall.search.dubbo.client.legend.warehouseout.result.LegendWarehouseOutDTO;
import com.tqmall.search.dubbo.client.legend.warehouseout.result.LegendWarehouseOutDetail;
import com.tqmall.search.dubbo.client.legend.warehouseout.service.LegendWarehouseOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sven on 16/8/23.
 */
@Service
@Slf4j
public class WarehouseOutFacadeImpl implements WarehouseOutFacade {
    @Resource
    private WarehouseOutService warehouseOutService;
    @Resource
    private IWarehouseOutDetailService warehouseOutDetailService;
    @Resource
    private ShopManagerService shopManagerService;
    @Resource
    private CustomerCarService customerCarService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private FinalInventoryService finalInventoryService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private OrderTrackService orderTrackService;
    @Resource
    private MaxSnService maxSnService;
    @Resource
    private LegendWarehouseOutService legendWarehouseOutService;
    @Resource
    private OrderGoodsService orderGoodsService;
    @Resource
    private LegendOrderService legendOrderService;
    @Resource
    private OrderTypeService orderTypeService;

    @Override
    public DefaultPage<LegendWarehouseOutDTOVo> select(LegendWarehouseOutRequest param, PageableRequest pageableRequest, Long shopId) {
        log.info("[DUBBO]调用搜索出库单查询接口,入参:{}", LogUtils.objectToString(param));
        Result result = legendWarehouseOutService.queryWarehouseListWithAggs(param, pageableRequest);
        if (!result.isSuccess()) {
            log.error("调用出库列表搜索接口异常,返回结果{}", new Gson().toJson(result));
            return new DefaultPage<>(new ArrayList<LegendWarehouseOutDTOVo>());
        }
        PageableResponseExtend<LegendWarehouseOutDTO> page = (PageableResponseExtend<LegendWarehouseOutDTO>) result.getData();
        if (page == null) {
            return new DefaultPage<>(new ArrayList<LegendWarehouseOutDTOVo>());
        }
        //组装list中的信息
        List<LegendWarehouseOutDTOVo> legendWarehouseOutDTOVoList = makeList(page.getContent(), shopId);
        DefaultPage<LegendWarehouseOutDTOVo> defaultPage = new DefaultPage<>(legendWarehouseOutDTOVoList, pageableRequest, ((Page<LegendWarehouseOutDTO>) result.getData()).getTotalElements());
        defaultPage.setOtherData(page.getExtend());
        return defaultPage;
    }

    @Override
    public String getSn(Long shopId, String prefix) {
        return maxSnService.getMaxSn(shopId, prefix);
    }

    @Override
    @Transactional
    public Long stockOut(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList, UserInfo userInfo) throws BizException {
        checkData(warehouseOut, detailList);
        warehouseOut.setStatus(WarehouseOutStatusEnum.LZCK.name());
        Long id = saveWarehouseOut(warehouseOut, userInfo);
        saveWarehouseOutDetail(warehouseOut, detailList);
        if (warehouseOut.getOrderId() != null) {
            operateData(warehouseOut);
        }
        operateGoods(detailList, userInfo, WarehouseOutStatusEnum.LZCK.name());
        return id;
    }

    @Override
    @Transactional
    public Long stockOutRefund(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList, UserInfo userInfo) throws BizException {
        checkData(warehouseOut, detailList);
        warehouseOut.setStatus(WarehouseOutStatusEnum.HZCK.name());
        Long id = saveWarehouseOut(warehouseOut, userInfo);
        saveWarehouseOutDetail(warehouseOut, detailList);
        operateGoods(detailList, userInfo, WarehouseOutStatusEnum.HZCK.name());
        return id;
    }

    @Override
    @Transactional
    public void abolishStockOut(Long id, UserInfo userInfo) throws BizException {
        WarehouseOut warehouseOut = warehouseOutService.selectByIdAndShopId(id, userInfo.getShopId());
        if (warehouseOut == null) {
            log.error("出库作废数据不存在,id:", id);
            throw new BizException("出库/退货记录不存在,作废失败");
        }
        if (WarehouseOutStatusEnum.CK_HZZF.name().equals(warehouseOut.getStatus()) ||
                WarehouseOutStatusEnum.CK_LZZF.name().equals(warehouseOut.getStatus())) {
            log.error("该数据已作废,id:", id);
            throw new BizException("出库/退货记录已作废,不允许重复作废");
        }
        //蓝字作废时,作废该单对应的所有红字入库记录
        if (WarehouseOutStatusEnum.LZCK.name().equals(warehouseOut.getStatus())) {
            Map<String, Object> param = new HashMap<>();
            param.put("relId", id);
            param.put("shopId", userInfo.getShopId());
            param.put("status", WarehouseOutStatusEnum.HZCK.name());
            List<WarehouseOut> warehouseOutList = warehouseOutService.select(param);
            if (!CollectionUtils.isEmpty(warehouseOutList)) {
                for (WarehouseOut out : warehouseOutList) {
                    abolishStockOut(out.getId(), userInfo);
                }
            }
        }
        //更新出库单
        WarehouseOut out = new WarehouseOut();
        out.setId(id);
        out.setShopId(userInfo.getShopId());
        out.setStatus(WarehouseOutStatusEnum.CK_HZZF.name());
        if (WarehouseOutStatusEnum.LZCK.name().equals(warehouseOut.getStatus())) {
            out.setStatus(WarehouseOutStatusEnum.CK_LZZF.name());
        }
        warehouseOutService.updateById(out);
        Map<String, Object> param = new HashMap<>();
        param.put("warehouseOutId", id);
        param.put("shopId", userInfo.getShopId());
        List<WarehouseOutDetail> detailList = warehouseOutDetailService.select(param);
        param.put("status", out.getStatus());
        //更新对应的出库详情
        warehouseOutDetailService.update(param);
        if (WarehouseOutStatusEnum.HZCK.name().equals(warehouseOut.getStatus())) {
            prefectStockOutDetail(warehouseOut, detailList, true);
        }
        //操作库存,结存单价
        operateGoods(detailList, userInfo, out.getStatus());
    }

    @Override
    public WarehouseOutVo select(Long id, Long shopId) {
        //获取出库单信息
        WarehouseOut warehouseOut = warehouseOutService.selectByIdAndShopId(id, shopId);
        if (warehouseOut == null) {
            return null;
        }
        //获取出库单详情
        Map<String, Object> param = new HashMap<>(2);
        param.put("warehouseOutId", id);
        param.put("shopId", shopId);
        List<WarehouseOutDetail> detailList = warehouseOutDetailService.select(param);
        // ]]
        WarehouseOutVo warehouseOutVo = new WarehouseOutVo();
        BeanUtils.copyProperties(warehouseOut, warehouseOutVo);
        List<WarehouseOutDetailVo> detailVoList = getStock(detailList);
        warehouseOutVo.setDetailVoList(detailVoList);
        //设置领料人名称
        ShopManager shopManager = shopManagerService.selectById(warehouseOutVo.getGoodsReceiver());
        if (shopManager != null) {
            warehouseOutVo.setReceiverName(shopManager.getName());
        }
        //设置出库人名称
        ShopManager manager = shopManagerService.selectById(warehouseOutVo.getCreator());

        if (manager != null) {
            warehouseOutVo.setOperatorName(manager.getName());
        }
        OrderInfo info = orderInfoService.selectById(warehouseOut.getOrderId(), shopId);
        if (info != null) {
            warehouseOutVo.setOrderSn(info.getOrderSn());
        }
        getTotal(warehouseOutVo);
        //设置成本总价,销售总价
        return warehouseOutVo;
    }

    //检查数据是否完整
    private void checkData(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList) throws BizException {
        if (warehouseOut == null) {
            throw new BizException("出库/退货记录不存在");
        }

        if (CollectionUtils.isEmpty(detailList)) {
            throw new BizException("出库/退货物料不存在");
        }
        for (WarehouseOutDetail detail : detailList) {
            if (BigDecimal.ZERO.compareTo(detail.getGoodsCount()) == 0) {
                log.error("出库物料数量不能为0,goodsId:", detail.getGoodsId());
                throw new BizException(detail.getGoodsId() + "出库物料数量不能为0");
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("warehouseOutSn", warehouseOut.getWarehouseOutSn());
        int count = warehouseOutService.selectCount(param);
        if (count > 0) {
            log.error("出库编号已存在,warehouseOutSn:", warehouseOut.getWarehouseOutSn());
            throw new BizException("出库编号已存在,保存失败");
        }
    }

    //保存出库单
    private Long saveWarehouseOut(WarehouseOut warehouseOut, UserInfo userInfo) {
        prefectWarehouseOut(warehouseOut, userInfo);
        warehouseOutService.insert(warehouseOut);
        return warehouseOut.getId();
    }

    //完善出库单信息
    private void prefectWarehouseOut(WarehouseOut warehouseOut, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        warehouseOut.setShopId(userInfo.getShopId());
        warehouseOut.setCreator(userInfo.getUserId());
        //其他出库车辆可不选
        if (warehouseOut.getCarLicense() != null) {
            CustomerCar car = customerCarService.selectByLicenseAndShopId(warehouseOut.getCarLicense(), shopId);
            if (car == null) {
                log.error("车辆不存在:carLicense:", warehouseOut.getCarLicense());
                throw new BizException(warehouseOut.getCarLicense() + "车辆信息不存在");
            }
            warehouseOut.setCarByname(car.getByName());
            warehouseOut.setCustomerId(car.getCustomerId());
            warehouseOut.setCustomerCarId(car.getId());
            if (StringUtil.isNull(warehouseOut.getCustomerName())) {
                warehouseOut.setCustomerName(car.getCustomerName());
            }
        }

    }

    //保存详情
    private void saveWarehouseOutDetail(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList) {
        //重复的商品合并
        if (warehouseOut.getOrderId() == null || warehouseOut.getOrderId().intValue() == 0) {
            detailList = deduplication(detailList);
        }
        //完善出库单信息
        prefectDetail(detailList);
        if (warehouseOut.getStatus().equals(WarehouseOutStatusEnum.HZCK.name())) {
            prefectStockOutDetail(warehouseOut, detailList, false);
        }
        for (WarehouseOutDetail detail : detailList) {
            detail.setShopId(warehouseOut.getShopId());
            if (detail.getGoodsCount() == null) {
                detail.setGoodsCount(BigDecimal.ZERO);
            }
            detail.setHandByIn(warehouseOut.getGoodsReceiver());
            detail.setHandByOn(warehouseOut.getCreator());
            detail.setStatus(warehouseOut.getStatus());
            detail.setGoodsRealCount(detail.getGoodsCount().abs());
            detail.setOrderId(warehouseOut.getOrderId());
            detail.setCreator(warehouseOut.getCreator());
            detail.setWarehouseOutId(warehouseOut.getId());
            detail.setWarehouseOutSn(warehouseOut.getWarehouseOutSn());
        }
        //校验工单出库数量
        if (warehouseOut.getOrderId() != null && warehouseOut.getOrderId().intValue() != 0
                && WarehouseOutStatusEnum.LZCK.name().equals(warehouseOut.getStatus())) {
            List<OrderGoods> orderGoodsList = selectTraceOrderGoods(warehouseOut.getOrderId(), warehouseOut.getShopId());
            checkOrderTraceNumber(orderGoodsList, detailList);
        }
        warehouseOutDetailService.batchInsert(detailList);
    }

    //更新对应蓝字出库数量,结存单价
    private void prefectStockOutDetail(WarehouseOut warehouseOut, List<WarehouseOutDetail> detailList, boolean abolish) throws BizException {
        Map<String, Object> param = new HashMap<>();
        param.put("status", WarehouseOutStatusEnum.LZCK.name());
        param.put("shopId", warehouseOut.getShopId());
        param.put("warehouseOutId", warehouseOut.getRelId());
        WarehouseOut out = warehouseOutService.selectByIdAndShopId(warehouseOut.getRelId(), warehouseOut.getShopId());
        List<WarehouseOutDetail> warehouseOutDetails = warehouseOutDetailService.select(param);
        if (CollectionUtils.isEmpty(warehouseOutDetails)) {
            log.error("蓝字出库对应详情不存在,id");
            throw new BizException("蓝字出库对应详情不存在,操作失败");
        }
        for (WarehouseOutDetail detail : detailList) {
            for (WarehouseOutDetail outDetail : warehouseOutDetails) {
                if (detail.getGoodsId().equals(outDetail.getGoodsId())) {
                    WarehouseOutDetail warehouseOutDetail = new WarehouseOutDetail();
                    detail.setRelDetailId(outDetail.getId());
                    detail.setRelId(outDetail.getWarehouseOutId());
                    detail.setWarehouseOutSn(out.getWarehouseOutSn());
                    BigDecimal goosAmount;
                    if (abolish) {
                        goosAmount = outDetail.getGoodsRealCount().subtract(detail.getGoodsCount());
                    } else {
                        goosAmount = outDetail.getGoodsRealCount().add(detail.getGoodsCount());
                    }
                    if (BigDecimal.ZERO.compareTo(goosAmount) > 0) {
                        throw new BizException(outDetail.getGoodsName() + "数量不足,操作失败");
                    }
                    warehouseOutDetail.setId(outDetail.getId());
                    warehouseOutDetail.setShopId(outDetail.getShopId());
                    warehouseOutDetail.setGoodsRealCount(goosAmount);
                    warehouseOutDetailService.updateById(warehouseOutDetail);
                }
            }
        }
    }

    //完善详情信息
    private void prefectDetail(List<WarehouseOutDetail> detailList) {
        List<Goods> goodsList = getGoodsList(detailList);
        Multimap<Long, Goods> map = Multimaps.index(goodsList, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods goods) {
                return goods.getId();
            }
        });
        for (WarehouseOutDetail detail : detailList) {
            for (Goods goods : map.get(detail.getGoodsId())) {
                detail.setGoodsSn(goods.getGoodsSn());
                detail.setGoodsFormat(goods.getFormat());
                detail.setGoodsName(goods.getName());
                detail.setCarInfo(goods.getCarInfo());
                detail.setInventoryPrice(goods.getInventoryPrice());
            }
        }
    }

    //该详情对应的所有配件信息
    private List<Goods> getGoodsList(List<WarehouseOutDetail> detailList) {
        List<Long> goodsId = new ArrayList<>();
        for (WarehouseOutDetail detail : detailList) {
            goodsId.add(detail.getGoodsId());
        }
        List<Goods> goodsList = goodsService.selectByIds(goodsId.toArray(new Long[goodsId.size()]));
        return goodsList;
    }

    //更新库存最新价格以及插入结存 记录
    private void operateGoods(List<WarehouseOutDetail> detailList, UserInfo userInfo, String status) {
        List<Goods> goodsList = getGoodsList(detailList);
        Multimap<Long, WarehouseOutDetail> multimap = Multimaps.index(detailList, new Function<WarehouseOutDetail, Long>() {
            @Override
            public Long apply(WarehouseOutDetail detail) {
                return detail.getGoodsId();
            }
        });
        List<FinalInventory> finalInventoryList = new ArrayList<>();
        for (Goods goods : goodsList) {
            Goods good = new Goods();
            FinalInventory finalInventory = new FinalInventory();
            good.setId(goods.getId());
            good.setShopId(goods.getShopId());
            BigDecimal stock = goods.getStock();
            BigDecimal count = BigDecimal.ZERO;
            BigDecimal costAmount = BigDecimal.ZERO;//成本总价
            for (WarehouseOutDetail detail : multimap.get(goods.getId())) {
                costAmount = costAmount.add(detail.getGoodsCount().multiply(detail.getInventoryPrice()));
                if (WarehouseOutStatusEnum.CK_HZZF.name().equals(status) ||
                        WarehouseOutStatusEnum.CK_LZZF.name().equals(status)) {
                    stock = stock.add(detail.getGoodsCount());
                } else {
                    stock = stock.subtract(detail.getGoodsCount());
                }
                count = count.add(detail.getGoodsCount());
                if (detail.getGoodsCount() == null) {
                    detail.setGoodsCount(BigDecimal.ZERO);
                }

                //蓝字出库不改变结存单价,不插记录
                if (!WarehouseOutStatusEnum.LZCK.name().equals(status)) {
                    BigDecimal inventoryPrice;
                    if (BigDecimal.ZERO.compareTo(count.add(goods.getStock())) == 0) {
                        inventoryPrice = goods.getInventoryPrice();
                    } else {
                        inventoryPrice = costAmount.add(goods.getStock().multiply(goods.getInventoryPrice())).divide(count.add(goods.getStock()), 8, BigDecimal.ROUND_HALF_UP);
                    }
                    finalInventory.setInventoryType(status);
                    finalInventory.setOrderId(detail.getWarehouseOutId());
                    finalInventory.setGoodsId(goods.getId());
                    finalInventory.setShopId(userInfo.getShopId());
                    finalInventory.setGoodsFinalPrice(inventoryPrice);
                    finalInventory.setGoodsCount(stock);
                    finalInventory.setCreator(userInfo.getUserId());
                    finalInventory.setFinalDate(new Date());
                    finalInventoryList.add(finalInventory);
                    good.setInventoryPrice(inventoryPrice);
                }
            }
            if (BigDecimal.ZERO.compareTo(stock) > 0) {
                log.error("库存不足,goodsId", goods.getId());
                throw new BizException(goods.getName() + "库存不足,操作失败");
            }
            good.setStock(stock);
            goodsService.updateById(good);

        }
        if (!WarehouseOutStatusEnum.LZCK.name().equals(status)) {
            finalInventoryService.batchInsert(finalInventoryList);
        }
    }

    /**
     * 更新和插入信息
     *
     * @param warehouseOut
     */
    private void operateData(WarehouseOut warehouseOut) {
        updateOrderInfo(warehouseOut);
        inserOrderTrack(warehouseOut);
    }

    //更新工单
    private void updateOrderInfo(WarehouseOut warehouseOut) {
        OrderInfo info = new OrderInfo();
        info.setShopId(warehouseOut.getShopId());
        info.setId(warehouseOut.getOrderId());
        info.setOrderStatus(OrderStatusEnum.DDSG.getKey());
        orderInfoService.updateById(info);
    }

    //插入记录流水
    private void inserOrderTrack(WarehouseOut warehouseOut) {
        // 更新订单列表
        OrderTrack track = new OrderTrack();
        track.setCreator(warehouseOut.getCreator());
        track.setOrderStatus(OrderStatusEnum.DDSG.getKey());
        track.setOrderId(warehouseOut.getOrderId());
        track.setShopId(warehouseOut.getShopId());
        orderTrackService.insert(track);
    }

    //获取库存数量
    private List<WarehouseOutDetailVo> getStock(List<WarehouseOutDetail> detailList) {
        List<Goods> goodsList = getGoodsList(detailList);
        Multimap<Long, Goods> map = Multimaps.index(goodsList, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods goods) {
                return goods.getId();
            }
        });
        List<WarehouseOutDetailVo> detailVoList = new ArrayList<>();
        for (WarehouseOutDetail detail : detailList) {
            for (Goods goods : map.get(detail.getGoodsId())) {
                WarehouseOutDetailVo detailVo = new WarehouseOutDetailVo();
                BeanUtils.copyProperties(detail, detailVo);
                detailVo.setStock(goods.getStock());
                detailVo.setDepot(goods.getDepot());
                detailVo.setGoodsSn(goods.getGoodsSn());
                detailVo.setCarInfo(goods.getCarInfo());
                detailVo.setMeasureUnit(goods.getMeasureUnit());
                detailVoList.add(detailVo);
            }
        }
        return detailVoList;
    }

    //将重复的商品合并为一条
    private List<WarehouseOutDetail> deduplication(List<WarehouseOutDetail> detailList) {
        Multimap<Long, WarehouseOutDetail> multimap = Multimaps.index(detailList, new Function<WarehouseOutDetail, Long>() {
            @Override
            public Long apply(WarehouseOutDetail detail) {
                return detail.getGoodsId();
            }
        });
        Set<Long> goodsIdSet = new HashSet<>();
        List<WarehouseOutDetail> warehouseOutDetailList = new ArrayList<>();
        for (WarehouseOutDetail detail : detailList) {
            goodsIdSet.add(detail.getGoodsId());
        }
        if (goodsIdSet.size() == detailList.size()) {
            return detailList;
        }
        for (Long goodsId : goodsIdSet) {
            WarehouseOutDetail outDetail = new WarehouseOutDetail();
            BigDecimal goodsCount = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (WarehouseOutDetail detail : multimap.get(goodsId)) {
                goodsCount = goodsCount.add(detail.getGoodsCount());
                totalAmount = totalAmount.add(detail.getGoodsCount().multiply(detail.getSalePrice()));
            }
            outDetail.setGoodsId(goodsId);
            outDetail.setGoodsCount(goodsCount);
            outDetail.setSalePrice(totalAmount.divide(goodsCount, 8, BigDecimal.ROUND_HALF_UP));
            if (goodsCount == BigDecimal.ZERO) {
                log.error("商品出库数量不能为0,对应goodsId:", goodsId);
                throw new BizException("商品出库数量不能为0,出库失败");
            }
            warehouseOutDetailList.add(outDetail);
        }
        return warehouseOutDetailList;
    }

    //获取销售总价,成本总价
    private void getTotal(WarehouseOutVo warehouseOutVo) {
        if (warehouseOutVo != null && !CollectionUtils.isEmpty(warehouseOutVo.getDetailVoList())) {
            BigDecimal saleAmount = BigDecimal.ZERO;
            BigDecimal costAmount = BigDecimal.ZERO;
            for (WarehouseOutDetail detail : warehouseOutVo.getDetailVoList()) {
                saleAmount = saleAmount.add(detail.getGoodsCount().multiply(detail.getSalePrice()));
                costAmount = costAmount.add(detail.getGoodsCount().multiply(detail.getInventoryPrice()));
            }
            warehouseOutVo.setCostAmount(costAmount);
            warehouseOutVo.setSaleAmount(saleAmount);
        }
    }

    //
    private List<LegendWarehouseOutDTOVo> makeList(List<LegendWarehouseOutDTO> legendWarehouseOutDTOs, Long shopId) {
        Map<String, Object> detailParam = new HashMap<>(2);
        List<LegendWarehouseOutDTOVo> legendWarehouseOutDTOVoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(legendWarehouseOutDTOs)) {
            return legendWarehouseOutDTOVoList;
        }
        List<Long> idList = new ArrayList<>();
        for (LegendWarehouseOutDTO dto : legendWarehouseOutDTOs) {
            if (!CollectionUtils.isEmpty(dto.getDetailList())) {
                for (LegendWarehouseOutDetail detail : dto.getDetailList()) {
                    idList.add(detail.getId());
                }
            }
        }
        if (CollectionUtils.isEmpty(idList)) {
            return legendWarehouseOutDTOVoList;
        }
        detailParam.put("ids", idList);
        detailParam.put("shopId", shopId);
        List<WarehouseOutDetail> detailList = warehouseOutDetailService.select(detailParam);
        if (CollectionUtils.isEmpty(detailList)) {
            return legendWarehouseOutDTOVoList;
        }
        List<WarehouseOutDetailVo> detailVoList = getStock(detailList);
        //详情分组
        Multimap<Long, WarehouseOutDetailVo> multimap = Multimaps.index(detailVoList, new Function<WarehouseOutDetailVo, Long>() {
            @Override
            public Long apply(WarehouseOutDetailVo warehouseOutDetailVo) {
                return warehouseOutDetailVo.getWarehouseOutId();
            }
        });
        for (LegendWarehouseOutDTO dto : legendWarehouseOutDTOs) {
            LegendWarehouseOutDTOVo vo = new LegendWarehouseOutDTOVo();
            BigDecimal saleAmount = BigDecimal.ZERO;
            BigDecimal costAmount = BigDecimal.ZERO;
            List<WarehouseOutDetailVo> detailVos = new ArrayList<>();
            for (WarehouseOutDetailVo detailVo : multimap.get(dto.getId().longValue())) {
                if (detailVo.getGoodsCount() == null) {
                    detailVo.setGoodsCount(BigDecimal.ZERO);
                }
                if (detailVo.getInventoryPrice() == null) {
                    detailVo.setInventoryPrice(BigDecimal.ZERO);
                }
                saleAmount = saleAmount.add(detailVo.getGoodsCount().multiply(detailVo.getSalePrice()));
                costAmount = costAmount.add(detailVo.getGoodsCount().multiply(detailVo.getInventoryPrice()));
                detailVo.setInventoryPrice(detailVo.getInventoryPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
                detailVos.add(detailVo);
            }
            BeanUtils.copyProperties(dto, vo);
            vo.setDetailVoList(detailVos);
            vo.setCostAmount(costAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setGmtCreate(DateUtil.convertDateToYMDHHmm(DateUtil.convertStringToDate(vo.getGmtCreate())));
            vo.setSaleAmount(saleAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            legendWarehouseOutDTOVoList.add(vo);
        }
        return legendWarehouseOutDTOVoList;
    }

    @Override
    public List<OrderGoods> selectTraceOrderGoods(Long orderId, Long shopId) {
        //查询实开物料
        List<OrderGoods> goodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        if (CollectionUtils.isEmpty(goodsList)) {
            throw new BizException("工单物料数据有误,操作失败");
        }
        //查询该工单已出库
        Map<String, Object> param = new HashMap<>(3);
        param.put("shopId", shopId);
        param.put("orderId", orderId);
        param.put("status", "LZCK");
        List<WarehouseOutDetail> detailList = warehouseOutDetailService.select(param);
        List<OrderGoods> traceOrderGoodsList = traceOrderGoods(detailList, goodsList);
        return traceOrderGoodsList;
    }

    private void checkOrderTraceNumber(List<OrderGoods> orderGoodsList, List<WarehouseOutDetail> detailList) {
        if (CollectionUtils.isEmpty(orderGoodsList) || CollectionUtils.isEmpty(detailList)) {
            throw new BizException("不存在未出库的物料,出库失败");
        }
        Multimap<Long, WarehouseOutDetail> multimap = Multimaps.index(detailList, new Function<WarehouseOutDetail, Long>() {
            @Override
            public Long apply(WarehouseOutDetail detail) {
                return detail.getOrderGoodsId();
            }
        });
        if (orderGoodsList.size() < detailList.size()) {
            throw new BizException("存在已完全出库的物料,不能重复出库");
        }
        Map<Long, OrderGoods> orderGoodsMap = Maps.newConcurrentMap();
        for (OrderGoods orderGoods : orderGoodsList) {
            orderGoodsMap.put(orderGoods.getId(), orderGoods);
        }
        for (WarehouseOutDetail detail : detailList) {
            if (!orderGoodsMap.containsKey(detail.getOrderGoodsId())) {
                throw new BizException("存在已完全出库的物料,不能重复出库");
            }
        }
        for (OrderGoods orderGoods : orderGoodsList) {
            BigDecimal traceNumber = orderGoods.getRemainingNumber();
            for (WarehouseOutDetail detail : multimap.get(orderGoods.getId())) {
                traceNumber = traceNumber.subtract(detail.getGoodsRealCount());
            }
            if (traceNumber.compareTo(BigDecimal.ZERO) == -1) {
                throw new BizException("工单可出库数量小于出库数量,出库失败");
            }
        }
    }

    @Override
    public DefaultPage<SearchOrderVo> selectOrderQuoteList(LegendOrderRequest orderRequest, PageableRequest pageable) {
        orderRequest.setOrderStatus(OrderNewStatusEnum.DBJANDYBJ.getOrderStatus());
        orderRequest.setOrderTag(Lists.newArrayList(OrderCategoryEnum.COMMON.getCode()));
        orderRequest.setPayStatus(Lists.newArrayList(OrderNewStatusEnum.DBJANDYBJ.getPayStatus()));
        Result<Page<LegendOrderInfoDTO>> result;
        try {
            log.info("[搜索平台]分页获取工单列表,入参:{}", LogUtils.objectToString(orderRequest));
            result = legendOrderService.queryLegendOrderList(orderRequest, pageable);
        } catch (Exception e) {
            log.error("[搜索平台]分页获取工单异常,异常信息:{}", e);
            return new DefaultPage(new ArrayList<SearchOrderVo>());
        }
        if (!result.isSuccess()) {
            log.error("[搜索平台]分页获取工单失败,失败信息:{}", result.getMessage());
            return new DefaultPage(new ArrayList<SearchOrderVo>());
        }
        Page page = result.getData();
        List<LegendOrderInfoDTO> legendOrderInfoDTOs = page.getContent();
        if (CollectionUtils.isEmpty(legendOrderInfoDTOs)) {
            return new DefaultPage(new ArrayList<SearchOrderVo>());
        }
        List<SearchOrderVo> searchOrderVos = new ArrayList<SearchOrderVo>();
        for (LegendOrderInfoDTO legendOrderInfoDTO : legendOrderInfoDTOs) {
            SearchOrderVo searchOrder = new SearchOrderVo();
            BeanUtils.copyProperties(legendOrderInfoDTO, searchOrder);
            searchOrderVos.add(searchOrder);
        }
        return new DefaultPage<>(searchOrderVos, pageable, page.getTotalElements());
    }

    @Override
    public DefaultPage<SearchOrderVo> selectOrderOutList(LegendOrderRequest orderRequest, PageableRequest pageable) {
        orderRequest.setOrderStatus(OrderNewStatusEnum.YPG.getOrderStatus());
        orderRequest.setPayStatus(Lists.newArrayList(OrderNewStatusEnum.YPG.getPayStatus()));
        log.info("[搜索平台]分页获取工单列表,入参:{}", ObjectUtils.objectToJSON(orderRequest));
        Long shopId = Long.parseLong(orderRequest.getShopId());
        Result<Page<LegendOrderInfoDTO>> result;
        try {
            result = legendOrderService.queryLegendOrderList(orderRequest, pageable);
        } catch (Exception e) {
            log.error("[搜索平台]分页获取工单异常,异常信息:{}", e);
            return new DefaultPage(new ArrayList<SearchOrderVo>());
        }
        if (!result.isSuccess()) {
            log.error("[搜索平台]分页获取工单失败,失败信息:{}", result.getMessage());
            return new DefaultPage(new ArrayList<SearchOrderVo>());
        }
        Page page = result.getData();
        List<LegendOrderInfoDTO> legendOrderInfoDTOs = page.getContent();
        if (CollectionUtils.isEmpty(legendOrderInfoDTOs)) {
            return new DefaultPage(new ArrayList<SearchOrderVo>());
        }
        List<SearchOrderVo> searchOrderVos = new ArrayList<SearchOrderVo>();

        for (LegendOrderInfoDTO legendOrderInfoDTO : legendOrderInfoDTOs) {
            SearchOrderVo searchOrder = new SearchOrderVo();
            BeanUtils.copyProperties(legendOrderInfoDTO, searchOrder);
            searchOrderVos.add(searchOrder);
        }
        // 断定工单状态(继续出库,全部出库,领料出库,添加物料)
        assertOrderWarehouseStatus(shopId, searchOrderVos);

        return new DefaultPage<SearchOrderVo>(searchOrderVos, pageable, page.getTotalElements());
    }


    /**
     * 断定工单状态(继续出库,全部出库,领料出库,添加物料)
     *
     * @param shopId
     */
    private void assertOrderWarehouseStatus(Long shopId, List<SearchOrderVo> searchOrderVoList) {
        List<Long> orderIdList = Lists.newArrayList();
        List<OrderType> orderTypeList = orderTypeService.selectByShopId(shopId);
        for (SearchOrderVo vo : searchOrderVoList) {
            //设置工单类型名称
            //注:SearchOrderVo中orderType是Integer类型
            for (OrderType orderType : orderTypeList) {
                if (orderType.getId().equals(Long.valueOf(vo.getOrderType()))) {
                    vo.setOrderTypeName(orderType.getName());
                    break;
                }
            }
            orderIdList.add(Long.parseLong(vo.getId()));
        }
        List<OrderGoods> orderGoodsList = orderGoodsService.selectActual(orderIdList, shopId);
        Map<String, Object> param = new HashMap<>(2);
        param.put("shopId", shopId);
        param.put("orderIds", orderIdList);
        param.put("status", "LZCK");
        List<WarehouseOutDetail> detailList = warehouseOutDetailService.select(param);
        List<WarehouseOut> warehouseOutList = warehouseOutService.select(param);
        Map<Long, WarehouseOut> outMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(warehouseOutList)) {
            for (WarehouseOut warehouseOut : warehouseOutList) {
                outMap.put(warehouseOut.getOrderId(), warehouseOut);
            }
        }
        List<OrderGoods> traceOrderGoodsList = traceOrderGoods(detailList, orderGoodsList);
        Multimap<Long, OrderGoods> traceMultimap = Multimaps.index(traceOrderGoodsList, new Function<OrderGoods, Long>() {
            @Override
            public Long apply(OrderGoods orderGoods) {
                return orderGoods.getOrderId();
            }
        });
        Multimap<Long, OrderGoods> goodsMultimap = Multimaps.index(orderGoodsList, new Function<OrderGoods, Long>() {
            @Override
            public Long apply(OrderGoods orderGoods) {
                return orderGoods.getOrderId();
            }
        });
        for (SearchOrderVo searchOrderVo : searchOrderVoList) {
            Long id = Long.valueOf(searchOrderVo.getId());
            BigDecimal count = BigDecimal.ZERO;
            BigDecimal sum = BigDecimal.ZERO;
            for (OrderGoods orderGoods : goodsMultimap.get(id)) {
                sum = sum.add(BigDecimal.ONE);
                sum = sum.add(BigDecimal.ONE.multiply(orderGoods.getGoodsNumber()));
            }
            for (OrderGoods orderGoods : traceMultimap.get(id)) {
                count = count.add(BigDecimal.ONE);
                count = count.add(BigDecimal.ONE.multiply(orderGoods.getRemainingNumber()));
            }
            if (BigDecimal.ZERO.compareTo(sum) == 0) {
                searchOrderVo.setGoodsOutFlag(0);//添加物料
                continue;
            }
            if (BigDecimal.ZERO.compareTo(count) == 0) {
                searchOrderVo.setGoodsOutFlag(3); //出库完成
                continue;
            }
            if (count.compareTo(sum) == 0 && !outMap.containsKey(Long.valueOf(searchOrderVo.getId()))) {
                searchOrderVo.setGoodsOutFlag(1);//领料出库
                continue;
            }
            searchOrderVo.setGoodsOutFlag(2);//继续出库
        }

    }

    private List<OrderGoods> traceOrderGoods(List<WarehouseOutDetail> detailList, List<OrderGoods> goodsList) {
        List<OrderGoods> orderGoodsList = new ArrayList<>(goodsList);
        if (!CollectionUtils.isEmpty(detailList)) {
            Multimap<Long, WarehouseOutDetail> multimap = Multimaps.index(detailList, new Function<WarehouseOutDetail, Long>() {
                @Override
                public Long apply(WarehouseOutDetail detail) {
                    if (detail.getOrderGoodsId() != null) {
                        return detail.getOrderGoodsId();
                    }
                    return -1L;
                }
            });
            Iterator<OrderGoods> iterator = orderGoodsList.iterator();
            while (iterator.hasNext()) {
                OrderGoods orderGoods = iterator.next();
                BigDecimal traceNumber = orderGoods.getGoodsNumber();
                BigDecimal outNumber = BigDecimal.ZERO;
                for (WarehouseOutDetail detail : multimap.get(orderGoods.getId())) {
                    traceNumber = traceNumber.subtract(detail.getGoodsRealCount());
                    outNumber = outNumber.add(detail.getGoodsRealCount());
                }
                if (BigDecimal.ZERO.compareTo(traceNumber) != -1) {
                    iterator.remove();
                } else {
                    orderGoods.setRemainingNumber(traceNumber);
                    orderGoods.setOutNumber(outNumber);
                }
            }
        } else {
            for (OrderGoods orderGoods : orderGoodsList) {
                orderGoods.setOutNumber(BigDecimal.ZERO);
                orderGoods.setRemainingNumber(orderGoods.getGoodsNumber());
            }
        }

        return orderGoodsList;
    }

    @Override
    public List<WarehouseOutDetail> selectByOrderIdAndShopId(Long orderId, Long shopId) {
        Map<String, Object> param = new HashMap<>(3);
        param.put("orderId", orderId);
        param.put("shopId", shopId);
        param.put("status", WarehouseOutStatusEnum.LZCK.name());
        List<WarehouseOutDetail> detailList = warehouseOutDetailService.select(param);
        return detailList;
    }

    @Override
    @Transactional
    public void batchUpdateWarehouseDetail(List<WarehouseOutDetail> detailList, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        for (WarehouseOutDetail detail : detailList) {
            detail.setModifier(userId);
            detail.setShopId(shopId);
            warehouseOutDetailService.updateById(detail);
        }
    }

    @Override
    @Transactional
    public void delete(Long shopId, Long id) {
        if (shopId == null) {
            throw new BizException("门店不存在");
        }
        if (id == null) {
            throw new BizException("出库记录不存在");
        }
        WarehouseOut warehouseOut = warehouseOutService.selectByIdAndShopId(id, shopId);
        if (WarehouseOutStatusEnum.HZCK.name().equals(warehouseOut.getStatus()) ||
                WarehouseOutStatusEnum.HZCK.name().equals(warehouseOut.getStatus())) {
            throw new BizException("非作废记录不允许删除");
        }
        warehouseOutService.deleteByShopIdAndId(shopId, id);
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("warehouseOutId", id);
        warehouseOutDetailService.delete(param);
    }

    @Override
    public DefaultPage<WarehouseOutRefundVo> selectOutRefund(Long customerCarId, Long shopId, PageRequest pageRequest) {
        DefaultPage defaultPage = new DefaultPage(Lists.newArrayList());
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("customerCarId", customerCarId);
        param.put("status", WarehouseOutStatusEnum.HZCK.name());
        param.put("shopId", shopId);
        List<WarehouseOut> warehouseOutList = warehouseOutService.select(param);
        if (CollectionUtils.isEmpty(warehouseOutList)) {
            return defaultPage;
        }
        List<Long> orderIds = Lists.transform(warehouseOutList, new Function<WarehouseOut, Long>() {
            @Override
            public Long apply(WarehouseOut warehouseOut) {
                return warehouseOut.getOrderId();
            }
        });
        List<OrderInfo> orderInfoList = orderInfoService.selectByIdsAndShopId(shopId, orderIds);
        List<Long> warehouseOutIds = Lists.transform(warehouseOutList, new Function<WarehouseOut, Long>() {
            @Override
            public Long apply(WarehouseOut warehouseOut) {
                return warehouseOut.getId();
            }
        });
        Map<String, Object> detailParam = Maps.newConcurrentMap();
        detailParam.put("warehouseOutIds", warehouseOutIds);
        detailParam.put("shopId", shopId);
        detailParam.put("limit", pageRequest.getPageSize());
        detailParam.put("offset", pageRequest.getOffset());
        detailParam.put("sorts", new String[]{"id desc"});
        int total = warehouseOutDetailService.selectCount(detailParam);
        List<WarehouseOutDetail> detailList = warehouseOutDetailService.select(detailParam);
        List<WarehouseOutRefundVo> outRefundVoList = Lists.newArrayList();
        for (WarehouseOutDetail detail : detailList) {
            WarehouseOutRefundVo vo = new WarehouseOutRefundVo();
            vo.setGoodsFormat(detail.getGoodsFormat());
            vo.setGoodsName(detail.getGoodsName());
            vo.setSalePrice(detail.getSalePrice());
            vo.setGoodsCount(detail.getGoodsCount());
            vo.setOutTime(detail.getGmtCreate());
            vo.setWarehouseOutId(detail.getWarehouseOutId());
            for (OrderInfo info : orderInfoList) {
                if (info.getId().equals(detail.getOrderId())) {
                    vo.setOrderId(info.getId());
                    vo.setOrderSn(info.getOrderSn());
                    break;
                }
            }
            for (WarehouseOut out : warehouseOutList) {
                if (out.getId().equals(detail.getWarehouseOutId())) {
                    vo.setWarehouseOutSn(out.getWarehouseOutSn());
                    vo.setRemark(out.getComment());
                    break;
                }
            }
            outRefundVoList.add(vo);
        }
        defaultPage = new DefaultPage(outRefundVoList, pageRequest, total);
        return defaultPage;
    }
}
