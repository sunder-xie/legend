package com.tqmall.legend.facade.onlinepay;

import com.tqmall.finance.model.result.user.BankCardDTO;
import com.tqmall.finance.model.result.user.UserBankCardDTO;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.facade.insurance.bo.PayFinanceBo;
import com.tqmall.legend.facade.onlinepay.bo.OnlinePayBo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 2017/2/21.
 */
public interface LianlianPayFacade {


    /**
     * 获取支持的借记卡列表
     *
     * @return
     */
    List<BankCardDTO> selectSupportDebitCardList();

    /**
     * 获取支持的信用卡列表
     *
     * @return
     */
    List<BankCardDTO> selectSupportCreditCardList();

    /**
     * 获取已支付过的银行卡列表
     *
     * @param ucShopId
     * @return
     */
    List<UserBankCardDTO> selectUserBankCardList(Integer ucShopId);

    /**
     * 校验卡号
     *
     * @param cardNo
     * @param bankCode
     * @param cardType
     * @return
     */
    boolean checkCardNo(String cardNo, String bankCode, Integer cardType);

    /**
     * 连连支付
     *
     * @param onlinePayBo
     * @return
     */
    String pay(OnlinePayBo onlinePayBo);


    boolean verify(Map<String, String[]> param);
}
