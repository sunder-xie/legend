package com.tqmall.legend.facade.discount.configuration.handler.calc;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.discount.bo.AccountComboDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.legend.facade.discount.configuration.handler.CalcHandler;

import java.math.BigDecimal;
import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNull;

/**
 * @Author 辉辉大侠
 * @Date:4:35 PM 03/03/2017
 */
public class ComboCalcHandler implements CalcHandler {

    @Override
    public void doDiscount(DiscountContext cxt) {
        if (isNotEmpty(cxt.getSelected().getSelectedComboList())) {

            ImmutableList<AccountComboDiscountBo> selectComboList = FluentIterable.from(cxt.getAllComboServiceList())
                    .filter(new Predicate<AccountComboDiscountBo>() {
                        @Override
                        public boolean apply(AccountComboDiscountBo input) {
                            return input.isSelected();
                        }
                    }).toList();
            cxt.setDiscountAmount(cxt.getDiscountAmount().add(discountComboList(cxt.getDiscountServiceList(), selectComboList)));
        }
    }

    private BigDecimal discountComboList(List<DiscountServiceBo> serviceList, List<AccountComboDiscountBo> comboList) {
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (isNotEmpty(comboList)) {
            for (AccountComboDiscountBo combo : comboList) {
                if (combo.isSelected()) {
                    int count = combo.getUseCount();
                    while (count > 0) {
                        /**
                         * 计次卡可用并且被选中的时候需要抵扣相应的服务金额
                         */
                        DiscountServiceBo deductionService = null;
                        if (isNotEmpty(serviceList)) {
                            for (DiscountServiceBo service : serviceList) {
                                if (service.getServiceId().equals(combo.getServiceId())) {
                                    deductionService = service;
                                    break;
                                }
                            }
                        }
                        if (isNull(deductionService)) {
                            /**
                             * 不存在可抵扣服务的情况
                             */
                            throw new BizException("选中的计次卡无可用抵扣服务.");
                        } else {
                            /**
                             * 存在抵扣服务,则进行相应的金额抵扣
                             */
                            serviceList.remove(deductionService);
                            combo.setDiscount(combo.getDiscount().add(deductionService.getAmount()));
                            deductionService.setAmount(BigDecimal.ZERO);
                        }
                        count--;
                    }
                    discountAmount = discountAmount.add(combo.getDiscount());
                }
            }
        }
        return discountAmount;
    }
}
