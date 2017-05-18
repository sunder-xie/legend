package com.tqmall.legend.server.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.object.result.account.WxCouponDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/12.
 */
public class CouponListConverter implements Converter<List<AccountCoupon>, List<WxCouponDTO>> {
    @Override
    public List<WxCouponDTO> convert(List<AccountCoupon> source) {
        List<WxCouponDTO> destination = Lists.newArrayList();
        if (source == null) {
            return destination;
        }
        for (AccountCoupon coupon : source) {
            WxCouponDTO dto = new CouponConverter().convert(coupon);
            destination.add(dto);
        }
        return destination;
    }
}
