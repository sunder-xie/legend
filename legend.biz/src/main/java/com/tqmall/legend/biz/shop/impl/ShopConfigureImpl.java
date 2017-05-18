package com.tqmall.legend.biz.shop.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.biz.component.converter.DataBigDecimalConverter;
import com.tqmall.legend.biz.component.converter.DataIntegerConverter;
import com.tqmall.legend.biz.component.converter.DataLongConverter;
import com.tqmall.legend.biz.component.converter.DataStringConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.dao.shop.ShopConfigureDao;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO 2016-12-14 refactor 注释,代码实现逻辑等
 * <p>
 * 门店配置 实现类
 */
@Slf4j
@Service
public class ShopConfigureImpl extends BaseServiceImpl implements ShopConfigureService {

    @Autowired
    ShopConfigureDao shopConfigureDao;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public Integer add(ShopConfigure shopConfigure) {
        return shopConfigureDao.insert(shopConfigure);
    }

    @Override
    public Integer update(ShopConfigure shopConfigure) {
        return shopConfigureDao.updateById(shopConfigure);
    }

    @Override
    public List<ShopConfigure> select(Map<String, Object> searchMap) {
        return shopConfigureDao.select(searchMap);
    }

    @Override
    public Optional<ShopConfigure> getShopConfigure(ShopConfigureTypeEnum shopConfigureTypeEnum, long shopId) {
        Map<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("shopId", shopId);
        paramMap.put("confType", shopConfigureTypeEnum.getCode());
        try {
            List<ShopConfigure> shopConfigureList = shopConfigureDao.select(paramMap);
            if (CollectionUtils.isEmpty(shopConfigureList)) {
                return Optional.absent();
            }
            return Optional.fromNullable(shopConfigureList.get(0));
        } catch (Exception e) {
            log.error("获取门店配置信息异常，异常信息:", e);
            return Optional.absent();
        }
    }

    @Override
    public Optional<ShopConfigure> getShopConfigure(ShopConfigureTypeEnum shopConfigureTypeEnum, @NotNull String configureKey, long shopId) {
        Map<String, Object> paramMap = new HashMap<String, Object>(3);
        paramMap.put("shopId", shopId);
        paramMap.put("confType", shopConfigureTypeEnum.getCode());
        paramMap.put("confKey", configureKey);

        try {
            List<ShopConfigure> shopConfigureList = shopConfigureDao.select(paramMap);
            if (CollectionUtils.isEmpty(shopConfigureList)) {
                return Optional.absent();
            }
            return Optional.fromNullable(shopConfigureList.get(0));
        } catch (Exception e) {
            log.error("获取门店配置信息异常，异常信息:", e);
            return Optional.absent();
        }
    }


    @Override
    public String getShopConfigure(Long shopId, Integer confType, String confKey) {
        return getShopConfigure(shopId, confType, confKey, new DataStringConverter<String>());
    }

    @Override
    public <T> T getShopConfigure(Long shopId, Integer confType, String confKey, Class<T> cl) {
        Preconditions.checkNotNull(cl);
        if (cl.equals(String.class)) {
            return (T) getShopConfigure(shopId, confType, confKey, new DataStringConverter<String>());
        }
        if (cl.equals(Integer.class)) {
            return (T) getShopConfigure(shopId, confType, confKey, new DataIntegerConverter<Integer>());
        }
        if (cl.equals(Long.class)) {
            return (T) getShopConfigure(shopId, confType, confKey, new DataLongConverter<Long>());
        }
        if (cl.equals(BigDecimal.class)) {
            return (T) getShopConfigure(shopId, confType, confKey, new DataBigDecimalConverter<BigDecimal>());
        }
        return null;

    }

    @Override
    public <T> T getShopConfigure(Long shopId, Integer confType, DataConverter<T> dataConverter) {
        return (T) getShopConfigure(shopId, confType, null, dataConverter);
    }

    @Override
    public <T> T getShopConfigure(Long shopId, Integer confType, String confKey, DataConverter<T> dataConverter) {
        return getShopConfigure(shopId, confType, confKey, dataConverter, null);
    }

    private List getShopConfigureList(Map param) {
        //先获取该门店的配置信息和默认的配置信息比较
        Long shopId = (Long) param.get("shopId");
        List<ShopConfigure> shopConfigures = shopConfigureDao.select(param);
        param.put("shopId", 0);
        List<ShopConfigure> shopConfigureList = shopConfigureDao.select(param);
        for (ShopConfigure defaultShopConfigure : shopConfigureList) {
            defaultShopConfigure.setShopId(shopId);
            for (ShopConfigure configure : shopConfigures) {
                if (defaultShopConfigure.getConfType().equals(configure.getConfType()) && configure.getConfKey() != null && defaultShopConfigure.getConfKey().equals(configure.getConfKey())) {
                    BeanUtils.copyProperties(configure, defaultShopConfigure);
                    break;
                } else if (defaultShopConfigure.getConfType().equals(configure.getConfType()) && configure.getConfKey() == null
                        && aBoolean(defaultShopConfigure)) {
                    BeanUtils.copyProperties(configure, defaultShopConfigure);
                    break;
                }
            }
        }
        return shopConfigureList;
    }

