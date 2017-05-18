package com.tqmall.legend.facade.sms.newsms.util;


/**
 * Created by majian on 16/11/24.
 */
public class RedisKeyParser {

    public static String getNumberKey(String key) {
        return key + "_1";
    }
    public static String getContentKey(String key) {
        return key + "_0";
    }
    public static String getTemplateKey(String key) {
        return key + "_2";
    }

    public static String getPositionKey(String key) {
        return key + "3";
    }
}
