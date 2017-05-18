package com.tqmall.legend.facade.insurance.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.insurance.AnxinManaSettleFacade;
import com.tqmall.mana.client.beans.settle.SettleServiceCheckDetailDTO;
import com.tqmall.mana.client.beans.settle.SettleShopDTO;
import com.tqmall.mana.client.beans.settle.SettleShopRuleIntroductionDTO;
import com.tqmall.mana.client.service.settle.RpcSettleServicePackageService;
import com.tqmall.mana.client.service.settle.RpcSettleShopRuleIntroductionService;
import com.tqmall.mana.client.service.settle.RpcSettleShopService;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.PageableResponseExtend;
import com.tqmall.search.dubbo.client.mana.param.SettleCheckDetailRequest;
import com.tqmall.search.dubbo.client.mana.result.SettleCheckDetailResult;
import com.tqmall.search.dubbo.client.mana.service.SettleCheckDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxg on 17/1/20.
 * 16:14
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
@Slf4j
public class AnxinManaSettleFacadeImpl implements AnxinManaSettleFacade {

    @Resource
    private RpcSettleShopService rpcSettleShopService;
    @Resource
    private SettleCheckDetailService settleCheckDetailService;
    @Resource
    private RpcSettleServicePackageService rpcSettleServicePackageService;
    @Resource
    private RpcSettleShopRuleIntroductionService rpcSettleShopRuleIntroductionService;

    @Override
    public SettleShopDTO getSettleShopDetailByShopId(Integer shopId) {
        log.info("[DUBBO] get shop detail about settle from mana ,the shopId:{}", shopId);

        if(shopId == null){
            return null;
        }
        Result<SettleShopDTO> dtoResult = rpcSettleShopService.getSettleShopDetailByShopId(shopId);
        if(!dtoResult.isSuccess()){
            log.error("[DUBBO] fail,reason:"+ LogUtils.objectToString(dtoResult));
            return null;
        }
        return dtoResult.getData();
    }

    @Override
    public SettleServiceCheckDetailDTO getServicePackageDetailByInsuranceSn(String insuranceOrderSn) {
        log.info("[DUBBO] get service package detail about settle from mana ,the insuranceOrderSn:{}", insuranceOrderSn);

        if(StringUtils.isEmpty(insuranceOrderSn)){
            return null;
        }


        Result<SettleServiceCheckDetailDTO> dtoByInsuranceOrderSn = rpcSettleServicePackageService.getDTOByInsuranceOrderSn(insuranceOrderSn);

        if(!dtoByInsuranceOrderSn.isSuccess()){
            log.error("[DUBBO] fail,reason:"+ LogUtils.objectToString(dtoByInsuranceOrderSn));
            return null;
        }
        return dtoByInsuranceOrderSn.getData();
    }

    @Override
    public PageableResponseExtend<SettleCheckDetailResult> getSettleCheckDetailPage(SettleCheckDetailRequest settleCheckDetailRequest, PageableRequest pageableRequest) {
        log.info("[DUBBO] get shop detail pages settle from search ,the params:{}", settleCheckDetailRequest.toString());

        com.tqmall.search.common.result.Result<PageableResponseExtend<SettleCheckDetailResult>> settleCheckDetails = settleCheckDetailService.getSettleCheckDetails(settleCheckDetailRequest, pageableRequest);
        if(!settleCheckDetails.isSuccess()){
            log.error("[DUBBO] fail,reason:"+ LogUtils.objectToString(settleCheckDetails));
            return null;
        }
        PageableResponseExtend<SettleCheckDetailResult> page = settleCheckDetails.getData();
        if(page == null){
            log.error("[DUBBO] fail,reason:result.data is null");
            return null;
        }
        return page;
    }

    @Override
    public List<SettleShopRuleIntroductionDTO> getIntroductionList() {
        log.info("[DUBBO] getIntroductionList from mana ");

        Result<List<SettleShopRuleIntroductionDTO>> introductionList = rpcSettleShopRuleIntroductionService.getIntroductionList();
        if(!introductionList.isSuccess()){
            log.error("[DUBBO] fail,reason:"+ LogUtils.objectToString(introductionList));
            return Lists.newArrayList();
        }
        return introductionList.getData();
    }
}
