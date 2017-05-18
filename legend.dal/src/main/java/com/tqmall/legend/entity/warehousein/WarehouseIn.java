package com.tqmall.legend.entity.warehousein;


import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.common.CommonUtils;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.enums.warehouse.PayMethodEnum;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Excel
public class WarehouseIn extends BaseEntity {

    private Long shopId;//店铺id
    private Long supplierId;//'商品来源,
    @ExcelCol(value = 1, title = "供应商", width = 16)
    private String supplierName;//供应商名称
    @ExcelCol(value = 2, title = "入库单号", width = 24)
    private String warehouseInSn;//入库编号
    private Long relId;//红字入库关联蓝字id
    private String purchaseSn;//淘汽档口采购单编号
    private Long purchaseAgent;//采购人id
    private String purchaseAgentName;//采购人姓名
    private Date inTime;//入库时间
    private BigDecimal goodsAmount;//物料进货总价
    private BigDecimal tax;//税费
    private BigDecimal freight;//运费
    @ExcelCol(value = 3, title = "采购金额", width = 16)
    private BigDecimal totalAmount;//入库总金额
    private Integer invoiceType;//发票类型
    private String invoiceTypeName;//发票类型名称
    @ExcelCol(value = 6, title = "备注", width = 20)
    private String comment;//入库单备注
    private String depot;//仓位信息
    private String status;//入库状态:lzrk:蓝字入库;hzrk:红字入库;lzzf:蓝字作废;hzzf:红字作废;
    private String paymentStatus;//'订单支付状态:
    private String paymentMode;//支付方式
    @ExcelCol(value = 4, title = "已付金额", width = 16)
    private BigDecimal amountPaid;//已支付金额
    @ExcelCol(value = 5, title = "未付金额", width = 16)
    private BigDecimal amountPayable;//挂账金额
    private String paymentComment;//入库单支付备注

    private Date firstPayTime;//首次支付时间
    private Date latestPayTime;//最近一次支付时间
    private String contact;//联系人
    private String contactMobile;//联系人电话

    @ExcelCol(value = 0, title = "入库日期", width = 12)
    private String gmtCreateStr;
    //付款方式
    private Integer payMethod;
    private String inTimeStr;

    public Boolean getPaid() {
        if (amountPaid == null) {
            return false;
        }
        return amountPaid.compareTo(goodsAmount) == 0 ? true : false;
    }

    public String getGoodsAmountStr() {
        return CommonUtils.convertMoney(goodsAmount);
    }

    public String getAmountPayableStr() {
        return CommonUtils.convertMoney(amountPayable);
    }

    public String getAmountPaidStr() {
        return CommonUtils.convertMoney(amountPaid);
    }

    public String getGmtCreateStr() {
        if (null == gmtCreate)
            return DateUtil.convertDateToYMD(new Date());
        else
            return DateUtil.convertDateToYMD(gmtCreate);
    }

    public Integer getPayMethod() {
        return PayMethodEnum.getCodeByMessage(paymentMode);
    }

}


