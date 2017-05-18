package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.wheel.lang.Langs;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zwb on 14/10/29.
 */
@Service
public class ShopServiceImpl extends BaseServiceImpl implements ShopService {
    Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    ShopDao shopDao;

    @Override
    public Shop selectById(Long id) {
        return shopDao.selectById(id);
    }

    @Override
    public List<Shop> select(Map<String, Object> searchMap) {
        // Object --> String
        if (searchMap.containsKey("userGlobalId") && searchMap.get("userGlobalId") != null) {
            Object userGlobalId = searchMap.get("userGlobalId");
            searchMap.put("userGlobalId", String.valueOf(userGlobalId));

        }
        // Collection<Object> --> List<String>
        if (searchMap.containsKey("userGlobalIds")) {
            Collection userGlobalIds = (Collection) searchMap.get("userGlobalIds");
            if (Langs.isNotEmpty(userGlobalIds)) {
                List<String> userGlobalIdStrList = new ArrayList<>(userGlobalIds.size());
                for (Object userGlobalId : userGlobalIds) {
                        userGlobalIdStrList.add(String.valueOf(userGlobalId));
                }
                searchMap.put("userGlobalIds", userGlobalIdStrList);
            }
        }
        return shopDao.select(searchMap);
    }

    @Override
    public Boolean add(Shop shop) {
        shop.setGmtCreate(new Date());
        shopDao.insert(shop);
        return true;
    }

    @Override
    public Result update(Shop shop) {
        if (StringUtils.isBlank(shop.getName()) || StringUtils.isBlank(shop.getContact()) || StringUtils.isBlank(shop.getProvinceName()) || StringUtils.isBlank(shop.getCityName()) || StringUtils.isBlank(shop.getDistrictName()) || StringUtils.isBlank(shop.getStreetName()) || StringUtils.isBlank(shop.getAddress())) {
            return Result.wrapErrorResult("", "店铺信息不完整 *号为必填项");
        }
        Shop mShop = shopDao.selectById(shop.getId());
        if (mShop != null) {
            if (mShop.getShopStatus() == 3L) {
                if (shop.getName() != null && !shop.getName().contains("测试")) {
                    return Result.wrapErrorResult("", "测试门店的门店名称必须包含测试字样.");
                }
                if (shop.getContact() != null && !shop.getContact().contains("测试")) {
                    return Result.wrapErrorResult("", "测试门店的联系人必须包含测试字样");
                }
            }
        } else {
            return Result.wrapErrorResult("", "更新门店不存在.");
        }
        shop.setGmtModified(new Date());
        try {
            shopDao.updateById(shop);
            return Result.wrapSuccessfulResult("更新成功");
        } catch (Exception e) {
            logger.error("DB操作错误");
            return Result.wrapErrorResult("", "DB操作错误");
        }
    }


    @Override
    @Transactional
    public Result updateById(Shop shop) {
        try {
            shopDao.updateById(shop);
            return Result.wrapSuccessfulResult(shop.getId());
        } catch (Exception e) {
            logger.error("初始化更新门店状态,DB操作错误,门店详细信息为:" + shop + "错误信息为:" + e.getMessage());
            return Result.wrapErrorResult("", "DB操作错误");
        }
    }

    @Override
    public Map<Long, Shop> getAllShopMap() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("isDeleted", "N");
        List<Shop> shopList = shopDao.select(paramMap);
        Map<Long, Shop> allShopMap = new HashMap<Long, Shop>();
        for (Shop shop : shopList) {
            allShopMap.put(shop.getId(), shop);
        }
        return allShopMap;
    }

    @Override
    public Result updateCity(Shop shop) {
        try {
            shopDao.updateById(shop);
            return Result.wrapSuccessfulResult(shop.getId());
        } catch (Exception e) {
            logger.error("切换城市站,DB操作错误,门店详细信息为:" + shop + "错误信息为:" + e.getMessage());
            return Result.wrapErrorResult("", "DB操作错误");
        }
    }

    @Override
    public Long getUserGlobalId(Long shopId) throws BizException {
        Assert.notNull(shopId, "shopId不能为空");
        Shop shop = shopDao.selectById(shopId);
        if (shop == null) {
            throw new BizException("shopId为" + shopId + "的门店不存在");
        }
        if (StringUtils.isBlank(shop.getUserGlobalId())) {
            throw new BizException("shopId为" + shopId + "的门店的userGlobalId不存在");
        }
        Long userGlobalId = Long.parseLong(shop.getUserGlobalId());
        return userGlobalId;
    }

    @Override
    public Integer getCount(Map param) {
        return shopDao.selectCount(param);
    }

    @Override
    public List<Shop> selectShopByUserGlobalIdList(List<String> ucShopIdList) {
        if (CollectionUtils.isEmpty(ucShopIdList)) {
            return null;
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("userGlobalIds", ucShopIdList);
        List<Shop> shopList = shopDao.select(param);
        return shopList;
    }

    @Override
    public Shop getShopByUserGlobalId(Long userGlobalId) {
        if (userGlobalId == null) {
            return null;
        }
        Map<String, Object> shopParam = Maps.newHashMap();
        shopParam.put("userGlobalId", userGlobalId.toString());
        List<Shop> shopList = this.select(shopParam);
        if (CollectionUtils.isEmpty(shopList)) {
            return null;
        }
        return shopList.get(0);
    }

    @Override
    public List<Shop> matchName(String shopName) {
        Map<String, Object> param = new HashMap<>();
        param.put("nameLike", shopName);
        return shopDao.select(param);
    }

    @Override
    public List<Shop> getListByIds(List<Long> shopIds) {
        if (CollectionUtils.isEmpty(shopIds)) {
            return Collections.emptyList();
        }
        Long[] shopIdArr = new Long[shopIds.size()];
        return shopDao.selectByIds(shopIds.toArray(shopIdArr));
    }

    @Override
    public List<Shop> getShopsByMobile(String mobile) {
        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);
        List<Shop> shopList = shopDao.select(param);
        return shopList;
    }
}
