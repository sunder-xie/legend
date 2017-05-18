package com.tqmall.legend.biz.warehouse.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.validation.constraints.NotNull;

import com.tqmall.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.enums.InventoryTypeEnum;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderTrackService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.biz.warehouse.IWarehouseOutDetailService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.goods.FinalInventoryDao;
import com.tqmall.legend.dao.goods.GoodsDao;
import com.tqmall.legend.dao.order.OrderInfoDao;
import com.tqmall.legend.dao.order.OrderTrackDao;
import com.tqmall.legend.dao.warehouseout.WarehouseOutDao;
import com.tqmall.legend.dao.warehouseout.WarehouseOutDetailDao;
import com.tqmall.legend.entity.goods.FinalInventory;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsCar;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.warehouseout.WarehouseOut;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.pojo.warehouseOut.WarehouseOutDetailVO;

/**
 * Created by tan.li on 14/10/29.
 */
@Service
@Slf4j
public class WarehouseOutServiceImpl extends BaseServiceImpl implements WarehouseOutService {


    @Autowired
    private WarehouseOutDao warehouseOutDao;

    @Autowired
    private WarehouseOutDetailDao warehouseOutDetailDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private FinalInventoryDao finalInventoryDao;

    @Autowired
    OrderGoodsService orderGoodsService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    IWarehouseOutDetailService warehouseOutDetailService;
    @Autowired
    IOrderService orderService;
    @Autowired
    OrderTrackService orderTrackService;
    @Autowired
    MaxSnService maxSnService;



    @Override
    public List<WarehouseOut> select(Map<String, Object> searchMap) {
        return warehouseOutDao.select(searchMap);
    }

