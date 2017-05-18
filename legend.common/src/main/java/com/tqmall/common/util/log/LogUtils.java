package com.tqmall.common.util.log;

import com.tqmall.common.util.ObjectUtils;
import com.tqmall.core.common.entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jason on 15-7-8.
 */
public class LogUtils {

    static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * Info级别的开关打开的情况下，打印版本和渠道
     *
     * @param refer
     * @param version
     */
    public static void printVersinAndChannel(String refer, String version, String sys) {
        if (logger.isInfoEnabled()) {
            logger.info("渠道为：" + refer + "，版本为：" + version + "，系统版本为：" + sys);
        }
    }

    /**
     * 打印档口返回的信息
     * @param result
     * @return
     */
    public static String wraperResult(Result result){
        if(result == null){
            return "result is null";
        }

        if(result.isSuccess()){
            return "result is success";
        }

        List<String> errMsgList = new LinkedList();
        if(StringUtils.isNotBlank(result.getCode())){
            errMsgList.add(String.format("code[%s]",result.getCode()));
        }

        if(StringUtils.isNotBlank(result.getMessage())){
            errMsgList.add(String.format("message[%s]", result.getMessage()));
        }

        if(result.getData() != null){
            errMsgList.add(String.format("data[%s]", result.getData().toString()));
        }

        return StringUtils.join(errMsgList.toArray(), ',');
    }

    public static String objectToString(Object object){
        return object == null?"null": ObjectUtils.objectToJSON(object);
    }

    /**
     * 封装工具类,用于日志化第三方接口的入参\返回值信息,仅仅限于单值入参
     * @param param 方法入参
     * @param result 方法返回
     * @return
     */
    public static String funToString(Object param, Object result){
        StringBuffer sb = new StringBuffer();
        sb.append("Result Info:").append(result == null?"null":objectToString(result)).append(",");
        sb.append("Param Info:").append(param == null?"null":objectToString(param));
        return sb.toString();
    }
}
