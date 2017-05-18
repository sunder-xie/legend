package com.tqmall.legend.web.account.vo;

import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.MemberCard;
import lombok.Data;

import java.util.List;

/**
 * Created by zsy on 16/6/23.
 * 客户详情会员账户Vo对象
 */
@Data
@Deprecated
public class CustomerMemberInfoVo {
    private List<MemberCard> memberCardList;
    private List<AccountCoupon> accountCouponList;
    private List<AccountCombo> accountComboList;
}