package com.tqmall.legend.web.security;

/**
 * Created by litan on 14-11-5.
 */

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.IpUtil;
import com.tqmall.legend.biz.privilege.FuncService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.security.UserLoginFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SHOPID = "shopId";
    Logger logger = LoggerFactory.getLogger(MyUsernamePasswordAuthenticationFilter.class);
    /**
     * 用户登录表
     */
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private FuncService funcService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserLoginFacade userLoginFacade;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            sendRedirectErrorPage(request, response, "pe");
            return null;
        }
        Long shopId = obtainShopId(request);
        //验证用户账号与密码是否对应
        username = username.trim();
        // 查询用户登录信息
        password = MD5Util.MD5(password);
        List<ShopManagerLogin> shopManagerLoginList = userInfoService.getLoginInfo(username, password);
        //如果只有1条数据，则可登录
        int loginSize = shopManagerLoginList.size();
        ShopManagerLogin login = null;
        if(loginSize > 1){
            if(shopId == null){
                sendRedirectErrorPage(request, response, "pe");
                return null;
            }
            for(ShopManagerLogin shopManagerLogin : shopManagerLoginList){
                Long checkShopId = shopManagerLogin.getShopId();
                if(Long.compare(checkShopId,shopId) == 0){
                    login = shopManagerLogin;
                }
            }
            if (login == null) {
                sendRedirectErrorPage(request, response, "pe");
                return null;
            }
        } else if(loginSize == 0){
            sendRedirectErrorPage(request, response, "pe");
            return null;
        } else{
            login = shopManagerLoginList.get(0);
        }
        // 查询用户角色
        ShopManager userInfo = getShopManager(request, response, login);
        if (userInfo == null) {
            logger.error("用户登录时，获取用户角色信息为空！");
            return null;
        }
        // 获取门店信息
        Shop shop = getShopInfo(request, response, userInfo);
        if (shop == null) {
            logger.error("用户登录时，获取用户所属门店信息为空！");
            return null;
        }
        List<FuncF> list = getUserFunc(userInfo, shop.getLevel());
        try {
            userLoginFacade.loginForPC(list, userInfo, shop, response);
        } catch (BizException e) {
            logger.error("设置cookie异常", e);
            sendRedirectErrorPage(request, response, "se");
            return null;
        }
        String account = login.getAccount();
        StringBuffer logSb = new StringBuffer();
        logSb.append("用户登录开始，登录时间为：")
            .append(DateUtil.convertDateToYMDHMS(new Date()))
            .append(" 输入账户为：")
            .append(username).append(" 实际登录账户为：")
            .append(account).append(" 登录IP为：")
            .append(IpUtil.getClientIP(request));
        logger.info(logSb.toString());
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(account, password);

        // 允许子类设置详细属性
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private Shop getShopInfo(HttpServletRequest request, HttpServletResponse response, ShopManager userInfo) {
        Shop shop = shopService.selectById(userInfo.getShopId());
        if (shop == null) {
            sendRedirectErrorPage(request, response, "" +
                    "");
            return null;
        }
        return shop;
    }

    private ShopManager getShopManager(HttpServletRequest request, HttpServletResponse response, ShopManagerLogin login) {
        ShopManager userInfo = userInfoService.getUserInfo(login.getManagerId());
        if (userInfo == null) {
            sendRedirectErrorPage(request, response, "ue");
            return null;
        }
        return userInfo;
    }

    private List<FuncF> getUserFunc(ShopManager userInfo, Integer shopLevel) {
        List<FuncF> list = null;
        if (!StringUtils.isBlank(userInfo.getRoleId() + "")) {
            list = funcService.getFuncFsForUser(userInfo.getRoleId(), userInfo.getShopId(), shopLevel);
        }
        return list;
    }

    /**
     * @param request
     * @param response
     */
    private void sendRedirectErrorPage(HttpServletRequest request, HttpServletResponse response, String flag) {
        try {
            response.sendRedirect(request.getContextPath() + "/index?error=" + flag);
        } catch (IOException e) {
            logger.error("跳转错误页面异常", e);
        }
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        Object obj = request.getParameter(USERNAME);
        return null == obj ? "" : obj.toString();
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        Object obj = request.getParameter(PASSWORD);
        return null == obj ? "" : obj.toString();
    }

    protected Long obtainShopId(HttpServletRequest request) {
        Object obj = request.getParameter(SHOPID);
        String shopId = obj.toString();
        if(StringUtils.isBlank(shopId)){
            return null;
        }
        return Long.valueOf(shopId);
    }

}
