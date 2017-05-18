package com.tqmall.legend.web.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.tqmall.core.utils.UserUtils;

public class ServletUtils {

    public static final String DEFAULT_SEARCH_TAG = "search_";

    public static Map<String, Object> getParametersMapStartWith(HttpServletRequest request) {
        return getParametersMapStartWith(request, DEFAULT_SEARCH_TAG);
    }

    public static String getParametersStringStartWith(ServletRequest request) {
        return getParametersStringStartWith(request, DEFAULT_SEARCH_TAG);
    }

    public static Map<String, Object> getParametersMapStartWith(HttpServletRequest request,
        String start) {
        if (request == null)
            return null;

        Map<String, Object> searchParams = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getParameter(name);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (name.startsWith(start))
                searchParams.put(name.substring(start.length()), value.trim());
        }
        searchParams.put("shopId", UserUtils.getShopIdForSession(request));
        return searchParams;
    }

    public static String getParametersStringStartWith(ServletRequest request, String start) {
        if (request == null)
            return null;

        StringBuilder sb = new StringBuilder();

        @SuppressWarnings("unchecked")
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getParameter(name);
            if (StringUtils.isEmpty(value)) {
                continue;
            }

            if (name.startsWith(start)) {
                if (sb.length() > 0)
                    sb.append("&");

                sb.append(name).append("=").append(value);
            }
        }
        return sb.toString();
    }
}

