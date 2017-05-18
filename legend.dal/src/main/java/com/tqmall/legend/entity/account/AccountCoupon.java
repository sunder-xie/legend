package com.tqmall.legend.entity.account;

import com.tqmall.common.util.DateUtil;
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

@Getter
@Setter
@Excel
public class AccountCoupon extends BaseEntity {
    @ExcelRowNumber
    private Integer rowNumber;
    @ExcelCol(value = 0,title = "车主电话(必填)")
    @NotBlank(message = "车主电话不能为空")
    @Length(max = 20,message = "车主电话长度不能超过20")
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{8}$",message = "车主电话填写不正确")
    private String mobile;//车主电话
    private Long shopId;//门店id
    private String couponCode;//优惠劵码
    private Integer couponSource;//来源：0充值1赠送2导入
    private Date effectiveDate;//生效时间
    private String effectiveDateStr;
    @ExcelCol(value = 3,title = "过期时间(必填)(2018/01/01)")
    @NotNull(message = "过期时间不能为空")
    private Date expireDate;//失效时间
    private String expireDateStr;
    private Integer usedStatus;//是否使用0未使用1已使用
    private Long accountId;//账户id
    private Long couponInfoId;//优惠劵id
    private Integer couponType;//优惠劵类型,0:折扣卷;1:现金券
    @ExcelCol(value = 1,title = "优惠券名称(必填)")
    @NotBlank(message = "优惠劵名称不能为空")
    private String couponName;//优惠劵名称
    private Long suiteId;//来源某个套餐
    private Long flowId;//充值流水id
    private String flowSn;//流水号
    private CouponInfo couponInfo;
    private String operatorName;//操作人
    @ExcelCol(value = 2,title = "优惠券数量(必填)")
    @Min(value = 1,message = "优惠券张数不能为负数")
    @NotNull(message = "优惠券张数不能为空")
    private Integer couponNum;//优惠券张数
    private String suiteName;//套餐名称
    private String customerName;

    public enum SourceEnum {
        RECHARGE(0,"充值"),
        GIFT(1,"赠送"),
        IMPORT(3,"导入");

        private int code;
        private String msg;

        SourceEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    public enum StateEnum {
        UN_USED(0,"未使用"),
        USED(1,"已使用");

        private int code;
        private String msg;

        StateEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    private AccountInfo accountInfo;

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

    public boolean isExpired() {
        if (expireDate == null) {
            return false;
        }
        return expireDate.compareTo(new Date()) < 0;
    }

    public String getGmtCreateStr(){
        if(gmtCreate != null){
            return DateUtil.convertDateToYMD(gmtCreate);
        }
        return null;
    }

    public String getCouponTypeName(){
        if(couponType == null){
            return null;
        }else if(couponType == 0){
            return "折扣券";
        }else if(couponType == 1){
            return "现金券";
        }else{
            return "通用券";
        }
    }
}

