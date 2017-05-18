package com.tqmall.legend.facade.discount.configuration.handler.calc;

import com.beust.jcommander.internal.Lists;
import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.SelectedCouponBo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by 辉辉大侠 on 06/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CouponCalcHandlerTest {

    @Test
    public void testSort() throws Exception {
        List<AccountCouponDiscountBo> list = Lists.newArrayList();
        AccountCouponDiscountBo c = new AccountCouponDiscountBo();
        c.setCouponId(2L);
        c.setCouponType(CouponTypeEnum.UNIVERSAL_COUPON);
        list.add(c);
        c = new AccountCouponDiscountBo();
        c.setCouponId(4L);
        c.setCouponType(CouponTypeEnum.CASH_COUPON);
        c.setRange(CouponInfoUseRangeEnum.ZXZDFWXMDZ);
        list.add(c);
        c = new AccountCouponDiscountBo();
        c.setCouponId(1L);
        c.setCouponType(CouponTypeEnum.UNIVERSAL_COUPON);
        list.add(c);
        c = new AccountCouponDiscountBo();
        c.setCouponId(6L);
        c.setCouponType(CouponTypeEnum.CASH_COUPON);
        c.setRange(CouponInfoUseRangeEnum.ZXZDFWXMDZ);
        list.add(c);
        c = new AccountCouponDiscountBo();
        c.setCouponId(5L);
        c.setCouponType(CouponTypeEnum.CASH_COUPON);
        c.setRange(CouponInfoUseRangeEnum.QCTY);
        list.add(c);

        CouponCalcHandler handler = new CouponCalcHandler();
        handler.sort(list);
        Assert.assertTrue(4L == list.get(0).getCouponId());

        Assert.assertTrue(6L == list.get(1).getCouponId());
        Assert.assertTrue(5L == list.get(2).getCouponId());

    }

}