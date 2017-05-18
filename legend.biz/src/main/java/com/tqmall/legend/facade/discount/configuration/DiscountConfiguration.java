package com.tqmall.legend.facade.discount.configuration;

import com.google.common.collect.Lists;
import com.tqmall.legend.facade.discount.configuration.handler.CalcHandler;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.legend.facade.discount.configuration.handler.calc.CardCalcHandler;
import com.tqmall.legend.facade.discount.configuration.handler.calc.ComboCalcHandler;
import com.tqmall.legend.facade.discount.configuration.handler.calc.CouponCalcHandler;
import com.tqmall.legend.facade.discount.configuration.handler.init.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author 辉辉大侠
 * @Date:10:51 AM 02/03/2017
 */
@Configuration
public class DiscountConfiguration {

    @Bean(name = "init-handler-list")
    public List<InitHandler> initHandlerList() {
        List<InitHandler> initHandlerList = Lists.newArrayList();
        initHandlerList.add(customerCarInfoInit());
        initHandlerList.add(orderInfoInit());
        initHandlerList.add(accountInfoInit());
        initHandlerList.add(bindAccountInfoInit());
        initHandlerList.add(guestAccountInfoInit());

        initHandlerList.add(cardSelectedInit());
        initHandlerList.add(comboSelectedInit());
        initHandlerList.add(couponSelectedInit());

        initHandlerList.add(timeLimitInit());
        initHandlerList.add(cardCompatibleWithCouponLimitInit());
        initHandlerList.add(comboLimitInit());
        initHandlerList.add(couponRangeAndServicesLimitInit());
        initHandlerList.add(couponSingleUseLimitInit());
        initHandlerList.add(couponCompatibleWithCardLimitInit());

        return initHandlerList;
    }

    /**
     * 客户车辆信息初始化
     */
    @Bean
    public InitHandler customerCarInfoInit() {
        return new CustomerCarInfoinitHandler();
    }

    /**
     * 工单信息初始化
     */
    @Bean
    public InitHandler orderInfoInit() {
        return new OrderInfoInitHandler();
    }

    /**
     * 账户信息初始化
     */
    @Bean
    public InitHandler accountInfoInit() {
        return new AccountInfoInitHandler();
    }

    /**
     * 绑定账户信息初始化
     */
    @Bean
    public InitHandler bindAccountInfoInit() {
        return new BindAccountInfoInitHandler();
    }

    /**
     * 他人账户信息初始化
     */
    @Bean
    public InitHandler guestAccountInfoInit() {
        return new GuestAccountInfoInitHandler();
    }

    /**
     * 选中会员卡信息初始化
     */
    @Bean
    public InitHandler cardSelectedInit() {
        return new CardSelectedInitHandler();
    }

    /**
     * 已选中计次卡初始化
     */
    @Bean
    public InitHandler comboSelectedInit() {
        return new ComboSelectedInitHandler();
    }

    /**
     * 选中优惠券信息初始化
     */
    @Bean
    public InitHandler couponSelectedInit() {
        return new CouponSelectedInitHandler();
    }

    @Bean
    public InitHandler timeLimitInit() {
        return new TimeLimitInitHandler();
    }

    @Bean
    public InitHandler cardCompatibleWithCouponLimitInit() {
        return new CardCompatibleWithCouponLimit();
    }

    @Bean
    public InitHandler comboLimitInit() {
        return new ComboServiceLimitHandler();
    }

    @Bean
    public InitHandler couponRangeAndServicesLimitInit() {
        return new CouponRangeAndServicesLimitInitHandler();
    }

    @Bean
    public InitHandler couponSingleUseLimitInit() {
        return new CouponSingleUseLimitHandler();
    }

    @Bean
    public InitHandler couponCompatibleWithCardLimitInit() {
        return new CouponCompatibleWithCardLimit();
    }

    @Bean(name = "calc-handler-list")
    public List<CalcHandler> calcHandlerList() {
        List<CalcHandler> calcHandlerList = Lists.newArrayList();
        calcHandlerList.add(cardCalc());
        calcHandlerList.add(comboCalc());
        calcHandlerList.add(couponCalc());
        return calcHandlerList;
    }

    @Bean
    public CalcHandler cardCalc() {
        return new CardCalcHandler();
    }

    @Bean
    public CalcHandler comboCalc() {
        return new ComboCalcHandler();
    }

    @Bean
    public CalcHandler couponCalc() {
        return new CouponCalcHandler();
    }
}
