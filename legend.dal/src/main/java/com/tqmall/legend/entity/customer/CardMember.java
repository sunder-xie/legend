package com.tqmall.legend.entity.customer;


import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class CardMember extends BaseEntity {

    private String creatorName;
    private Long shopId;
    private String cardSn;
    private String contact;
    private String mobile;
    private Date expireTime;
    //    private String expireTimeStr;
    private String status;
    private String plateNumber;
    private String carModel;
    private Long carId;
    private Date latestTime;
    //    private String latestTimeStr;
    private BigDecimal balance;
    // 消费金额
    private BigDecimal memberPayAmount;
    private Long accumulate;
    private String level;
    private String comment;
    private Date deliverTime;
    //    private String deliverTimeStr;
    private Integer discount;
    private String carAlias;


    // TODO 用于工单结算页，关联“会员服务列表”
    private List<MemberServiceEntity> selectedMemberServiceEntity;

    // 会员折扣金额
    private BigDecimal memberDiscountAmount;
    // 会员卡优惠原因
    private String cardDiscountReason;

    public CardMember() {
        accumulate = 0l;
        level = "0";
        balance = BigDecimal.ZERO;
        status = "ZCSY";
        discount = 100;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        deliverTime = gc.getTime();
        gc.add(1, 2);
        expireTime = gc.getTime();
    }

    public String getDeliverTimeStr() {
        if (deliverTime == null) {
            return DateUtil.convertDateToYMD(new Date());
        } else
            return DateUtil.convertDateToYMD(deliverTime);
    }

    public String getExpireTimeStr() {
        if (expireTime == null) {
            return DateUtil.convertDateToYMD(new Date());
        } else
            return DateUtil.convertDateToYMD(expireTime);
    }
}

