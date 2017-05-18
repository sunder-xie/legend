package com.tqmall.legend.biz.inventory.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.cube.shop.RpcWarehouseAnalysisService;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsNumDTO;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.FinalInventoryService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.inventory.InventoryService;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.goods.GoodsDao;
import com.tqmall.legend.dao.inventory.InventoryRecordDao;
import com.tqmall.legend.dao.inventory.InventoryStockDao;
import com.tqmall.legend.entity.goods.FinalInventory;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.inventory.InventoryRecord;
import com.tqmall.legend.entity.inventory.InventoryStock;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.warehouse.CalculateInventoryAmountBo;
import com.tqmall.legend.entity.warehouse.GoodsTqmallPriceBo;
import com.tqmall.legend.entity.warehouse.InventoryFormBo;
import com.tqmall.legend.entity.warehouse.InventoryGoodsBo;
import com.tqmall.legend.entity.warehouse.InventoryStatusEnum;
import com.tqmall.legend.entity.warehouse.StockStatusOfOrder;
import com.tqmall.legend.facade.goods.GoodsFacade;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.tqmallstall.domain.result.Legend.LegendGoodsInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lixiao on 14-12-8.
 */
@Service
public class InventoryServiceImpl extends BaseServiceImpl implements InventoryService {
    private static Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRecordDao inventoryRecordDao;
    @Autowired
    private InventoryStockDao inventoryStockDao;
    @Autowired
    private MaxSnService maxSnService;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ShopService shopService;
    @Autowired
    FinalInventoryService finalInventoryService;
    @Autowired
    RpcWarehouseAnalysisService rpcWarehouseAnalysisService;
    @Autowired
    OrderGoodsService orderGoodsService;
    @Autowired
    WarehouseOutService warehouseOutService;
    @Autowired
    GoodsFacade goodsFacade;

