package com.tqmall.legend.biz.sys;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.pojo.ShopManagerCom;

import java.util.List;
import java.util.Map;

/**
 * @author litan
 *         <p/>
 *         2014年7月22日
 */
public interface UserInfoService {
    /**
     * 用户登录（包含门店和门店子账户）
     * 查询同一个账户的登录信息
     *
     * @param userName
     * @return
     */
    List<ShopManagerLogin> getLoginInfo(String userName);

    /**
     * 根据账户名、密码获取门店登录信息
     *
     * @param userName
     * @param password
     * @return
     */
    List<ShopManagerLogin> getLoginInfo(String userName, String password);

    /**
     * 根据账户account,获取login
     *
     * @param account
     * @return
     */
    ShopManagerLogin getLoginByAccount(String account);

    /**
     * 根据ID获取用户信息
     *
     * @param managerId
     * @return
     */
    public ShopManager getUserInfo(long managerId);

    /**
     * 更新用户信息
     *
     * @param shopManager
     * @return
     */
    public Result updateUserInfo(ShopManager shopManager);


    /**
     * 获取门店管理员的基本信息
     *
     * @param shopId
     * @return
     */
    public ShopManager getShopAdminUserInfo(Long shopId);


    /**
     * 根据ID获取用户信息
     *
     * @param resultMap
     * @return
     */
    public List<ShopManager> getUserInfo(Map<String, Object> resultMap);

    /**
     * 添加B门店账户
     *
     * @param com
     * @param resultMap
     * @return
     */
    public Result addShopUser(ShopManagerCom com, Map<String, Object> resultMap);

    /**
     * 根据ID获取用户信息
     *
     * @param mobile
     * @return
     */
    public ShopManagerLogin getUserInfoByMobile(String mobile);
}
