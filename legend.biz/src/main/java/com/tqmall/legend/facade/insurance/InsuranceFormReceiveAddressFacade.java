package com.tqmall.legend.facade.insurance;

import com.tqmall.insurance.domain.result.address.InsuranceFormReceiveAddrDTO;
import com.tqmall.legend.facade.insurance.vo.InsuranceFormReceiveAddressVo;

import java.util.List;

/**
 * Created by zhouheng on 17/4/26.
 */
public interface InsuranceFormReceiveAddressFacade {

    /**
     * 新增地址保单接收地址信息
     *
     * @param addressVo
     * @return
     */
    void createFormReceiveAddress(InsuranceFormReceiveAddressVo addressVo);

    /**
     * 删除地址保单接收地址信息
     *
     * @param addressId
     * @return
     */
    void deleteFormReceiveAddress(Integer addressId, Integer modifier);

    /**
     * 更新地址保单接收地址信息
     *
     * @param addressVo
     * @return
     */
    void updateFormReceiveAddress(InsuranceFormReceiveAddressVo addressVo);

    /**
     * 获取地址保单接收地址信息列表
     *
     * @param agentId
     * @return
     */
    List<InsuranceFormReceiveAddressVo> getFormReceiveAddressList(Integer agentId);

    /**
     * 通过id获取收保单地址信息
     *
     * @param addressId
     * @return
     */
    InsuranceFormReceiveAddressVo getFormReceiveAddressById(Integer addressId);

}
