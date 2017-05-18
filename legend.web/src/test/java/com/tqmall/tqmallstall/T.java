package com.tqmall.tqmallstall;

import com.tqmall.common.util.DateUtil;

import java.util.Calendar;

/**
 * Created by wanghui on 12/23/15.
 */
public class T {
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1000000000);
        System.out.println(DateUtil.convertDateToYMDHHmm(c.getTime()));
    }
}
