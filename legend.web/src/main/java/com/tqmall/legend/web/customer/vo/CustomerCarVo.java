package com.tqmall.legend.web.customer.vo;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Mokala on 8/27/15.
 */
@Data
public class CustomerCarVo {
    private Long id;
    private Long shopId;
    private Date gmtCreate;
    private Date gmtModified;
    private String license;
    private String vin;
    private String engineNo;
    private Long customerId;
    private String carBrand;
    private String carSeries;
    private String carModel;
    private String importInfo;
    private Date remindAuditingTime;
    private Date remindInsuranceTime;
    private Date remindKeepupTime;
    private Long carBrandId;
    private Long carSeriesId;
    private Long carModelId;
    private String carCompany;
    private Integer precheckCount;
    private Date latestPrecheck;
    private Integer maintainCount;

    private  Date latestMaintain;
    private Integer repairCount;
    private Integer appointCout;
    private Date latestRepair;
    private Date auditingTime;
    private Date insuranceTime;
    private Date keepupTime;
    private Date buyTime;
    private String customerName;
    private String mobile;
    private Long primaryKey;
    private String contact;
    private String contactMobile;
    private String company;
    private Date latestPaied;
    private BigDecimal expenseAmount;
    private String latestPaiedStr;


    private String gmtCreateStr;


    public String getRemindAuditingTimeStr(){
        return getRemindAuditingTime() == null?null: DateUtil.convertDateToYMD(getRemindAuditingTime());
    }

    public String getRemindInsuranceTimeStr(){
        return getRemindInsuranceTime()==null?null:DateUtil.convertDateToYMD(getRemindInsuranceTime());
    }

    public String getLatestPrecheckStr(){
        return getLatestPrecheck() == null? null:DateUtil.convertDateToYMD(getLatestPrecheck());
    }

    public String getLatestRepairStr(){
        return getLatestRepair() == null ? null:DateUtil.convertDateToYMD(getLatestRepair());
    }

    public String getLatestMaintainStr(){
        return getLatestMaintain() == null? null:DateUtil.convertDateToYMD(getLatestMaintain());
    }

    public String getAuditingTimeStr(){
        return getAuditingTime() == null?null:DateUtil.convertDateToYMD(getAuditingTime());
    }
    public String getInsuranceTimeStr(){
        return getInsuranceTime() == null?null:DateUtil.convertDateToYMD(getInsuranceTime());
    }

    public String getBuyTimeStr(){
        return getBuyTime() == null?null:DateUtil.convertDateToYMD(getBuyTime());
    }

    public String getKeepupTimeStr(){
        return getKeepupTime() == null? null:DateUtil.convertDateToYMD(getKeepupTime());
    }

    public String getLatestPaiedStr(){
        return getLatestPaied() == null?null:DateUtil.convertDateToYMD(getLatestPaied());
    }

    public String getGmtCreateStr(){
        return getGmtCreate() == null? null:DateUtil.convertDateToYMD(getGmtCreate());
    }

    public String getCarInfo(){
        StringBuffer sb = new StringBuffer();
        if(getCarBrand() != null){
            sb.append(getCarBrand());
        }
        if(StringUtils.isNotBlank(getImportInfo())){
            sb.append('(').append(getImportInfo()).append(')');
        }
        if(StringUtils.isNotBlank(getCarModel())){
            sb.append(' ').append(getCarModel());
        } else if(StringUtils.isNotBlank(getCarSeries())){
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }
}
