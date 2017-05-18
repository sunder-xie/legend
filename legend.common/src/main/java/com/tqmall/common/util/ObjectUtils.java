package com.tqmall.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by wanghui on 11/27/15.
 */
public class ObjectUtils {

    /**
     * 将Object对象转成JSON格式字符串
     * <pre>
     *     Example:{"age":38,"name":"Mokala"}
     * </pre>
     * @param object
     * @return
     */
    public static final String objectToJSON(Object object){
        return object == null?"null": JSON.toJSONString(object, false);
    }

    /**
     * 将object对象属性转成字符串(同时输出类全名)输出
     * <pre>
     *     com.tqmall.util.Person@2cb4c3ab[name=Mokala,age=38,address=<null>]
     * </pre>
     * @param object
     * @return
     */
    public static final String objectToFullString(Object object){
        return object == null?"null": ToStringBuilder.reflectionToString(object);
    }

    /**
     * 将object对象属性转成字符串输出(同时输出类名)
     * <pre>
     *     Person[name=Mokala,age=38,address=<null>]
     * </pre>
     * @param object
     * @return
     */
    public static final String objectToShortString(Object object){
        return object == null?"null": ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * 将对象转成字符串输出(多行)
     * <pre>
     *     com.tqmall.util.Person@2cb4c3ab[
     *      name=Mokala
     *      age=38
     *      address=<null>
     ]
     * </pre>
     * @param object
     * @return
     */
    public static final String objectToBeautyString(Object object){
        return object == null?"null": ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * 判断对象是否为空
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

}
