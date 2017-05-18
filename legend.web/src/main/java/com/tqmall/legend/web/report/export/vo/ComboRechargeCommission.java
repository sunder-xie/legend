package com.tqmall.legend.web.report.export.vo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by tanghao on 17/2/8.
 */
@Setter
@Getter
@com.tqmall.wheel.component.excel.annotation.Excel
public class ComboRechargeCommission {
    @ExcelCol(value = 0, title = "发放时间")
    private String rechargeTime="";//充值时间
    @ExcelCol(value = 1, title = "交易类型")
    private String tradeTypeName="";//交易类型名
    @ExcelCol(value = 2, title = "车牌")
    private String license="";//车牌号
    @ExcelCol(value = 4, title = "车主号码")
    private String mobile="";//车主号码

    private String cardNum="";//会员卡号码
    @ExcelCol(value = 5, title = "计次卡类型")
    private String cardCouponType="";//会员卡类型

    private BigDecimal rechargeAmount=BigDecimal.ZERO;//充值金额

    private BigDecimal cardBalance=BigDecimal.ZERO;//卡内余额
    @ExcelCol(value = 7, title = "有效期")
    private Long effectDay;//有效期
    @ExcelCol(value = 8, title = "过期时间")
    private String expireate="";//失效时间
    @ExcelCol(value = 9, title = "收款金额")
    private BigDecimal reciveAmount=BigDecimal.ZERO;//收款金额
    @ExcelCol(value = 10, title = "操作人")
    private String operatorName="";//操作人姓名
    @ExcelCol(value = 6, title = "卡内项目数")
    private Integer countNum;//计次卡的卡内项目数,优惠券的发放数量

    private String source="";//优惠券来源

    private String reviceName="";//接收人姓名

    private Integer pageNum;//页数

    private Integer pageSize;//每页条数
    @ExcelCol(value = 3, title = "车主")
    private String customerName;//客户姓名

    private Integer limit;

    private Integer offset;
}
