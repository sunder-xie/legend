package com.tqmall.legend.biz.sell;

import com.tqmall.legend.biz.base.BizJunitBase;
import com.tqmall.legend.cache.SnFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by xiangdong.qu on 17/2/27 19:44.
 */
@Slf4j
public class SellOrderSnGenerateTest extends BizJunitBase {

    @Resource
    private SnFactory snFactory;

    @Test
    public void sellOrderSnGenerate() {
        String orderSn = snFactory.generateSellOrderSn(0l);
        Assert.assertTrue(orderSn.startsWith(SnFactory.SELL_ORDER));
    }
}
