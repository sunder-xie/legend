package com.tqmall.legend.biz.config.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.config.ShopManagerDeviceConfigService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.dao.config.ShopManagerDeviceConfigDao;
import com.tqmall.legend.entity.config.ShopManagerDeviceConfig;
import com.tqmall.legend.entity.privilege.ShopManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/9/6.
 */
@Service
public class ShopManagerDeviceConfigServiceImpl implements ShopManagerDeviceConfigService {
    @Autowired
    private ShopManagerDeviceConfigDao shopManagerDeviceConfigDao;
    @Autowired
    private ShopManagerService shopManagerService;

    @Override
    public List<ShopManagerDeviceConfig> getShopManagerDevices(@NotNull Long shopId) {
        List<ShopManagerDeviceConfig> shopManagerDeviceConfigs = shopManagerDeviceConfigDao.getShopManagerDevices(shopId);
        if(CollectionUtils.isEmpty(shopManagerDeviceConfigs)){
            return Lists.newArrayList();
        }
        List<Long> managerIds = Lists.transform(shopManagerDeviceConfigs, new Function<ShopManagerDeviceConfig, Long>() {
            @Override
            public Long apply(ShopManagerDeviceConfig input) {
                return input.getManagerId();
            }
        });
        if(!CollectionUtils.isEmpty(managerIds)){
            List<ShopManager> shopManagers = shopManagerService.selectByIds(managerIds.toArray(new Long[] { }));
            Map<Long,ShopManager> shopManagerMap = Maps.uniqueIndex(shopManagers, new Function<ShopManager, Long>() {
                @Override
                public Long apply(ShopManager input) {
                    return input.getId();
                }
            });
            for (ShopManagerDeviceConfig managerDeviceConfig : shopManagerDeviceConfigs) {
                if(!CollectionUtils.isEmpty(shopManagerMap) && shopManagerMap.containsKey(managerDeviceConfig.getManagerId()) &&
                        shopManagerMap.get(managerDeviceConfig.getManagerId()) != null){
                    managerDeviceConfig.setManagerName(shopManagerMap.get(managerDeviceConfig.getManagerId()).getName());
                    managerDeviceConfig.setManagerMobile(shopManagerMap.get(managerDeviceConfig.getManagerId()).getMobile());
                }
            }
        }
        return shopManagerDeviceConfigs;
    }

    @Override
    public List<ShopManagerDeviceConfig> getDevicesByShopIdAndManagerId(@NotNull Long shopId, @NotNull Long managerId) {
        return shopManagerDeviceConfigDao.getDevicesByShopIdAndManagerId(shopId,managerId);
    }

    @Override
    public boolean updateDeviceConfigStatus(@NotNull Long id, @NotNull Long userId,@NotNull int status) {
        return shopManagerDeviceConfigDao.updateDeviceConfigStatus(id, userId,status) > 0 ? true : false;
    }

    /**
     * 根据设备ID获取
     *
     * @param managerId
     * @param deviceId
     * @return
     */
    @Override
    public ShopManagerDeviceConfig getShopManagerDevice(Long managerId, String deviceId) {
        Map<String,Object> searchParam = new HashMap<>();
        searchParam.put("managerId",managerId);
        searchParam.put("deviceId",deviceId);
        List<ShopManagerDeviceConfig> list = shopManagerDeviceConfigDao.select(searchParam);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    /**
     * 新增设备
     *
     * @param shopManagerDeviceConfig
     * @return
     */
    @Override
    public boolean insertShopManagerDevice(ShopManagerDeviceConfig shopManagerDeviceConfig) {
        shopManagerDeviceConfigDao.insert(shopManagerDeviceConfig);
        return true;
    }

    @Override
    public Page<ShopManagerDeviceConfig> findDevicesByPage(Pageable pageable, Map<String, Object> params) {
        Integer count = shopManagerDeviceConfigDao.selectCount(params);
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            List<String> sorts = Lists.newArrayList();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            params.put("sorts", sorts);
        }

        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1
                : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1
                : pageable.getPageSize(), pageable.getSort());
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getPageSize());
        List<ShopManagerDeviceConfig> deviceConfigs = shopManagerDeviceConfigDao.select(params);
        List<Long> managerIds = Lists.transform(deviceConfigs, new Function<ShopManagerDeviceConfig, Long>() {
            @Override
            public Long apply(ShopManagerDeviceConfig input) {
                return input.getManagerId();
            }
        });
        if(!CollectionUtils.isEmpty(managerIds)){
            List<ShopManager> shopManagers = shopManagerService.selectByIds(managerIds.toArray(new Long[] { }));
            Map<Long,ShopManager> shopManagerMap = Maps.uniqueIndex(shopManagers, new Function<ShopManager, Long>() {
                @Override
                public Long apply(ShopManager input) {
                    return input.getId();
                }
            });
            for (ShopManagerDeviceConfig managerDeviceConfig : deviceConfigs) {
                if(!CollectionUtils.isEmpty(shopManagerMap) && shopManagerMap.containsKey(managerDeviceConfig.getManagerId()) &&
                        shopManagerMap.get(managerDeviceConfig.getManagerId()) != null){
                    managerDeviceConfig.setManagerName(shopManagerMap.get(managerDeviceConfig.getManagerId()).getName());
                    managerDeviceConfig.setManagerMobile(shopManagerMap.get(managerDeviceConfig.getManagerId()).getMobile());
                }
            }
        }

        DefaultPage<ShopManagerDeviceConfig> page = new DefaultPage<ShopManagerDeviceConfig>(deviceConfigs, pageRequest, count);
        return page;
    }
}
