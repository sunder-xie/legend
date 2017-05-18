package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.enums.warehouse.PayMethodEnum;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.component.excel.annotation.ExcelRowNumber;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
@Excel
public class Supplier extends BaseEntity {
    @ExcelRowNumber
    private Integer rowNumber;
    @ExcelCol(value = 0,title = "*供应商编号")
    @Length(max = 100,message = "供应商编号长度不能超过100")
    @NotBlank(message = "供应商编号不能为空")
    private String supplierSn;//供应商编号
    @ExcelCol(value = 1,title = "*供应商名称")
    @Length(max = 50,message = "供应商名称长度不能超过50")
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;//供应商名称
    @ExcelCol(value = 2,title = "固定电话(区号-号码)")
    @Length(max = 20,message = "固定电话长度不能超过20")
    private String tel;//固定电话
    @ExcelCol(value = 3,title = "供应商地址")
    @Length(max = 255,message = "供应商地址长度不能超过255")
    private String address;//供应商地址
    @ExcelCol(value = 4,title = "联系人")
    @Length(max = 45,message = "联系人长度不能超过45")
    private String contact;//联系人
    @ExcelCol(value = 5,title = "联系人电话")
    @Length(max = 45,message = "联系人电话长度不能超过45")
    private String mobile;//联系人手机号
    @ExcelCol(value = 6,title = "主营业务")
    @Length(max = 200,message = "主营业务长度不能超过200")
    private String content;//供应商主营业务
    private Long shopId;//门店id
    private Integer category;//供应商分类：1.一级供应商

    private Integer payMethod;//结算方式：1月结，2季结

    private Integer invoiceType;//发票类型：1普通发票2增值税发票
    private String companyName;//单位名称（开发票抬头）
    private String invoiceNo;//单位税号
    private String openingBank;//开户银行
    private String bankAccount;//银行账号

    private String payMode;

    public String getPayMode() {
        return PayMethodEnum.getMessageByCode(payMethod);
    }
}
