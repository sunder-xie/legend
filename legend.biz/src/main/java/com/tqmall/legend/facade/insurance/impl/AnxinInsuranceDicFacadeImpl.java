package com.tqmall.legend.facade.insurance.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.insurance.AnxinInsuranceDicFacade;
import com.tqmall.legend.facade.insurance.bo.InsuranceDicBO;
import com.tqmall.mana.client.beans.insurance.InsuranceDicDTO;
import com.tqmall.mana.client.service.insurance.InsuranceDicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 17/1/20.
 * 15:32
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class AnxinInsuranceDicFacadeImpl implements AnxinInsuranceDicFacade {

    @Resource
    private InsuranceDicService insuranceDicService;

    @Override
    public List<InsuranceDicBO> getCooperationModeList() {
        log.info("[DUBBO] 开始获得模式列表 字典数据");
        Result<List<InsuranceDicDTO>> result = insuranceDicService.getCooperationModeList();
        return getResultList(result);
    }

    @Override
    public List<InsuranceDicBO> getSettleProjectList() {
        log.info("[DUBBO] 获得结算项目列表 字典数据");
        Result<List<InsuranceDicDTO>> result = insuranceDicService.getSettleProjectList();
        return getResultList(result);
    }



    private List<InsuranceDicBO> getResultList(Result<List<InsuranceDicDTO>> result){

        if(!result.isSuccess()){
            log.error("[DUBBO] 获得字典数据 fail,错误原因:{}", LogUtils.objectToString(result));
            return Lists.newArrayList();
        }
        List<InsuranceDicDTO> resultList = result.getData();

        return BdUtil.bo2do4List(resultList,InsuranceDicBO.class);
    }
}
