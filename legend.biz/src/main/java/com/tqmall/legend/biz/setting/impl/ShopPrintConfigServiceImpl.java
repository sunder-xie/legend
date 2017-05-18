package com.tqmall.legend.biz.setting.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.setting.ShopPrintConfigService;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.dao.setting.ShopPrintConfigDao;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/11/3.
 */
@Slf4j
@Service
public class ShopPrintConfigServiceImpl extends BaseServiceImpl implements ShopPrintConfigService{
    @Autowired
    private ShopPrintConfigDao shopPrintConfigDao;

    //标示状态为开启
    private final static Integer OPENSTATUS = 1;
    //标准版
    private final static Integer STAND_TYPE = 2;
    //自定义版本
    private final  static Integer DEFINED_TYPE = 4;
    @Autowired
    private JedisClient jedisClient;

    /**
     * 获取门店的打印设置纪录
     *
     * @param shopId
     * @return
     */
    @Override
    public List<ShopPrintConfig> getShopPrintConfigs(Long shopId,Integer openStatus) {
        Map<String , Object> param = new HashMap<>();
        param.put("shopId",shopId);
        List<ShopPrintConfig> configList = shopPrintConfigDao.select(param);
        //获取默认配置列表
        List<ShopPrintConfig> templateList = getPrintConfigTemplates();
        Map<Integer , ShopPrintConfig> configMap = new HashMap<>();
        for (ShopPrintConfig shopPrintConfig : configList){
            configMap.put(shopPrintConfig.getPrintTemplate(),shopPrintConfig);
        }
        //如果缺省的采用默认数据
        List<ShopPrintConfig> descList = new ArrayList<>();
        for (ShopPrintConfig shopPrintConfig : templateList){
            Integer template = shopPrintConfig.getPrintTemplate();
            if (configMap.containsKey(template)){
                shopPrintConfig = configMap.get(template);
            }
            Integer status = shopPrintConfig.getOpenStatus();
            //获取开启的打印设置
            if (null != openStatus && !status.equals(openStatus)){
                continue;
            }
            descList.add(shopPrintConfig);
        }
        Collections.sort(descList, new Comparator<ShopPrintConfig>() {
            @Override
            public int compare(ShopPrintConfig o1, ShopPrintConfig o2) {
                if (o1.getPrintTemplate() > o2.getPrintTemplate()){
                    return 1;
                }
                return -1;
            }
        });
        return descList;
    }

    /**
     * 获取打印的设置模版
     *
     * @return
     */
    @Override
    public List<ShopPrintConfig> getPrintConfigTemplates() {
        Map<String , Object> param = new HashMap<>();
        param.put("shopId",0l);
        //标准版
        param.put("printType",STAND_TYPE);
        List<ShopPrintConfig> configList = shopPrintConfigDao.select(param);
        return configList;
    }

    /**
     * 根据shopID和模版 获取打印设置纪录
     *
     * @param shopId
     * @param printTemplate
     * @return
     */
    @Override
    public ShopPrintConfig getConfigByPrintTemplate(Long shopId, Integer printTemplate) {
        ShopPrintConfig shopPrintConfig = shopPrintConfigDao.getConfigByPrintTemplate(shopId , printTemplate , null);
        if (null == shopPrintConfig){
            return shopPrintConfigDao.getConfigByPrintTemplate(0l , printTemplate , STAND_TYPE);
        }
        return shopPrintConfig;
    }

    /**
     * 修改开启状态
     *
     * @param shopId
     * @param printTemplate
     * @return
     */
    @Override
    public void changeOpenStatus(Long shopId, Integer printTemplate) throws BizException {
        if (null == printTemplate){
            throw new BizException("打印单据模版参数未传递");
        }
        ShopPrintConfig shopPrintConfig = shopPrintConfigDao.getConfigByPrintTemplate(shopId,printTemplate,null);
        if (null == shopPrintConfig){
            //复制一条模版数据
            shopPrintConfig = shopPrintConfigDao.getConfigByPrintTemplate(0l,printTemplate,STAND_TYPE);
            shopPrintConfig.setShopId(shopId);
            shopPrintConfig.changeOpenStatus();
            shopPrintConfigDao.insert(shopPrintConfig);
            return;
        }
        shopPrintConfig.changeOpenStatus();
        shopPrintConfigDao.updateById(shopPrintConfig);
    }



