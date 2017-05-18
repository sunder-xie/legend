package com.tqmall.legend.annotation;


import java.lang.annotation.*;
import java.util.Map;

/**
 * 记录http请求日志的注解
 * Created by wushuai on 17/3/2.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpRequestLog {
    /**
     * 固定条件，页面传参，不需要关注值的属性,如：condition = {"k1","k2"}
     * @return
     */
    Condition[] conditions() default {};

    /**
     * 扩展条件，页面传参，需要关注值的属性,如：params = {"k1","k2"}
     * @return
     */
    Param[] params() default {};
}
