package com.tqmall.legend.object.result.member;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xiangDong.qu on 16/3/20.
 */
@Data
@ToString
public class ShopMemberDTO implements Serializable{
    private static final long serialVersionUID = -3323702724794848454L;

    /**
     * id
     * */
    private Long id;
     /**
     * 会员号码
     */
    private String cardNumber = "";
    /**
     * 会员类型
     */
    private String cardType = "普通会员";
    /**
     * 会员卡内余额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 消费总金额
     */
    private BigDecimal expenseAmount = BigDecimal.ZERO;
    /**
     * 折扣
     */
    private Integer discount = Integer.valueOf("100");
    /**
     * 优惠劵
     */
    List<ShopMemberServiceRelDTO> shopMemberServiceRelDTOList;
}
