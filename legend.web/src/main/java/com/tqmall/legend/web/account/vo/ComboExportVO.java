package com.tqmall.legend.web.account.vo;

import java.util.Date;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by majian on 17/1/5.
 */
@Excel
public class ComboExportVO {
    private Long customerId;
    @ExcelCol(value = 0, title = "车主")
    private String customerName;
    @ExcelCol(value = 1, title = "车主电话", width = 12)
    private String mobile;
    @ExcelCol(value = 2, title = "车牌号", width = 12)
    private String licenses;
    @ExcelCol(value = 3, title = "计次卡类型", width = 10)
    private String comboInfo;
    @ExcelCol(value = 4, title = "计次卡服务项目", width = 14)
    private String comboService;
    @ExcelCol(value = 5, title = "剩余次数")
    private Integer remainNumber;//剩余次数
    @ExcelCol(value = 6, title = "过期时间", width = 12)
    private Date expireDate;
    @ExcelCol(value = 7, title = "办卡时间", width = 12)
    private Date createDate;

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getComboInfo() {
        return comboInfo;
    }

    public void setComboInfo(String comboInfo) {
        this.comboInfo = comboInfo;
    }

    public String getComboService() {
        return comboService;
    }

    public void setComboService(String comboService) {
        this.comboService = comboService;
    }

    public Integer getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(Integer remainNumber) {
        this.remainNumber = remainNumber;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
