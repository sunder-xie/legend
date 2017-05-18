package com.tqmall.legend.facade.insurance.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchCashCouponParam;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchFormForCashCouponParam;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.UseCashCouponParam;
import com.tqmall.insurance.domain.result.cashcoupon.CashCouponDetailDTO;
import com.tqmall.insurance.domain.result.cashcoupon.ShopCashCouponDTO;
import com.tqmall.insurance.service.insurance.cashcoupon.RpcInsuranceCashCouponService;
import com.tqmall.legend.facade.insurance.InsuranceCashCouponFacade;
import com.tqmall.mana.client.beans.cashcoupon.CreateRuleConfigResultDTO;
import com.tqmall.mana.client.service.cashcoupon.RpcCashCouponRuleConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouheng on 17/3/11.
 */
@Service
@Slf4j
public class InsuranceCashCouponFacadeImpl implements InsuranceCashCouponFacade {

    @Autowired
    private RpcInsuranceCashCouponService rpcInsuranceCashCouponService;

    @Autowired
    private RpcCashCouponRuleConfigService rpcCashCouponRuleConfigService;


    @Override
    public ShopCashCouponDTO getShopCashCouponInfo(SearchFormForCashCouponParam param) {
        log.info("[dubbo-->insurance]调用Insurance接口获取门店现金券信息开始,param:{}", LogUtils.objectToString(param));
        if (param.getAgentId() == null) {
            throw new BizException("获取门店优惠券信息参数异常");
        }
        Result<ShopCashCouponDTO> result = rpcInsuranceCashCouponService.getShopCashCouponTotalInfo(param);
        if (!result.isSuccess()) {
            log.error("调用Insurance接口获取门店现金券信息接口失败", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    @Override
    public Result useCashCoupon(UseCashCouponParam uesCashCouponParam) {
        log.info("[dubbo-->insurance]调用insurance接口useCashCoupon开始, uesCashCouponParam:{}", LogUtils.objectToString(uesCashCouponParam));
        Result result = rpcInsuranceCashCouponService.useCashCoupon(uesCashCouponParam);
        if (!result.isSuccess()) {
            log.error("确认用券失败", LogUtils.objectToString(result));
            return Result.wrapErrorResult("", result.getMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }

    @Override
    public String getCouponDesc(String keyDesc) {
        if (keyDesc == null) {
            throw new BizException("调用Insurance接口获取优惠券描述接口参数为空");
        }
        Result<String> result = rpcInsuranceCashCouponService.getDescByKey(keyDesc);
        if (!result.isSuccess()) {
            log.error("调用Insurance接口获取优惠券描述接口出错", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();

    }

    @Override
    public PagingResult<CashCouponDetailDTO> queryCashCouponPage(SearchCashCouponParam searchCashCouponParam) {
        log.info("[dubbo-->insurance]获取门店优惠券详细信息分页列表开始,param:{}", LogUtils.objectToString(searchCashCouponParam));
        if (searchCashCouponParam == null) {
            throw new BizException("获取门店优惠券详细分页信息列表参数异常");
        }
        PagingResult<CashCouponDetailDTO> pageResult = rpcInsuranceCashCouponService.queryCashCouponPage(searchCashCouponParam);
        if (!pageResult.isSuccess()) {
            log.error("调用insurance接口queryCashCouponPage失败", LogUtils.objectToString(pageResult));
            throw new BizException(pageResult.getMessage());
        }
        return pageResult;
    }

    public boolean judgeIsOpenCashCoupon(String cityCode) {
        log.info("[dubbo-->mana]调用mana接口getIsOpenByCityCode开始, getIsOpenByCityCode:{}", cityCode);
        Result<Boolean> result = rpcCashCouponRuleConfigService.getIsOpenByCityCode(cityCode, "LEGEND");
        if (!result.isSuccess()) {
            log.error("调用mana接口getIsOpenByCityCode失败", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }


    @Override
    public CreateRuleConfigResultDTO getCashCouponSettleRule(String cityCode) {
        log.info("[dubbo-->mana]调用mana接口getCreateRuleConfigInfo开始, getIsOpenByCityCode:{}", cityCode);
        Result<CreateRuleConfigResultDTO> result = rpcCashCouponRuleConfigService.getCreateRuleConfigInfo(cityCode);
        if (!result.isSuccess()) {
            log.error("调用mana接口getCreateRuleConfigInfo失败", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }
}
