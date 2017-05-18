package com.tqmall.legend.rpc.crm.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.holy.provider.entity.customer.CustomerFilePathDTO;
import com.tqmall.holy.provider.entity.customer.CustomerJoinAuditLegendDTO;
import com.tqmall.holy.provider.param.CustomerJoinAuditLegendParam;
import com.tqmall.holy.provider.service.RpcCustomerService;
import com.tqmall.legend.entity.shop.CustomerFilePath;
import com.tqmall.legend.entity.shop.CustomerJoinAudit;
import com.tqmall.legend.rpc.crm.CrmCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilige on 17/3/9.
 */
@Slf4j
@Service
public class CrmCustomerServiceImpl implements CrmCustomerService {

    @Autowired
    private RpcCustomerService rpcCustomerService;

    /**
     * crm:获取云修或者微信版档口门店基本信息
     *
     * @param userGlobalId
     * @return
     */
    @Override
    public CustomerJoinAudit showShopInformation(Long userGlobalId) throws BizException {
        log.info("[从crm获取门店资料失败]参数 userGlobalId:{}",userGlobalId);
        Result<CustomerJoinAuditLegendDTO> crmResult = rpcCustomerService.showShopInformation(userGlobalId);
        if (null == crmResult || !crmResult.isSuccess()) {
            log.error("[从crm获取门店资料失败]crmResult:{} ,参数 userGlobalId:{}", LogUtils.objectToString(crmResult), userGlobalId);
            throw new BizException("从crm获取门店资料失败");
        }
        CustomerJoinAudit customerJoinAudit = new CustomerJoinAudit();
        CustomerJoinAuditLegendDTO dto = crmResult.getData();
        if (null != dto) {
            BeanUtils.copyProperties(dto, customerJoinAudit);
            //另外设置图片和坐标
            List<CustomerFilePathDTO> customerFilePathList = dto.getCustomerFilePathDTOList();
            if (CollectionUtils.isEmpty(customerFilePathList)){
                return customerJoinAudit;
            }
            List<CustomerFilePath> customerFilePaths = new ArrayList<>();
            for (CustomerFilePathDTO customerFilePath : customerFilePathList) {
                CustomerFilePath img = new CustomerFilePath();
                try {
                    BeanUtils.copyProperties(customerFilePath,img);
                    customerFilePaths.add(img);
                } catch (Exception e) {
                    log.error("[编辑app资料保存,类型转换错误]customerFilePath:{}", customerFilePath);
                    continue;
                }
            }
            customerJoinAudit.setCustomerFilePathList(customerFilePaths);
            return customerJoinAudit;
        }
        throw new BizException("门店资料不存在");
    }

    /**
     * crm:删除图片接口
     *
     * @param id
     * @throws BizException
     */
    @Override
    public void deleteFilePath(Long id) throws Exception {
        log.info("[调用CRM接口删除图片失败],图片id:{}", id);
        com.tqmall.core.common.entity.Result<Boolean> result = rpcCustomerService.deleteFilePath(id);
        if (null == result || !result.isSuccess()) {
            log.error("[调用CRM接口删除图片失败],图片id:{},原因:{}", id, LogUtils.objectToString(result));
            throw new BizException("[调用CRM接口删除图片失败]");
        }
    }

    /**
     * crm 更新门店基本信息
     *
     * @param customerJoinAudit
     * @return
     * @throws Exception
     */
    @Override
    public void updateShopInformation(CustomerJoinAudit customerJoinAudit) throws Exception {
        List<CustomerFilePath> customerFilePathList = customerJoinAudit.getCustomerFilePathList();
        List<CustomerFilePathDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customerFilePathList)) {
            int i = 0;
            for (CustomerFilePath customerFilePath : customerFilePathList) {
                customerFilePath.setCustomerId(customerJoinAudit.getCustomerId());
                customerFilePath.setOrderIdx(i);
                CustomerFilePathDTO dto = new CustomerFilePathDTO();
                try {
                    BeanUtils.copyProperties(customerFilePath,dto);
                    dtoList.add(dto);
                } catch (Exception e) {
                    log.error("[编辑app资料保存,类型转换错误]customerFilePath:{}", customerFilePath);
                    continue;
                } finally {
                    i++;
                }
            }
        }
        CustomerJoinAuditLegendParam param = new CustomerJoinAuditLegendParam();
        param.setCustomerFilePathDTOList(dtoList);
        try {
            BeanUtils.copyProperties(customerJoinAudit,param);
            param.setLatitude(new BigDecimal(customerJoinAudit.getLatitude()));
            param.setLongitude(new BigDecimal(customerJoinAudit.getLongitude()));
        } catch (Exception e) {
            log.error("[编辑app资料保存,类型转换错误]customerJoinAudit:{}", customerJoinAudit);
            throw new BizException("编辑app资料保存失败");
        }
        log.info("[编辑app资料更新]customerJoinAudit:{}", customerJoinAudit);
        com.tqmall.core.common.entity.Result<Boolean> crmResult = rpcCustomerService.updateShopInformation(param);
        if (null == crmResult || !crmResult.isSuccess()){
            log.error("[编辑app资料保存失败]crmResult:{}",LogUtils.objectToString(crmResult));
            throw new BizException("编辑app资料保存失败");
        }
    }

    /**
     * crm 根据userGlobalId 获取samobile
     *
     * @param customerId
     * @return
     */
    @Override
    public String selectSaMobilePhone(Long customerId) {
        log.info("[crm-dubbo获取接待人手机号]userGlobalId:{}",customerId);
        Result<String> crmResult = rpcCustomerService.selectSaMobilePhone(customerId);
        if (null == crmResult || !crmResult.isSuccess()){
            log.error("[crm-dubbo获取接待人手机号失败]:userGlobalId:{},crmResult:{}",customerId,crmResult);
            throw new BizException("获取手机号失败");
        }
        String saMobile = crmResult.getData();
        return saMobile;
    }


}
