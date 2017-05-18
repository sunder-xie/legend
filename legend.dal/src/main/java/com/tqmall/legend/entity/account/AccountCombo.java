package com.tqmall.legend.entity.account;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.component.excel.annotation.ExcelRowNumber;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Excel
public class AccountCombo extends BaseEntity {

    public static final Integer EXHAUSTED = 1;
    public static final Integer NOT_EXHAUSTED = 0;

    @ExcelRowNumber
    private Integer rowNumber;
    @ExcelCol(value = 0,title = "车主电话(必填)")
    @NotBlank(message = "车主电话不能为空")
    @Length(max = 20,message = "车主电话长度不能超过20")
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{8}$",message = "车主电话填写不正确")
    private String mobile;
    @ExcelCol(value = 1,title = "计次卡名称(必填)")
    @NotBlank(message = "计次卡名称不能为空")
    private String comboName;//计次卡名称
    @ExcelCol(value = 2,title = "服务名称(必填)")
    @NotBlank(message = "服务名称不能为空")
    private String serviceName;//服务名称
    @ExcelCol(value = 3,title = "服务次数(必填)")
    @Min(value = 1,message = "服务次数不能为负数")
    @NotNull(message = "服务次数不能为空")
    private Integer serviceCount;//服务数量
    @ExcelCol(value = 4,title = "过期时间(必填)(2018/01/01)")
    @NotNull(message = "过期时间不能为空")
    private Date expireDate;//失效时间
    private String gmtCreateStr;
    private Long shopId;//门店id
    private Date effectiveDate;//生效时间
    private String expireDateStr;
    private String effectiveDateStr;//
    private Long receiver;//服务顾问
    private String receiverName;//服务顾问名字
    private Long accountId;//账户id
    private Integer comboStatus;//0:正常1:用完
    private Long comboInfoId;//计次卡类型id

    private String operatorName;//操作人
    private String customerName;

    private List<AccountComboServiceRel> serviceList;

    private Integer totalServiceCount;//服务项目总数量
    private Long effectivePeriodDays;//有效期(天)
    private ComboInfo comboInfo;

    public void setGmtCreateStr(String gmtCreateStr) {
        this.gmtCreateStr = gmtCreateStr;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setExpireDateStr(String expireDateStr) {
        this.expireDateStr = expireDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getComboStatus() {
        return comboStatus;
    }

    public void setComboStatus(Integer comboStatus) {
        this.comboStatus = comboStatus;
    }

    public Long getComboInfoId() {
        return comboInfoId;
    }

    public void setComboInfoId(Long comboInfoId) {
        this.comboInfoId = comboInfoId;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<AccountComboServiceRel> getServiceList() {
        if(serviceList == null) {
            serviceList = Lists.newArrayList();
        }
        return serviceList;
    }

    public void setServiceList(List<AccountComboServiceRel> serviceList) {
        this.serviceList = serviceList;
    }

    public Integer getTotalServiceCount() {
        return totalServiceCount;
    }

    public void setTotalServiceCount(Integer totalServiceCount) {
        this.totalServiceCount = totalServiceCount;
    }

    public Long getEffectivePeriodDays() {
        return effectivePeriodDays;
    }

    public void setEffectivePeriodDays(Long effectivePeriodDays) {
        this.effectivePeriodDays = effectivePeriodDays;
    }

    public ComboInfo getComboInfo() {
        return comboInfo;
    }

    public void setComboInfo(ComboInfo comboInfo) {
        this.comboInfo = comboInfo;
    }

    public boolean isExpired(){
        boolean flagA = isEffectivedCheck();
        boolean flagB = isExpireDateCheck();
        return flagA||flagB ;
    }

    public boolean isExpireDateCheck() {
        if (expireDate == null) {
            return false;
        }
        return expireDate.compareTo(new Date()) < 0;
    }

    public boolean isEffectivedCheck() {
        if(effectiveDate == null){
            return false;
        }
        return effectiveDate.compareTo(new Date()) >0;
    }

    public String getExpireDateStr() {
        if (expireDateStr != null) {
            return expireDateStr;
        }
        if (expireDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(expireDate);
        }
        return null;
    }

    public String getEffectiveDateStr() {
        if (effectiveDateStr != null) {
            return effectiveDateStr;
        }
        if (effectiveDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(effectiveDate);
        }
        return null;
    }

    public String getGmtCreateStr() {
        if (gmtCreateStr != null) {
            return gmtCreateStr;
        }
        if (gmtCreate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(gmtCreate);
        }
        return null;
    }
}