    private boolean aBoolean(ShopConfigure shopConfigure) {
        return (shopConfigure.getConfType().intValue() == 0 || shopConfigure.getConfType().intValue() == 1 || shopConfigure.getConfType().intValue() == 12);
    }

    @Override
    public <T> boolean saveOrUpdateShopConfigure(Long shopId, Integer confType, DataConverter<T> dataConverter, T data) {
        Preconditions.checkNotNull(dataConverter);
        Preconditions.checkNotNull(data);
        Map param = Maps.newConcurrentMap();
        if (shopId != null) {
            param.put("shopId", shopId);
        } else {
            throw new BizException("shopId不能为空");
        }
        if (confType != null) {
            param.put("confType", confType);
        } else {
            throw new BizException("confType不能为空");
        }
        List<ShopConfigure> shopConfigures = shopConfigureDao.select(param);
        List<ShopConfigure> configures = (List<ShopConfigure>) dataConverter.encode(data);
        if (CollectionUtils.isEmpty(shopConfigures)) {
            addShopConfigure(shopId, param, configures);
        } else {
            updateShopConfigure(shopId, param, shopConfigures, configures);
        }
        return true;
    }

    @Override
    public <T> boolean saveOrUpdateShopConfigure(Long shopId, Integer confType, String confKey, DataConverter<T> dataConverter, T data) {
        Preconditions.checkNotNull(dataConverter);
        Preconditions.checkNotNull(data);
        Map param = Maps.newConcurrentMap();
        if (shopId != null) {
            param.put("shopId", shopId);
        } else {
            throw new RuntimeException("shopId不能为空");
        }
        if (confType != null) {
            param.put("confType", confType);
        } else {
            throw new RuntimeException("confType不能为空");
        }
        if (confKey != null) {
            param.put("confKey", confKey);
        }
        List<ShopConfigure> shopConfigures = shopConfigureDao.select(param);
        List<ShopConfigure> configures = (List<ShopConfigure>) dataConverter.encode(data);
        if (CollectionUtils.isEmpty(shopConfigures)) {
            addShopConfigure(shopId, param, configures);
        } else {
            updateShopConfigure(shopId, param, shopConfigures, configures);
        }
        return true;
    }

    @Override
    public <T> T getShopConfigure(Long shopId, Integer confType, String confKey, DataConverter<T> dataConverter, Class<T> cl) {
        List shopConfigures = getShopConfigList(shopId, confType, confKey, dataConverter);
        if (Langs.isEmpty(shopConfigures)) {
            return null;
        }
        if (cl == null) {
            return dataConverter.decode(shopConfigures);
        } else {
            return (T) JSONUtil.jsonStr2List((String) dataConverter.decode(shopConfigures), cl);
        }

    }

    private <T> List getShopConfigList(Long shopId, Integer confType, String confKey, DataConverter<T> dataConverter) {
        Preconditions.checkNotNull(dataConverter);
        Map param = Maps.newConcurrentMap();
        if (shopId != null) {
            param.put("shopId", shopId);
        } else {
            throw new RuntimeException("shopId不能为空");
        }
        if (confType != null) {
            param.put("confType", confType);
        } else {
            throw new RuntimeException("confType不能为空");
        }
        if (StringUtils.isNotBlank(confKey)) {
            param.put("confKey", confKey);
        }
        //
        List shopConfigures = null;
        try {
            shopConfigures = getShopConfigureList(param);
        } catch (Exception e) {
            log.error("根据shopId={}，confType={}，confKey={}，从redis中获取信息异常，异常信息：{}", shopId, confType, confKey, e);
            shopConfigures = getShopConfigureList(param);
        }

        return shopConfigures;
    }

    @Override
    public <T> T getConfigureByJson(Long shopId, Integer confType, String confKey, Class<T> cl) {
        return (T) getShopConfigure(shopId, confType, confKey, new DataStringConverter<T>(), cl);
    }

    /**
     * 获取打印版本 配置
     *
     * @param shopId 门店ID
     * @return
     */
    @Override
    public ShopConfigure getPrintVersion(Long shopId) {
        String versionJson = (String) jedisClient.hget("" + shopId, Constants.SHOP_PRINT_VERSION);
        if (StringUtils.isBlank(versionJson)) {
            Optional<ShopConfigure> configOptional = getShopConfigure(ShopConfigureTypeEnum.PRINT_VERSION, shopId);
            ShopConfigure config = null;
            if (configOptional.isPresent()) {
                config = configOptional.get();
            } else {
                configOptional = getShopConfigure(ShopConfigureTypeEnum.PRINT_VERSION, 0l);
                config = configOptional.get();
            }
            String configJson = new Gson().toJson(config);
            jedisClient.hset("" + shopId, Constants.SHOP_PRINT_VERSION, configJson);
            return config;
        }
        return new Gson().fromJson(versionJson, ShopConfigure.class);
    }

