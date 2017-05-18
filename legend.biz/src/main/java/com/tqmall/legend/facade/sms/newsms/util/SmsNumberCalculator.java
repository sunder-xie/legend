package com.tqmall.legend.facade.sms.newsms.util;

/**
 * Created by majian on 16/11/29.
 */
public class SmsNumberCalculator {
    public static final int LIMIT = 70;
    public static int getNumber(int length) {
        double result = Math.ceil((double) length / (double) LIMIT);
        return (int)result;
    }
}
