package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.discount.bo.AccountCouponDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.SelectedCouponBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.wheel.lang.Objects;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNull;

/**
 * @Author 辉辉大侠
 * @Date:11:01 AM 06/03/2017
 */
@Slf4j
public class CouponSingleUseLimitHandler implements InitHandler {
    @Override
    public void init(DiscountContext cxt) {
        /**
         * 已有选中的优惠券
         */
        List<SelectedCouponBo> selectedCouponList = cxt.getSelected().getSelectedCouponList();

        if (isNotEmpty(selectedCouponList)) {
            /**
             * 获取可用并且一张工单只能使用一张的优惠券类型id
             */
            Set<Long> singleUseCouponTypeIds = FluentIterable.from(cxt.getAllCouponList())
                    .filter(new Predicate<AccountCouponDiscountBo>() {
                        /**
                         * 只取可用和只能单张使用的优惠券
                         * @param input
                         * @return
                         */
                        @Override
                        public boolean apply(AccountCouponDiscountBo input) {
                            return input.isAvailable() && input.isSingleUse();
                        }
                    }).transform(new Function<AccountCouponDiscountBo, Long>() {
                        @Override
                        public Long apply(AccountCouponDiscountBo input) {
                            return input.getCouponTypeId();
                        }
                    }).toSet();
            /**
             * 存在一张工单只能使用一张类型的优惠券
             */
            if (isNotEmpty(singleUseCouponTypeIds)) {
                /**
                 * 获取优惠券id和优惠券实体的Map
                 */
                Map<Long, AccountCouponDiscountBo> couponIdMap = Maps.uniqueIndex(cxt.getAllCouponList(), new Function<AccountCouponDiscountBo, Long>() {
                    @Override
                    public Long apply(AccountCouponDiscountBo input) {
                        return input.getCouponId();
                    }
                });
                Map<Long, Integer> usedTypes = Maps.newHashMap();
                for (Long typeId : singleUseCouponTypeIds) {
                    usedTypes.put(typeId, 0);
                }

                for (SelectedCouponBo selectedCoupon : selectedCouponList) {
                    AccountCouponDiscountBo coupon = couponIdMap.get(selectedCoupon.getCouponId());
                    if (isNull(coupon)) {
                        log.error("从账户中找不到id为[{}]的优惠券信息,cxt:{}", selectedCoupon.getCouponId(), Objects.toJSON(cxt));
                        throw new BizException("从账户列表中找不到已选中的优惠券信息");
                    } else {
                        if (usedTypes.containsKey(coupon.getCouponTypeId())) {
                            Integer usedCount = usedTypes.get(coupon.getCouponTypeId());
                            usedCount++;
                            usedTypes.put(coupon.getCouponTypeId(), usedCount);
                            if (usedCount > 1) {
                                log.error("优惠券类型id:[{}]只能被使用一张,但系统中超过一张被选中.", coupon.getCouponTypeId());
                                throw new BizException("一张工单只能使用一种类型的优惠券被选中了多张.");
                            }
                        }
                    }
                }
                for (AccountCouponDiscountBo coupon : cxt.getAllCouponList())
                    if (coupon.isAvailable() && !coupon.isSelected()) {
                        if (usedTypes.containsKey(coupon.getCouponTypeId())
                                && usedTypes.get(coupon.getCouponTypeId()) == 1) {
                            coupon.setAvailable(false);
                            coupon.setMessage("此类型的优惠券一张工单只能使用一张,已选中一张,其他此类型的优惠券不可使用.");
                        }
                    }

            }
        }
    }
}