    @Transactional
    @Override
    public Result generate(InventoryFormBo inventoryFormBo, UserInfo userInfo) {

        Long currentShopId = userInfo.getShopId();
        Long currentUserId = userInfo.getUserId();

        // 1.插入库存记录
        InventoryRecord inventoryRecord = new InventoryRecord();
        // 盘点单编号
        inventoryRecord.setRecordSn(inventoryFormBo.getRecordSn());
        // 开单人
        inventoryRecord.setOperatorName(userInfo.getName());
        // 状态{1:草稿;2:正式单}
        inventoryRecord.setStatus(inventoryFormBo.getStatus());
        // 盘点人ID | 盘点人姓名
        inventoryRecord.setInventoryCheckerId(inventoryFormBo.getInventoryCheckerId());
        inventoryRecord.setInventoryCheckerName(inventoryFormBo.getInventoryCheckerName());
        inventoryRecord.setInventoryRemark(inventoryFormBo.getInventoryRemark());
        // 操作人信息
        inventoryRecord.setShopId(currentShopId);
        inventoryRecord.setCreator(currentUserId);
        inventoryRecord.setGmtCreate(new Date());
        inventoryRecord.setModifier(currentUserId);
        inventoryRecord.setGmtModified(new Date());
        // 保存盘点记录
        inventoryRecordDao.insert(inventoryRecord);

        // 盘点配件列表
        List<InventoryGoodsBo> inventoryGoodsBos = inventoryFormBo.getInventoryGoodsBos();

        // 新生成盘点记录编号
        Long newInventoryRecordId = inventoryRecord.getId();
        String newInventoryRecordSn = inventoryRecord.getRecordSn();

        // batch query 配件的成本价
        Map<Long, Goods> goodsMap = batchFetchGoods(inventoryGoodsBos);

        // 生成配件盘点
        List<InventoryStock> inventoryStocks = new ArrayList<InventoryStock>(inventoryGoodsBos.size());
        InventoryStock inventoryStock = null;
        Long goodsId = null;
        Goods goods = null;

        // 最后结存
        List<FinalInventory> finalInventories = new ArrayList<FinalInventory>();
        FinalInventory finalInventory = null;
        for (InventoryGoodsBo inventoryGoodsBo : inventoryGoodsBos) {
            inventoryStock = new InventoryStock();
            // 配件ID
            goodsId = inventoryGoodsBo.getGoodsId();
            goods = goodsMap.get(goodsId);
            // 过滤配件被盘点同时,配件被删除情况
            if (goods == null) {
                continue;
            }

            // 配件名称|配件零件号|物料编码|配件单位
            inventoryStock.setGoodsId(goodsId);
            inventoryStock.setGoodsName(goods.getName());
            inventoryStock.setGoodsFormat(goods.getFormat());
            inventoryStock.setGoodsSn(goods.getGoodsSn());
            inventoryStock.setMeasureUnit(goods.getMeasureUnit());
            // 系统当前库存
            BigDecimal currentStock = goods.getStock();
            currentStock = (currentStock == null) ? BigDecimal.ZERO : currentStock;
            inventoryStock.setCurrentStock(goods.getStock());
            // 实盘库存为null时 不计算库存差额
            BigDecimal realStock = inventoryGoodsBo.getRealStock();
            if (realStock != null) {
                inventoryStock.setRealStock(realStock);
                // 库存差额
                inventoryStock.setDiffStock(realStock.subtract(currentStock));
            }

            // 盘点前的成本价
            BigDecimal inventoryPrePrice = inventoryGoodsBo.getInventoryPrePrice();
            inventoryPrePrice = (inventoryPrePrice == null) ? BigDecimal.ZERO : inventoryPrePrice;
            inventoryStock.setInventoryPrePrice(inventoryPrePrice);
            // 盘点后的成本价
            BigDecimal inventoryPrice = inventoryGoodsBo.getInventoryPrice();
            inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
            inventoryStock.setInventoryPrice(inventoryPrice);
            inventoryStock.setReason(inventoryGoodsBo.getReason());

            // 关联盘点记录ID | 关联盘点记录编号
            inventoryStock.setRecordId(newInventoryRecordId);
            inventoryStock.setRecordSn(newInventoryRecordSn);

            // 操作人信息
            inventoryStock.setShopId(currentShopId);
            inventoryStock.setCreator(currentUserId);
            inventoryStock.setGmtCreate(new Date());

            inventoryStocks.add(inventoryStock);

            // 如果生成正式单 更新库存
            Integer inventoryStatus = inventoryRecord.getStatus();
            if (inventoryStatus != null && inventoryStatus == 2) {
                if (goods.getShopId().equals(currentShopId)) {

                    // 结存对象
                    finalInventory = new FinalInventory();
                    finalInventory.setInventoryType("INVENTORY");
                    finalInventory.setGoodsId(goods.getId());
                    finalInventory.setShopId(currentShopId);
                    finalInventory.setGoodsFinalPrice(inventoryPrice);
                    finalInventory.setGoodsCount(realStock);
                    finalInventory.setCreator(currentUserId);
                    finalInventory.setFinalDate(new Date());
                    finalInventories.add(finalInventory);

                    // 真实库存
                    goods.setStock(realStock);
                    // 盘点后成本价
                    goods.setInventoryPrice(inventoryPrice);
                    goods.setModifier(currentUserId);
                    goods.setGmtModified(new Date());
                    goodsDao.updateById(goods);
                }
            }
        }

        // 更新盘点配件种类数
        inventoryRecord.setGoodsCount(Long.valueOf(inventoryStocks.size() + ""));
        inventoryRecordDao.updateById(inventoryRecord);

        // batch insert 配件盘点记录
        inventoryStockDao.batchInsert(inventoryStocks);

        // 批量保存结存
        if (!CollectionUtils.isEmpty(finalInventories)) {
            finalInventoryService.batchInsert(finalInventories);
        }

        return Result.wrapSuccessfulResult(newInventoryRecordId);
    }

    @Override
    public List<InventoryStock> getInventoryStocks(Long inventoryRecordId, Long shopId) {
        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("recordId", inventoryRecordId);
        parameters.put("shopId", shopId);
        parameters.put("isDeleted", 'N');
        List<InventoryStock> inventoryStocks = null;
        try {
            inventoryStocks = inventoryStockDao.select(parameters);
        } catch (Exception e) {
            logger.error("获取被盘点配件列表失败,原因:", e);
            inventoryStocks = new ArrayList<InventoryStock>();
            return inventoryStocks;
        }

        if (CollectionUtils.isEmpty(inventoryStocks)) {
            inventoryStocks = new ArrayList<InventoryStock>();
        }

        return inventoryStocks;
    }

