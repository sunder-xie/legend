package com.tqmall.legend.biz.support.mvc;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by mokala on 10/8/15.
 * 还是先注释掉吧,采用yangf以前的错误页面
 */
@Slf4j
@Component
public class SpringMVCExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /**
         * 对无法控制的ClientAbort异常,不做日志打印
         */
        if (!"org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getCanonicalName())) {
            log.error("Spring Controller invoker error.", ex);
        }
        ModelAndView mv = new ModelAndView();
        /**
         * 如果是异步请求,则返回系统异常
         */
        if (request.getHeader("x-requested-with") != null) {
            String message = "系统内部未知错误，请联系云修客服人员。" ;
            if (ex instanceof BizException
                    || ex instanceof BusinessCheckedException) {
                message = ex.getMessage();
            }
            mv.addObject("message", message);
            mv.addObject("errorMsg", message);
            mv.addObject("success", false);
            mv.setView(new FastJsonJsonView());
        } else {
            String url = request.getContextPath();
            if (ex instanceof PermissionException) {
                url += "/html/error-web/permission_error.html";
            } else {
                url += "/html/error-web/web_error.html";
            }
            try {
                response.sendRedirect(url);
            } catch (Exception e) {
                log.error("SpringMVCExceptionHandler invoke error. ", e);
            }
        }
        return mv;
    }
}
