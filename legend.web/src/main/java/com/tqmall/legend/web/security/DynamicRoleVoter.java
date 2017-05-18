package com.tqmall.legend.web.security;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.enums.shop.ShopTagCodeEnum;
import com.tqmall.legend.facade.shop.GrayReleaseFacade;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;

/**
 * URL访问权限控制
 *
 * @author yi.yang
 *         <p/>
 *         2014年7月24日
 */
public class DynamicRoleVoter implements AccessDecisionVoter<FilterInvocation> {

    Logger logger = LoggerFactory.getLogger(DynamicRoleVoter.class);

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Autowired
    private GrayReleaseFacade grayReleaseFacade;

    @Override
    public int vote(Authentication authentication, FilterInvocation fi,
                    @SuppressWarnings("rawtypes") Collection attributes) {


        Cookie cookieName = CookieUtils.getCookieByName(fi.getRequest(), Constants.SESSION_USER_NAME);
        Cookie cookie = CookieUtils.getCookieByName(fi.getRequest(), Constants.SESSION_UUID);

        if (null == cookieName || null == cookie) {
            fi.getRequest().setAttribute("errormsg", "会话已过期，请重新登录");
            return ACCESS_DENIED;
        }
        pageSupports(fi.getRequest(), fi.getHttpResponse(), cookieName, cookie);
        return ACCESS_GRANTED;
    }

    private List<FuncF> pageSupports(HttpServletRequest request, HttpServletResponse response, Cookie cookieName, Cookie cookie) {
        // 从redis获取权限列表
        List<FuncF> list = null;
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        // 档口版域名控制，后续根据版本不同可以定义不同的域名
        StringBuilder newUrl = new StringBuilder();
        try {
            String key = cookie.getValue();
            if (null != jedis && !StringUtils.isBlank(key)) {
                list = new Gson().fromJson(jedis.hget(key, Constants.SESSION_USER_ROLE_FUNC), new TypeToken<List<FuncF>>() {
                }.getType());
                if (!CollectionUtils.isEmpty(list)) {
                    request.setAttribute(Constants.SESSION_USER_ROLE_FUNC, list);
                }
                request.setAttribute(Constants.SESSION_USER_IS_ADMIN, jedis.hget(key, Constants.SESSION_USER_IS_ADMIN));
            }
            Long shopId = Long.valueOf(jedis.hget(key, Constants.SESSION_SHOP_ID));
            //灰度发布
            grayReleaseFacade.getRelease(request, shopId);
            request.setAttribute(Constants.SESSION_ANXIN_INSURANCE_CITY_IS_OPEN, jedis.hget(key, Constants.SESSION_ANXIN_INSURANCE_CITY_IS_OPEN));
            request.setAttribute(Constants.SESSION_SHOP_ID, shopId);
            request.setAttribute(Constants.SESSION_SHOP_LEVEL, jedis.hget(key, Constants.SESSION_SHOP_LEVEL));
            request.setAttribute(Constants.SESSION_USER_ID, jedis.hget(key, Constants.SESSION_USER_ID));
            request.setAttribute(Constants.SESSION_USER_NAME, URLDecoder.decode(cookieName.getValue(), "UTF-8"));
            request.setAttribute(Constants.SESSION_SHOP_JOIN_STATUS, jedis.hget(key, Constants.SESSION_SHOP_JOIN_STATUS));
            request.setAttribute(Constants.SESSION_SHOP_WORKSHOP_STATUS, jedis.hget(key, Constants.SESSION_SHOP_WORKSHOP_STATUS));
            request.setAttribute(Constants.BPSHARE, jedis.hget(key, Constants.BPSHARE));
            request.setAttribute(Constants.SESSION_USER_GLOBAL_ID, jedis.hget(key, Constants.SESSION_USER_GLOBAL_ID));
            request.setAttribute(Constants.SESSION_WAREHOUSE_SHARE_ROLE, jedis.hget(key, Constants.SESSION_WAREHOUSE_SHARE_ROLE));

            // 手机号
            String mobile = jedis.hget(key, Constants.SESSION_MOBILE);
            if (StringUtils.isBlank(mobile)) {
                mobile = "";
            }
            request.setAttribute(Constants.SESSION_MOBILE, mobile);

            // 查询角色
            String pvgRoleName = jedis.hget(key, Constants.SESSION_PVGROLE_NAME);
            if (StringUtils.isBlank(pvgRoleName)) {
                pvgRoleName = "";
            }
            request.setAttribute(Constants.SESSION_PVGROLE_NAME, pvgRoleName);
            // 是否认证
            String readyCheck = jedis.hget(key, Constants.SESSION_READYCHECK);
            if (StringUtils.isEmpty(readyCheck)) {
                readyCheck = "false";
            }
            request.setAttribute(Constants.SESSION_READYCHECK, BooleanUtils.toBoolean(readyCheck));

            // 是否是档口版
            String isTqmallVersion = jedis.hget(key, Constants.SESSION_SHOP_IS_TQMALL_VERSION);
            if (StringUtils.isBlank(isTqmallVersion) || !isTqmallVersion.equals("true")) {
                //如果是空，则默认不是档口版
                isTqmallVersion = "false";
            } else {
                isTqmallVersion = "true";
            }
            request.setAttribute(Constants.SESSION_SHOP_IS_TQMALL_VERSION, isTqmallVersion);
            //设置门店选择的安心保险模式
            String anxinInsuranceModel = jedis.hget(key, Constants.SESSION_ANXIN_INSURANCE_MODEL);
            if (StringUtils.isBlank(anxinInsuranceModel)) {
                anxinInsuranceModel = "";
            }
            request.setAttribute(Constants.SESSION_ANXIN_INSURANCE_MODEL, anxinInsuranceModel);
            //设置门店标签信息，如是否是样板门店
            ShopTagCodeEnum[] shopTagCodeEnums = ShopTagCodeEnum.getShopTags();
            for (ShopTagCodeEnum shopTagCodeEnum : shopTagCodeEnums) {
                String field = shopTagCodeEnum.getTagCode();
                String hasTagName = jedis.hget(key, field);
                request.setAttribute(field, hasTagName);
            }
        } catch (Exception e) {
            logger.error("获取用户信息异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        if (StringUtils.isNotBlank(newUrl)) {
            try {
                response.sendRedirect(newUrl.toString());
            } catch (Exception e) {
                logger.error("【门店版本重定向新域名】出现异常", e);
            }
        }
        return list;
    }

}