    @Override
    public Optional<InventoryRecord> get(Long itemId, Long shopId) {
        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("id", itemId);
        parameters.put("shopId", shopId);
        parameters.put("isDeleted", 'N');
        InventoryRecord inventoryRecord = null;
        try {
            List<InventoryRecord> inventoryRecords = inventoryRecordDao.select(parameters);
            if (!CollectionUtils.isEmpty(inventoryRecords)) {
                inventoryRecord = inventoryRecords.get(0);
            }
        } catch (Exception e) {
            logger.error("获取盘点记录失败,原因:{}", e);
            return Optional.absent();
        }

        return Optional.fromNullable(inventoryRecord);
    }

    @Override
    @Transactional
    public Result updateStatus(@NotNull InventoryRecord inventoryRecord, @NotNull InventoryStatusEnum statusEnum, UserInfo currentLoginUser) {
        inventoryRecord.setStatus(statusEnum.getCode());
        inventoryRecord.setGmtModified(new Date());
        inventoryRecord.setModifier(currentLoginUser.getUserId());
        int result = inventoryRecordDao.updateById(inventoryRecord);
        if (result == 1) {
            return Result.wrapSuccessfulResult(inventoryRecord.getId());
        } else {
            return Result.wrapErrorResult("", "更新失败");
        }
    }

