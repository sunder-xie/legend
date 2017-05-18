package com.tqmall.core.utils;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by litan on 14-11-4.
 */
public class UserUtils {


    private static Logger logger = LoggerFactory.getLogger(UserUtils.class);

    /**
     * 从redis中获取用户信息
     *
     * @param request
     * @return
     */
    public static UserInfo getUserInfo(HttpServletRequest request) {
        UserInfo userInfo = new UserInfo();
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
            if (cookie == null) {
                return userInfo;
            }
            String key = cookie.getValue();
            if (null != jedis) {
                /**
                 * 修改为管道方式,一次性获取数据
                 */
                Pipeline pipeline = jedis.pipelined();
                pipeline.hget(key, Constants.SESSION_USER_ID);
                pipeline.hget(key, Constants.SESSION_SHOP_ID);
                pipeline.hget(key, Constants.SESSION_USER_IS_ADMIN);
                pipeline.hget(key, Constants.SESSION_USER_NAME);
                pipeline.hget(key, Constants.SESSION_USER_ROLE_ID);
                pipeline.hget(key, Constants.SESSION_SHOP_LEVEL);
                pipeline.hget(key, Constants.SESSION_USER_ROLE_FUNC);
                pipeline.hget(key, Constants.SESSION_USER_GLOBAL_ID);
                List<Object> returnObjects = pipeline.syncAndReturnAll();

                Object object = null;
                object = returnObjects.get(0);
                userInfo.setUserId(object == null ? 0 : Long.valueOf(object.toString()));

                object = returnObjects.get(1);
                userInfo.setShopId(object == null ? 0 : Long.valueOf(object.toString()));

                object = returnObjects.get(2);
                userInfo.setUserIsAdmin(object == null ? 0 : Integer.parseInt(object.toString()));

                object = returnObjects.get(3);
                userInfo.setName(object == null ? "" : object.toString());

                object = returnObjects.get(4);
                userInfo.setRolesId(object == null ? 0 : Long.valueOf(object.toString()));

                object = returnObjects.get(5);
                userInfo.setLevel(object == null ? 0 : Integer.valueOf(object.toString()));

                object = returnObjects.get(6);
                userInfo.setUserRoleFunc(object == null ? "" : object.toString());

                object = returnObjects.get(7);
                userInfo.setUserGlobalId(object == null ? "" : object.toString());

            }
        } catch (Exception e) {
            logger.error("获取用户信息异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return userInfo;
    }

    /**
     * 从session中获取门店ID
     *
     * @param request
     * @return
     */
    public static Long getShopIdForSession(HttpServletRequest request) {
        Long shopId = 0l;
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            if (cookie == null) {
                return shopId;
            }
            String value = jedis.hget(cookie.getValue(), Constants.SESSION_SHOP_ID);
            if (null != jedis && null != value) {
                shopId = Long.valueOf(value);
            }
        } catch (Exception e) {
            logger.error("获取门店ID异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return shopId;
    }

    /**
     * 从redis中获取UserId
     *
     * @param request
     * @return
     */
    public static Long getUserIdForSession(HttpServletRequest request) {
        Long userId = 0l;
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            if (cookie == null) {
                return userId;
            }
            String value = jedis.hget(cookie.getValue(), Constants.SESSION_USER_ID);
            if (null != jedis && null != value) {
                userId = Long.valueOf(value);
            }
        } catch (Exception e) {
            logger.error("获取用户ID异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return userId;
    }

    /**
     * 从redis中获取ShopLevel
     *
     * @param request
     * @return
     */
    public static Integer getShopLevelForSession(HttpServletRequest request) {
        Integer levelId = 0;
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            if (cookie == null) {
                return levelId;
            }
            String value = jedis.hget(cookie.getValue(), Constants.SESSION_SHOP_LEVEL);
            if (null != jedis && null != value) {
                levelId = Integer.valueOf(value);
            }
        } catch (Exception e) {
            logger.error("获取门店等级异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return levelId;
    }

    /**
     * 从redis中获取userGlobalId
     *
     * @param request
     * @return
     */
    public static String getUserGlobalIdForSession(HttpServletRequest request) {
        String userGlobalId = "";
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            if (cookie == null) {
                return userGlobalId;
            }
            userGlobalId = jedis.hget(cookie.getValue(), Constants.SESSION_USER_GLOBAL_ID);
            if("0".equals(userGlobalId)){
                userGlobalId = "";
                return userGlobalId;
            }
            if (StringUtils.isNotBlank(userGlobalId)) {
                return userGlobalId;
            }
        } catch (Exception e) {
            logger.error("获取门店userGlobalId异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return userGlobalId;
    }

    /**
     * 从redis中获取userGlobalId
     *
     * @param request
     * @return
     */
    public static boolean isYBD(HttpServletRequest request) {
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            if (cookie == null) {
                return false;
            }
            String isYBD = jedis.hget(cookie.getValue(), Constants.YBD);
            if (StringUtils.isBlank(isYBD) || isYBD.equals("false")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("获取门店标签选项异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return false;
    }
}
