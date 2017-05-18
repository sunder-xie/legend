package com.tqmall.legend.server.common;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.server.BaseCaseTest;
import com.tqmall.legend.service.common.RpcSnService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zsy on 17/2/21.
 */
public class RpcSnServiceTest extends BaseCaseTest {
    @Autowired
    private RpcSnService rpcSnService;

    @Test
    public void generateSnNoParamTest() {
        Result<String> generateSnResult = rpcSnService.generateSn(null, null);
        Assert.assertFalse(generateSnResult.isSuccess());
        Assert.assertEquals("类型不能为空", generateSnResult.getMessage());
    }

    @Test
    public void generateSnNoShopIdTest() {
        Result<String> generateSnResult = rpcSnService.generateSn(RpcSnService.GOODS, null);
        Assert.assertFalse(generateSnResult.isSuccess());
        Assert.assertEquals("门店id不能为空", generateSnResult.getMessage());
    }

    @Test
    public void generateSnTest() {
        Result<String> generateSnResult = rpcSnService.generateSn(RpcSnService.GOODS, 1l);
        Assert.assertTrue(generateSnResult.isSuccess());
    }
}