    @Override
    @Transactional
    public Result updateRecordAndStock(InventoryFormBo inventoryFormBo, UserInfo currentLoginUser) {

        Long shopId = currentLoginUser.getShopId();
        Long userId = currentLoginUser.getUserId();

        // 1.更新盘点记录表
        Long recordId = inventoryFormBo.getRecordId();
        Optional<InventoryRecord> inventoryRecordOptional = this.get(recordId, shopId);
        if (!inventoryRecordOptional.isPresent()) {
            return Result.wrapErrorResult("", "盘点记录不存在!");
        }
        InventoryRecord inventoryRecord = inventoryRecordOptional.get();

        // 状态
        inventoryRecord.setStatus(inventoryFormBo.getStatus());
        // 更新人
        inventoryRecord.setModifier(userId);
        // 更新时间
        inventoryRecord.setGmtModified(new Date());
        // 盘点人
        inventoryRecord.setInventoryCheckerId(inventoryFormBo.getInventoryCheckerId());
        inventoryRecord.setInventoryCheckerName(inventoryFormBo.getInventoryCheckerName());
        // 备注
        inventoryRecord.setInventoryRemark(inventoryFormBo.getInventoryRemark());


        Long newInventoryRecordId = inventoryRecord.getId();
        String newInventoryRecordSn = inventoryRecord.getRecordSn();

        // 2.更新配件盘点表
        List<InventoryGoodsBo> inventoryGoodsBos = inventoryFormBo.getInventoryGoodsBos();
        Map<Long, Goods> goodsMap = batchFetchGoods(inventoryGoodsBos);

        // 历史配件盘点记录
        List<InventoryStock> oldInventoryStocks = this.getInventoryStocks(newInventoryRecordId, shopId);
        Map<Long, InventoryStock> oldInventoryStockMap = new HashMap<Long, InventoryStock>();
        for (InventoryStock stock : oldInventoryStocks) {
            oldInventoryStockMap.put(stock.getId(), stock);
        }

        // 有效配件盘点明细(新增的+待更新的)
        List<InventoryStock> validInventoryStocks = new ArrayList<InventoryStock>();
        // 新增配件盘点明细
        List<InventoryStock> newInventoryStocks = new ArrayList<InventoryStock>();

        // 新增的配件盘点明细
        InventoryStock newInventoryStock = null;
        // 待更新的配件盘点明细
        InventoryStock toUpdateInventoryStock = null;
        for (InventoryGoodsBo inventoryGoodsBo : inventoryGoodsBos) {
            Long inventoryStockId = inventoryGoodsBo.getStockId();

            // 配件ID
            Long goodsId = inventoryGoodsBo.getGoodsId();
            Goods goods = goodsMap.get(goodsId);
            // 过滤配件被盘点同时,配件被删除情况
            if (goods == null) {
                continue;
            }
            // 当前库存
            BigDecimal currentStock = BigDecimal.ZERO;
            // 新增的配件盘点
            if (inventoryStockId == null) {
                newInventoryStock = new InventoryStock();
                newInventoryStock.setGoodsId(goodsId);
                // 配件名称
                newInventoryStock.setGoodsName(goods.getName());
                //  配件零件号
                newInventoryStock.setGoodsFormat(goods.getFormat());
                // 物料编码
                newInventoryStock.setGoodsSn(goods.getGoodsSn());
                // 配件单位
                newInventoryStock.setMeasureUnit(goods.getMeasureUnit());
                // 系统当前库存
                currentStock = goods.getStock();
                currentStock = (currentStock == null) ? BigDecimal.ZERO : currentStock;
                newInventoryStock.setCurrentStock(goods.getStock());
                //实盘库存为null时 不计算库存差额
                BigDecimal realStock = inventoryGoodsBo.getRealStock();
                if (realStock != null) {
                    newInventoryStock.setRealStock(realStock);
                    // 库存差额
                    newInventoryStock.setDiffStock(realStock.subtract(currentStock));
                }

                // 原因
                newInventoryStock.setReason(inventoryGoodsBo.getReason());
                // 库存单价
                BigDecimal inventoryPrice = inventoryGoodsBo.getInventoryPrice();
                inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
                newInventoryStock.setInventoryPrice(inventoryPrice);
                // 关联盘点记录ID
                newInventoryStock.setRecordId(newInventoryRecordId);
                // 关联盘点记录编号
                newInventoryStock.setRecordSn(newInventoryRecordSn);

                newInventoryStock.setShopId(shopId);
                newInventoryStock.setCreator(userId);
                newInventoryStock.setGmtCreate(new Date());
                newInventoryStocks.add(newInventoryStock);
                validInventoryStocks.add(newInventoryStock);
            }

            // 待更新的
            if (inventoryStockId != null && oldInventoryStockMap.containsKey(inventoryStockId)) {
                toUpdateInventoryStock = oldInventoryStockMap.get(inventoryStockId);
                // 盘点日期
                toUpdateInventoryStock.setGmtModified(new Date());
                // 当前库存
                currentStock = toUpdateInventoryStock.getCurrentStock();
                currentStock = (currentStock == null) ? BigDecimal.ZERO : currentStock;
                // 实盘库存为null时 不计算库存差额
                BigDecimal realStock = inventoryGoodsBo.getRealStock();
                if (realStock != null) {
                    toUpdateInventoryStock.setRealStock(realStock);
                    // 库存差额
                    toUpdateInventoryStock.setDiffStock(realStock.subtract(currentStock));
                }

                // 库存单价
                BigDecimal inventoryPrice = inventoryGoodsBo.getInventoryPrice();
                inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
                toUpdateInventoryStock.setInventoryPrice(inventoryPrice);

                // 原因
                toUpdateInventoryStock.setReason(inventoryGoodsBo.getReason());
                inventoryStockDao.updateById(toUpdateInventoryStock);
                validInventoryStocks.add(toUpdateInventoryStock);
                oldInventoryStockMap.remove(inventoryStockId);
            }
        }

        // batch insert 配件盘点记录
        if (!CollectionUtils.isEmpty(newInventoryStocks)) {
            inventoryStockDao.batchInsert(newInventoryStocks);
        }

        // batch delete 配件盘点记录
        if (!CollectionUtils.isEmpty(oldInventoryStockMap)) {
            Set<Long> toDelInventoryStocks = oldInventoryStockMap.keySet();
            inventoryStockDao.deleteByIds(toDelInventoryStocks.toArray());
        }

        // 更新盘点记录
        inventoryRecord.setGoodsCount(Long.valueOf(validInventoryStocks.size() + ""));
        inventoryRecordDao.updateById(inventoryRecord);

        // IF 生成正式盘点 THEN 同步goods配件实际库存
        Integer inventoryStatus = inventoryRecord.getStatus();
        if (inventoryStatus != null && inventoryStatus == InventoryStatusEnum.FORMAL.getCode()) {
            this.syncGoodsStock(validInventoryStocks, currentLoginUser);
        }

        return Result.wrapSuccessfulResult(newInventoryRecordId);
    }

