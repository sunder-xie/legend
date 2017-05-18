package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.configuration.handler.init.converter.AccountDiscountConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotBlank;
import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:2:05 PM 02/03/2017
 */
@Slf4j
public class GuestAccountInfoInitHandler extends AbstractAccountInfoInitHandler {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountInfoService accountInfoService;
    private Long customerId;

    @Override
    public void init(DiscountContext cxt) {
        this.initAccountInfo(cxt);
        initAccountCardsInfo(cxt.getShopId(), cxt.getGuestAccountDiscount());
        initAccountComboInfo(cxt.getShopId(), cxt.getGuestAccountDiscount());
        initAccountCouponInfo(cxt.getShopId(), cxt.getGuestAccountDiscount());
    }

    private void initAccountInfo(DiscountContext cxt) {
        if (isNotBlank(cxt.getGuestMobile())) {
            checkMobile(cxt);
            List<Customer> customerList = customerService.getCustomerByMobile(cxt.getGuestMobile(), cxt.getShopId());
            if (customerList.size() == 0) {
                log.error("根据手机号查不到他人车主信息,mobile:[{}]", cxt.getGuestMobile());
            } else if (customerList.size() > 1) {
                log.error("根据手机号【{}】查找到{}个车主信息.", cxt.getGuestMobile(), customerList.size());
                throw new BizException("根据手机号[" + cxt.getGuestMobile() + "]查找到" + customerList.size() + "个车主信息.");
            } else {
                customerId = customerList.get(0).getId();
                AccountInfo accountInfo = this.accountInfoService.getAccountInfoByCustomerIdAndShopId(cxt.getShopId(), customerId);
                if (isNotNull(accountInfo)) {
                    cxt.setGuestAccountDiscount(new AccountDiscountConverter().apply(accountInfo, new AccountDiscountBo()));
                } else {
                    if (log.isErrorEnabled()) {
                        log.error("根据客户id获取账户信息异常,customerId:{}", customerId);
                    }
                    throw new BizException("获取他人[" + cxt.getGuestMobile() + "]账户信息异常.");
                }
            }
        }

    }

    private void checkMobile(DiscountContext cxt) {
        if (isNotNull(cxt.getAccountDiscount())) {
            if (cxt.getGuestMobile().equals(cxt.getAccountDiscount().getCustomerMobile())) {
                throw new BizException("他人手机号为车辆账户下的手机号");
            }
        }
        if (isNotEmpty(cxt.getBindAccountDiscountList())) {
            for (AccountDiscountBo account : cxt.getBindAccountDiscountList()) {
                if (cxt.getGuestMobile().equals(account.getCustomerMobile())) {
                    throw new BizException("他人手机号为车辆绑定账户下的手机号");
                }
            }
        }
    }
}
