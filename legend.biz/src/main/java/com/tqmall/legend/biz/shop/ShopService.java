package com.tqmall.legend.biz.shop;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Shop;

import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 14/10/29.
 */
public interface ShopService {
    public List<Shop> select(Map<String, Object> searchMap);

    /**
     * 根据ID获取单个信息
     *
     * @param id
     *
     * @return
     */
    public Shop selectById(Long id);

    /**
     * 添加记录
     *
     * @param shop
     *
     * @return
     */
    public Boolean add(Shop shop);

    /**
     * 更新记录，不更新null的字段
     *
     * @param shop
     *
     * @return
     */
    public Result update(Shop shop);

    /**
     * 根据id更新门店
     *
     * @param shop
     *
     * @return
     */
    public Result updateById(Shop shop);

    /**
     * 获取所有店铺
     *
     * @return
     */
    Map<Long, Shop> getAllShopMap();

    /**
     * 更新切换城市站的ID
     *
     * @param shop
     *
     * @return
     */
    Result updateCity(Shop shop);

    /**
     * 查询门店的userGlobalId
     *
     * @param shopId
     *
     * @return
     */
    public Long getUserGlobalId(Long shopId) throws BizException;

    /**
     * 门店数量
     *
     * @return
     */
    Integer getCount(Map param);

    public Shop getShopByUserGlobalId(Long userGlobalId);

    /**
     * 批量获取门店信息
     *
     * @param ucShopIdList
     *
     * @return
     */

    List<Shop> selectShopByUserGlobalIdList(List<String> ucShopIdList);

    List<Shop> matchName(String shopName);

    List<Shop> getListByIds(List<Long> shopIds);

    /**
     * 根据客户电话号码 获取店铺列表
     *
     * @param mobile
     *
     * @return
     */
    List<Shop> getShopsByMobile(String mobile);
}
