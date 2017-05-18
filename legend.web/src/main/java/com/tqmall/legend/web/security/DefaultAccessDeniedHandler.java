package com.tqmall.legend.web.security;

import com.google.gson.Gson;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.web.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 403权限验证失败处理
 * Created by yi.yang on 2014/8/4.
 */
public class DefaultAccessDeniedHandler extends AccessDeniedHandlerImpl {

    Logger logger = LoggerFactory.getLogger(DefaultAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //request.setAttribute("idpurl", "http://127.0.0.1:8080/legend");
        logger.error("用户登录报错403无权访问");
        boolean isJson = false;
        if (!isJson) {
            super.handle(request, response, accessDeniedException);
            return;
        }

        Result<?> result = Result.wrapErrorResult(LegendError.PERMISSION_ERROR);
        String json = new Gson().toJson(result);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(json);
        } catch (IOException e) {
            super.handle(request, response, accessDeniedException);
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }
}
