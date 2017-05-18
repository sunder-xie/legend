package com.tqmall.legend.biz.inventory;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.inventory.InventoryRecord;
import com.tqmall.legend.entity.inventory.InventoryStock;
import com.tqmall.legend.entity.warehouse.InventoryFormBo;
import com.tqmall.legend.entity.warehouse.InventoryStatusEnum;
import com.tqmall.legend.entity.warehouse.StockStatusOfOrder;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 14-12-8.
 */
public interface InventoryService {

    public Result recordDel(Long id, UserInfo userInfo);

    /**
     * 生成盘点单
     *
     * @param inventoryFormBo  盘点单表单实体
     * @param currentLoginUser 当前操作人
     * @return
     */
    Result generate(InventoryFormBo inventoryFormBo, UserInfo currentLoginUser);

    /**
     * 获取被盘点配件集合
     *
     * @param inventoryRecordId 盘点记录ID
     * @param shopId            当前门店ID
     * @return List<InventoryStock>
     */
    List<InventoryStock> getInventoryStocks(Long inventoryRecordId, Long shopId);

    /**
     * 获取盘点记录
     *
     * @param itemId 主键ID
     * @param shopId 门店ID
     * @return
     */
    Optional<InventoryRecord> get(Long itemId, Long shopId);

    /**
     * 更新盘点单状态
     *
     * @param inventoryRecord  盘点记录对象
     * @param currentLoginUser 当前操作人
     * @return
     */
    Result updateStatus(InventoryRecord inventoryRecord, InventoryStatusEnum statusEnum, UserInfo currentLoginUser);


    /**
     * 更新盘点记录AND配件盘点明细
     *
     * @param inventoryFormBo  盘点记录表单实体
     * @param currentLoginUser 当前操作人
     * @return Result
     */
    Result updateRecordAndStock(InventoryFormBo inventoryFormBo, UserInfo currentLoginUser);

    /**
     * 计算盈亏金额
     *
     * @param inventoryIds 被盘点记录集合
     * @return
     */
    Result calculateInventoryAmount(Long[] inventoryIds, UserInfo userInfo);

    /**
     * batch get goods inventory price
     *
     * @param goodsIds          配件IDS
     * @param currentLoginUser 当前操作人
     * @return
     */
    Result batchGetGoodsInventoryPrice(Long[] goodsIds, UserInfo currentLoginUser);

    /**
     * 盘点单 由'草稿'转'正式'
     *
     * @param inventoryRecord  盘点记录
     * @param currentLoginUser 当前登录人
     * @return
     */
    Result turnIntoFormal(InventoryRecord inventoryRecord, UserInfo currentLoginUser);


    /**
     * 同步更新Goods配件库存
     *
     * @param inventoryStocks 被盘点配件明细
     * @param userInfo        当前登录人
     */
    void syncGoodsStock(List<InventoryStock> inventoryStocks, UserInfo userInfo);

    /**
     * 获取配件最新采购价
     *
     * @param goodses          配件集合
     * @param currentLoginUser
     */
    void batchGetGoodsInventoryPrice(List<SearchGoodsVo> goodses, UserInfo currentLoginUser);

    /**
     * 判断工单物料出库状态
     * 0:工单无物料
     * 1:工单物料还未出库
     * 2:工单物料部分出库
     * 3:工单物料已全部出库
     *
     * @param orderIds 工单IDS
     * @param shopId   门店ID
     * @return
     */
    List<StockStatusOfOrder> assertStockStatus(Long[] orderIds, Long shopId);

    public Page<InventoryRecord> getInventoryRecordPage(Pageable pageable, Map<String, Object> searchParams);
}