    @Override
    public Result calculateInventoryAmount(Long[] inventoryIds, UserInfo userInfo) {
        // 当前门店ID
        Long shopId = userInfo.getShopId();

        // 获取盘点记录
        List<InventoryRecord> inventoryRecords = inventoryRecordDao.selectByIds(inventoryIds);

        // 计算盘点盈亏对象集合
        List<CalculateInventoryAmountBo> calculateInventoryAmountBos =
                new ArrayList<CalculateInventoryAmountBo>(inventoryRecords.size());
        // 计算盈亏对象
        CalculateInventoryAmountBo calculateInventoryAmountBo = null;
        for (InventoryRecord record : inventoryRecords) {
            calculateInventoryAmountBo = new CalculateInventoryAmountBo();
            // 被盘点记录ID
            Long recordId = record.getId();
            // 获取被盘点配件集合
            List<InventoryStock> stocks = this.getInventoryStocks(recordId, shopId);
            // 总亏损数量
            BigDecimal kuiTotal = BigDecimal.ZERO;
            // 总亏损金额
            BigDecimal kuiTotalAmount = BigDecimal.ZERO;
            // 总盈数量
            BigDecimal yinTotal = BigDecimal.ZERO;
            // 总盈数金额
            BigDecimal yinTotalAmount = BigDecimal.ZERO;
            for (InventoryStock inventoryStock : stocks) {
                BigDecimal diffStock = inventoryStock.getDiffStock();

                // 配件盈亏数量为null, 不纳入盘点记录盈亏数量计算
                if (diffStock != null) {
                    // <0 盘亏 | >0 盘盈
                    if (diffStock.compareTo(BigDecimal.ZERO) == 1) {
                        yinTotal = yinTotal.add(diffStock);
                    } else {
                        kuiTotal = kuiTotal.add(diffStock);
                    }
                }

                // <0 盘亏金额 | >0 盘盈金额
                // 盘点前库存
                BigDecimal currentStock = inventoryStock.getCurrentStock();
                currentStock = (currentStock == null) ? BigDecimal.ZERO : currentStock;
                // 盘点前配件成本价
                BigDecimal inventoryPrePrice = inventoryStock.getInventoryPrePrice();
                inventoryPrePrice = (inventoryPrePrice == null) ? BigDecimal.ZERO : inventoryPrePrice;
                // 配件当前成本总额 (盘点前配件成本价 * 盘点前库存)
                BigDecimal currentInventoryAmount = currentStock.multiply(inventoryPrePrice);

                // 实盘库存为null时: 不纳入盘点记录 盈亏金额计算
                BigDecimal realStock = inventoryStock.getRealStock();
                if (realStock == null) {
                    continue;
                }
                // 盘点后配件成本价
                BigDecimal inventoryPrice = inventoryStock.getInventoryPrice();
                inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;

                // 盘点后配件真实成本总额(盘点后配件库存 * 盘点后配件成本价 )
                BigDecimal realInventoryAmount = realStock.multiply(inventoryPrice);
                // 配件盘点差价
                BigDecimal diffInventoryAmount = realInventoryAmount.subtract(currentInventoryAmount);
                if (diffInventoryAmount.compareTo(BigDecimal.ZERO) == 1) {
                    yinTotalAmount = yinTotalAmount.add(diffInventoryAmount);
                } else {
                    kuiTotalAmount = kuiTotalAmount.add(diffInventoryAmount);
                }
            }

            calculateInventoryAmountBo.setRecordId(recordId);
            calculateInventoryAmountBo.setKuiTotal(kuiTotal);
            calculateInventoryAmountBo.setKuiTotalAmount(kuiTotalAmount);
            calculateInventoryAmountBo.setYinTotal(yinTotal);
            calculateInventoryAmountBo.setYinTotalAmount(yinTotalAmount);

            calculateInventoryAmountBos.add(calculateInventoryAmountBo);
        }

        return Result.wrapSuccessfulResult(calculateInventoryAmountBos);
    }

