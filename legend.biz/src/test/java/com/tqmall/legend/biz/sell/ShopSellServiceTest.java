package com.tqmall.legend.biz.sell;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.base.BizJunitBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by feilong.li on 17/2/28.
 */
public class ShopSellServiceTest extends BizJunitBase {

    @Autowired
    private ShopSellService shopSellService;

    /**
     *空手机号码传入时异常校验
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkMobileIsBAccountTest_01() {
        shopSellService.checkMobileIsBAccount(null);
    }

    /**
     *非B账号提示校验
     */
    @Test(expected = BizException.class)
    public void checkMobileIsBAccountTest_02() {
        String mobile = "13255711271";
        shopSellService.checkMobileIsBAccount(mobile);
    }

    /**
     *B账户返回通过测试
     */
    @Test
    public void checkMobileIsBAccountTest_03() {
        String mobile = "18768107319";
        Boolean result = shopSellService.checkMobileIsBAccount(mobile);
        Assert.assertTrue(result);

    }

    /**
     *已开通系统门店登陆校验
     */
    @Test
    public void checkMobileIsExistShopTest_01() {
        String mobile = "18057108185";

        Boolean isExist = shopSellService.checkMobileIsExistShop(mobile);
        Assert.assertTrue(isExist);
    }


}
