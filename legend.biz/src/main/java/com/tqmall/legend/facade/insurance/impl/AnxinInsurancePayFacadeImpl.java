package com.tqmall.legend.facade.insurance.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.insurance.AnxinInsurancePayFacade;
import com.tqmall.mace.param.anxin.RpcAxPayParam;
import com.tqmall.mace.param.anxin.RpcAxPayTypeEnum;
import com.tqmall.mace.service.anxin.RpcAxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zwb on 17/3/8.
 */
@Slf4j
@Service
public class AnxinInsurancePayFacadeImpl implements AnxinInsurancePayFacade {
    @Resource
    RpcAxService rpcAxService;

    @Override
    public String getAliPayPageInfo(RpcAxPayParam param) {
        param.setSource(Constants.CUST_SOURCE);
        param.setPayTypeId(RpcAxPayTypeEnum.ALIPAY_WEB.getPayType());
        log.info("[DUBBO]获取mace支付宝支付接口信息,ucShopId:{},保单号:{}", param.getShopId(), param.getInsuranceOrderSn());
        Result<String> result = rpcAxService.getAxPayPage(param);
        if (!result.isSuccess() || "".equals(result.getData())) {
            log.error("[DUBBO]获取mace支付宝支付接口信息,失败原因:{}", LogUtils.objectToString(result));
            throw new BizException("获取支付信息失败");
        }
        return result.getData();
    }
}
