package com.tqmall.legend.object.result.customer;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zsy on 16/7/7.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerCarDTO extends BaseEntityDTO {
    private static final long serialVersionUID = -4308386479282802613L;

    private Long id;//车辆id
    private String license;//车牌号
    private String vin;//vin码
    private String engineNo;//发动机号
    private Long shopId;//门店id
    private Long customerId;//客户id
    private Long precheckCount;//预检次数
    private Date latestPrecheck;//最近一次预检时间
    private Long maintainCount;//保养次数（废弃，都算到维修里）
    private Date latestMaintain;//最近一次保养时间（废弃，都算到维修里）
    private Long repairCount;//维修次数
    private Date latestRepair;//最近一次维修时间
    private Long appointCout;//预约次数
    private Date latestAppoint;//最近一次预约时间
    private Long carBrandId;//车品牌id
    private String carBrand;//车品牌
    private Long carSeriesId;//车系列id
    private String carSeries;//车系列
    private String carCompany;//厂家
    private String importInfo;//进出口
    private Long mileage;//最后纪录行驶里程
    private Long haodeUserId;//${item.comment}
    private String byName;//车辆别名
    private Date insuranceTime;//保险时间
    private Date buyTime;//购车时间
    private String carNumber;//行驶证车辆型号
    private Date auditingTime;//年审日期
    private Long insuranceId;//保险公司id
    private Long visitCount;//来电次数
    private BigDecimal expenseAmount;//消费总金额
    private Date productionDate;//出厂日期
    private String driver;//驾驶员
    private String driverMobile1;//驾驶员电话1
    private String driverMobile2;//驾驶员电话2
    private Date latestPaied;//上一次结算时间
    private Date receiveLicenseTime;//领证日期
    private String insuranceCompany;//保险公司名称
    private String color;//车身颜色
    private String licenseType;//车牌类型，本地、外地
    private Date remindAuditingTime;//提醒年审日期
    private Date remindInsuranceTime;//提醒保险日期
    private String note;//备注，用于处理excel导入不同门店的特有但却重要的数据
    private Date keepupTime;//保养日期
    private Date remindKeepupTime;//提醒保养日期
    private String ver;//版本号
    private String refer;//来源：0:web,1:android,2:ios
    private Long carModelId;//车型id
    private String carModel;//车型
    private Long carPowerId;//排量id
    private String carPower;//排量
    private Long carYearId;//年款id
    private String carYear;//年款
    private Long carGearBoxId;//变速箱id
    private String carGearBox;//变速箱
    private String carLevel;//车辆级别
    private String carType;//车辆类型
    private String upkeepMileage;//下次保养里程
}