    @Override
    public Result batchGetGoodsInventoryPrice(Long[] goodsIds, UserInfo currentLoginUser) {
        // 当前门店ID
        Long shopId = currentLoginUser.getShopId();
        // 获取当前门店信息
        Shop shop = shopService.selectById(shopId);

        // 从cube获取配件 前三月均销量and建议库存
        Map<Long, GoodsNumDTO> goodsNumDTOMap = getSalesAndStockFromCube(goodsIds, shopId);

        // 配件采购列表
        List<GoodsTqmallPriceBo> goodsTqmallPriceBos = new ArrayList<GoodsTqmallPriceBo>();
        List<Goods> goodses = goodsService.selectByIds(goodsIds);
        String tqmallPrice = "";
        GoodsTqmallPriceBo goodsTqmallPriceBo = null;
        for (Goods goods : goodses) {
            goodsTqmallPriceBo = new GoodsTqmallPriceBo();
            Long tqmallGoodsId = goods.getTqmallGoodsId();
            if (tqmallGoodsId != null && tqmallGoodsId > 0l) {
                Optional<LegendGoodsInfoDTO> goodsInfoDTOOptional = goodsFacade.getTqmallGoods(tqmallGoodsId, shop.getCity());
                if (goodsInfoDTOOptional.isPresent()) {
                    tqmallPrice = goodsInfoDTOOptional.get().getCityPrice();
                }
            }

            // 采购价
            Long goodsId = goods.getId();
            goodsTqmallPriceBo.setGoodsId(goodsId);
            goodsTqmallPriceBo.setTqmallGoodsId(goods.getTqmallGoodsId());
            goodsTqmallPriceBo.setTqmallPrice(tqmallPrice);

            // cube前三月均销量 和建议库存
            GoodsNumDTO goodsNumDTO = goodsNumDTOMap.get(goodsId);
            if (goodsNumDTO != null) {
                BigDecimal averageNumber = goodsNumDTO.getAverageNumber();
                averageNumber = (averageNumber == null) ? BigDecimal.ZERO : averageNumber;
                BigDecimal suggestGoodsNumber = goodsNumDTO.getSuggestGoodsNumber();
                suggestGoodsNumber = (suggestGoodsNumber == null) ? BigDecimal.ZERO : suggestGoodsNumber;
                goodsTqmallPriceBo.setAverageNumber(averageNumber);
                goodsTqmallPriceBo.setSuggestGoodsNumber(suggestGoodsNumber);
            }

            goodsTqmallPriceBos.add(goodsTqmallPriceBo);
        }

        return Result.wrapSuccessfulResult(goodsTqmallPriceBos);
    }


    @Override
    public void batchGetGoodsInventoryPrice(List<SearchGoodsVo> goodses, UserInfo currentLoginUser) {

        // 配件id集合
        Set<Long> goodsIdSet = new HashSet<Long>();
        for (SearchGoodsVo goods : goodses) {
            goodsIdSet.add(Long.parseLong(goods.getId()));
        }
        Long[] goodsIds = goodsIdSet.toArray(new Long[goodsIdSet.size()]);

        // 当前门店ID | 获取当前门店
        Long shopId = currentLoginUser.getShopId();
        Shop shop = shopService.selectById(currentLoginUser.getShopId());

        // 从cube获取配件 前三月均销量and建议库存
        Map<Long, GoodsNumDTO> goodsNumDTOMap = getSalesAndStockFromCube(goodsIds, shopId);

        // 云修采购价
        String tqmallPrice = "";
        for (SearchGoodsVo goods : goodses) {
            Long goodsId = Long.parseLong(goods.getId());
            Long tqmallGoodsId = Long.parseLong(goods.getTqmallGoodsId() == null ? "0" : goods.getTqmallGoodsId());
            if (tqmallGoodsId != null && tqmallGoodsId > 0l) {
                // TODO 云修支持批量接口
                Optional<LegendGoodsInfoDTO> goodsInfoDTOOptional = goodsFacade.getTqmallGoods(tqmallGoodsId, shop.getCity());
                if (goodsInfoDTOOptional.isPresent()) {
                    tqmallPrice = goodsInfoDTOOptional.get().getCityPrice();
                }
            }
            goods.setTqmallPrice(tqmallPrice);

            // cube前三月均销量 和建议库存
            GoodsNumDTO goodsNumDTO = goodsNumDTOMap.get(goodsId);
            if (goodsNumDTO != null) {
                BigDecimal averageNumber = goodsNumDTO.getAverageNumber();
                averageNumber = (averageNumber == null) ? BigDecimal.ZERO : averageNumber;
                BigDecimal suggestGoodsNumber = goodsNumDTO.getSuggestGoodsNumber();
                suggestGoodsNumber = (suggestGoodsNumber == null) ? BigDecimal.ZERO : suggestGoodsNumber;
                goods.setAverageNumber(averageNumber);
                goods.setSuggestGoodsNumber(suggestGoodsNumber);
            }
        }
    }

