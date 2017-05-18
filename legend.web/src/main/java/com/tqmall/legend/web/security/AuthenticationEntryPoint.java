package com.tqmall.legend.web.security;

import com.tqmall.common.Constants;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.web.common.Result;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.RedirectUrlBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by litan on 14-11-4.
 */
public class AuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String redirectUrl = null;

        String url = buildHttpReturnUrlForRequest(request);
        logger.debug("登录退出前url为"+url);

        if (logger.isDebugEnabled()) {
            logger.debug("url:" + url);
        }

        String requestType = request.getHeader("X-Requested-With");
        // 非ajax请求
        if (requestType== null) {
            logger.debug("登录前URL非ajax 请求");
            if (this.isUseForward()) {

                if (this.isForceHttps() && "http".equals(request.getScheme())) {
                    // First redirect the current request to HTTPS.
                    // When that request is received, the forward to the login page will be used.
                    redirectUrl = buildHttpsRedirectUrlForRequest(httpRequest);
                }

                if (redirectUrl == null) {
                    String loginForm = determineUrlToUseForThisRequest(httpRequest, httpResponse, authException);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Server side forward to: " + loginForm);
                    }

                    RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginForm);

                    dispatcher.forward(request, response);

                    return;
                }
            } else {
                // redirect to login page. Use https if forceHttps true

                redirectUrl = buildRedirectUrlToLoginPage(httpRequest, httpResponse, authException);

            }
            CookieUtils.addCookie(response, Constants.SESSION_REFERER, url, 43200);
            redirectStrategy.sendRedirect(httpRequest, httpResponse, redirectUrl);
        } else {
            //如果是AJAX 请求，直接到登录页面
            logger.debug("登录前URL 为ajax 请求");
            String loginForm = determineUrlToUseForThisRequest(httpRequest, httpResponse, authException);

            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginForm);

            dispatcher.forward(request, response);

            // ajax请求，返回json，替代redirect到login page
            /**
             if (logger.isDebugEnabled()) {
             logger.debug("ajax request or post");
             }

             ObjectMapper objectMapper = new ObjectMapper();
             response.setHeader("Content-Type", "application/json;charset=UTF-8");
             JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(),
             JsonEncoding.UTF8);
             try {
             Result jsonData = new Result();
             jsonData.setSuccess(true);
             objectMapper.writeValue(jsonGenerator, jsonData);
             } catch (JsonProcessingException ex) {
             throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
             }
             */
        }
    }


    protected String buildHttpReturnUrlForRequest(HttpServletRequest request)
            throws IOException, ServletException {

        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
        urlBuilder.setScheme("http");
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(request.getServerPort());
        urlBuilder.setContextPath(request.getContextPath());
        urlBuilder.setServletPath(request.getServletPath());
        urlBuilder.setPathInfo(request.getPathInfo());
        urlBuilder.setQuery(request.getQueryString());
        return urlBuilder.getUrl();
    }

}
