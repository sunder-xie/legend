package com.tqmall.legend.biz.shop;

import com.tqmall.legend.biz.base.BizJunitBase;
import com.tqmall.legend.entity.shop.Shop;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lixiao on 16/12/23.
 */
public class ShopServiceTest extends BizJunitBase {

    @Autowired
    private ShopService shopService;

    @Test
    public void selectByIdTest01(){
        Shop shop = shopService.selectById(1l);
        Assert.assertEquals("N", shop.getIsDeleted());
    }
}

