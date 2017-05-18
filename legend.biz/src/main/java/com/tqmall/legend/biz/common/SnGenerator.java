package com.tqmall.legend.biz.common;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2014-11-28 14:37
 */
public class SnGenerator {
    public static final String SERVICE = "FW";
    public static final String GOODS = "PJ";
    public static final String FEE = "FY";
    public static final String SUPPLIER = "GY";
    public static final String CLEARING = "JS";
    public static final String PAINT_USE_RECORD = "YQ";

    public static final String SMS = "SMS";

    public static String generateWithMillisecond(String type) {
        SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmssSSS");
        String dateTime = f.format(new Date());
        String sn = type + dateTime;
        return sn;
    }

    /**
     * create by jason 2015-08-04
     * 生成随机数yyMMddHHmmssSSS + 3位随机数
     */
    public static String generateRandomNum(String type) {
        String num = RandomStringUtils.random(3, false, true);
        SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmssSSS");
        String dateTime = f.format(new Date());
        String sn = type + dateTime + num;
        return sn;
    }

}
