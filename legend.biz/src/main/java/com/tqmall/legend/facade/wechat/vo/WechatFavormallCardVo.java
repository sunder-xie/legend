package com.tqmall.legend.facade.wechat.vo;

import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.MembershipCardDTO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wushuai on 16/9/9.
 */
@Data
public class WechatFavormallCardVo extends MembershipCardDTO {
    private String typeName;//会员卡类型名
    private String cardInfoExplain;//描述
    private Integer serviceDiscountType;//服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
    private BigDecimal serviceDiscount;//服务折扣
    private Integer goodDiscountType;//配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
    private BigDecimal goodDiscount;//配件折扣
    private Integer discountType;//折扣类型:0.无折扣;1.全部工单折扣;2.全部服务折扣;3.全部配件折扣;4.多种类型 折扣（服务折扣类型、配件折扣类型）
    private BigDecimal orderDiscount;//工单折扣
    private String discountDescript;//折扣描述(非数据库字段)
    private Long effectivePeriodDays;//有效期
}
