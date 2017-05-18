package com.tqmall.legend.facade.shop.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopVersionConfigRelService;
import com.tqmall.legend.biz.shop.ShopVersionConfigService;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopVersionConfig;
import com.tqmall.legend.entity.shop.ShopVersionConfigRel;
import com.tqmall.legend.enums.shop.ShopVersionEnum;
import com.tqmall.legend.enums.shop.ShopVersionStatusEnum;
import com.tqmall.legend.facade.shop.GrayReleaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 灰度发布
 * Created by sven on 2017/1/10.
 */
@Service
@Slf4j
public class GrayReleaseFacadeImpl implements GrayReleaseFacade {
    @Resource
    private ShopVersionConfigService shopVersionConfigService;
    @Resource
    private ShopConfigureService shopConfigureService;
    @Resource
    private JedisClient jedisClient;
    @Resource
    private ShopVersionConfigRelService shopVersionConfigRelService;

    @Override
    public void initRelease(Long shopId) {
        List<ShopVersionConfig> versionConfigList = shopVersionConfigService.select();
        if (versionConfigList.size() == 0) {
            return;
        }
        //稳定版本处理
        stableToRedis(versionConfigList, shopId);
        //不稳定版本处理
        unstableToRedis(versionConfigList, shopId);
    }

    /**
     * 1.稳定状态的加入缓存
     * 2.剔除稳定版本的模块
     * 3.模块key加入缓存
     *
     * @param versionConfigList
     * @param shopId
     * @return
     */
    private void stableToRedis(List<ShopVersionConfig> versionConfigList, Long shopId) {
        //将已确定版本的放入缓存
        Iterator<ShopVersionConfig> it = versionConfigList.iterator();
        List<String> keyList = Lists.newArrayList();
        while (it.hasNext()) {
            ShopVersionConfig config = it.next();
            keyList.add(config.getModuleKey());
            if (ShopVersionStatusEnum.STABLE.getCode().equals(config.getStableStatus())) {
                hset(config, shopId, config.getDefaultVersion());
                it.remove();
                continue;
            }
        }
        //将所有模块信息放入缓存
        jedisClient.setHash(Constants.SESSION_MODULE_KEY_LIST, 0, shopId.toString(), keyList);
    }

    /**
     * 不稳定版本放入缓存
     *
     * @param versionConfigList
     * @param shopId
     */
    private void unstableToRedis(List<ShopVersionConfig> versionConfigList, Long shopId) {
        List<Long> ids = Lists.newArrayList();
        for (ShopVersionConfig versionConfig : versionConfigList) {
            ids.add(versionConfig.getId());
        }
        List<ShopVersionConfigRel> relList = shopVersionConfigRelService.selectByShopIdAndIds(shopId, ids);
        Multimap<Long, ShopVersionConfigRel> multimap = Multimaps.index(relList, new Function<ShopVersionConfigRel, Long>() {
            @Override
            public Long apply(ShopVersionConfigRel rel) {
                return rel.getConfigId();
            }
        });

        for (ShopVersionConfig versionConfig : versionConfigList) {
            //模块不存在配置取默认配置
            int size = multimap.get(versionConfig.getId()).size();
            if (size == 0) {
                hset(versionConfig, shopId, versionConfig.getDefaultVersion());
                continue;
            }
            // 同一模块存在多个配置,取修改时间最新的,id最大的
            Integer code = latest(multimap.get(versionConfig.getId()));
            Integer defaultCode = versionConfig.getDefaultVersion();
            versionConfig.setDefaultVersion(code);
            hset(versionConfig, shopId, defaultCode);
        }
    }

