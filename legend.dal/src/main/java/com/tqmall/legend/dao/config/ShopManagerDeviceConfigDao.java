package com.tqmall.legend.dao.config;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.config.ShopManagerDeviceConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zsy on 16/9/6.
 */
@MyBatisRepository
public interface ShopManagerDeviceConfigDao extends BaseDao<ShopManagerDeviceConfig> {

    List<ShopManagerDeviceConfig> getShopManagerDevices(Long shopId);

    List<ShopManagerDeviceConfig> getDevicesByShopIdAndManagerId(@Param("shopId")Long shopId,@Param("managerId")Long managerId);

    int updateDeviceConfigStatus(@Param("id")Long id,@Param("userId")Long userId,@Param("status")int status);

}
