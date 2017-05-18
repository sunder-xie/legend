package com.tqmall.legend.annotation;

/**
 * Created by xin on 2017/5/9.
 * 关注参数名和参数值
 */
public @interface Param {
    /**
     * 参数名
     * @return
     */
    String name();

    /**
     * 默认值
     * @return
     */
    String defaultValue() default "";

    /**
     * 别名（参数名是方法实际接收的参数，别名是打印log显示的名称）
     * @return
     */
    String aliasName() default "";
}
