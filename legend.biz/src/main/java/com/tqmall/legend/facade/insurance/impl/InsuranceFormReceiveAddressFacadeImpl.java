package com.tqmall.legend.facade.insurance.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.address.InsuranceFormReceiveAddrDTO;
import com.tqmall.insurance.service.insurance.address.RpcFormReceiveAddrService;
import com.tqmall.legend.facade.insurance.InsuranceFormReceiveAddressFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceFormReceiveAddressVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhouheng on 17/4/26.
 */
@Slf4j
@Service
public class InsuranceFormReceiveAddressFacadeImpl implements InsuranceFormReceiveAddressFacade {

    @Autowired
    private RpcFormReceiveAddrService rpcFormReceiveAddrService;

    @Override
    public void createFormReceiveAddress(InsuranceFormReceiveAddressVo addressVo) {
        log.info("调用接口保存收保地址信息. 入参:{}", addressVo);
        InsuranceFormReceiveAddrDTO addrDTO = BdUtil.bo2do(addressVo, InsuranceFormReceiveAddrDTO.class);

        Result<Void> result = rpcFormReceiveAddrService.createFormReceiveAddr(addrDTO);
        if (!result.isSuccess()) {
            log.error("调用接口保存收保地址信息出错,错误原因:{}", result.getMessage());
            throw new BizException("调用接口保存收保地址信息失败!");
        }

    }

    @Override
    public void deleteFormReceiveAddress(Integer addressId, Integer modifier) {
        log.info("调用接口删除收保地址信息. 入参:{},{}", addressId, modifier);
        if (addressId == null) {
            throw new BizException("调用接口删除收保地址信息参数异常!");
        }
        Result<Void> result = rpcFormReceiveAddrService.deleteFormReceiveAddr(addressId, modifier);
        if (!result.isSuccess()) {
            log.error("调用接口删除收保地址信息出错,错误原因:{}", result.getMessage());
            throw new BizException("调用接口删除收保地址信息出错");
        }

    }

    @Override
    public void updateFormReceiveAddress(InsuranceFormReceiveAddressVo addressVo) {
        log.info("调用接口更新收保地址信息. 入参:{},", addressVo);
        if (addressVo == null) {
            throw new BizException("调用接口更新收保地址信息参数异常");
        }
        InsuranceFormReceiveAddrDTO addrDTO = BdUtil.bo2do(addressVo, InsuranceFormReceiveAddrDTO.class);
        Result<Void> result = rpcFormReceiveAddrService.updateFormReceiveAddr(addrDTO);
        if (!result.isSuccess()) {
            log.error("调用接口更新收保地址信息出错,错误原因:{}", result.getMessage());
            throw new BizException("调用接口更新收保地址信息出错");
        }
    }

    @Override
    public List<InsuranceFormReceiveAddressVo> getFormReceiveAddressList(Integer agentId) {
        log.info("调用接口获取收保地址列表信息. 入参:{},", agentId);
        if (agentId == null) {
            throw new BizException("调用接口获取收保地址列表信息参数异常");
        }
        Result<List<InsuranceFormReceiveAddrDTO>> result = rpcFormReceiveAddrService.getFormReceiveAddrList(agentId);
        if (!result.isSuccess()) {
            log.error("调用接口获取收保地址信息列表出错,错误原因:{}", result.getMessage());
            throw new BizException("调用接口获取收保地址信息列表出错");
        }
        List<InsuranceFormReceiveAddressVo> list = BdUtil.bo2do4List(result.getData(), InsuranceFormReceiveAddressVo.class);
        return list;
    }

    @Override
    public InsuranceFormReceiveAddressVo getFormReceiveAddressById(Integer addressId) {
        log.info("调用接口获取收保地址信息. 入参:{},", addressId);
        if (addressId == null) {
            throw new BizException("调用接口获取收保地址信息参数异常");
        }
        Result<InsuranceFormReceiveAddrDTO> result = rpcFormReceiveAddrService.getFormReceiveAddressById(addressId);
        if (!result.isSuccess()) {
            log.error("调用接口获取收保地址信息列表出错,错误原因:{}", result.getMessage());
            throw new BizException("调用接口获取收保地址信息出错");
        }
        InsuranceFormReceiveAddressVo addressVo = BdUtil.bo2do(result.getData(), InsuranceFormReceiveAddressVo.class);

        return addressVo;
    }
}
