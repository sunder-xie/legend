package com.tqmall.legend.object.param.order.speedily;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 2017/2/9.
 */
@Getter
@Setter
public class OrderInfoParam implements Serializable{
    private static final long serialVersionUID = -492374367867177462L;

    private Long id;//工单id
    private Long appointId;//预约的id
    private String carLicense;//车牌
    private Long carSeriesId;
    private Long carBrandId;
    private String carBrand;
    private String carSeries;
    private Long carModelsId;
    private String carModels;
    private Long carPowerId;
    private String carPower;
    private Long carYearId;
    private String carYear;
    private Long carGearBoxId;
    private String carGearBox;
    private String carCompany;
    private String importInfo;
    private String contactName;
    private String contactMobile;
    private BigDecimal downPayment;
}
