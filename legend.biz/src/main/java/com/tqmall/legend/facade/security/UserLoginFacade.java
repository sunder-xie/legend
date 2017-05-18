package com.tqmall.legend.facade.security;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.object.param.security.LoginParam;
import com.tqmall.legend.object.result.security.UserInfoDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zsy on 16/8/11.
 */
public interface UserLoginFacade {

    /**
     * 正常登陆接口
     *
     * @param loginParam loginParam
     * @return
     */
    public Result<UserInfoDTO> loginForApp(LoginParam loginParam);

    /**
     * 同意协议
     *
     * @param loginParam loginParam
     * @return
     */
    public Result<UserInfoDTO> agreeProtocolForApp(LoginParam loginParam);

    /**
     * sam免登陆门店
     *
     * @param userGlobalId
     * @param request
     * @param response
     * @return
     */
    String samAvoidLogin(String userGlobalId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 管理后台免登陆门店
     *
     * @param shopId
     * @param request
     * @param response
     * @return
     */
    String legendmAvoidLogin(Long shopId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据登录账户获取用户信息
     *
     * @param loginAccount
     * @return
     */
    public Result<UserInfoDTO> getUserInfoByLoginAccountForApp(String loginAccount,String password, Long chooseShopId,boolean checkPassword);

    /**
     * 二维码登录
     */
    void loginForPCQRCodeLogin(Long shopId, Long userId, String uuid);

    /**
     * PC登录
     * @param list
     * @param shopManager
     * @param shop
     * @param response
     */
    void loginForPC(List<FuncF> list,ShopManager shopManager,Shop shop, HttpServletResponse response);

    /**
     * 校验登录等级1
     * @param shopId
     * @param userId
     */
    void checkShopLevelOneLogin(Long shopId,Long userId);
}
