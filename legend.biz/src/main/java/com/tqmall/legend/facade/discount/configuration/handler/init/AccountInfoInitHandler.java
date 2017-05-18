package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.configuration.handler.init.converter.AccountDiscountConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tqmall.wheel.lang.Langs.isNotNull;
import static com.tqmall.wheel.lang.Langs.isNull;

/**
 * @Author 辉辉大侠
 * @Date:2:04 PM 02/03/2017
 */
@Slf4j
public class AccountInfoInitHandler extends AbstractAccountInfoInitHandler {

    @Autowired
    private AccountInfoService accountInfoService;

    @Override
    public void init(DiscountContext cxt) {
        initAccountInfo(cxt);
        this.initAccountCardsInfo(cxt.getShopId(), cxt.getAccountDiscount());
        this.initAccountComboInfo(cxt.getShopId(), cxt.getAccountDiscount());
        this.initAccountCouponInfo(cxt.getShopId(), cxt.getAccountDiscount());
    }

    /**
     * 初始化拥有账户信息
     *
     * @param cxt
     */
    private void initAccountInfo(DiscountContext cxt) {
        if (isNotNull(cxt.getCustomerCarId())) {
            AccountInfo accountInfo = this.accountInfoService.getAccountInfoByCarIdAndShopId(cxt.getShopId(), cxt.getCustomerCarId());
            if (isNull(accountInfo)) {
                if (log.isErrorEnabled()) {
                    log.error("根据车辆id获取账户信息异常,customerCarId:{}", cxt.getCustomerCarId());
                }
                throw new BizException("根据车辆信息获取账户信息异常.");
            } else {
                cxt.setAccountDiscount(new AccountDiscountConverter().apply(accountInfo, new AccountDiscountBo()));
            }
        }
    }
}
