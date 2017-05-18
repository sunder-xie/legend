package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.configuration.handler.init.converter.AccountDiscountConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:2:04 PM 02/03/2017
 */
public class BindAccountInfoInitHandler extends AbstractAccountInfoInitHandler {
    @Autowired
    private AccountInfoService accountInfoService;

    @Override
    public void init(DiscountContext cxt) {
        List<AccountDiscountBo> bindAccountDiscountList = Lists.newArrayList();

        if (isNotNull(cxt.getCustomerCarId())) {
            List<AccountInfo> bindAccountInfoList = this.accountInfoService.getBindAccountInfosByCarId(cxt.getShopId(), cxt.getCustomerCarId());
            if (isNotEmpty(bindAccountInfoList)) {
                for (AccountInfo accountInfo : bindAccountInfoList) {
                    AccountDiscountBo accountDiscount = new AccountDiscountConverter().apply(accountInfo, new AccountDiscountBo());
                    initAccountCardsInfo(cxt.getShopId(), accountDiscount);
                    initAccountComboInfo(cxt.getShopId(), accountDiscount);
                    initAccountCouponInfo(cxt.getShopId(), accountDiscount);
                    bindAccountDiscountList.add(accountDiscount);
                }
            }
        }
        cxt.setBindAccountDiscountList(bindAccountDiscountList);
    }
}
