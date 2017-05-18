package com.tqmall.legend.object.param.customer;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.Date;

/**
 * Created by zsy on 16/1/5.
 * 创建客户传递对象
 */
@Data
public class CustomerCarParam extends BaseRpcParam {
    private static final long serialVersionUID = -6101339486579654673L;
    private String license;         //车牌号
    private Long shopId;            //门店id
    private Long carBrandId;        //车品牌ID
    private String carBrand;        //车品牌名称
    private Long carSeriesId;       //车系列ID
    private String carSeries;       //车系列名称
    private Long carModelId;        //车型ID
    private String carModel;        //车型名称
    private String byName;          //车型参数/别名
    private String customerName;    //车主
    private String mobile;          //车主电话
    private String contact;         //联系人
    private String contactMobile;   //联系人电话
    private String vin;             //vin码
    private String importInfo;//车辆进出口信息
    private Long mileage;        //行驶里程
    private Date keepupTime;        //下次保养日期
    private String upkeepMileage;   //下次保养里程
    private Date buyTime;           //购车日期
    private Long insuranceId;//保险公司名称id
    private String insuranceCompany;//保险公司名称
    private Date insuranceTime;//保险到期时间
    private String ver;             //版本号
    private String refer;           //来源：0:web,1:android,2:ios
    /**
     * 排量
     */
    private Long carPowerId;
    private String carPower;

    /**
     * 年款
     */
    private Long carYearId;
    private String carYear;

    /**
     * 变速箱
     */
    private Long carGearBoxId;
    private String carGearBox;

}