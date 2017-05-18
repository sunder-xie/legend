package com.tqmall.legend.biz.warehouse.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.warehouse.WarehouseShareShopContactService;
import com.tqmall.legend.dao.warehouseshare.WarehouseShareShopContactDao;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContact;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContactVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghao on 16/11/12.
 */
@Service
@Slf4j
public class WarehouseShareShopContactServiceImpl implements WarehouseShareShopContactService {
    @Autowired
    private WarehouseShareShopContactDao warehouseShareShopContactDao;

    @Override
    public List<WarehouseShareShopContact> queryContactByShopId(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空");
        Map queryParam = Maps.newHashMap();
        queryParam.put("shopId",shopId);
        return warehouseShareShopContactDao.select(queryParam);
    }

    @Override
    public List<WarehouseShareShopContactVO> queryContactWithShopInfoByShopId(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空");
        return warehouseShareShopContactDao.queryContactWithShopInfoByShopId(shopId,null);
    }

    @Override
    public List<WarehouseShareShopContact> checkContactWithShopInfoByShopId(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空");
        return warehouseShareShopContactDao.checkContactWithShopInfoByShopId(shopId);
    }

    @Override
    public boolean isContact(Long userId) {
        Assert.notNull(userId,"员工id不能为空");
        Map param = new HashMap();
        param.put("contactId",userId);
        List<WarehouseShareShopContact> list = warehouseShareShopContactDao.select(param);
        if(CollectionUtils.isEmpty(list)){
            return false;
        }
        return true;
    }

    @Override
    public Integer insert(WarehouseShareShopContact warehouseShareShopContact) {
        Assert.notNull(warehouseShareShopContact,"联系人对象不能为空.");
        Assert.notNull(warehouseShareShopContact.getShopId(),"门店id不能为空.");
        Assert.notNull(warehouseShareShopContact.getContactId(),"联系人id不能为空.");
        Assert.notNull(warehouseShareShopContact.getContactName(),"联系人姓名不能为空.");
        Assert.notNull(warehouseShareShopContact.getContactMobile(),"联系人手机号不能为空.");
        return warehouseShareShopContactDao.insert(warehouseShareShopContact);
    }

    @Override
    public void deleteByShopId(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        warehouseShareShopContactDao.delete(param);
    }
}
