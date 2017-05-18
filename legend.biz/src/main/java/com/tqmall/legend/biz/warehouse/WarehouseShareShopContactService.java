package com.tqmall.legend.biz.warehouse;

import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContact;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContactVO;

import java.util.List;

/**
 * Created by tanghao on 16/11/12.
 */
public interface WarehouseShareShopContactService {

    /**
     * 根据门店id获取门店联系人信息
     * @param shopId
     * @return
     */
    List<WarehouseShareShopContact> queryContactByShopId(Long shopId);

    /**
     * 根据门店id获取联系人信息
     * @param shopId
     * @return
     */
    List<WarehouseShareShopContactVO> queryContactWithShopInfoByShopId(Long shopId);

    /**
     * 根据门店id检测门店联系人
     * @param shopId
     * @return
     */
    List<WarehouseShareShopContact> checkContactWithShopInfoByShopId(Long shopId);

    /**
     * 检测是否有库存共享权限
     * @param userId
     * @return
     */
    boolean isContact(Long userId);

    /**
     * 插入联系人信息
     * @param warehouseShareShopContact
     * @return
     */
    Integer insert(WarehouseShareShopContact warehouseShareShopContact);

    /**
     * 删除配置信息
     * @param shopId
     */
    void deleteByShopId(Long shopId);
}
