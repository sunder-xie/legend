package com.tqmall.legend.biz.config;

import com.tqmall.legend.entity.config.ShopManagerDeviceConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/9/6.
 */
public interface ShopManagerDeviceConfigService {

    /**
     * 获取指定门店的设备列表
     * @param shopId
     * @return
     */
    List<ShopManagerDeviceConfig> getShopManagerDevices(@NotNull Long shopId);

    /**
     * 根据门店id和员工id,获取设备信息
     * @param shopId
     * @param managerId
     * @return
     */
    List<ShopManagerDeviceConfig> getDevicesByShopIdAndManagerId(@NotNull Long shopId,@NotNull Long managerId);

    /**
     * 更新指定设备的授权状态
     * @param id
     * @param status
     * @return
     */
    boolean updateDeviceConfigStatus(@NotNull Long id,@NotNull Long userId,@NotNull int status);

    /**
     * 根据设备ID获取
     * @param managerId
     * @param deviceId
     * @return
     */
    ShopManagerDeviceConfig getShopManagerDevice(Long managerId , String deviceId );

    /**
     * 新增设备
     * @param shopManagerDeviceConfig
     * @return
     */
    boolean insertShopManagerDevice(ShopManagerDeviceConfig shopManagerDeviceConfig);

    Page<ShopManagerDeviceConfig> findDevicesByPage(Pageable pageable, Map<String, Object> params);

}
