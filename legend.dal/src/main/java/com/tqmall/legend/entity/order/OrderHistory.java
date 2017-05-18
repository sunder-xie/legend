package com.tqmall.legend.entity.order;

/**
 * Created by lilige on 15/11/16.
 */

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.component.excel.annotation.ExcelRowNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;


@EqualsAndHashCode(callSuper = false)
@Data
@Excel
public class OrderHistory extends BaseEntity {
    @ExcelRowNumber
    private Integer rowNumber;
    private Long shopId;
    @ExcelCol(value = 0,title = "*工单编号")
    @NotBlank(message = "工单编号不能为空")
    @Length(min = 1,max = 20,message = "工单编号长度不能超过20")
    private String orderSn;
    @ExcelCol(value = 1,title = "*车牌")
    @NotBlank(message = "车牌号不能为空")
    @Length(min = 1,max = 20,message = "车牌号长度必须在1和20之间")
    private String carLicense;
    @ExcelCol(value = 2,title = "开单时间(1900/01/01)")
    private Date orderCreateTime;
    @ExcelCol(value = 3,title = "接车人")
    @Length(max = 20,message = "接车人长度不能超过20")
    private String receiver;
    @ExcelCol(value = 4,title = "工单类型")
    @Length(max = 20,message = "工单类型长度不能超过20")
    private String orderType;
    @ExcelCol(value = 5,title = "工单状态")
    @Length(max = 10,message = "工单状态长度不能超过10")
    private String orderStatus;
    @ExcelCol(value = 6,title = "车辆型号")
    @Length(max = 50,message = "车辆型号长度不能超过50")
    private String carModel;

    private String mileage;
    @ExcelCol(value = 7,title = "里程数")
    @Min(value = 0,message = "里程数不能为负数")
    private Long carMileage;
    @ExcelCol(value = 8,title = "VIN码")
    @Length(max = 17,message = "VIN码长度不能超过17")
    private String vin;
    @ExcelCol(value = 9,title = "发动机号")
    @Length(max = 45,message = "发动机号长度不能超过45")
    private String engineNo;
    @ExcelCol(value = 10,title = "客户姓名")
    @Length(max = 50,message = "客户姓名长度不能超过50")
    private String customerName;
    @ExcelCol(value = 11,title = "客户手机")
    @Length(max = 20,message = "客户手机长度不能超过20")
    @Pattern(regexp = "^((\\s+)||(1[3|4|5|7|8][0-9]\\d{8}))$",message = "客户手机填写不正确")
    private String customerMobile;
    @ExcelCol(value = 12,title = "联系人姓名")
    @Length(max = 50,message = "联系人姓名长度不能超过50")
    private String contactName;
    @ExcelCol(value = 13,title = "联系人手机")
    @Length(max = 20,message = "联系人手机长度不能超过20")
    @Pattern(regexp = "^((\\s+)||(1[3|4|5|7|8][0-9]\\d{8}))$",message = "联系人手机填写不正确")
    private String contactMobile;
    @ExcelCol(value = 14,title = "服务项目")
    @Length(max = 500,message = "服务项目长度不能超过500")
    private String serviceName;
    @ExcelCol(value = 15,title = "所需物料")
    @Length(max = 500,message = "所需物料长度不能超过500")
    private String goodsName;
    @ExcelCol(value = 16,title = "服务费合计")
    @DecimalMin(value = "0.00",message = "服务费合计必须大于或等于0.00")
    private BigDecimal serviceAmount;
    @ExcelCol(value = 17,title = "服务费优惠")
    @DecimalMin(value = "0.00",message = "服务费优惠必须大于或等于0.00")
    private BigDecimal serviceDiscount;
    @ExcelCol(value = 18,title = "物料费合计")
    @DecimalMin(value = "0.00",message = "物料费合计必须大于或等于0.00")
    private BigDecimal goodsAmount;
    @ExcelCol(value = 19,title = "物料费优惠")
    @DecimalMin(value = "0.00",message = "物料费优惠必须大于或等于0.00")
    private BigDecimal goodsDiscount;
    @ExcelCol(value = 20,title = "应付金额合计")
    @DecimalMin(value = "0.00",message = "应付金额合计必须大于或等于0.00")
    private BigDecimal payableAmount;

    private BigDecimal discount;
    @ExcelCol(value = 21,title = "优惠金额")
    @DecimalMin(value = "0.00",message = "优惠金额必须大于或等于0.00")
    private BigDecimal discountAmount;
    @ExcelCol(value = 22,title = "实际应付金额")
    @DecimalMin(value = "0.00",message = "实际应付金额必须大于或等于0.00")
    private BigDecimal actualPayableAmount;
    @ExcelCol(value = 23,title = "实付金额")
    @DecimalMin(value = "0.00",message = "实付金额必须大于或等于0.00")
    private BigDecimal payAmount;
    @ExcelCol(value = 24,title = "挂账金额")
    @DecimalMin(value = "0.00",message = "挂账金额必须大于或等于0.00")
    private BigDecimal signAmount;
    @ExcelCol(value = 25,title = "开单人")
    @Length(max = 20,message = "开单人长度不能超过20")
    private String operatorName;
    @ExcelCol(value = 26,title = "维修工")
    @Length(max = 20,message = "维修工长度不能超过20")
    private String worker;
    @ExcelCol(value = 27,title = "结算人")
    @Length(max = 20,message = "结算人长度不能超过20")
    private String payName;
    @ExcelCol(value = 28,title = "完工时间(1900/01/01)")
    private Date finishTime;
    @ExcelCol(value = 29,title = "结算时间(1900/01/01)")
    private Date payTime;
    @ExcelCol(value = 30,title = "备注")
    @Length(max = 500,message = "备注长度不能超过500")
    private String remark;
    //时间展示字段
    private String orderCreateTimeStr;
    private String payTimeStr;
    private String finishTimeStr;


    public String getOrderCreateTimeStr(){
        return DateUtil.convertDateToYMD(orderCreateTime);
    }
    public String getPayTimeStr(){
        return DateUtil.convertDateToYMD(payTime);
    }
    public String getFinishTimeStr(){
        return DateUtil.convertDateToYMD(finishTime);
    }

}

