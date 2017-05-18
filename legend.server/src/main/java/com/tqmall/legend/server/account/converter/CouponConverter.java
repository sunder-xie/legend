package com.tqmall.legend.server.account.converter;

import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.object.result.account.WxCouponDTO;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by majian on 16/9/12.
 */
public class CouponConverter implements Converter<AccountCoupon, WxCouponDTO> {

    @Override
    public WxCouponDTO convert(AccountCoupon source) {
        WxCouponDTO destination = new WxCouponDTO();
        destination.setCouponId(source.getId());
        destination.setCouponName(source.getCouponName());
        destination.setCouponSn(source.getCouponCode());
        destination.setCouponTypeId(source.getCouponInfoId());
        destination.setEffectiveDate(source.getEffectiveDate());
        destination.setExpireDate(source.getExpireDate());
        destination.setUsed(source.getUsedStatus() == 1);
        destination.setUniversal(source.getCouponType() == 2);
        CouponInfo couponInfo = source.getCouponInfo();
        if (couponInfo != null) {
            destination.setLimitAmount(couponInfo.getAmountLimit());
            destination.setUseRange(couponInfo.getUseRange());
            destination.setUseRangeDetail(couponInfo.getUseRangeDescript());
            destination.setDiscountAmount(couponInfo.getDiscountAmount());
            destination.setRemark(couponInfo.getRemark());
            destination.setSingleUse(couponInfo.getSingleUse());
            destination.setCompatibleWithCard(couponInfo.getCompatibleWithCard());
        }
        return destination;
    }
}
