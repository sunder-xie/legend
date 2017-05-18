package com.tqmall.legend.annotation;

/**
 * Created by xin on 2017/5/9.
 *
 * 只关注参数名
 */
public @interface Condition {
    /**
     * 参数名
     * @return
     */
    String name();

    /**
     * 别名（参数名是方法实际接收的参数，别名是打印log显示的名称）
     * @return
     */
    String aliasName() default "";
}
