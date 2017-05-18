package com.tqmall.legend.web.account.converter;

import com.tqmall.legend.biz.account.bo.CouponCreateBo;
import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.legend.web.account.vo.UniversalCouponCreateParam;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by majian on 16/9/27.
 */
public class UniversalCouponCreateConverter implements Converter<UniversalCouponCreateParam, CouponCreateBo> {
    @Override
    public CouponCreateBo convert(UniversalCouponCreateParam source) {
        CouponCreateBo destination = new CouponCreateBo();
        destination.setId(source.getId());
        destination.setCouponType(CouponTypeEnum.UNIVERSAL_COUPON.getCode());
        destination.setCouponName(source.getCouponName());
        destination.setDiscountAmount(source.getDiscountAmount());
        destination.setUseRange(0);
        destination.setPeriodCustomized(false);
        destination.setEffectivePeriodDays(source.getEffectivePeriodDays());
        destination.setCompatibleWithCard(source.isCompatibleWithCard());
        destination.setSingleUse(source.isSingleUse());
        destination.setRemark(source.getRemark());
        return destination;
    }
}
