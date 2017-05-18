package com.tqmall.legend.entity.customer;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.component.excel.annotation.ExcelRowNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by litan on 14-11-3.
 */

@EqualsAndHashCode(callSuper = false)
@Data
@Excel
public class CustomerCar extends BaseEntity {
    @ExcelRowNumber
    private Integer rowNumber;
    @ExcelCol(value = 0,title = "*车牌")
    @NotBlank(message = "车牌不能为空")
    @Length(min = 3, max = 10,message = "车牌长度必须在3和10之间")
    private String license;
    @ExcelCol(value = 1,title = "车主(允许为公司名称)")
    @Length(max = 50,message = "车主长度不能超过50")
    private String customerName;
    @ExcelCol(value = 2,title = "车主电话")
    @Length(max = 20,message = "车主电话长度不能超过20")
    @Pattern(regexp = "^((\\s+)||(1[3|4|5|7|8][0-9]\\d{8}))$",message = "车主电话填写不正确")
    private String mobile;

    private Long carId;
    @ExcelCol(value = 3,title = "驾驶证号")
    @Length(max = 50,message = "驾驶证号长度不能超过50")
    private String drivingLicense;
    @ExcelCol(value = 4,title = "客户地址")
    @Length(max = 50,message = "客户地址长度不能超过50")
    private String customerAddr;
    @ExcelCol(value = 5,title = "客户生日(1900/01/01)")
    private Date birthday;
    @ExcelCol(value = 6,title = "客户单位")
    @Length(max = 100,message = "客户单位长度不能超过100")
    private String company;
    @ExcelCol(value = 7,title = "身份证")
    @Length(max = 20,message = "身份证长度不能超过20")
    private String identityCard;
    @ExcelCol(value = 8,title = "驾驶证年检日期(1900/01/01)")
    private Date licenseCheckDate;
    @ExcelCol(value = 9,title = "驾驶证领证日期(1900/01/01)")
    private Date licenseGetDate;
    @ExcelCol(value = 10,title = "联系人(驾驶员)")
    @Length(max = 50,message = "联系人长度不能超过50")
    private String contact;
    @ExcelCol(value = 11,title = "客户电话")
    @Length(max = 20,message = "客户电话长度不能超过20")
    @Pattern(regexp = "^((\\s+)||(1[3|4|5|7|8][0-9]\\d{8}))$",message = "客户电话填写不正确")
    private String contactMobile;
    @ExcelCol(value = 12,title = "备注")
    @Length(max = 500,message = "备注长度不能超过500")
    private String remark;//customer remark

    @ExcelCol(value = 13,title = "VIN码")
    @Length(max = 17,message = "VIN码长度不能超过17")
    private String vin;//vin码
    @ExcelCol(value = 14,title = "发动机号")
    @Length(max = 45,message = "发动机号长度不能超过45")
    private String engineNo;//发动机号
    @ExcelCol(value = 15,title = "车品牌")
    @Length(max = 50,message = "车品牌长度不能超过50")
    private String carBrand;//车品牌

    private Long carSeriesId;

    @ExcelCol(value = 16,title = "车系列")
    @Length(max = 50,message = "车系列长度不能超过50")
    private String carSeries;//车系列
    @ExcelCol(value = 17,title = "车别名")
    @Length(max = 20,message = "车别名长度不能超过20")
    private String byName;
    @ExcelCol(value = 18,title = "厂家")
    @Length(max = 50,message = "厂家长度不能超过50")
    private String carCompany;//厂家
    @ExcelCol(value = 19,title = "保险时间(1900/01/01)")
    private Date insuranceTime;//保险时间
    @ExcelCol(value = 20,title = "购车时间(1900/01/01)")
    private Date buyTime;//购车时间
    @ExcelCol(value = 21,title = "年审日期(1900/01/01)")
    private Date auditingTime;//年审日期
    @ExcelCol(value = 22,title = "保险公司")
    @Length(max = 50,message = "保险公司名长度不能超过50")
    private String insuranceCompany;//保险公司名
    private String insuranceName;//保险名
    private Long insuranceId;
    @ExcelCol(value = 23,title = "车身颜色")
    @Length(max = 32,message = "车身颜色长度不能超过32")
    private String color;//车身颜色
    @ExcelCol(value = 24,title = "车牌类型(本地/外地)")
    @Length(max = 20,message = "车牌类型长度不能超过20")
    private String licenseType;//车牌类型，本地、外地
    @ExcelCol(value = 25,title = "行驶里程/公里")
    @Min(value = 0,message = "行驶里程不能为负数")
    @Max(value = 2147483647,message = "行驶里程超出范围")
    private Long mileage;//最后纪录行驶里程

