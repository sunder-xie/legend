package com.tqmall.legend.object.result.customer;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dingbao on 16/9/22.
 */
@Data
public class ApiCustomerCarVoDTO implements Serializable {
    /**
     * 车辆id
     */
    private Long carId = Long.valueOf("0");
    /**
     * 客户id
     */
    private Long customerId = Long.valueOf("0");
    /**
     * 车牌信息
     */
    private String license = "";

    /**
     * 车型信息,已经组装好,格式为:品牌 车型 年款 批量
     */
    private String carInfo = "";

    /**
     * 车主信息
     */
    private String customerName = "";
    /**
     * 车主电话
     */
    private String mobile = "" ;
    /**
     * 到店次数(包括有效+无效工单数)
     */
    private Integer totalOrderCount = Integer.valueOf("0");
    /**
     * 维修次数(有效工单数)
     */
    private Integer validOrderCount = Integer.valueOf("0");
    /**
     * 挂帐工单数
     */
    private Integer suspendPaymentCount = Integer.valueOf("0");

    /**
     * 最近六个月消费金额
     */
    private BigDecimal recent6MonthAmount = BigDecimal.ZERO;
    /**
     * 挂帐金额
     */
    private BigDecimal suspendAmount = BigDecimal.ZERO;

    /**
     * 车辆图片信息
     */
    private List<ApiCarImgVoDTO> carImgList;
    /**
     * 车型信息
     */
    private Long carModelId = Long.valueOf("0");
    /**
     * 车系
     */
    private Long carSeriesId = Long.valueOf("0");
    /**
     * 行驶里程
     */
    private Long mileage = Long.valueOf("0");
    //最近一次到店时间
    private Date lastOrderDate ;

    private String lastOrderDateStr ;
    /**
     * 品牌id
     */
    private Long carBrandId = Long.valueOf("0");

    private Long carYearId = Long.valueOf("0");
    private Long carGearBoxId = Long.valueOf("0");

    /**
     * 品牌
     */
    private String carBrand;
    /**
     * 车型信息
     */
    private String carModel;
    /**
     * 车系信息
     */
    private String carSeries;
    /**
     * 年份
     */
    private String carYear;
    /**
     * 变速箱
     */
    private String carGearBox;


    /**
     * 排量id
     */
    private Long carPowerId = Long.valueOf("0");
    /**
     * 排量
     */
    private String carPower;
    /**
     * 厂家
     */
    private String carCompany;
    /**
     * 进出口
     */
    private String importInfo;



    public String getLastOrderDateStr(){
        if(lastOrderDateStr != null){
            return lastOrderDateStr;
        }
        if (lastOrderDate == null){
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(lastOrderDate);
    }
}
