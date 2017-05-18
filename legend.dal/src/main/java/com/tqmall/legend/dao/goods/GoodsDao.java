package com.tqmall.legend.dao.goods;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.goods.Goods;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@MyBatisRepository
public interface GoodsDao extends BaseDao<Goods> {
    public Integer deleteByIdAndShopId(@Param("id") Long id, @Param("shopId") Long shopId);

    public List<Goods> selectByGoodsSnAndShopId(@Param("goodsSn") String goodsSn, @Param("tqmallGoodsSn") String tqmallGoodsSn, @Param("shopId") Long shopId);

    public List<Goods> selectByTqmallGoodsSnsAndShopId(@Param("tqmallGoodsSns") String[] tqmallGoodsSns, @Param("shopId") Long shopId, @Param("name") String name, @Param("format") String format);

    /**
     * 更新商品库存
     *
     * @param goodsId
     * @param stock
     * @param optUserId
     * @return
     */
    int updateStock(@Param("id") Long goodsId, @Param("stock") BigDecimal stock, @Param("optUserId") Long optUserId);

    /**
     * 根据goodsIds和shopId获取物料列表
     *
     * @param goodsIds
     * @param shopId
     * @return
     */
    List<Goods> selectByIdsAndShopId(@Param("goodsIds") List<Long> goodsIds, @Param("shopId") Long shopId);

    /**
     * 根据ids批量查询已经删除的配件
     *
     * @param ids
     * @return
     */
    List<Goods> batchQueryDeletedIds(Long[] ids);

    /**
     * 获取门店N个月未改变的物料种类
     *
     * @param shopId   门店id
     * @param monthNum 未改变的月数
     * @return
     */
    Integer getNMonthUnChangeGoodsNum(@Param("shopId") Long shopId, @Param("monthNum") Integer monthNum);

    /**
     * 获取门店N个月未改变的物料进货金额和销售金额
     *
     * @param shopId   门店id
     * @param monthNum 未改变的月数
     * @return
     */
    Goods getNMonthUnChangeGoodsCostAndPrice(@Param("shopId") Long shopId, @Param("monthNum") Integer monthNum);

    /**
     * 获取门店平均库存
     *
     * @param shopId
     * @return
     */
    BigDecimal getAvgGoodsStock(@Param("shopId") Long shopId, @Param("tableName") String tableName);

    /**
     * @param goodsIds
     * @return
     */
    public List<Goods> selectPaintByIds(List<Long> goodsIds);

    /**
     * 根据配件型号获取配件信息
     *
     * @param shopId 门店id
     * @param format 配件型号
     * @return
     */
    List<Goods> findGoodsByFormat(@Param("shopId") Long shopId, @Param("formats") String... format);

    /**
     * 批量下架配件
     * {库存:0,成本价:0,下架}
     *
     * @param goodsIds 配件IDS
     * @param shopId   门店ID
     */
    void batchDownShelf(@Param("goodsIds") List<Long> goodsIds, @Param("shopId") Long shopId);


    List<String> getGoodsLocation(@Param("shopId") Long shopId);

    List<Goods> selectByTqmallGoodsIds(@Param("shopId")Long shopId, @Param("tqmallGoodsIds")Iterable<Long> tqmallGoodsIds);
}
