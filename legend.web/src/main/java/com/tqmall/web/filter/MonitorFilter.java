package com.tqmall.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.tqmall.common.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorFilter implements Filter {

    static Logger log = LoggerFactory.getLogger(MonitorFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        javax.servlet.http.HttpServletRequest request1 = (javax.servlet.http.HttpServletRequest) request;
        long start = System.currentTimeMillis();
        boolean hasExp = true;
        try {
            chain.doFilter(request, response);
            hasExp = false;
        } finally {
            log.info("IP:"+IpUtil.getClientIP(request1) +" "+ (System.currentTimeMillis() - start) + "ms " + (hasExp ? "has Exception " : "OK ")
                    + ((HttpServletRequest) request).getRequestURI());
        }
    }

    @Override
    public void destroy() {
    }
}
