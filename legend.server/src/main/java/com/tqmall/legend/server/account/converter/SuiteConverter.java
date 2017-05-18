package com.tqmall.legend.server.account.converter;

import com.tqmall.legend.entity.account.AccountSuite;
import com.tqmall.legend.object.result.account.CouponSuiteDTO;
import com.tqmall.legend.object.result.account.WxCouponDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/13.
 */
public class SuiteConverter implements Converter<AccountSuite, CouponSuiteDTO> {
    @Override
    public CouponSuiteDTO convert(AccountSuite source) {
        CouponSuiteDTO destination = new CouponSuiteDTO();
        destination.setSuiteName(source.getCouponSuiteName());
        List<WxCouponDTO> couponDTOs = new CouponListConverter().convert(source.getCoupons());
        destination.setCoupons(couponDTOs);
        return destination;
    }
}
