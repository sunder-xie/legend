package com.tqmall.legend.biz.customer.bo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.enums.customer.CustomerFeedbackNoteTypeEnum;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

import java.util.Date;

/**
 * Created by xin on 16/9/26.
 */
@com.tqmall.wheel.component.excel.annotation.Excel
public class CustomerFeedbackBO {
    private Long orderId;
    @ExcelCol(value = 1, title = "车牌", width = 12)
    private String carLicense;
    @ExcelCol(value = 5, title = "回访记录", width = 16)
    private String customerFeedback;
    @ExcelCol(value = 4, title = "操作人", width = 12)
    private String visitorName;
    @ExcelCol(value = 6, title = "提醒方式", width = 12)
    private String visitMethod;
    private Date visitTime;
    private Integer noteType;
    // 工单服务
    @ExcelCol(value = 3, title = "最近服务项目", width = 20)
    private String orderServiceNames;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public String getCustomerFeedback() {
        return customerFeedback;
    }

    public void setCustomerFeedback(String customerFeedback) {
        this.customerFeedback = customerFeedback;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitMethod() {
        return visitMethod;
    }

    public void setVisitMethod(String visitMethod) {
        this.visitMethod = visitMethod;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Integer getNoteType() {
        return noteType;
    }

    public void setNoteType(Integer noteType) {
        this.noteType = noteType;
    }

    public String getOrderServiceNames() {
        return orderServiceNames;
    }

    public void setOrderServiceNames(String orderServiceNames) {
        this.orderServiceNames = orderServiceNames;
    }

    @ExcelCol(value = 2, title = "提醒类型")
    public String getNoteTypeStr() {
        return CustomerFeedbackNoteTypeEnum.getMesByCode(noteType);
    }

    @ExcelCol(value = 0, title = "处理时间", width = 20)
    public String getVisitTimeStr() {
        return DateUtil.convertDateToYMDHMS(visitTime);
    }
}
