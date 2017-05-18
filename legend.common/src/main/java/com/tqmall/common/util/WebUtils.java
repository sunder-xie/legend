package com.tqmall.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wanghui on 3/17/16.
 */
public final class WebUtils {

    public final static String getHostUrl(HttpServletRequest request) {
        String baseUrl;
        String headerHost = request.getHeader("Host");
        String contextPath = request.getContextPath();
        String url = request.getRequestURL().toString();
        if (StringUtils.isEmpty(headerHost)) {
            baseUrl = url.substring(url.indexOf(contextPath) + contextPath.length());
        } else {
            baseUrl = url.substring(0, url.indexOf(":")) + "://" + headerHost;
            String headerPort = request.getHeader("Port");
            if (StringUtils.isEmpty(headerPort) || "80".equals(headerPort)) {

            } else {
                baseUrl += ":" + headerPort;
            }
            baseUrl += contextPath;
        }
        return baseUrl;
    }
}