    @Override
    public void getRelease(HttpServletRequest request, Long shopId) {
        if (shopId == null) {
            throw new BizException("用户信息获取失败");
        }
        List<String> keyList = jedisClient.getHash(Constants.SESSION_MODULE_KEY_LIST, shopId.toString(), List.class);
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        for (String key : keyList) {
            String field = getField(key, shopId);
            //当前版本 old or new
            Object version = jedisClient.hget(Constants.SESSION_MODULE_KEY_VERSION, field);
            if (version == null) {
                version = ShopVersionEnum.OLD.getValue();
            }
            request.setAttribute(key + "_version", version);
            //是不是新老版本切换
            boolean choose = jedisClient.hget(Constants.SESSION_MODULE_KEY_CHOOSE, field) == null ? false : true;
            request.setAttribute(key + "_choose", choose);
        }
    }

    @Override
    public boolean switchVersion(String version, String confKey, UserInfo userInfo) {
        Assert.notNull(version, "版本不能为空");
        Assert.notNull(confKey, "模块主键不能为空");
        Long shopId = userInfo.getShopId();
        Integer confType = ShopConfigureTypeEnum.SHOPCHOOSEVERSION.getCode();
        ShopConfigure shopConfigure = new ShopConfigure();
        shopConfigure.setConfValue(version);
        shopConfigure.setShopId(shopId);
        shopConfigure.setConfType(Long.valueOf(confType));
        shopConfigure.setConfKey(confKey);
        shopConfigure.setCreator(userInfo.getUserId());
        shopConfigure.setModifier(userInfo.getUserId());
        boolean success = shopConfigureService.saveOrUpdateConfigure(shopConfigure);
        jedisClient.hset(Constants.SESSION_MODULE_KEY_VERSION, confKey + "_" + userInfo.getShopId(), version);
        return success;
    }

    /**
     * 设置版本号
     *
     * @param config      版本信息
     * @param shopId      门店id
     * @param defaultCode 默认版本号
     */
    private void hset(ShopVersionConfig config, Long shopId, Integer defaultCode) {
        Integer code = config.getDefaultVersion();
        //仅当code或defaultCode为1时version 为新版本
        String version = ShopVersionEnum.getVersion(code, defaultCode);
        String field = getField(config.getModuleKey(), shopId);
        //若为可切换,则查询配置表
        jedisClient.delHashField(Constants.SESSION_MODULE_KEY_CHOOSE, field);
        if (ShopVersionEnum.SWITCH.getCode().equals(code)) {
            Optional<ShopConfigure> optional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.SHOPCHOOSEVERSION, config.getModuleKey(), shopId);
            if (optional.isPresent()) {
                ShopConfigure shopConfigure = optional.get();
                version = shopConfigure.getConfValue();
            }
            //版本可选择
            jedisClient.hset(Constants.SESSION_MODULE_KEY_CHOOSE, field, "true");
        }
        //显示版本 old or new
        jedisClient.hset(Constants.SESSION_MODULE_KEY_VERSION, field, version);

    }

    /**
     * 获取field名称
     *
     * @param moduleKey
     * @param shopId
     * @return
     */
    private String getField(String moduleKey, Long shopId) {
        StringBuilder field = new StringBuilder(moduleKey + "_" + shopId);
        return field.toString();
    }

    /**
     * 获取最新一条数据的版本
     *
     * @param
     * @return
     */
    private Integer latest(Collection<ShopVersionConfigRel> multimap) {
        if (CollectionUtils.isEmpty(multimap)) {
            throw new BizException("获取最新版本配置参数错误");
        }
        List<ShopVersionConfigRel> relList = Lists.newArrayList();
        for (ShopVersionConfigRel rel : multimap) {
            relList.add(rel);
        }
        //只有一条数据直接返回
        if (relList.size() == 1) {
            return relList.get(0).getReleaseVersion();
        }
        //假设第一条数据最大
        ShopVersionConfigRel configRel = relList.get(0);
        for (int i = 1; i < relList.size(); i++) {
            ShopVersionConfigRel rel = relList.get(i);
            if (configRel.compareTo(rel) == -1) {
                configRel = rel;
            }
        }
        return configRel.getReleaseVersion();
    }
}