    @Override
    @Transactional
    public Result stackOut(OrderInfo orderInfo, UserInfo userInfo) {

        // 当前操作人
        Long optUserId = userInfo.getUserId();
        // 工单ID
        Long orderId = orderInfo.getId();
        // 门店ID
        Long shopId = orderInfo.getShopId();

        // 没有物料，无须出库
        List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        if (CollectionUtils.isEmpty(orderGoodsList)) {
            log.info("[工单物料出库]工单未订购配件，无须出库。工单ID:{}", orderId);
            return Result.wrapSuccessfulResult("");
        }

        // IF 全部出库 THEN return true IF 非全部出库 THEN 执行出库操作
        boolean isAllStackOut = isAllStackOut(orderInfo, userInfo);
        if (isAllStackOut) {
            return Result.wrapSuccessfulResult("");
        }

        // 1.校验库存是否足够
        // 商品ID:出货数量
        Map<Long, BigDecimal> stackOutGoodsMap = new HashMap<Long, BigDecimal>();
        for (OrderGoods orderGoods : orderGoodsList) {
            Long goodsId = orderGoods.getGoodsId();
            // 出货数量
            BigDecimal goodsNumber = orderGoods.getGoodsNumber();
            goodsNumber = (goodsNumber == null) ? BigDecimal.ZERO : goodsNumber;
            // 累加选择相同商品的出货数量
            if (stackOutGoodsMap.containsKey(goodsId)) {
                BigDecimal totalGoodsNumber = stackOutGoodsMap.get(goodsId);
                totalGoodsNumber = totalGoodsNumber.add(goodsNumber);
                stackOutGoodsMap.put(goodsId, totalGoodsNumber);
            } else {
                stackOutGoodsMap.put(goodsId, goodsNumber);
            }
        }

        // 批量查询物料信息
        List<Goods> currentGoodsList = goodsService.selectByIds(stackOutGoodsMap.keySet().toArray(new Long[stackOutGoodsMap.size()]));
        Map<Long, Goods> currentGoodsMap = new HashMap<Long, Goods>();
        for (Goods goods : currentGoodsList) {
            currentGoodsMap.put(goods.getId(), goods);
        }

        // 校验库存{1:不缺货;0：缺货}
        int isStockOut = 1;
        StringBuffer stockOutSB = new StringBuffer();
        Set<Long> stackOutGoodIdSet = stackOutGoodsMap.keySet();
        Iterator<Long> goodIterator = stackOutGoodIdSet.iterator();
        while (goodIterator.hasNext()) {
            Long stackOutGoodId = goodIterator.next();
            // 出库数量
            BigDecimal stackOutGoodNum = stackOutGoodsMap.get(stackOutGoodId);
            Goods currentGoods = currentGoodsMap.get(stackOutGoodId);
            // 库存数量
            BigDecimal stock = currentGoods.getStock();
            String goodsName = currentGoods.getName();
            // IF 出库数量 > 库存数量 THEN 库存不足
            if (stackOutGoodNum.compareTo(stock) == 1) {
                stockOutSB.append("商品:");
                stockOutSB.append(goodsName);
                stockOutSB.append(",出库数量:");
                stockOutSB.append(stackOutGoodNum);
                stockOutSB.append(",库存数量:");
                stockOutSB.append(stock);
                isStockOut = isStockOut & 0;
            } else {
                // 剩余库存数量
                BigDecimal remanentStock = stock.subtract(stackOutGoodNum);
                currentGoods.setStock(remanentStock);
            }
        }
        if (isStockOut == 0) {
            log.error("[工单物料出库]物料库存不足，工单ID:{} 物料:{}", orderId, stockOutSB.toString());
            return Result.wrapErrorResult("", "物料出库失败,库存不足,请入库");
        }

        // 2.循环更新库存
        try {
            Collection<Goods> goodsCollection = currentGoodsMap.values();
            for (Goods goods : goodsCollection) {
                Long goodsId = goods.getId();
                BigDecimal stock = goods.getStock();
                goodsService.updateStock(goodsId, stock, optUserId);
            }
        } catch (Exception e) {
            log.error("[工单物料出库]同步更新商品库存异常,异常信息", e);
            return Result.wrapErrorResult("", "同步更新商品库存失败");
        }

        // 3.生成出库单
        WarehouseOut warehouseOut = new WarehouseOut();
        warehouseOut.setShopId(shopId);
        warehouseOut.setModifier(optUserId);
        warehouseOut.setCreator(optUserId);
        warehouseOut.setCustomerCarId(orderInfo.getCustomerCarId());
        warehouseOut.setGoodsReceiver(optUserId);
        warehouseOut.setOrderId(orderId);
        warehouseOut.setOutType("GDCK");
        String newWhOutSn = maxSnService.getMaxSn(shopId, Constants.BLUE_OUT_WARHOUSE);
        warehouseOut.setWarehouseOutSn(newWhOutSn);
        warehouseOut.setCarLicense(orderInfo.getCarLicense());
        warehouseOut.setCarByname(orderInfo.getCarAlias());
        warehouseOut.setCarType(orderInfo.getCarInfo());
        warehouseOut.setCustomerId(orderInfo.getCustomerId());
        //注：仓库取的是联系人、联系电话，不是车主和车主电话
        warehouseOut.setCustomerName(orderInfo.getContactName());
        warehouseOut.setCustomerMobile(orderInfo.getContactMobile());

        warehouseOut.setStatus(InventoryTypeEnum.OUT_WAREHOUSE.getIndex());
        try {
            this.save(warehouseOut);
        } catch (Exception e) {
            log.error("[工单物料出库]生成出库单异常,异常信息", e);
            return Result.wrapErrorResult("", "生成出库单失败");

        }

        // 4.生成出库明细
        WarehouseOutDetail detail = null;
        List<WarehouseOutDetail> toInsertedWarehouseOutDetail = new ArrayList<WarehouseOutDetail>();
        for (OrderGoods orderGoods : orderGoodsList) {
            Long goodsId = orderGoods.getGoodsId();
            Goods goods = currentGoodsMap.get(goodsId);
            detail = new WarehouseOutDetail();
            detail.setCreator(optUserId);
            detail.setModifier(optUserId);
            detail.setShopId(shopId);
            detail.setGoodsCount(orderGoods.getGoodsNumber());
            detail.setGoodsId(orderGoods.getGoodsId());
            detail.setOrderGoodsId(orderGoods.getId());
            detail.setHandByIn(optUserId);
            detail.setHandByOn(optUserId);
            detail.setSupplierId(orderInfo.getReceiver());
            detail.setWarehouseOutId(warehouseOut.getId());
            detail.setWarehouseOutSn(warehouseOut.getWarehouseOutSn());
            detail.setOrderId(orderId);
            detail.setGoodsRealCount(orderGoods.getGoodsNumber());
            detail.setStatus(InventoryTypeEnum.OUT_WAREHOUSE.getIndex());
            detail.setGoodsFormat(goods.getFormat());
            detail.setGoodsName(goods.getName());
            detail.setGoodsSn(goods.getGoodsSn());
            detail.setCarInfo(goods.getCarInfo());
            detail.setInventoryPrice(goods.getInventoryPrice());
            detail.setSalePrice(orderGoods.getGoodsPrice());
            toInsertedWarehouseOutDetail.add(detail);
        }
        try {
            // Batch insert warehouseOutDetail
            warehouseOutDetailService.batchInsert(toInsertedWarehouseOutDetail);
        } catch (Exception e) {
            log.error("[工单物料出库]工单出库失败，创建出库单明细异常，异常信息", e);
            return Result.wrapErrorResult("", "工单出库失败");
        }

        // 更新工单状态、记录流水
        try {
            orderService.updateOrderStatus(orderId, OrderStatusEnum.DDSG);
        } catch (Exception e) {
            log.error("[工单物料出库]变更工单状态为'修理中'异常，异常信息", e);
            return Result.wrapErrorResult("", "变更工单状态为'修理中'失败");
        }

        // 记录工单流水
        try {
            orderTrackService.record(orderId, shopId, OrderStatusEnum.DDSG, userInfo);
        } catch (Exception e) {
            log.error("[工单物料出库]记录工单流水异常，异常信息", e);
            return Result.wrapErrorResult("", "记录工单流水失败");
        }

        return Result.wrapSuccessfulResult("");
    }

