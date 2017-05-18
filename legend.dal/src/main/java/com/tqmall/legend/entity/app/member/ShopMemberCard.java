package com.tqmall.legend.entity.app.member;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jason on 15/9/10.
 * app端门店会员卡VO
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopMemberCard {

    //会员ID
    private Long memberId;
    //店铺名称
    private String shopName;
    //店铺ID
    private String userGlobalId;
    //会员卡号
    private String cardNumber;
    //会员卡余额
    private BigDecimal amount;
    //会员卡服务List
    private List<ShopMemberCardService> serviceList;


}
