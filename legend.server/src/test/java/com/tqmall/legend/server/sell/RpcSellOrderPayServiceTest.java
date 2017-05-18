package com.tqmall.legend.server.sell;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.sell.RpcSellNoticeParam;
import com.tqmall.legend.server.BaseCaseTest;
import com.tqmall.legend.service.sell.RpcSellOrderPayService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/27 17:23.
 */
public class RpcSellOrderPayServiceTest extends BaseCaseTest {

    @Resource
    private RpcSellOrderPayService rpcSellOrderPayService;

    /**
     * 参数不完全校验
     */
    @Test
    public void sellOrderPaySuccessNoticeTest() {
        RpcSellNoticeParam rpcSellNoticeParam = new RpcSellNoticeParam();

        rpcSellNoticeParam.setSellOrderSn("");
        Result<Boolean> result = null;
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());

        rpcSellNoticeParam.setSellOrderSn("YXSM");
        rpcSellNoticeParam.setPayAmount(BigDecimal.ONE);
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());

        rpcSellNoticeParam.setPayDate(new Date());
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());

        rpcSellNoticeParam.setPayId(1);
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());

        rpcSellNoticeParam.setPayNo("123");
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(StringUtils.equals("获取订单信息有误", result.getMessage()));
    }


    /**
     * 支付金额与售卖金额不符校验
     */
    @Test
    public void sellOrderPaySuccessNoticeTestOne() {
        RpcSellNoticeParam rpcSellNoticeParam = new RpcSellNoticeParam();
        rpcSellNoticeParam.setSellOrderSn("YXSM2017022710591212");
        rpcSellNoticeParam.setPayNo("1234567890");
        rpcSellNoticeParam.setPayId(1);
        rpcSellNoticeParam.setPayDate(new Date());
        rpcSellNoticeParam.setPayAmount(BigDecimal.ZERO);
        Result<Boolean> result = null;
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(StringUtils.equals("支付金额与售卖金额不符", result.getMessage()));
    }

    /**
     * 支付订单信息不存在校验
     */
    @Test
    public void sellOrderPaySuccessNoticeTestTwo() {
        RpcSellNoticeParam rpcSellNoticeParam = new RpcSellNoticeParam();
        rpcSellNoticeParam.setSellOrderSn("YY073191702270001");
        rpcSellNoticeParam.setPayNo("1234567890");
        rpcSellNoticeParam.setPayId(1);
        rpcSellNoticeParam.setPayDate(new Date());
        rpcSellNoticeParam.setPayAmount(BigDecimal.valueOf(5980));
        Result<Boolean> result = null;
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(StringUtils.equals("获取支付信息失败", result.getMessage()));
    }


    /**
     * 支付状态更改 回滚测试
     */
    @Test
    public void sellOrderPaySuccessNoticeTestThree() {
        RpcSellNoticeParam rpcSellNoticeParam = new RpcSellNoticeParam();
        rpcSellNoticeParam.setSellOrderSn("YXSM2017022710591212");
        //字段超长
        rpcSellNoticeParam.setPayNo("12345678900129192801924819841049810241024123123");
        rpcSellNoticeParam.setPayId(1);
        rpcSellNoticeParam.setPayDate(new Date());
        rpcSellNoticeParam.setPayAmount(BigDecimal.valueOf(0.01));
        Result<Boolean> result = null;
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(StringUtils.equals("支付状态更新失败", result.getMessage()));
    }

    /**
     * 支付状态更改
     */
    @Test
    public void sellOrderPaySuccessNoticeTestFour() {
        RpcSellNoticeParam rpcSellNoticeParam = new RpcSellNoticeParam();
        rpcSellNoticeParam.setSellOrderSn("YXSM073191703010005");
        //字段超长
        rpcSellNoticeParam.setPayNo("123456789001");
        rpcSellNoticeParam.setPayId(1);
        rpcSellNoticeParam.setPayDate(new Date());
        rpcSellNoticeParam.setPayAmount(BigDecimal.valueOf(1));
        Result<Boolean> result = null;
        result = rpcSellOrderPayService.sellOrderPaySuccessNotice(rpcSellNoticeParam);
        Assert.assertTrue(result.isSuccess());
    }
}
