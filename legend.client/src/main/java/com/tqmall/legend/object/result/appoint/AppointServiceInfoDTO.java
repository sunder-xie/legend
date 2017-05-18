package com.tqmall.legend.object.result.appoint;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by wushuai on 17/2/13.
 */
@Getter
@Setter
public class AppointServiceInfoDTO implements Serializable{
    private static final long serialVersionUID = 2075010169308500756L;

    private Long appointId;
    private String appointSn;
    private Long serviceId;
    private Long parentServiceId;//服务父ID
    private String serviceNote;
    private String serviceName;
    private String categoryName;//服务类别
    private BigDecimal servicePrice;//工时费(服务原价)
    private BigDecimal discountAmount;//服务优惠金额
    private Long templateId;//服务模板id
    private Long suiteNum;//0对应基本服务,1带物料服务,2.套餐服务
}
