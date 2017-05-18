package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

/**
 * Created by zsy on 16/6/2.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderDiscountFlow extends BaseEntity {

    private Long shopId;//店铺id
    private Long orderId;//工单id
    private Integer discountType;//优惠类型：1折扣、2优惠、3代金券、4折扣券、5淘汽优惠、6第三方优惠、7会员卡优惠、8现金券、9通用券、10计次卡
    private String discountName;//优惠名称：折扣、优惠、代金券、折扣券、淘汽优惠、第三方优惠
    private String discountSn;//优惠码
    private BigDecimal discountRate;//优惠折扣
    private BigDecimal discountAmount;//优惠金额
    private String discountReason;//优惠原因
    private Long relId;//关联id,如会员卡id、现金券id等
    private Integer validStatus;//优惠流水实际状态：1有效 0无效
    private Long accountId;//账户id,如使用优惠券、会员卡,则保存账户id

}

