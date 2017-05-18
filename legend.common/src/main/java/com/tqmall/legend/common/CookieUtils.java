package com.tqmall.legend.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by litan on 14-12-18.
 */
public class CookieUtils {

    @Getter
    @Setter
    private static String cookieDomain;

    /**
     * 设置cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if(!StringUtils.isBlank(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }
        if(maxAge == null) {
            cookie.setMaxAge(43200);
        }else{
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 设置cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if(!StringUtils.isBlank(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 根据名字获取cookie
     *
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 清除cookie
     *
     * @param request
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] diskCookies = request.getCookies();
        if (diskCookies != null) {
            for (int i = 0; i < diskCookies.length; ++i) {
                if (StringUtils.equals(diskCookies[i].getName(), name)) {
                    diskCookies[i].setMaxAge(0);
                    diskCookies[i].setPath("/");
                    if(!StringUtils.isBlank(cookieDomain)) {
                        diskCookies[i].setDomain(cookieDomain);
                    }
                    response.addCookie(diskCookies[i]);
                }
            }
        }
    }

    /**
     * 清除cookie
     *
     * @param request
     * @param response
     */
    public static void removeAllCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] diskCookies = request.getCookies();
        if (diskCookies != null) {
            for (int i = 0; i < diskCookies.length; ++i) {
                diskCookies[i].setMaxAge(0);
                diskCookies[i].setPath("/");
                response.addCookie(diskCookies[i]);
            }
        }
        if (diskCookies != null) {
            for (int i = 0; i < diskCookies.length; ++i) {
                diskCookies[i].setMaxAge(0);
                diskCookies[i].setPath("/");
                if(!StringUtils.isBlank(cookieDomain)) {
                    diskCookies[i].setDomain(cookieDomain);
                }
                response.addCookie(diskCookies[i]);
            }
        }
    }
}
