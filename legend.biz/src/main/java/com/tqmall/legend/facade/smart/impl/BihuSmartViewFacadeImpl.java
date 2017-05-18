package com.tqmall.legend.facade.smart.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.smart.SmartConsumeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartShopOrderDTO;
import com.tqmall.insurance.service.smart.RpcSmartConsumeRecordService;
import com.tqmall.insurance.service.smart.RpcSmartRechargeService;
import com.tqmall.insurance.service.smart.RpcSmartShopService;
import com.tqmall.legend.facade.smart.BihuSmartViewFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created by zwb on 16/12/21.
 */
@Service
@Slf4j
public class BihuSmartViewFacadeImpl implements BihuSmartViewFacade {
    @Resource
    private RpcSmartShopService rpcSmartShopService;
    @Resource
    private RpcSmartRechargeService rpcSmartRechargeService;
    @Autowired
    private RpcSmartConsumeRecordService rpcSmartConsumeRecordService;

    @Override
    public SmartShopOrderDTO getShopInfo(Integer agentId) {
        Assert.notNull(agentId, "agentId");
        log.info("[DUBBO]调用获取门店剩余充值次数接口,agentId:{}", agentId);
        Result<SmartShopOrderDTO> result = rpcSmartShopService.getRechargeShopInfo(agentId);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用获取门店剩余充值次数接口失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException("调用获取门店剩余充值次数接口失败");
        }
        return result.getData();
    }


    @Override
    public PagingResult<SmartRechargeRecordDTO> getRechargeRecordPage(Integer agentId, Integer start, Integer pageSize) {
        Assert.notNull(agentId, "请传入门店ID");
        log.info("[DUBBO]调用获取门店充值记录分页接口,agentId:{}，start:{}，pageSize:{}", agentId,start,pageSize);
        PagingResult<SmartRechargeRecordDTO> result = rpcSmartRechargeService.getRecordPage(agentId, start, pageSize);
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用获取门店充值记录分页接口失败,错误原因:{}", result.getMessage());
            return PagingResult.wrapErrorResult("", result.getMessage());
        }
        return result;
    }

    @Override
    public PagingResult<SmartConsumeRecordDTO> getConsumeRecordPageList(Pageable page, Integer shopId) {
        com.alibaba.dubbo.common.utils.Assert.notNull(shopId, "门店id不能为空");
        log.info("[DUBBO]调用获取门店消费记录分页接口,shopId:{}",shopId);
        PagingResult<SmartConsumeRecordDTO> result = rpcSmartConsumeRecordService.getConsumeRecordPage(shopId, page.getPageNumber(), page.getPageSize());
        if (!result.isSuccess()) {
            log.error("[DUBBO]调用获取门店消费记录分页接口失败,错误原因:{}", result.getMessage());
            return PagingResult.wrapErrorResult("", result.getMessage());
        }
        return result;
    }
}
