package com.tqmall.legend.facade.onlinepay.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.finance.model.param.pay.LianPayRequest;
import com.tqmall.finance.model.param.pay.OfferListLianFormParam;
import com.tqmall.finance.model.result.user.BankCardDTO;
import com.tqmall.finance.model.result.user.UserBankCardDTO;
import com.tqmall.finance.service.pay.WebPayService;
import com.tqmall.legend.facade.onlinepay.LianlianPayFacade;
import com.tqmall.legend.facade.onlinepay.bo.OnlinePayBo;
import com.tqmall.legend.facade.onlinepay.enums.OnlinePaySourceEnum;
import com.tqmall.legend.facade.sell.SellOrderPayFaced;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 2017/2/21.
 */
@Slf4j
@Service
public class LianlianPayFacadeImpl implements LianlianPayFacade {
    @Resource
    private WebPayService webPayService;
    @Resource
    private SellOrderPayFaced sellOrderPayFaced;

    @Override
    public List<BankCardDTO> selectSupportDebitCardList() {
        log.info("[DUBBO]获取连连支持的银行卡列表接口,source:{}", Constants.CUST_SOURCE);
        Result<List<BankCardDTO>> result = webPayService.queryCertifiedCardsForInsurance(Constants.CUST_SOURCE);
        if (!result.isSuccess()) {
            log.error("[DUBBO]获取连连支付支持的银行卡列表接口失败,失败原因:{}", LogUtils.objectToString(result));
            return new ArrayList<>();
        }
        return result.getData();
    }

    @Override
    public List<BankCardDTO> selectSupportCreditCardList() {
        log.info("[DUBBO]调用获取支持的信用卡列表接口,source:{}", Constants.CUST_SOURCE);
        Result<List<BankCardDTO>> result = webPayService.queryCreditCardsForQuickPayment(Constants.CUST_SOURCE);
        if (!result.isSuccess()) {
            log.error("[DUBBO]获取finance支持的信用卡列表接口失败,失败原因:{}", LogUtils.objectToString(result));
            return new ArrayList<>();
        }
        return result.getData();
    }

    @Override
    public List<UserBankCardDTO> selectUserBankCardList(Integer ucShopId) {
        log.info("[DUBBO]调用获取银行卡列表接口,ucShopId:{}", ucShopId);
        Result<List<UserBankCardDTO>> result = webPayService.queryUserBindCardListByShopId(Constants.CUST_SOURCE, ucShopId);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用获取银行卡列表接口失败,失败原因:{}", LogUtils.objectToString(result));
            return new ArrayList<>();
        }
        return result.getData();
    }

    @Override
    public boolean checkCardNo(String cardNo, String bankCode, Integer cardType) {
        Assert.notNull(cardNo);
        Assert.notNull(bankCode);
        Assert.notNull(cardType);
        LianPayRequest lianPayRequest = new LianPayRequest();
        lianPayRequest.setCardNo(cardNo);
        lianPayRequest.setSource(Constants.CUST_SOURCE);
        lianPayRequest.setIfEncrypt(false);
        //新版本
        lianPayRequest.setVersionNo(2);
        log.info("[DUBBO]获取连连支付银行卡校验接口,参数:{}" + LogUtils.objectToString(lianPayRequest));
        Result<BankCardDTO> result = webPayService.queryCardBin(lianPayRequest);
        if (!result.isSuccess()) {
            log.error("[DUBBO]获取连连支付银行卡校验接口失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("卡号不存在");
        }
        BankCardDTO bankCardDTO = result.getData();
        if (bankCardDTO == null || !bankCode.equals(bankCardDTO.getBankCode())) {
            return false;
        }
        Integer type = 2;//是储蓄卡和信用卡都支持
        if (!type.equals(bankCardDTO.getCardType()) && !cardType.equals(bankCardDTO.getCardType())) {
            throw new BizException("该卡不支持当前支付方式");
        }
        return true;
    }

    @Override
    public String pay(OnlinePayBo onlinePayBo) {
        OfferListLianFormParam param = new OfferListLianFormParam();
        param.setNoAgree(onlinePayBo.getNoAgree());
        param.setAcctName(onlinePayBo.getAcctName());
        param.setIsEncrypt(onlinePayBo.getIsEncrypt());
        param.setIdNo(onlinePayBo.getIdNo());
        param.setCardNo(onlinePayBo.getCardNo());
        param.setGmtCreate(new Date());
        param.setLianPayType(onlinePayBo.getPayMethod());
        param.setTotalFee(onlinePayBo.getTotalFee());
        param.setWebReturnUrl(onlinePayBo.getReturnUrl());
        String orderSn = onlinePayBo.getOrderSn();
        param.setSn(orderSn);
        param.setUid(onlinePayBo.getUcShopId());
        param.setSource(Constants.CUST_SOURCE);
        param.setSubject(OnlinePaySourceEnum.SELL.getMessage());
        param.setPayType(OnlinePaySourceEnum.SELL.getPayType());
        param.setBody("");
        log.info("[DUBBO]获取连连支付表单接口,参数:{}", LogUtils.objectToString(param));
        Result<String> result = webPayService.getLianPayParam4Insurance(param);
        if (!result.isSuccess()) {
            log.error("[DUBBO]获取连连支付信息失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取连连支付信息失败");
        }
        String formInfo = result.getData();
        analysisFrom(orderSn, formInfo);
        return formInfo;
    }

    @Override
    public boolean verify(Map<String, String[]> param) {
        log.info("[DUBBO]调用连连支付验证接口:{}", LogUtils.objectToString(param));
        boolean success = false;
        try {
            Result<String> result = webPayService.verifyLianReturnInsurance(Constants.CUST_SOURCE, param);
            if (!result.isSuccess()) {
                log.error("[DUBBO]调用连连支付验证接口,失败原因:{}", LogUtils.objectToString(result));
            }
            success = result.isSuccess();
        } catch (Exception e) {
            log.error("[DUBBO]调用连连支付验证接口,失败原因:{}", e);
        }
        return success;
    }


    /**
     * 获取flow_order_sn
     *
     * @param orderSn
     * @param formInfo
     */
    private void analysisFrom(String orderSn, String formInfo) {
        String value = "value=\"";
        String tempStr = formInfo.substring(formInfo.indexOf("no_order"),formInfo.length());
        String flow_order_sn = tempStr.substring(tempStr.indexOf(value) + value.length(), tempStr.indexOf("\"/>"));
        sellOrderPayFaced.createSellOrderPay(orderSn, flow_order_sn);
    }


}
