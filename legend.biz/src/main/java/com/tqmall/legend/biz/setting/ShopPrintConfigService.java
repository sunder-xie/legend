package com.tqmall.legend.biz.setting;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.entity.setting.ShopPrintConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/11/3.
 */
public interface ShopPrintConfigService {

    /**
     * 获取门店的打印设置纪录
     * @param shopId 门店ID
     * @param openStatus 开启状态 传值为null - 忽略开启状态
     * @return
     */
    List<ShopPrintConfig> getShopPrintConfigs(Long shopId , Integer openStatus);

    /**
     * 获取打印的设置模版
     * @return
     */
    List<ShopPrintConfig> getPrintConfigTemplates();

    /**
     * 根据shopID和模版 获取打印设置纪录
     * 先从缓存里获取,缓存里没有再去数据库取
     * @param shopId
     * @param printTemplate
     * @return
     */
    ShopPrintConfig getConfigByPrintTemplate(Long shopId , Integer printTemplate);

    /**
     * 修改开启状态
     * @param shopId
     * @param printTemplate
     * @return
     */
    void changeOpenStatus(Long shopId , Integer printTemplate) throws BizException;


    /**
     * 新增或修改设置内容
     * @param shopPrintConfig
     */
    void addOrUpdate(ShopPrintConfig shopPrintConfig , Long shopId);

    /**
     * 刷新缓存
     * @param shopId
     * @param request
     * @return
     */
    List<ShopPrintConfig> cacheReload(Long shopId , HttpServletRequest request);

    /**
     * 获取门店开启的单据
     * @param request
     * @return
     */
    List<ShopPrintConfig> getShopOpenConfig(HttpServletRequest request);

    /**
     * 获取版本
     * @param shopId
     * @param printTemplate
     * @param printType
     * @return
     */
    ShopPrintConfig getConfigByPrintTemplate(Long shopId , Integer printTemplate , Integer printType) throws BizException;

    /**
     * 校验开启状态
     * @param printTemplate
     * @return
     */
    ShopPrintConfig checkOpenStatus(Integer printTemplate, HttpServletRequest request);

    /**
     * 根据ids查找
     * @return
     */
    List<ShopPrintConfig> select(Map<String,Object> param);

    void batchInsert(List<ShopPrintConfig> printConfigList);
}
