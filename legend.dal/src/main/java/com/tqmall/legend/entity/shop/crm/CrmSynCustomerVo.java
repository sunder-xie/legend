package com.tqmall.legend.entity.shop.crm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jason on 15/8/5.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CrmSynCustomerVo {

    CrmCustomerVo crmCustomerVo; //客户信息对象
}