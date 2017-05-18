package com.tqmall.legend.facade.discount.configuration.handler.init.converter;

import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.wheel.lang.Converter;

import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:2:24 PM 02/03/2017
 */
public class AccountDiscountConverter implements Converter<AccountInfo, AccountDiscountBo> {
    @Override
    public AccountDiscountBo apply(AccountInfo accountInfo, AccountDiscountBo accountDiscount) {
        if (isNotNull(accountInfo) && isNotNull(accountDiscount)) {
            accountDiscount.setAccountId(accountInfo.getId());
            accountDiscount.setCustomerId(accountInfo.getCustomerId());
            accountDiscount.setCustomerName(accountInfo.getCustomerName());
            accountDiscount.setCustomerMobile(accountInfo.getMobile());

        }
        return accountDiscount;
    }
}