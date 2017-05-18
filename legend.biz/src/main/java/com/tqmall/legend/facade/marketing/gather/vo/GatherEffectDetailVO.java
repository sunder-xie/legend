package com.tqmall.legend.facade.marketing.gather.vo;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 2016/12/22.
 */
@Getter
@Setter
@Excel
public class GatherEffectDetailVO {
    private Long id;
    private Long orderId;//工单id
    private Long customerId;//客户id
    private Long customerCarId;//车辆id
    @ExcelCol(value = 0, title = "车牌", width = 10)
    private String carLicense;//车牌
    @ExcelCol(value = 1, title = "车主名称", width = 12)
    private String customerName;//客户姓名
    @ExcelCol(value = 2, title = "车主电话", width = 16)
    private String customerMobile;//手机号
    private Long userId;//服务顾问id
    @ExcelCol(value = 3, title = "客户归属", width = 12)
    private String userName;//服务顾问姓名
    @ExcelCol(value = 4, title = "集客时间", width = 16)
    private String gatherTime;//集客时间
    @ExcelCol(value = 5, title = "集客方案", width = 16)
    private String gatherType;//集客方式：0:盘活客户，1:老客户拉新
    private Long creator;
    @ExcelCol(value = 6, title = "操作人", width = 12)
    private String creatorName;//操作人姓名
    @ExcelCol(value = 7, title = "操作方式", width = 12)
    private String operateType;//操作方式：0:电话回访，1:短信回访，2:微信优惠券
    @ExcelCol(value = 8, title = "集客消费时间", width = 16)
    private String gatherConsumeTime;//集客消费时间
    @ExcelCol(value = 9, title = "集客消费金额", width = 12)
    private BigDecimal gatherConsumeAmount;//集客消费金额
}
