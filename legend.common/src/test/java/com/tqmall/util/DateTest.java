package com.tqmall.util;

import com.tqmall.common.util.DateUtil;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zxg on 16/12/29.
 * 20:29
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class DateTest {

    @Test
    public void testInsurance() throws ParseException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beforeDate = "2016-12-28 10:20:13";
        String afterDate = "2017-12-28 10:20:13";
        Date before  = sdf.parse(beforeDate);
        Date after  = sdf.parse(afterDate);

        System.out.println("================");
        System.out.println(DateUtil.insuranceDate(before));
        System.out.println("================");
        System.out.println(DateUtil.insuranceDate(after));
        System.out.println("================");
    }
}
