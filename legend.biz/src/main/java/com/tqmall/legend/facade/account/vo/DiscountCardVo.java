package com.tqmall.legend.facade.account.vo;

import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.pub.member.MemberCar;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wanghui on 6/6/16.
 */
@Data
public class DiscountCardVo {
    /**
     * 会员卡信息
     */
    private MemberCard memberCard;
    /**
     * 会员卡类型信息
     */
    private MemberCardInfo memberCardInfo;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 是否使用
     */
    private boolean used;
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 备注信息
     */
    private String message;

}
