package com.tqmall.error;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mokala on 11/24/15.
 */
public class ErrorCodeHelperTest {

    @Test
    public void test_code_1(){
        Assert.assertEquals("2010003", LegendErrorCode.CAR_LICENSE_PREFIX_EX.newResult().getCode());
    }

    @Test
    public void test_message_1() {
        Assert.assertEquals("从legend中获取所有省份的简称列表失败", LegendErrorCode.CAR_LICENSE_PREFIX_EX.newResult().getMessage());
    }

    @Test
    public void test_code_2(){
        Assert.assertEquals("2010006", LegendErrorCode.ORDER_LIST_EX.newResult().getCode());
    }

    @Test
    public void test_message_2() {
        Assert.assertEquals("分页偏移量=1从legend中获得工单列表失败", LegendErrorCode.ORDER_LIST_EX.newDataResult(1).getMessage());
    }
}