    @Override
    public boolean isAllStackOut(OrderInfo orderInfo, UserInfo userInfo) {
        Long orderId = orderInfo.getId();
        // 当前门店ID
        Long shopId = userInfo.getShopId();
        List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        boolean flag = true;
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                // 需要的商品数量
                BigDecimal goodsNumber = orderGoods.getGoodsNumber();
                BigDecimal outNumber = this.countOutGoods(orderGoods.getGoodsId(), shopId, orderId, orderGoods.getId());
                // 需要的商品数量>0 and 已出库商品数量<需要的商品数量
                if (goodsNumber.compareTo(BigDecimal.ZERO) == 1
                        && outNumber.compareTo(goodsNumber) == -1) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    @Override
    public boolean isStockOut(@NotNull OrderInfo orderInfo, UserInfo userInfo) {

        // 当前门店ID
        Long shopId = userInfo.getShopId();

        // 工单ID
        Long orderId = orderInfo.getId();

        // 没有物料，无须出库
        List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        if (CollectionUtils.isEmpty(orderGoodsList)) {
            // 未订购物料，无须出库
            log.info("[工单物料出库]工单未订购配件，无须出库。工单ID:{}", orderId);
            return Boolean.FALSE;
        }

        // 1.校验库存是否足够
        // 商品ID:出货数量
        Map<Long, BigDecimal> stackOutGoodsMap = new HashMap<Long, BigDecimal>();
        for (OrderGoods orderGoods : orderGoodsList) {
            Long goodsId = orderGoods.getGoodsId();
            // 出货数量
            BigDecimal goodsNumber = orderGoods.getGoodsNumber();
            goodsNumber = (goodsNumber == null) ? BigDecimal.ZERO : goodsNumber;
            // 累加选择相同商品的出货数量
            if (stackOutGoodsMap.containsKey(goodsId)) {
                BigDecimal totalGoodsNumber = stackOutGoodsMap.get(goodsId);
                totalGoodsNumber = totalGoodsNumber.add(goodsNumber);
                stackOutGoodsMap.put(goodsId, totalGoodsNumber);
            } else {
                stackOutGoodsMap.put(goodsId, goodsNumber);
            }
        }

        // 批量查询物料信息
        List<Goods> currentGoodsList = goodsService.selectByIds(stackOutGoodsMap.keySet().toArray(new Long[stackOutGoodsMap.size()]));
        Map<Long, Goods> currentGoodsMap = new HashMap<Long, Goods>();
        for (Goods goods : currentGoodsList) {
            currentGoodsMap.put(goods.getId(), goods);
        }

        // 校验库存{1:不缺货;0：缺货}
        int isStockOut = 1;
        StringBuffer stockOutSB = new StringBuffer();
        Set<Long> stackOutGoodIdSet = stackOutGoodsMap.keySet();
        Iterator<Long> goodIterator = stackOutGoodIdSet.iterator();
        while (goodIterator.hasNext()) {
            Long stackOutGoodId = goodIterator.next();
            // 出库数量
            BigDecimal stackOutGoodNum = stackOutGoodsMap.get(stackOutGoodId);
            Goods currentGoods = currentGoodsMap.get(stackOutGoodId);
            // 库存数量
            BigDecimal stock = currentGoods.getStock();
            String goodsName = currentGoods.getName();
            // IF 出库数量 > 库存数量 THEN 库存不足
            if (stackOutGoodNum.compareTo(stock) == 1) {
                stockOutSB.append("商品:");
                stockOutSB.append(goodsName);
                stockOutSB.append(",出库数量:");
                stockOutSB.append(stackOutGoodNum);
                stockOutSB.append(",库存数量:");
                stockOutSB.append(stock);
                isStockOut = isStockOut & 0;
            }
        }

        if (isStockOut == 0) {
            log.error("[工单物料出库]物料库存不足，工单ID:{} 物料:{}", orderId, stockOutSB.toString());
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }


    @Override
    public int save(WarehouseOut warehouseOut) {
        return warehouseOutDao.insert(warehouseOut);
    }

    /**
     * 统计已出库商品数量
     *
     * @param goodsId      物料ID
     * @param shopId       门店ID
     * @param orderId      工单ID
     * @param orderGoodsId 工单物料ID
     * @return
     */
    @Override
    public BigDecimal countOutGoods(Long goodsId, Long shopId, Long orderId, Long orderGoodsId) {
        List<WarehouseOutDetail> list = getWarehouseOutDetail(goodsId, shopId, orderId, orderGoodsId);
        if (CollectionUtils.isEmpty(list)) {
            return BigDecimal.ZERO;
        }

        // 出库单数量
        BigDecimal realCount = BigDecimal.ZERO;
        for (WarehouseOutDetail detail : list) {
            BigDecimal goodsRealCount = detail.getGoodsRealCount();
            goodsRealCount = (goodsRealCount == null) ? BigDecimal.ZERO : goodsRealCount;
            realCount = realCount.add(goodsRealCount);
        }

        return realCount;
    }

    private List<WarehouseOutDetail> getWarehouseOutDetail(Long goodsId, Long shopId, Long orderId, Long orderGoodsId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("goodsId", goodsId);
        parameters.put("shopId", shopId);
        parameters.put("orderId", orderId);
        parameters.put("orderGoodsId", orderGoodsId);
        parameters.put("status", InventoryTypeEnum.OUT_WAREHOUSE.getIndex());


        List<WarehouseOutDetail> warehouseOutDetailList = null;
        try {
            warehouseOutDetailList = warehouseOutDetailDao.select(parameters);
        } catch (Exception e) {
            log.error("[DB] query legend_warehouse_out failure 异常信息", e);
            warehouseOutDetailList = new ArrayList<WarehouseOutDetail>();
        }

        if (CollectionUtils.isEmpty(warehouseOutDetailList)) {
            warehouseOutDetailList = new ArrayList<WarehouseOutDetail>();
        }

        return warehouseOutDetailList;
    }

    /**
     * @param goodsId
     * @param shopId
     * @param orderId
     * @param orderGoodsId
     * @return
     */
    @Override
    public List<WarehouseOutDetail> getWarehouseDetail(Long goodsId, Long shopId, Long orderId, Long orderGoodsId) {
        return getWarehouseOutDetail(goodsId, shopId, orderId, orderGoodsId);
    }



    /**
     * @param searchParams
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Result blueInvalid(Map<String, Object> searchParams, UserInfo userId) {
        // 获取这个出库单所包含的所有的已出库物料
        List<WarehouseOutDetail> list = warehouseOutDetailDao.select(searchParams);
        if (!CollectionUtils.isEmpty(list)) {
            for (WarehouseOutDetail detail : list) {
                Object id = detail.getGoodsId();
                log.info("蓝字作废开始: goodsId=" + id);
                Goods goods = goodsDao.selectById(detail.getGoodsId());
                // 计算总额
                BigDecimal totalAmount = goods.getInventoryPrice().multiply(goods.getStock()).add(detail.getInventoryPrice().multiply(detail.getGoodsCount()));
                // 计算总数
                BigDecimal totalCount = detail.getGoodsCount().add(goods.getStock());
                //入库物料总数
                goods.setInventoryPrice(totalAmount.divide(totalCount, 8, BigDecimal.ROUND_HALF_UP));
                // 更新库存
                goods.setModifier(userId.getUserId());
                goods.setStock(goods.getStock().add(detail.getGoodsCount()));
                goodsDao.updateById(goods);

                FinalInventory finalInventory = new FinalInventory();
                finalInventory.setFinalDate(new Date());
                finalInventory.setInventoryType(InventoryTypeEnum.BLUE_INVALID.getIndex());
                finalInventory.setGoodsId(goods.getId());
                finalInventory.setCreator(userId.getUserId());
                finalInventory.setGoodsCount(totalCount);
                finalInventory.setGoodsFinalPrice(goods.getInventoryPrice());
                finalInventory.setModifier(userId.getUserId());
                finalInventory.setShopId(userId.getShopId());
                finalInventory.setOrderId(detail.getOrderId());
                finalInventoryDao.insert(finalInventory);

                WarehouseOut out = new WarehouseOut();
                out.setId(Long.valueOf(searchParams.get("warehouseOutId") + ""));
                out.setStatus(InventoryTypeEnum.BLUE_INVALID.getIndex());
                warehouseOutDao.updateById(out);

                detail.setStatus(InventoryTypeEnum.BLUE_INVALID.getIndex());
                warehouseOutDetailDao.updateById(detail);
                log.info("蓝字作废结束: goodsId=" + id);
            }
        }

        return Result.wrapSuccessfulResult(true);
    }

    /**
     * @param searchParams
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Result redInvalid(Map<String, Object> searchParams, Long userId) {
        // 获取这个出库单所包含的所有的已出库物料
        List<WarehouseOutDetail> list = warehouseOutDetailDao.select(searchParams);
        if (!CollectionUtils.isEmpty(list)) {
            for (WarehouseOutDetail detail : list) {
                Object id = detail.getGoodsId();
                log.info("红字作废开始: goodsId=" + id);
                Goods goods = goodsDao.selectById(detail.getGoodsId());
                BigDecimal goodsCount = detail.getGoodsCount().abs();

                // 计算总数
                BigDecimal totalCount = goods.getStock().subtract(goodsCount);

                // 更新库存
                if (totalCount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new RuntimeException("【" + detail.getGoodsName() + "】库存数量不足");
                }
                if (totalCount.compareTo(BigDecimal.ZERO) > 0) {
                    // 计算总额
                    BigDecimal totalAmount = goods.getInventoryPrice().multiply(goods.getStock()).subtract(detail.getInventoryPrice().multiply(goodsCount));
                    goods.setInventoryPrice(totalAmount.divide(totalCount, 8, BigDecimal.ROUND_HALF_UP));
                }
                goods.setModifier(userId);
                goods.setStock(totalCount);
                goodsDao.updateById(goods);

                FinalInventory finalInventory = new FinalInventory();
                finalInventory.setFinalDate(new Date());
                finalInventory.setInventoryType(InventoryTypeEnum.BLUE_INVALID.getIndex());
                finalInventory.setGoodsId(goods.getId());
                finalInventory.setCreator(userId);
                finalInventory.setGoodsCount(totalCount);
                finalInventory.setGoodsFinalPrice(goods.getInventoryPrice());
                finalInventory.setModifier(userId);
                finalInventory.setShopId(detail.getShopId());
                finalInventory.setOrderId(detail.getOrderId());
                finalInventoryDao.insert(finalInventory);

                WarehouseOut out = new WarehouseOut();
                out.setId(Long.valueOf(searchParams.get("warehouseOutId") + ""));
                out.setStatus(InventoryTypeEnum.RED_INVALID.getIndex());
                warehouseOutDao.updateById(out);

                detail.setStatus(InventoryTypeEnum.RED_INVALID.getIndex());
                warehouseOutDetailDao.updateById(detail);

                // 修改原单的出库数量
                WarehouseOutDetail oldDetail = warehouseOutDetailDao.selectById(detail.getRelDetailId());
                // 原单的实际出库量增加
                oldDetail.setGoodsRealCount(oldDetail.getGoodsRealCount().add(goodsCount));
                oldDetail.setModifier(userId);
                warehouseOutDetailDao.updateById(oldDetail);
                log.info("红字作废结束: goodsId=" + id);
            }
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 获取某条物料出库记录的信息
     *
     * @param goodsId
     * @param shopId
     * @param orderId
     * @param orderGoodsId
     * @return
     */
    @Override
    public WarehouseOutDetailVO getWarehouseDetailForGoods(Long goodsId, Long shopId, Long orderId, Long orderGoodsId) {
        WarehouseOutDetailVO detailVO = new WarehouseOutDetailVO();
        List<WarehouseOutDetail> list = getWarehouseDetail(goodsId, shopId, orderId, orderGoodsId);

        // 分支一：已有出库记录
        if (!CollectionUtils.isEmpty(list)) {
            BigDecimal count = BigDecimal.ZERO;
            BigDecimal salePrice = BigDecimal.ZERO;
            BigDecimal inventoryPrice = BigDecimal.ZERO;
            for (WarehouseOutDetail detail : list) {
                count = count.add(detail.getGoodsRealCount());
                salePrice = salePrice.add(detail.getSalePrice().multiply(detail.getGoodsRealCount()));
                inventoryPrice = inventoryPrice.add(detail.getInventoryPrice().multiply(detail.getGoodsRealCount()));
            }
            // 分支三：有出库记录但是出了0个
            if (count.compareTo(BigDecimal.ZERO) <= 0) {
                detailVO.setGoodsCount(BigDecimal.ZERO);
                detailVO.setSaleAmount(BigDecimal.ZERO);
                detailVO.setInventoryAmount(BigDecimal.ZERO);
                detailVO.setSalePrice(BigDecimal.ZERO);
                detailVO.setInventoryPrice(BigDecimal.ZERO);
            } else { //分支四：有出库记录且数量大于0
                // 计算售卖数量
                detailVO.setGoodsCount(count);
                detailVO.setSalePrice(salePrice.divide(count, 2, BigDecimal.ROUND_HALF_UP));
                detailVO.setInventoryPrice(inventoryPrice.divide(count, 2, BigDecimal.ROUND_HALF_UP));
            }
            // 计算售卖总价
            detailVO.setSaleAmount((detailVO.getSalePrice().multiply(detailVO.getGoodsCount()).setScale(2, BigDecimal.ROUND_HALF_UP)));
            // 计算售成本总价
            detailVO.setInventoryAmount(detailVO.getInventoryPrice().multiply(detailVO.getGoodsCount()).setScale(2, BigDecimal.ROUND_HALF_UP));

        } else {// 分支二：没有有出库记录
            detailVO.setGoodsCount(BigDecimal.ZERO);
            detailVO.setSaleAmount(BigDecimal.ZERO);
            detailVO.setInventoryAmount(BigDecimal.ZERO);
            detailVO.setSalePrice(BigDecimal.ZERO);
        }
        return detailVO;
    }

    @Override
    public int updateById(WarehouseOut warehouseOut) {
        return warehouseOutDao.updateById(warehouseOut);
    }

    @Override
    public int deleteByShopIdAndId(Long shopId, Long id) {
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("shopId", shopId);
        param.put("id", id);
        return warehouseOutDao.delete(param);
    }

    @Override
    public Map<Long, BigDecimal> mapOrderId2realInventoryAmount(Long shopId, List<Long> orderIds) {
        Assert.notNull(shopId);
        Map<Long, BigDecimal> orderId2realInventoryAmountMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(orderIds)) {
            return orderId2realInventoryAmountMap;
        }
        List<CommonPair<Long, BigDecimal>> pairs = warehouseOutDetailDao.listOrderId2RealInventoryAmountPair(shopId, orderIds);
        for (CommonPair<Long, BigDecimal> pair : pairs) {
            orderId2realInventoryAmountMap.put(pair.getDataF(), pair.getDataS());
        }
        return orderId2realInventoryAmountMap;
    }

    @Override
    public int selectCount(Map<String, Object> param) {
        return warehouseOutDao.selectCount(param);
    }

    @Override
    public WarehouseOut selectByIdAndShopId(Long id, Long shopId) {
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("id", id);
        List<WarehouseOut> warehouseOutList = warehouseOutDao.select(param);
        if (CollectionUtils.isEmpty(warehouseOutList)) {
            return null;
        }
        return warehouseOutList.get(0);
    }

    @Override
    public int insert(WarehouseOut warehouseOut) {
        return warehouseOutDao.insert(warehouseOut);
    }

    /**
     * 物料出库单作废
     *
     * @param orderId
     * @param userInfo
     * @return
     */
    @Override
    @Transactional
    public Result invalid(Long orderId, UserInfo userInfo) {
        // 工单是否有物料出库单
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("orderId", orderId);
        List<WarehouseOut> warehouseOutList = select(searchMap);

        if (!CollectionUtils.isEmpty(warehouseOutList)) {
            try {
                for (WarehouseOut warehouseOut : warehouseOutList) {
                    String status = warehouseOut.getStatus();
                    searchMap = new HashMap<>();
                    searchMap.put("warehouseOutId", warehouseOut.getId());
                    searchMap.put("shopId", userInfo.getShopId());
                    if (InventoryTypeEnum.OUT_WAREHOUSE.getIndex().equals(status)) {
                        // 蓝字出库作废
                        blueInvalid(searchMap, userInfo);
                    } else if (InventoryTypeEnum.OUT_WAREHOUSE_RED.getIndex().equals(status)) {
                        // 红字出库作废
                        redInvalid(searchMap, userInfo.getUserId());
                    }
                    log.info("出库单作废: 出库单号为:{} 工单号为:{} 操作人:{}", warehouseOut.getId(), orderId, userInfo.getUserId());
                }
            } catch (Exception e) {
                log.error("出库单作废异常, 异常信息", e);
                throw new BizException("出库单无效失败");
            }
        }
        return Result.wrapSuccessfulResult("出库单作废成功");
    }
}
