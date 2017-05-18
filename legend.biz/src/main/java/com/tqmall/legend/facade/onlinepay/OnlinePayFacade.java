package com.tqmall.legend.facade.onlinepay;

import com.tqmall.legend.facade.onlinepay.bo.OnlinePayBo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by sven on 2017/2/21.
 */
public interface OnlinePayFacade {
    /**
     * 获取支付金额
     *
     * @param source
     * @param orderSn
     * @return
     */
    BigDecimal selectPayAmount(Integer source, String orderSn);

    /**
     * 获取门店id
     *
     * @param orderSn
     * @param source
     * @return
     */
    Integer selectUserGlobalShopId(String orderSn, Integer source, HttpServletRequest request);

    String payChoice(OnlinePayBo onlinePayBo);
}
