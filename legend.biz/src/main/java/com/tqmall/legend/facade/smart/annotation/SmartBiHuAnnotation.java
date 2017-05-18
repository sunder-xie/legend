package com.tqmall.legend.facade.smart.annotation;

import java.lang.annotation.*;
import java.math.BigDecimal;

/**
 * Created by jinju.zeng on 2016/12/27.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmartBiHuAnnotation {
    String name() default "";

    //是否需要设置不计免赔这个字段
    boolean isDeductible() default true;
}