    @Override
    public List<StockStatusOfOrder> assertStockStatus(Long[] orderIds, Long shopId) {

        // 工单物料出库状态
        List<StockStatusOfOrder> stockStatusOfOrders = new ArrayList<StockStatusOfOrder>(orderIds.length);
        StockStatusOfOrder stockStatus = null;
        for (Long orderId : orderIds) {
            stockStatus = new StockStatusOfOrder();
            stockStatus.setOrderId(orderId);

            // 配件
            List<OrderGoods> orderGoodsList =
                    orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
            // 配件总数
            int totalGoodsNum = orderGoodsList.size();
            // 已出库配件
            int outboundTotal = 0;
            for (OrderGoods orderGoods : orderGoodsList) {
                // TODO optimize 性能太差 查询已出库数量
                BigDecimal outNumber = warehouseOutService.countOutGoods(orderGoods.getGoodsId(),
                        shopId, orderId, orderGoods.getId());
                orderGoods.setOutNumber(outNumber);
                BigDecimal goodsNum = orderGoods.getGoodsNumber();
                if (goodsNum != null && goodsNum.compareTo(outNumber) == 0) {
                    outboundTotal++;
                }
            }
            if (outboundTotal == 0 && totalGoodsNum == 0) {
                stockStatus.setGoodsOutFlag(0);
            } else if (outboundTotal == 0) {
                stockStatus.setGoodsOutFlag(1);
            } else if (outboundTotal == totalGoodsNum) {
                stockStatus.setGoodsOutFlag(3);
            } else if (outboundTotal < totalGoodsNum) {
                stockStatus.setGoodsOutFlag(2);
            }

            stockStatusOfOrders.add(stockStatus);
        }

        return stockStatusOfOrders;

    }


    /**
     * 从cube获取配件 前三月均销量and建议库存
     *
     * @param goodsIds 配件ID集合
     * @param shopId   当前门店
     * @return Map<Long, GoodsNumDTO>
     */
    private Map<Long, GoodsNumDTO> getSalesAndStockFromCube(Long[] goodsIds, Long shopId) {

        // 配件-前3月均销量and建议库存
        Map<Long, GoodsNumDTO> goodsNumDTOMap = new HashMap<Long, GoodsNumDTO>();

        com.tqmall.core.common.entity.Result<List<GoodsNumDTO>> result = null;
        try {
            result = rpcWarehouseAnalysisService.getGoodsNumList(shopId, Arrays.asList(goodsIds));
        } catch (Exception e) {
            logger.error("[dubbo]获取cube配件前三月均销量 异常, 异常信息:{}", e);
        }

        if(result == null){
            logger.error("[dubbo]获取cube配件前三月均销量失败, 返回result为空");
        }

        if (!result.isSuccess()) {
            logger.error("[dubbo]获取cube配件前三月均销量 失败, 失败信息:{}", result.getMessage());
        }

        List<GoodsNumDTO> goodsNumDTOs = result.getData();
        if (goodsNumDTOs != null) {
            for (GoodsNumDTO goodsNumDTO : goodsNumDTOs) {
                goodsNumDTOMap.put(goodsNumDTO.getGoodsId(), goodsNumDTO);
            }
        } else {
            goodsNumDTOMap = new HashMap<Long, GoodsNumDTO>();
        }

        return goodsNumDTOMap;
    }

    @Override
    @Transactional
    public Result turnIntoFormal(InventoryRecord inventoryRecord, UserInfo currentLoginUser) {
        // 盘点记录ID
        Long inventoryRecordId = inventoryRecord.getId();
        // 当前门店
        Long shopId = currentLoginUser.getShopId();
        // 1. 盘点单转正式
        this.updateStatus(inventoryRecord, InventoryStatusEnum.FORMAL, currentLoginUser);

        // 2. 同步更新Goods配件库存
        // 获取盘点单的配件盘点明细
        List<InventoryStock> inventoryStocks = this.getInventoryStocks(inventoryRecordId, shopId);
        syncGoodsStock(inventoryStocks, currentLoginUser);

        return Result.wrapSuccessfulResult("");
    }

