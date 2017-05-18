package com.tqmall.legend.facade.supplier.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by sven on 2017/1/4.
 */
@Getter
@Setter
public class SupplierBo {
    private String supplierName; //供应商名称
    private Long supplierId;   //供应商id
    private Long reqSupplierId;  //被更新的供应商id
    private Long shopId;  //门店id
    private Long operator;
}
