package com.tqmall.legend.entity.app.member;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jason on 15/9/10.
 * app端会员卡服务关联关系表VO
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopMemberCardServiceDetail {
    //会员卡ID
    private Long memberId;
    //会员卡号
    private String cardNumber;
    //会员卡余额
    private BigDecimal amount;
    //店铺名称
    private String shopName;
    //店铺ID
    private String userGlobalId;
    //会员等级
    private String levelName;
    //会员积分
    private Long bonusPoint;
    //会员等级积分区间上限
    private Integer maxPoint;
    //卡券list
    private List<ShopMemberCardService> serviceList;

}
