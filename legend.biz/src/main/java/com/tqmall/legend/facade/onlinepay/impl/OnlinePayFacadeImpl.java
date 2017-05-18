package com.tqmall.legend.facade.onlinepay.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.facade.onlinepay.LianlianPayFacade;
import com.tqmall.legend.facade.onlinepay.OnlinePayFacade;
import com.tqmall.legend.facade.onlinepay.bo.OnlinePayBo;
import com.tqmall.legend.facade.onlinepay.enums.OnlinePayMethodEnum;
import com.tqmall.legend.facade.onlinepay.enums.OnlinePaySourceEnum;
import com.tqmall.legend.facade.sell.SellOrderPayFaced;
import com.tqmall.ucenter.object.result.account.AccountDTO;
import com.tqmall.ucenter.service.account.RpcAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by sven on 2017/2/21.
 */
@Slf4j
@Service
public class OnlinePayFacadeImpl implements OnlinePayFacade {
    @Autowired
    private SellOrderPayFaced sellOrderPayFaced;
    @Autowired
    private RpcAccountService rpcAccountService;
    @Autowired
    private LianlianPayFacade lianlianPayFacade;

    @Override
    public BigDecimal selectPayAmount(Integer source, String orderSn) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (OnlinePaySourceEnum.isSell(source)) {
            SellOrder order = sellOrderPayFaced.getSellOrderByOrderSnAndCheckDiscount(orderSn);
            if (order == null || BigDecimal.ZERO.compareTo(order.getSellAmount()) > -1) {
                log.error("支付订单有误,无法支付,订单号:{}", orderSn);
                throw new BizException("支付订单有误,无法支付");
            }
            totalAmount = order.getSellAmount();
        }
        return totalAmount;
    }

    @Override
    public Integer selectUserGlobalShopId(String orderSn, Integer source, HttpServletRequest request) {
        if (OnlinePaySourceEnum.isSell(source)) {
            SellOrder order = sellOrderPayFaced.getSellOrderByOrderSnAndCheckDiscount(orderSn);
            if (order == null) {
                throw new BizException("客户信息有误");
            }
            log.info("【DUBBO】调用ucenter根据手机号获取账户信息,mobile:{}", order.getBuyMobile());
            Result<AccountDTO> result = rpcAccountService.getAccountByMobile(Constants.CUST_SOURCE, order.getBuyMobile());
            if (!result.isSuccess()) {
                log.error("【DUBBO】调用ucenter根据手机号获取账户信息失败,错误原因:{}", LogUtils.objectToString(request));
                throw new BizException("获取不到客户信息");
            }
            return result.getData().getShopId();
        }
        String userGlobalId = UserUtils.getUserGlobalIdForSession(request);
        return Integer.valueOf(userGlobalId);
    }

    @Override
    public String payChoice(OnlinePayBo onlinePayBo) {
        BigDecimal totalFee = selectPayAmount(onlinePayBo.getSource(), onlinePayBo.getOrderSn());
        if (totalFee.compareTo(onlinePayBo.getTotalFee()) != 0) {
            throw new BizException("支付金额有误,支付失败");
        }
        if (OnlinePayMethodEnum.isLianlianPay(onlinePayBo.getPaymentMethod())) {
            return lianlianPayFacade.pay(onlinePayBo);
        }
        return "";
    }
}
