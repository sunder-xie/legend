package com.tqmall.legend.object.result.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by lifeilong on 2016/3/21.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerPrechecksDTO implements Serializable {
    private static final long serialVersionUID = -2147562654536272467L;

    //预检单(上次车况)
    private Long   id; //预检单id 或 淘汽检测id
    private String gmtCreateStr; //创建时间
    private String checksName;// 车况登记 / 淘汽检测xx项
    private Integer checksFlag; // 0：车况登记 , 1:淘汽检测xx项

    //淘汽检测记录
    private String carLicense;     //车牌号
    private String carInfo;     //车辆型号信息 (carBrandName+(importInfo)+ carModel+carSeriesName)
}
