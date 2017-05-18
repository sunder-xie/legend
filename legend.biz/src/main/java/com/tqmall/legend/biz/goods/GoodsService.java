package com.tqmall.legend.biz.goods;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.bo.goods.GoodsBo;
import com.tqmall.legend.biz.bo.goods.GoodsDTO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsOnsaleEnum;
import com.tqmall.legend.entity.goods.GoodsQueryParam;
import com.tqmall.legend.object.enums.warehouse.ZeroStockRangeEnum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 配件service
 */
public interface GoodsService {


    /**
     * 根据ID获取商品
     *
     * @param id 配件ID
     * @return Optional<Goods>
     */
    Optional<Goods> selectById(Long id);


    /**
     * 根据ID获取商品
     *
     * @param id     配件ID
     * @param shopId 门店ID
     * @return Optional<Goods>
     */
    Optional<Goods> selectById(Long id, Long shopId);

    /**
     * 根据goodsIds查询
     *
     * @param goodsIds 配件ID集合
     * @return List<Goods>
     */
    List<Goods> selectByIds(Long[] goodsIds);

    /**
     * 获取配件
     *
     * @param goodsIdes 配件ID集合
     * @param shopId    门店ID
     * @return
     */
    List<Goods> selectByIdsAndShopId(List<Long> goodsIdes, Long shopId);

    /**
     * 获取门店配件
     *
     * @param shopId 门店id
     * @return
     */
    List<Goods> selectByShopId(Long shopId);


    /**
     * 获取油漆配件
     *
     * @param goodsIds 配件ID集合
     * @return
     */
    List<Goods> selectPaintByIds(List<Long> goodsIds);


    /**
     * 根据条件查询
     *
     * @return List<Goods>
     */
    List<Goods> queryGoods(GoodsQueryParam queryParam);


    /**
     * 根据淘汽商品ID集合获取配件
     *
     * @param tqmallGoodsIds 淘汽商品ID集合
     * @param shopId         门店ID
     * @return List<Goods>
     */
    List<Goods> selectByTqmallIds(Long[] tqmallGoodsIds, Long shopId);


    /**
     * 根据配件型号获取配件信息
     *
     * @param shopId 门店id
     * @param format 配件型号
     * @return
     */
    List<Goods> findGoodsByFormat(Long shopId, String... format);


    /**
     * 查询被删除的配件
     *
     * @param goodsIds 配件ID集合
     * @return List<Goods>
     */
    List<Goods> selectDeletedGoods(Long[] goodsIds);

    /**
     * 获取门店配件
     *
     * @param shopId             门店ID
     * @param onsaleEnum         上|下架状态
     * @param zeroStockRangeEnum 0|非0库存
     * @return List<Goods>
     */
    List<Goods> getGoods(Long shopId, GoodsOnsaleEnum onsaleEnum, ZeroStockRangeEnum zeroStockRangeEnum);


    /**
     * 添加商品，包括商品属性和适配车型
     *
     * @param goodsBo
     * @return
     */
    Result addWithAttrCar(GoodsBo goodsBo);

    /**
     * 更新商品，包括商品属性和适配车型
     *
     * @param goodsBo
     * @return
     */
    Result updateWithAttrCar(GoodsBo goodsBo);

    /**
     * 更新配件
     *
     * @param goods
     * @return
     */
    Integer updateById(Goods goods);


    /**
     * 删除配件
     *
     * @param goodsId 配件ID
     * @param shopId  门店ID
     * @return
     */
    Result deleteByIdAndShopId(Long goodsId, Long shopId);


    /**
     * 获取淘汽配件
     * <p/>
     * 淘汽配件唯一的标识:{tqmallGoodsSns + goodsName + goodsFormat}
     *
     * @param tqmallGoodsSns 淘汽配件编号
     * @param goodsName      配件名称
     * @param goodsFormat    配件型号
     * @param shopId         门店ID
     * @return
     */
    List<Goods> selectByTqmallGoods(String[] tqmallGoodsSns, String goodsName, String goodsFormat, Long shopId);

    /**
     * 物料基本信息 新增
     *
     * @param goods 物料基本信息
     * @return
     */
    GoodsDTO addBasicGoods(GoodsDTO goods, UserInfo userInfo);


    /**
     * 更新商品库存
     *
     * @param goodsId   商品ID
     * @param stock     新的库存
     * @param optUserId 当前操作用户ID
     * @return
     */
    int updateStock(Long goodsId, BigDecimal stock, Long optUserId);


    /**
     * TODO 写TDD
     * 合并重复配件
     *
     * @param srcGoodsIds 源配件集合
     * @param destGoodsId 目标配件
     * @param userInfo    当前操作人
     * @return
     */
    Result mergeGoods(Long[] srcGoodsIds, Long destGoodsId, UserInfo userInfo);

    /**
     * 统计配件库存预警总数量
     *
     * @param shopId 门店ID
     * @return
     */
    int statisticsWarningTotal(Long shopId);

    /**
     * 获取门店N个月未改变的物料种类
     *
     * @param shopId   门店id
     * @param monthNum 未改变的月数
     * @return
     */
    Integer getNMonthUnChangeGoodsNum(Long shopId, Integer monthNum);

    /**
     * 获取门店N个月未改变的物料成本
     *
     * @param shopId   门店id
     * @param monthNum 未改变的月数
     * @return
     */
    Goods getNMonthUnChangeGoodsCostAndPrice(Long shopId, Integer monthNum);

    /**
     * 获取门店N个月未改变的物料损失金额
     *
     * @param shopId   门店id
     * @param monthNum 未改变的月数
     * @return
     */
    BigDecimal getAvgGoodsStock(Long shopId, Integer monthNum);

    /**
     * 导入配件时，生成配件编号
     *
     * @param i
     * @param size
     * @return
     */
    String generatGoodsSn(int i, int size);


    /**
     * 批量增加配件
     *
     * @param goodses 配件集合
     */
    void batchSave(List<Goods> goodses);

    List<String> getGoodsLocation(Long shopId);

    List<Goods> findByTqmallGoodsIds(Long shopId, Collection<Long> tqmallGoodsIds);
}
