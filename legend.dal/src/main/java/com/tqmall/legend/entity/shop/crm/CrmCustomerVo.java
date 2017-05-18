package com.tqmall.legend.entity.shop.crm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jason on 15/8/5.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CrmCustomerVo {

    private String contactsMobile;       //联系人手机号码：C/B客户的手机号码
    private String contactsName;         //联系人名称：C/B客户的联系人名称
    private String companyName;          //B客户的公司名称
    private Long province;
    private Long city;
    private Long district;
    private Long street;
    private String address;
    private String salesMobile;          //B客户存在负责的销售人员手机号

    private Integer userType;            //客户类型，1表示B，2表示C
    private String zipcode;              //邮编
    private Integer certifyStatus;        //认证状态
    private Long customerId;             //客户ID，新增的时候必须传
    private String userGlobalId;         //全局同步ID
    private Long sellerId;               //商家ID
    private String appReg;               //创建客户的来源
}
