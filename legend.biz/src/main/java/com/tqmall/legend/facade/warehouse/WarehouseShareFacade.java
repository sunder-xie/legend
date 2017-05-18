package com.tqmall.legend.facade.warehouse;

import com.tqmall.legend.entity.goods.GoodsQueryRequest;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.warehouseshare.WarehouseShare;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareCountVO;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContact;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContactVO;
import com.tqmall.legend.facade.warehouse.vo.Over2MonthTurnoverVO;
import com.tqmall.legend.facade.warehouse.vo.PublishGoodsVO;

import java.util.List;

/**
 * Created by tanghao on 16/11/10.
 */
public interface WarehouseShareFacade {

    /**
     * 获取N个月未改变的库存信息
     * @param shopId 门店id
     * @param month n个月的月数
     * @return
     */
    Over2MonthTurnoverVO getOver2MonthTurnoverInfo(Long shopId,Integer month);

    /**
     * 共享库存接口
     * @param warehouseShares 待发布对象List
     * @return
     */
    PublishGoodsVO publishGoods(List<WarehouseShare> warehouseShares,Long userId);

    /**
     * 重新发布库存接口
     * @param warehouseShare
     * @return
     */
    String rePublishGoods(WarehouseShare warehouseShare,Long userId);

    /**
     * 检测门店是否配置了库存共享交易的联系人
     * @param shopId
     * @return true :有联系人, false:没有联系人
     */
    WarehouseShareShopContact checkContact(Long shopId);


    /**
     * 查询门店联系人
     * @param shopId
     * @return
     */
    List<WarehouseShareShopContactVO> queryShopContact(Long shopId);

    /**
     * 改变出售库存的状态
     * @param id
     * @param goodsStatus
     * @return
     */
    boolean changeGoodsStatus(Long id,Integer goodsStatus);

    /**
     * 通过状态查询出售库存list
     * @param goodsQueryRequest
     * @return
     */
    SimplePage<WarehouseShare> querySaleListByGoodsStatus(GoodsQueryRequest goodsQueryRequest);

    /**
     * 改变门店联系人
     * @param shopId
     * @return
     */
    boolean changeShopContact(Long shopId,Long userId);

    /**
     * 获取各种状态的销售列表数量
     * @param shopId
     * @return
     */
    WarehouseShareCountVO querySaleCount(Long shopId);

    /**
     * 查询门店是否发布了商品
     * @param shopId
     * @return
     */
    boolean isPublish(Long shopId);

}
