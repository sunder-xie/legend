package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderInvoiceLog extends BaseEntity {

    private Long shopId;
    private Long orderId;
    private String orderSn;
    private Long invoiceType;
    private String invoiceSn;
    private String company;
    private BigDecimal price;
    private String postscript;
    private String operatorName;

    public String getPostscript(){
        return this.postscript ==null ? "":this.postscript;
    }

    public void setPostscript(String postscript){
        this.postscript = (postscript==null) ? "":postscript;
    }

}

