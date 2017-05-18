package com.tqmall.legend.object.result.supplier;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 16/11/22.
 */
@Getter
@Setter
public class SupplierDTO implements Serializable {
    private static final long serialVersionUID = -2189446155000000856L;

    private Long id;//供应商id
    private Long shopId;//门店id
    private String supplierName;//供应商名称
    private Integer category;//供应商分类：1.一级供应商'
    private String supplierSn;//供应商编号
    private Integer payMethod;//结算方式：1月结，2季结
    private String payMode;//结算方式名称
    private String contact;//联系人
    private String mobile;//联系人手机号
    private String tel;//固定电话
    private String address;//单位固定电话
    private Integer invoiceType;//发票类型：1普通发票2增值税发票
    private String companyName;//单位名称（开发票抬头）
    private String invoiceNo;//单位税号
    private String openingBank;//开户银行
    private String bankAccount;//银行账号
    private String content;//供应商主营业务
}
