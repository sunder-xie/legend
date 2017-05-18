package com.tqmall.legend.biz.shop;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.shop.ShopServiceCate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 14/10/29.
 */
public interface ShopServiceCateService {

    public List<ShopServiceCate> select(Map<String, Object> searchMap);

    /**
     * 2C-APP接口
     * 获得车主服务一级类别信息
     * create by jason 2015-07-16
     */
    public List<ServiceCateVo> selectFirstCate();

    public List<ShopServiceCate> selectNoCache(Map<String, Object> searchMap);

    /**
     * 如果没有重复记录就添加
     *
     * @param shopServiceCate
     * @return
     */
    public Result addWithoutRepeat(ShopServiceCate shopServiceCate);

    public ShopServiceCate selectById(Long id);

    public List<ShopServiceCate> selectByIds(List<Long> ids);

    /**
     * create by jason 2015-09-17
     * 处理一级,二级服务类别
     */
    public Map<Long, ShopServiceCate> dealCateInfo();

    /**
     * create by jason 2015-09-17
     * 组装车主一级服务类别Map
     */
    public Map<Long, ServiceCateVo> warpFirstCateInfo();

    /**
     * create by zsy 2015-09-21
     * 根据shopId,获取标准化服务类别及门店的标准服务
     *
     * @param shopId
     * @return
     */
    public List<ShopServiceCate> getNormalService(Long shopId);

    int insert(ShopServiceCate shopServiceCate);

    List<ShopServiceCate> list(Long shopId, Collection<Long> serviceCatIds);

    /**
     * 获取车主服务二级分类id
     * key = secondCatId
     * value = firstCatId
     * @return
     */
    Map<Long, Long> getSecondCate();

    /**
     * 根据门店id、服务类别名，获取服务类别信息
     *
     * @param shopId   门店id
     * @param catNames 服务类别名
     * @return
     */
    List<ShopServiceCate> findServiceCatesByCatNames(Long shopId,Integer cateType, String... catNames);

    void batchSave(List<ShopServiceCate> shopServiceCates);
}
