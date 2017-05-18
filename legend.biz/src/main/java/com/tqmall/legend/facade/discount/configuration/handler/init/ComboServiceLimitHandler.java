package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.tqmall.legend.facade.discount.bo.AccountComboDiscountBo;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;

import java.util.List;
import java.util.Set;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:10:15 AM 03/03/2017
 */
public class ComboServiceLimitHandler implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        /**
         * 去工单中的所有服务id,转换成Set
         */
        Set<Long> serviceIdSet = FluentIterable.from(cxt.getDiscountServiceList()).transform(new Function<DiscountServiceBo, Long>() {
            @Override
            public Long apply(DiscountServiceBo input) {
                return input.getServiceId();
            }
        }).toSet();
        /**
         * 拥有账户下的计次卡
         */
        if (isNotNull(cxt.getAccountDiscount())) {
            doLimit(serviceIdSet, cxt.getAccountDiscount().getComboDiscountList());
        }
        /**
         * 绑定账户下的计次卡
         */
        if (isNotEmpty(cxt.getBindAccountDiscountList())) {
            for (AccountDiscountBo account : cxt.getBindAccountDiscountList()) {
                doLimit(serviceIdSet, account.getComboDiscountList());
            }
        }
        /**
         * 使用他人账户时的计次卡
         */
        if (isNotNull(cxt.getGuestAccountDiscount())) {
            doLimit(serviceIdSet, cxt.getGuestAccountDiscount().getComboDiscountList());
        }
    }

    protected void doLimit(Set<Long> serviceIdSet, List<AccountComboDiscountBo> comboList) {
        if (isNotEmpty(comboList)) {
            for (AccountComboDiscountBo combo : comboList) {
                if (combo.isAvailable() && !serviceIdSet.contains(combo.getServiceId())) {
                    combo.setAvailable(false);
                    combo.setMessage("计次卡不包含本次工单可抵扣的服务,计次卡不可用.");
                }
            }
        }
    }
}