    @Override
    @Transactional
    public void syncGoodsStock(List<InventoryStock> inventoryStocks, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();

        // batch query Goods
        Set<Long> inventoryGoodsIds = new HashSet<Long>();
        for (InventoryStock inventoryStock : inventoryStocks) {
            inventoryGoodsIds.add(inventoryStock.getGoodsId());
        }
        Long[] goodsIds = inventoryGoodsIds.toArray(new Long[inventoryGoodsIds.size()]);
        List<Goods> goodses = goodsService.selectByIds(goodsIds);
        Map<Long, Goods> goodsMap = new HashMap<Long, Goods>(goodses.size());
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        // 最后结存
        List<FinalInventory> finalInventories = new ArrayList<FinalInventory>();
        FinalInventory finalInventory = null;
        for (InventoryStock stock : inventoryStocks) {
            // 配件ID
            Long goodsId = stock.getGoodsId();
            Goods goods = goodsMap.get(goodsId);
            // 过滤操作配件时,配件被删除情况
            if (goods == null) {
                continue;
            }

            // 盘点真实库存
            BigDecimal realStock = stock.getRealStock();
            realStock = (realStock == null) ? BigDecimal.ZERO : realStock;
            BigDecimal inventoryPrice = stock.getInventoryPrice();
            inventoryPrice = (inventoryPrice == null) ? BigDecimal.ZERO : inventoryPrice;
            // 更新库存
            if (goods.getShopId().equals(shopId)) {
                // 结存对象
                finalInventory = new FinalInventory();
                finalInventory.setInventoryType("INVENTORY");
                finalInventory.setGoodsId(goods.getId());
                finalInventory.setShopId(shopId);
                finalInventory.setGoodsFinalPrice(inventoryPrice);
                finalInventory.setGoodsCount(realStock);
                finalInventory.setCreator(userId);
                finalInventory.setFinalDate(new Date());
                finalInventories.add(finalInventory);

                goods.setStock(realStock);
                goods.setInventoryPrice(inventoryPrice);
                goods.setModifier(userId);
                goods.setGmtModified(new Date());
                goodsDao.updateById(goods);
            }

            // 批量保存结存
            if (!CollectionUtils.isEmpty(finalInventories)) {
                finalInventoryService.batchInsert(finalInventories);
            }
        }
    }

    /**
     * 批量获取商品信息
     *
     * @param inventoryGoodsBos 被盘点商品集合
     * @return
     */
    private Map<Long, Goods> batchFetchGoods(List<InventoryGoodsBo> inventoryGoodsBos) {
        // batch query 配件的成本价
        Set<Long> inventoryGoodsIds = new HashSet<Long>();
        for (InventoryGoodsBo inventoryGoodsBo : inventoryGoodsBos) {
            inventoryGoodsIds.add(inventoryGoodsBo.getGoodsId());
        }
        Long[] goodsIds = inventoryGoodsIds.toArray(new Long[inventoryGoodsIds.size()]);
        List<Goods> goodses = goodsService.selectByIds(goodsIds);
        Map<Long, Goods> goodsMap = new HashMap<Long, Goods>(goodses.size());
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }
        return goodsMap;
    }




    @Transactional
    @Override
    public Result recordDel(Long id, UserInfo userInfo) {
        InventoryRecord inventoryRecord = inventoryRecordDao.selectById(id);
        if (inventoryRecord != null && inventoryRecord.getStatus() == 1 && inventoryRecord.getShopId().equals(userInfo.getShopId())) {
            inventoryRecord.setIsDeleted("Y");
            inventoryRecord.setModifier(userInfo.getUserId());
            int flag = inventoryRecordDao.updateById(inventoryRecord);

            Map map = new HashMap();
            map.put("shopId", userInfo.getShopId());
            map.put("recordId", id);
            List<InventoryStock> inventoryStockList = inventoryStockDao.select(map);
            for (InventoryStock inventoryStock : inventoryStockList) {
                inventoryStock.setIsDeleted("Y");
                inventoryStock.setModifier(userInfo.getUserId());
                inventoryStockDao.updateById(inventoryStock);
            }

            return Result.wrapSuccessfulResult(flag);
        } else {
            return Result.wrapErrorResult("", "操作失败");
        }
    }



    @Override
    public Page<InventoryRecord> getInventoryRecordPage(Pageable pageable, Map<String, Object> searchParams) {
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            searchParams.put("sorts", sorts);
        }
        Integer totalSize = inventoryRecordDao.selectCountInventoryPage(searchParams);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());

        List<InventoryRecord> data = inventoryRecordDao.selectInventoryPage(searchParams);
        return new DefaultPage<InventoryRecord>(data, pageRequest, totalSize);
    }

}