    @ExcelCol(value = 26,title = "固定电话(区号-号码)")
    @Length(max = 20,message = "固定电话长度不能超过20")
    private String tel;//固定电话

    private Long shopId;

    private Long customerId;

    private Integer precheckCount;

    private Date latestPrecheck;

    private Integer maintainCount;

    private Date latestMaintain;

    private Integer repairCount;

    private Date latestRepair;

    private Integer appointCout;

    private Date latestAppoint;

    private Long carBrandId;

    //新增车辆类型
    private String carType;
    //新增车辆等级
    private String carLevel;

    /**
     * 车型信息
     */
    private Long carModelId;
    private String carModel;

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

    /**
     * 版本号
     */
    private String ver;
    /**
     * 数据来源 0:web,1:android,2:ios
     */
    private String refer;
    /**
     * 车辆图片
     */
    private String carPictureUrl;

    private String importInfo;

    private Long haodeUserId;


    private Date remindInsuranceTime;


    private String carNumber;


    private Date remindAuditingTime;


    private Long visitCount;

    private BigDecimal expenseAmount;

    private Date productionDate;

    private String productionDateStr;

    private String driver;

    private String driverMobile1;

    private String driverMobile2;

    private Date latestPaied;

    private Date receiveLicenseTime;

    private Boolean hasPayable;


    private String customerSource;

    private String customerOldName;

    private String customerOldMobile;

    private String oldContact;

    private String oldContactMobile;

    private String birthdayStr;

    private String insuranceTimeStr;

    private String buyTimeStr;

    private String auditingTimeStr;

    private String receiveLicenseTimeStr;

    private Long appointId;

    private Date keepupTime;

    private Date remindKeepupTime;

    private String keepupTimeStr;

    private String carInfo;//车辆信息

    /**
     * 下次保养里程
     */
    private String upkeepMileage;

    public String getBuyTimeStr() {
        if (buyTimeStr != null) {
            return buyTimeStr;
        } else {
            return DateUtil.convertDateToYMD(buyTime);
        }
    }

    public String getLatestPrecheckStr() {
        return DateUtil.convertDate(latestPrecheck);
    }

    public String getLatestRepairStr() {
        return DateUtil.convertDate(latestRepair);
    }

    public String getLatestMaintainStr() {
        return DateUtil.convertDate(latestMaintain);
    }

    public String getLatestAppointStr() {
        return DateUtil.convertDate(latestAppoint);
    }

    public String getBirthdayStr() {
        if (birthdayStr != null) {
            return birthdayStr;
        } else {
            return DateUtil.convertDateToYMD(birthday);
        }
    }

    public String getAuditingTimeStr() {
        if (auditingTimeStr != null) {
            return auditingTimeStr;
        } else {
            return DateUtil.convertDateToYMD(auditingTime);
        }
    }

    public String getReceiveLicenseTimeStr() {
        if (receiveLicenseTimeStr != null) {
            return receiveLicenseTimeStr;
        } else {
            return DateUtil.convertDateToYMD(receiveLicenseTime);
        }
    }

    public String getInsuranceTimeStr() {
        if (insuranceTimeStr != null) {
            return insuranceTimeStr;
        } else {
            return DateUtil.convertDateToYMD(insuranceTime);
        }
    }

    public String getProductionDateStr() {
        if (productionDateStr != null) {
            return productionDateStr;
        } else {
            return DateUtil.convertDateToYM(productionDate);
        }
    }

    public String getKeepupTimeStr() {
        if (keepupTimeStr != null) {
            return keepupTimeStr;
        } else {
            return DateUtil.convertDateToYMD(keepupTime);
        }
    }

    public String getCarInfo() {
        StringBuffer sb = new StringBuffer();
        if (getCarBrand() != null) {
            sb.append(getCarBrand());
        }
        if (StringUtils.isNotBlank(getImportInfo())) {
            sb.append('(').append(getImportInfo()).append(')');
        }
        if (StringUtils.isNotBlank(getCarModel())) {
            sb.append(' ').append(getCarModel());
        } else if (StringUtils.isNotBlank(getCarSeries())) {
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }

}