    /**
     * 新增或修改设置内容
     *
     * @param shopPrintConfig
     */
    @Override
    public void addOrUpdate(ShopPrintConfig shopPrintConfig , Long shopId) {
        if (null == shopPrintConfig){
            throw new BizException("没有获取到需要保存的打印配置");
        }
        Integer printTemplate = shopPrintConfig.getPrintTemplate();
        if (null == printTemplate){
            throw new BizException("没有指定打印设置的模版");
        }
        log.info("门店打印设置保存，shopId：{}，设置选项：{}", shopId, LogUtils.objectToString(shopPrintConfig));
        if (DEFINED_TYPE != shopPrintConfig.getPrintType()){
            //获取模版数据
            ShopPrintConfig templateConfig = shopPrintConfigDao.getConfigByPrintTemplate(0l,shopPrintConfig.getPrintTemplate(),shopPrintConfig.getPrintType());
            shopPrintConfig.setConfigField(templateConfig.getConfigField());
        }
        ShopPrintConfig config = shopPrintConfigDao.getConfigByPrintTemplate(shopId,printTemplate,null);
        if (null == config){
            shopPrintConfig.setShopId(shopId);
            shopPrintConfigDao.insert(shopPrintConfig);
            return;
        }
        shopPrintConfig.setId(config.getId());
        shopPrintConfigDao.updateById(shopPrintConfig);
    }

    @Override
    public List<ShopPrintConfig> cacheReload(Long shopId, HttpServletRequest request) {
        List<ShopPrintConfig> printConfigList =  getShopPrintConfigs(shopId,OPENSTATUS);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(printConfigList)){
            String printConfigJson = new Gson().toJson(printConfigList);
            jedisClient.hset(""+shopId, Constants.SHOP_OPEN_PRINT,printConfigJson);
        }
        return printConfigList;
    }

    /**
     * 获取门店开启的单据
     *
     * @param request
     * @return
     */
    @Override
    public List<ShopPrintConfig> getShopOpenConfig(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopPrintConfig> configList;
        boolean flag = false;
        String printJson = (String) jedisClient.hget(""+shopId,Constants.SHOP_OPEN_PRINT);
        if (StringUtils.isBlank(printJson) || flag){
            //加载缓存
            configList = cacheReload(shopId,request);
        }else{
            configList = new Gson().fromJson(printJson,new TypeToken<List<ShopPrintConfig>>(){}.getType());
        }
        return configList;
    }

    /**
     * 获取版本
     *
     * @param shopId
     * @param printTemplate
     * @param printType
     * @return
     */
    @Override
    public ShopPrintConfig getConfigByPrintTemplate(Long shopId, Integer printTemplate, Integer printType) {
        if (null == printTemplate) {
            throw new BizException("打印单据模版参数为空");
        }
        ShopPrintConfig shopPrintConfig = shopPrintConfigDao.getConfigByPrintTemplate(shopId,printTemplate,null);
        if (DEFINED_TYPE == printType || null == printType){
            if (null != shopPrintConfig){
                return shopPrintConfig;
            }
            shopPrintConfig = shopPrintConfigDao.getConfigByPrintTemplate(0l,printTemplate,STAND_TYPE);
            return shopPrintConfig;
        }
        ShopPrintConfig templateConfig = shopPrintConfigDao.getConfigByPrintTemplate(0l,printTemplate,printType);
        if (null == shopPrintConfig){
            return templateConfig;
        }
        shopPrintConfig.setConfigField(templateConfig.getConfigField());
        return shopPrintConfig;
    }

    /**
     * 校验开启状态
     *
     * @param printTemplate
     * @param request
     * @return
     */
    @Override
    public ShopPrintConfig checkOpenStatus(Integer printTemplate, HttpServletRequest request) {
        List<ShopPrintConfig> shopPrintConfigs = getShopOpenConfig(request);
        if (CollectionUtils.isEmpty(shopPrintConfigs)){
            //没有可供打印的单据
            return null;
        }
        for(ShopPrintConfig shopPrintConfig : shopPrintConfigs){
            if (shopPrintConfig.getPrintTemplate().equals(printTemplate)){
                return shopPrintConfig;
            }
        }
        return null;
    }


    @Override
    public List<ShopPrintConfig> select(Map<String,Object> param) {
        return shopPrintConfigDao.select(param);
    }

    @Override
    public void batchInsert(List<ShopPrintConfig> printConfigList) {
        shopPrintConfigDao.batchInsert(printConfigList);
    }


}
