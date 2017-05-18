package com.tqmall.legend.web.report.export.vo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by tanghao on 17/2/8.
 */
@Setter
@Getter
@com.tqmall.wheel.component.excel.annotation.Excel
public class MemberCardConsumeCommission {
    @ExcelCol(value = 0, title = "消费时间")
    private String consumeTime;//消费时间
    @ExcelCol(value = 1, title = "工单号")
    private String orderSn;//工单号
    @ExcelCol(value = 2, title = "车牌")
    private String license;//车牌号
    @ExcelCol(value = 4, title = "车主号码")
    private String mobile;//手机号
    @ExcelCol(value = 5, title = "会员卡号")
    private String cardNum;//会员卡号
    @ExcelCol(value = 6, title = "会员卡类型")
    private String cardCouponInfo;//会员卡,计次卡,优惠券类型
    @ExcelCol(value = 8, title = "会员卡余额抵扣")
    private BigDecimal cardBalancePay;//会员卡余额抵扣金额
    private BigDecimal cardPrivilegePay;//会员卡特权抵扣金额
    @ExcelCol(value = 10, title = "会员卡余额")
    private BigDecimal cardBalance;//会员卡余额
    private BigDecimal discountRate;//折扣
    private String serviceName;//计次卡服务项目
    private BigDecimal serviceAmount;//工时费
    private BigDecimal serviceHour;//服务工时
    private Integer consumeCount;//消费次数
    @ExcelCol(value = 9, title = "会员卡特权抵扣")
    private BigDecimal discountAmount;//抵扣金额
    private String couponCode;//优惠券码
    private Integer discountType;//会员卡优惠类型

    private String discountTypeStr;
    @ExcelCol(value = 3, title = "车主")
    private String customerName;//車主姓名

    @ExcelCol(value = 7, title = "会员特权")
    public String getCardDiscountInfo(){
        return (StringUtils.isEmpty(discountTypeStr) ? "" : discountTypeStr) + (discountRate == null ? "" : discountRate + "折") ;
    }

    public String getDiscountTypeStr(){
        if(null != discountType){
            if(discountType==0){
                return "无折扣";
            }else if(discountType==1){
                return "全部工单";
            }else if(discountType==2){
                return "全部服务";
            }else if(discountType==3){
                return "全部配件";
            }
            return "";
        }else {
            return "";
        }
    }
}
