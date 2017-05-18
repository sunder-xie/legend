package com.tqmall.legend.facade.customer.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mokala on 8/27/15.
 */
@Data
@Excel
public class CustomerCarVo {
    private String id;
    @ExcelCol(value = 0, title = "车牌", width = 10)
    private String license;//prefix
    private String customerId;
    private String carBrand;//split
    private String carSeries;//split
    private String importInfo;//split
    private String remindAuditingTime;//range []
    private String remindInsuranceTime;//range []
    private String shopId;
    @ExcelCol(value = 11, title = "VIN码", width = 20)
    private String vin;//prefix
    private String gmtModified;
    private String gmtCreate;
    @ExcelCol(value = 13, title = "发动机号", width = 20)
    private String engineNo;
    private String carBrandId;
    private String carSeriesId;
    private String carCompany;
    private String precheckCount;
    private String latestPrecheck;
    private String maintainCount;
    private String latestMaintain;
    private String repairCount;
    private String appointCout;
    private String latestRepair;
    @ExcelCol(value = 9, title = "年审到期时间", width = 20)
    private String auditingTime;
    @ExcelCol(value = 7, title = "保险到期时间", width = 20)
    private String insuranceTime;
    private String buyTime;
    @ExcelCol(value = 1, title = "车主", width = 12)
    private String customerName;
    @ExcelCol(value = 2, title = "车主电话", width = 12)
    private String mobile;
    private String remindKeepupTime;
    @ExcelCol(value =8, title = "下次保养时间", width = 20)
    private String keepupTime;
    private String contact;//customer.contact
    private String contactMobile;//customer.contact_mobile
    private Integer carModelId;
    private String carModel;
    private Integer carPowerId;
    private String carPower;
    private Integer carYearId;
    private String carYear;
    private Double expenseAmount;
    private String latestPaied;
    @ExcelCol(value = 10, title = "客户单位", width = 16)
    private String company;
    @ExcelCol(value = 12, title = "承保公司",width = 16)
    private String insuranceCompany;
    @ExcelCol(value = 5, title = "行驶里程", width = 16)
    private Integer mileage;//最后纪录行驶里程
    @ExcelCol(value = 6, title = "下次行驶里程", width = 16)
    private String upkeepMileage;//下次保养行驶里程
    private String carGearBox;

    //保险到期日期，yyyy-mm-dd 格式
    private String insuranceTimeYMDStr;
    //下次保养日期，yyyy-mm-dd 格式
    private String keepupTimeYMDStr;

    public String getInsuranceTimeYMDStr(){
        if(!StringUtils.isBlank(insuranceTime)){
            return DateUtil.convertDateToYMD(DateUtil.convertStringToDate(insuranceTime));
        }
        return "";
    }

    public String getKeepupTimeYMDStr(){
        if(!StringUtils.isBlank(keepupTime)){
            return DateUtil.convertDateToYMD(DateUtil.convertStringToDate(keepupTime));
        }
        return "";
    }



    public String getGmtModified() {
        return gmtModified == null ? gmtCreate : gmtModified;
    }

    public String getRemindAuditingTimeStr() {
        return this.getRemindAuditingTime();
    }

    public String getRemindInsuranceTimeStr() {
        return this.remindInsuranceTime;
    }

    public String getLatestPrecheckStr() {
        return this.latestPrecheck;
    }

    public String getLatestRepairStr() {
        return this.latestRepair;
    }

    public String getLatestMaintainStr() {
        return this.latestMaintain;
    }

    public String getAuditingTimeStr() {
        return this.auditingTime;
    }

    public String getInsuranceTimeStr() {
        return this.insuranceTime;
    }

    public String getBuyTimeStr() {
        return this.buyTime;
    }

    public String getKeepupTimeStr() {
        return this.keepupTime;
    }

    public String getLatestPaiedStr() {
        return this.latestPaied;
    }

    public String getGmtCreateStr() {
        return this.gmtCreate;
    }

    @ExcelCol(value = 3, title = "车型", width = 26)
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

    @ExcelCol(value = 4, title = "年款排量", width = 26)
    public String getCarYearPower(){
        StringBuilder sb = new StringBuilder(50);
        if (StringUtils.isNotBlank(carYear) ){
            sb.append(carYear).append(" ");
        }
        if (StringUtils.isNotBlank(carGearBox)){
            sb.append(carGearBox);
        }
        return sb.toString();
    }
}
