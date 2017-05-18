package com.tqmall.legend.web.account.converter;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.account.bo.CouponCreateBo;
import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.legend.web.account.vo.CashCouponCreateParam;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Created by majian on 16/9/27.
 */
public class CashCouponCreateConverter implements Converter<CashCouponCreateParam, CouponCreateBo> {
    @Override
    public CouponCreateBo convert(CashCouponCreateParam source) {
        CouponCreateBo destination = new CouponCreateBo();
        destination.setId(source.getId());
        destination.setCouponType(CouponTypeEnum.CASH_COUPON.getCode());
        destination.setCouponName(source.getCouponName());
        destination.setUseRange(source.getUseRange());
        destination.setPeriodCustomized(source.isPeriodCustomized());
        destination.setEffectivePeriodDays(source.getEffectivePeriodDays());

        Date effectiveDate = DateUtil.convertStringToDateYMD(source.getEffectiveDate());
        Date expiredDate = DateUtil.convertStringToDateYMD(source.getExpiredDate());
        if (source.isPeriodCustomized()) {
            if (effectiveDate == null || expiredDate == null) {
                throw new BizException("生效日期或失效日期格式错误");
            }
        }
        destination.setEffectiveDate(effectiveDate);
        destination.setExpiredDate(expiredDate);

        destination.setCompatibleWithCard(source.isCompatibleWithCard());
        destination.setSingleUse(source.isSingleUse());
        destination.setDiscountAmount(source.getDiscountAmount());
        destination.setLimitAmount(source.getLimitAmount());
        destination.setRemark(source.getRemark());
        destination.setServiceIds(source.getServiceIds());
        return destination;
    }
}
