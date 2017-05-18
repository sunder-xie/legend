package com.tqmall.legend.biz.shop;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * 门店配置 service
 */
public interface ShopConfigureService {

    public Integer add(ShopConfigure shopConfigure);

    public Integer update(ShopConfigure shopConfigure);

    // TODO 2017-01-03 不建议map入参
    public List<ShopConfigure> select(Map<String, Object> searchMap);


    /**
     * 获取门店配置项
     *
     * @param shopConfigureTypeEnum 配置类型
     * @param shopId                门店ID
     * @return Optional<ShopConfigure>
     * @See ShopConfigureTypeEnum
     */
    Optional<ShopConfigure> getShopConfigure(ShopConfigureTypeEnum shopConfigureTypeEnum, long shopId);


    /**
     * 获取门店配置项
     *
     * @param shopConfigureTypeEnum 配置类型
     * @param configureKey          对应KEY值
     * @param shopId                门店ID
     * @return Optional<ShopConfigure>
     */
    Optional<ShopConfigure> getShopConfigure(ShopConfigureTypeEnum shopConfigureTypeEnum, String configureKey, long shopId);

    /**
     * 获取门店配置内容
     *
     * @param shopId   门店id
     * @param confType 配置类型
     * @param confKey  配置键值
     * @return 配置内容
     */
    public String getShopConfigure(Long shopId, Integer confType, String confKey);

    /**
     * 获取门店配置内容，可按自定义基本类型
     *
     * @param shopId   门店id
     * @param confType 配置类型
     * @param confKey  配置键值
     * @param cl       基本类型
     * @param <T>      自定义类型
     * @return
     */
    public <T> T getShopConfigure(Long shopId, Integer confType, String confKey, Class<T> cl);


    /**
     * 获取门店配置内容，实现DataConverter，获取自定义类型的内容
     *
     * @param shopId        门店id
     * @param confType      配置类型
     * @param dataConverter 自定义类型转换器
     * @param <T>           自定义类型
     * @return
     */
    public <T> T getShopConfigure(Long shopId, Integer confType, DataConverter<T> dataConverter);

    /**
     * 获取门店配置内容，实现DataConverter，获取自定义类型的内容
     *
     * @param shopId        门店id
     * @param confType      配置类型
     * @param confKey       配置键值
     * @param dataConverter 自定义类型转换器
     * @param <T>           自定义类型
     * @return
     */
    public <T> T getShopConfigure(Long shopId, Integer confType, String confKey, DataConverter<T> dataConverter);

    /**
     * 获取门店配置内容，实现DataConverter，获取自定义类型的内容
     *
     * @param shopId        门店id
     * @param confType      配置类型
     * @param confKey       配置键值
     * @param dataConverter 自定义类型转换器
     * @param cl            自定义对象类型
     * @param <T>           自定义类型
     * @return
     */
    public <T> T getShopConfigure(Long shopId, Integer confType, String confKey, DataConverter<T> dataConverter, Class<T> cl);


    /**
     * 持久化门店配置信息，实现DataConverter，获取自定义类型的内容
     *
     * @param shopId        门店id
     * @param confType      配置类型
     * @param dataConverter 自定义类型转换器
     * @param data          需要持久化的内容
     * @param <T>           自定义类型
     * @return
     */
    public <T> boolean saveOrUpdateShopConfigure(Long shopId, Integer confType, DataConverter<T> dataConverter, T data);


    /**
     * 持久化门店配置信息，实现DataConverter，获取自定义类型的内容
     *
     * @param shopId        门店id
     * @param confType      配置类型
     * @param confKey       配置KEY
     * @param dataConverter 自定义类型转换器
     * @param data          需要持久化的内容
     * @param <T>           自定义类型
     * @return
     */
    public <T> boolean saveOrUpdateShopConfigure(Long shopId, Integer confType, String confKey, DataConverter<T> dataConverter, T data);

    /**
     * 获取json格式的配置内容
     *
     * @param shopId   门店id
     * @param confType 配置类型
     * @param confKey  配置键值
     * @param <T>      自定义类型
     * @return
     */
    <T> T getConfigureByJson(Long shopId, Integer confType, String confKey, Class<T> cl);

    /**
     * 获取打印版本 配置
     *
     * @param shopId 门店ID
     * @return
     */
    public ShopConfigure getPrintVersion(Long shopId) ;

    /**
     * 保存或更新
     * @param shopConfigure
     * @return
     */
    boolean saveOrUpdateConfigure(ShopConfigure shopConfigure);
}
