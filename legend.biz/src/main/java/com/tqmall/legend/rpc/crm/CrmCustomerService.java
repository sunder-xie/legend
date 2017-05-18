package com.tqmall.legend.rpc.crm;

import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.Result;
import com.tqmall.holy.provider.entity.customer.CustomerJoinAuditLegendDTO;
import com.tqmall.holy.provider.param.CustomerJoinAuditLegendParam;
import com.tqmall.legend.entity.shop.CustomerJoinAudit;

/**
 * Created by lilige on 17/3/9.
 */
public interface CrmCustomerService {

//    selectSaMobilePhone(Long customerId);

    /**
     * crm:获取云修或者微信版档口门店基本信息
     * @param userGlobalId
     * @return
     */
    CustomerJoinAudit showShopInformation(Long userGlobalId) throws Exception;

    /**
     * crm:删除图片接口
     * @param id
     * @throws BizException
     */
    void deleteFilePath(Long id) throws Exception;

    /**
     * crm 更新门店基本信息
     * @param customerJoinAudit
     * @return
     * @throws Exception
     */
    void updateShopInformation(CustomerJoinAudit customerJoinAudit) throws Exception;


    /**
     * crm 根据userGlobalId 获取samobile
     * @param customerId
     * @return
     */
    String selectSaMobilePhone(Long customerId) throws Exception;

}
