package com.tqmall.legend.entity.member;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopMember extends BaseEntity {
    private String creatorName;

    private Long shopId;

    private String cardNumber;

    private Long carId;

    private String email;

    private BigDecimal amount;

    private BigDecimal expenseAmount;

    private Integer expenseCount;

    private Date latestTime;

    private String latestTimeFormat;

    private String level;

    private Integer discount;

    private String comment;

    private String rechargeComment;//充值服务套餐内容

    private String mobile;

    private String customerName;

    private String license;

    private String vin;

    private List<ShopMemberServiceRel> serviceVoList;

    public void setAmount(BigDecimal amount){
        if(amount != null && amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException(String.format("更新卡内余额失败,输入数据非法:%s",amount.toString()));
        } else {
            this.amount = amount;
        }
    }

    public void setExpenseAmount(BigDecimal expenseAmount){
        if(expenseAmount != null && expenseAmount.compareTo(BigDecimal.ZERO) <= 0){
            this.expenseAmount = BigDecimal.ZERO;
        } else {
            this.expenseAmount = expenseAmount;
        }
    }

    public void setExpenseCount(Integer expenseCount){
        if(expenseCount != null && expenseCount.compareTo(0) <= 0 ){
            this.expenseCount = 0;
        } else {
            this.expenseCount = expenseCount;
        }
    }
    public String getLatestTimeFormat() {
        if (latestTimeFormat != null) {
            return latestTimeFormat;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (latestTime != null) {
            return df.format(latestTime);
        }
        return null;
    }

}
