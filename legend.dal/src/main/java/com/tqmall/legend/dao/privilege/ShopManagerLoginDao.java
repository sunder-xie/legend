package com.tqmall.legend.dao.privilege;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;

import java.util.Map;

/**
 * Created by QXD on 2014/10/29.
 */
@MyBatisRepository
public interface ShopManagerLoginDao extends BaseDao<ShopManagerLogin> {
    /**
     * 根据用户账号查找用户manager_login信息
     *
     * @param account 用户账号
     */
    @Deprecated
    public ShopManagerLogin selectByAccountAndShopId(String account, Long shopId);

    /**
     * 根据用户手机查找用户manager_login信息
     *
     * @param mobile 用户账号
     */
    public ShopManagerLogin selectByMobile(String mobile);

    /**
     * 根据用户帐号 删除用户信息
     *
     * @param shopManagerLogin 用户信息
     */
    public Integer updateByAccount(ShopManagerLogin shopManagerLogin);
}
