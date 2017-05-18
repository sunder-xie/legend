package com.tqmall.legend.entity.shop;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class Gift extends BaseEntity {

    private String giftSn;
    private Long customerCarId;
    private String giftNote;
    private Long registrantId;
    private String registrantName;
    private Long shopId;
    private String giftContent;//礼品内容

    private String license;
    private String carModel;
    private String carAlias;
    private String customerName;
    private String mobile;

    private String gmtCreateStr;

    public String getGmtCreateStr() {
        return DateUtil.convertDateToYMDHHmm(gmtCreate);
    }



}

