package com.tqmall.legend.biz.privilege.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.privilege.ShopManagerLoginDao;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/31.
 */
@Slf4j
@Service
public class ShopManagerLoginServiceImpl implements ShopManagerLoginService {
    @Autowired
    private ShopManagerLoginDao shopManagerLoginDao;

    @Override
    public Result updateById(ShopManagerLogin shopManagerLogin) {
        try {
            shopManagerLoginDao.updateById(shopManagerLogin);
            log.info("更新门店登录信息{}",shopManagerLogin);
            return Result.wrapSuccessfulResult(true);
        } catch (Exception e) {
            log.error("更新用户登录数据{},DB操作失败{}", shopManagerLogin,e);
        }
        return Result.wrapErrorResult("", "DB操作失败");
    }

    @Override
    public List<ShopManagerLogin> select(Map<String, Object> param) {
        return shopManagerLoginDao.select(param);
    }

    @Override
    public Optional<ShopManagerLogin> get(@NotNull Long userId, @NotNull Long shopId) {
        Map<String, Object> param = new HashMap<String, Object>(3);
        param.put("managerId", userId);
        param.put("shopId", shopId);
        param.put("isDeleted", "N");
        List<ShopManagerLogin> shopManagerLogins = null;
        try {
            shopManagerLogins = shopManagerLoginDao.select(param);
        } catch (Exception e) {
            log.error("[DB]获取用户的登录信息异常,异常信息:{}", e);
            return Optional.absent();
        }
        if (shopManagerLogins == null || shopManagerLogins.size() == 0) {
            return Optional.absent();
        }

        return Optional.fromNullable(shopManagerLogins.get(0));
    }

    @Override
    public ShopManagerLogin selectShopManagerByManagerId(Long managerId) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("managerId",managerId);
        List<ShopManagerLogin> shopManagerLoginList = select(searchMap);
        if(CollectionUtils.isEmpty(shopManagerLoginList)){
            return null;
        }
        ShopManagerLogin shopManagerLogin = shopManagerLoginList.get(0);
        return shopManagerLogin;
    }

    @Override
    public List<ShopManagerLogin> findByManagerIds(Long shopId, List<Long> managerIds) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("managerIds",managerIds);
        return shopManagerLoginDao.select(param);
    }

    @Override
    public ShopManagerLogin findByShopIdAndManagerId(Long shopId, Long managerId) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("managerId",managerId);
        List<ShopManagerLogin> shopManagerLogins = shopManagerLoginDao.select(param);
        if(CollectionUtils.isEmpty(shopManagerLogins)){
            return new ShopManagerLogin();
        }
        return shopManagerLogins.get(0);
    }

    @Override
    public ShopManagerLogin findByShopIdAndAccount(Long shopId, String account) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("account",account);
        List<ShopManagerLogin> shopManagerLogins = shopManagerLoginDao.select(param);
        if (CollectionUtils.isEmpty(shopManagerLogins)){
            return new ShopManagerLogin();
        }
        return shopManagerLogins.get(0);
    }

    @Override
    public void updateByManagerId(ShopManagerLogin shopManagerLogin) {
        shopManagerLoginDao.updateByAccount(shopManagerLogin);
    }
}
