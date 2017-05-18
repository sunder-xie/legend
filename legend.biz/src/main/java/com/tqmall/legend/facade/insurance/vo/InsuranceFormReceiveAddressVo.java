package com.tqmall.legend.facade.insurance.vo;

import lombok.Data;

/**
 * Created by zhouheng on 17/4/26.
 */
@Data
public class InsuranceFormReceiveAddressVo {

    /**主键ID**/
    private Integer id;

    /**创建人ID**/
    private Integer creator;

    /**更新人ID**/
    private Integer modifier;

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**更新时间**/
    private java.util.Date gmtModified;

    /**卖保险代理人id**/
    private Integer agentId;

    /**卖保险代理人名称**/
    private String agentName;

    /**收保单人姓名**/
    private String receiverName;

    /**收保单人手机号**/
    private String receiverPhone;

    /**收保单人所在省名称**/
    private String receiverProvince;

    /**收保单人所在省编码**/
    private String receiverProvinceCode;

    /**收保单人所在市名称**/
    private String receiverCity;

    /**收保单人所在市编码**/
    private String receiverCityCode;

    /**收保单人所在地区（县）名称**/
    private String receiverArea;

    /**收保单人所地区（县）编码**/
    private String receiverAreaCode;

    /**收保单人所在详细地址**/
    private String receiverAddr;

    /**收保单人电子邮箱**/
    private String receiverEmail;

    /**是否设置为默认地址 1:默认 0:不默认**/
    private Integer isDefaultAddress;

}
