package com.tqmall.legend.server.account.converter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.object.result.account.DiscountCouponDTO;
import com.tqmall.wheel.lang.Langs;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2017/3/16.
 */
public class CouponDiscountConverter {

    public static List<DiscountCouponDTO> convertList(List<AccountCouponDiscountBo> couponDiscountBoList) {
        if (Langs.isEmpty(couponDiscountBoList)) {
            return Collections.emptyList();
        }
        return Lists.transform(couponDiscountBoList, new Function<AccountCouponDiscountBo, DiscountCouponDTO>() {
            @Override
            public DiscountCouponDTO apply(AccountCouponDiscountBo couponDiscountBo) {
                return convert(couponDiscountBo);
            }
        });
    }

    public static DiscountCouponDTO convert(AccountCouponDiscountBo couponDiscountBo) {
        if (couponDiscountBo == null) {
            return null;
        }
        DiscountCouponDTO discountCouponDTO = new DiscountCouponDTO();
        discountCouponDTO.setAccountCouponId(couponDiscountBo.getCouponId());
        discountCouponDTO.setCouponSn(couponDiscountBo.getCouponSn());
        discountCouponDTO.setCouponName(couponDiscountBo.getCouponName());
        discountCouponDTO.setExpireDate(couponDiscountBo.getExpireDate());
        discountCouponDTO.setCouponTypeName(couponDiscountBo.getCouponType().getAlias());
        discountCouponDTO.setCouponType(couponDiscountBo.getCouponType().getCode());
        discountCouponDTO.setCouponAmount(couponDiscountBo.getDiscount());
        discountCouponDTO.setDiscountAmount(couponDiscountBo.getFinalDiscount());
        discountCouponDTO.setSelected(couponDiscountBo.isSelected());
        discountCouponDTO.setAvailable(couponDiscountBo.isAvailable());
        //设置限制条件
        discountCouponDTO.setCompatibleWithCard(couponDiscountBo.isCompatibleWithCard());
        discountCouponDTO.setSingleUse(couponDiscountBo.isSingleUse());
        return discountCouponDTO;
    }
}
