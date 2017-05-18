package com.tqmall.legend.entity.account;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.component.excel.annotation.ExcelRowNumber;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Excel
public class MemberCard extends BaseEntity {
    @ExcelRowNumber
    private Integer rowNumber;
    @ExcelCol(value = 0,title = "车主电话(必填)")
    @NotBlank(message = "车主电话不能为空")
    @Length(max = 20,message = "车主电话长度不能超过20")
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{8}$",message = "车主电话填写不正确")
    private String mobile;
    private Long shopId;//门店id
    @ExcelCol(value = 1,title = "会员卡类型(必填)")
    @NotBlank(message = "会员卡类型名不能为空")
    private String cardTypeName;//会员卡类型名
    @ExcelCol(value = 2,title = "会员卡号(必填)")
    @NotBlank(message = "会员卡号不能为空")
    @Length(min = 1,max = 50,message = "会员卡号长度不能超过50")
    private String cardNumber;//会员卡号
    @ExcelCol(value = 3,title = "失效时间(必填)(2018/01/01)")
    @NotNull(message = "失效时间不能为空")
    private Date expireDate;//失效时间
    @ExcelCol(value = 4,title = "余额")
    @DecimalMin(value = "0.00",message = "余额必须大于或等于0.00")
    private BigDecimal balance;//卡内金额
    @ExcelCol(value = 5,title = "积分")
    @DecimalMin(value = "0.00",message = "积分必须大于或等于0.00")
    private BigDecimal expenseAmount;//累计消费金额
    private Integer expenseCount;//累计消费次数
    private Long receiver;//服务顾问
    private String receiverName;//服务顾问名字
    private Integer depositCount;//累计充值次数
    @ExcelCol(value = 6,title = "累计充值金额")
    @DecimalMin(value = "0.00",message = "累计充值金额必须大于或等于0.00")
    private BigDecimal depositAmount;//累计充值金额
    private Long publisher;//发卡人
    private String publisherName;//发卡人
    private Long cardTypeId;//会员卡类型id
    private Long accountId;//账户id
    private String customerName;
    private String cardNumberStr;//隐藏几个数字的会员卡号 例如1234*****5
    /**
     * 会员卡升级时,关联原会员卡的会员卡id,若为新办卡,则为0
     */
    private Long rawCardId;

    /**
     * 流水表信息
     */
    private Long flowId;//流水id
    private Integer isReversed;//是否回退
    private BigDecimal payAmount;//付款金额

    /**
     * 会员卡类型信息
     */
    private MemberCardInfo memberCardInfo;

    /**
     * 会员卡关联的账户拥有的车辆
     */
    private List<String> license;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MemberCard that = (MemberCard) o;

        if (shopId != null ? !shopId.equals(that.shopId) : that.shopId != null) return false;
        if (cardNumber != null ? !cardNumber.equals(that.cardNumber) : that.cardNumber != null) return false;
        if (expireDate != null ? !expireDate.equals(that.expireDate) : that.expireDate != null) return false;
        if (cardTypeName != null ? !cardTypeName.equals(that.cardTypeName) : that.cardTypeName != null) return false;
        return mobile != null ? mobile.equals(that.mobile) : that.mobile == null;

    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        result = 31 * result + (expireDate != null ? expireDate.hashCode() : 0);
        result = 31 * result + (cardTypeName != null ? cardTypeName.hashCode() : 0);
        result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
        return result;
    }

    /**
     * 会员卡是否过期
     *
     * @return true:已过期,false:未过期
     */
    public boolean isExpired() {
        if (expireDate == null) {
            return false;
        }
        return this.expireDate.compareTo(new Date()) < 0;
    }

    public BigDecimal changeBalance(BigDecimal amount) {
        balance = balance.add(amount);
        return balance;
    }

    public Integer addDepositCount() {
        depositCount = depositCount + 1;
        return depositCount;
    }

    public Integer subDepositCount() {
        depositCount = depositCount - 1;
        return depositCount;
    }

    public BigDecimal changeDepositAmount(BigDecimal amount) {
        depositAmount = depositAmount.add(amount);
        return depositAmount;
    }


    /**
     * 会员积分,暂时与会员消费总额相等
     *
     * @return
     */
    public BigDecimal getCardPoints() {
        return this.expenseAmount;
    }

    public String getGmtCreateStr() {
        if (gmtCreate != null) {
            return DateUtil.convertDateToYMD(gmtCreate);
        }
        return null;
    }

    public String getExpireDateStr() {
        if (expireDate != null) {
            return DateUtil.convertDateToYMD(expireDate);
        }
        return null;
    }

    public String getCardNumberStr(){
        if(StringUtils.isEmpty(cardNumber)){
            return "";
        }
        int length = cardNumber.length();
        if (length < 3){
            String str = cardNumber.replaceAll(".","\\*");
            return str;
        }
        int start = length/3-1;
        int end = length/3*2;
        StringBuffer str = new StringBuffer();
        for(int i = 0 ; i < length ; i++){
            if (i > start && i < end){
                str.append("*");
                continue;
            }
            str.append(cardNumber.charAt(i));
        }
        return str.toString();
    }

}