    @Override
    public boolean saveOrUpdateConfigure(ShopConfigure shopConfigure) {
        Assert.notNull(shopConfigure, "配置参数不能为空");
        Assert.notNull(shopConfigure.getShopId(), "门店id不能为空");
        Assert.notNull(shopConfigure.getConfType(), "配置类型不饿能够为空");
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("shopId", shopConfigure.getShopId());
        param.put("confType", shopConfigure.getConfType());
        if (StringUtils.isNotBlank(shopConfigure.getConfKey())) {
            param.put("confKey", shopConfigure.getConfKey());
        }
        List<ShopConfigure> configureList = shopConfigureDao.select(param);
        if(!CollectionUtils.isEmpty(configureList)){
            shopConfigure.setId(configureList.get(0).getId());
            shopConfigureDao.updateById(shopConfigure);
            return true;
        }
        shopConfigureDao.insert(shopConfigure);
        return true;
    }

    //更新门店配置
    private void updateShopConfigure(Long shopId, Map param, List<ShopConfigure> shopConfigures, List<ShopConfigure> configures) {
        param.put("shopId", 0);
        List<ShopConfigure> shopConfigureList = shopConfigureDao.select(param);
        if (!CollectionUtils.isEmpty(shopConfigureList) && !CollectionUtils.isEmpty(shopConfigures) && shopConfigureList.size() == shopConfigures.size()) {
            update(shopConfigures, configures);
        } else if (!CollectionUtils.isEmpty(shopConfigureList) && !CollectionUtils.isEmpty(shopConfigures) && shopConfigureList.size() != shopConfigures.size()) {
            //
            List<ShopConfigure> newShopConfigure = Lists.newArrayList();
            newShopConfigure.addAll(shopConfigureList);
            isNotExist(shopConfigures, shopConfigureList, newShopConfigure);
            //添加
            if (!CollectionUtils.isEmpty(newShopConfigure)) {
                batchInsert(shopId, configures, newShopConfigure);
            }
            //更新
            update(shopConfigures, configures);

        }

    }

    //获取不存在的配置
    private void isNotExist(List<ShopConfigure> shopConfigures, List<ShopConfigure> shopConfigureList, List<ShopConfigure> newShopConfigure) {
        for (ShopConfigure shopConfigure : shopConfigureList) {
            for (ShopConfigure configure : shopConfigures) {
                if (shopConfigure.getConfType().equals(configure.getConfType()) && shopConfigure.getConfKey().equals(configure.getConfKey())) {
                    newShopConfigure.remove(shopConfigure);
                }
            }
        }
    }

    private void update(List<ShopConfigure> shopConfigures, List<ShopConfigure> configures) {
        for (ShopConfigure shopConfigure : shopConfigures) {
            setConfValue(configures, shopConfigure);
            shopConfigureDao.updateById(shopConfigure);
        }
    }


    //添加门店配置
    private void addShopConfigure(Long shopId, Map param, List<ShopConfigure> configures) {
        param.put("shopId", 0);
        List<ShopConfigure> shopConfigures = shopConfigureDao.select(param);
        batchInsert(shopId, configures, shopConfigures);
    }

    private void batchInsert(Long shopId, List<ShopConfigure> configures, List<ShopConfigure> shopConfigures) {
        for (ShopConfigure shopConfigure : shopConfigures) {
            shopConfigure.setId(null);
            shopConfigure.setGmtCreate(null);
            shopConfigure.setGmtModified(null);
            shopConfigure.setShopId(shopId);
            setConfValue(configures, shopConfigure);
        }
        shopConfigureDao.batchInsert(shopConfigures);
    }

    //设置配置内容
    private void setConfValue(List<ShopConfigure> configures, ShopConfigure shopConfigure) {
        for (ShopConfigure configure : configures) {
            shopConfigure.setCreator(configure.getCreator());
            shopConfigure.setModifier(configure.getModifier());
            int confType = shopConfigure.getConfType() == null ? 0 : shopConfigure.getConfType().intValue();
            //.门店新老版本切换配置时confKey强制设置
            if (ShopConfigureTypeEnum.SHOPCHOOSEVERSION.getCode() == confType) {
                shopConfigure.setConfKey(configure.getConfKey());
            } else {
                if(shopConfigure.getConfKey()==null){
                    shopConfigure.setConfKey(configure.getConfKey());
                }
            }
            if (shopConfigure.getShopId() != null && shopConfigure.getConfType() != null && shopConfigure.getConfKey() != null &&
                    shopConfigure.getShopId().equals(configure.getShopId()) &&
                    shopConfigure.getConfType().equals(configure.getConfType()) &&
                    shopConfigure.getConfKey().equals(configure.getConfKey())) {
                shopConfigure.setConfValue(configure.getConfValue());
            } else if (shopConfigure.getShopId() != null && shopConfigure.getConfType() != null && shopConfigure.getConfKey() == null) {
                shopConfigure.setConfValue(configure.getConfValue());
            }
        }
    }
}
