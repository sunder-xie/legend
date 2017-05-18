package com.tqmall.legend.biz.order.vo;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 16/6/13.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderDiscountFlowVo {
    private Long id;
    protected Date gmtCreate;
    protected Long creator;
    private Long shopId;//店铺id
    private Long orderId;//工单id
    private Integer discountType;//优惠类型：1:折扣、2:优惠、3:代金券、4:折扣券、5:淘汽优惠、6第三方优惠
    private String discountName;//优惠名称：折扣、优惠、代金券、折扣券、淘汽优惠、第三方优惠
    private String discountSn;//优惠码
    private BigDecimal discountRate;//优惠折扣
    private BigDecimal discountAmount;//优惠金额
    private String discountReason;//优惠原因
    private String operatorName;//收银人
    private Long relId;//关联id,如会员卡id、现金券id等
    private Long accountId;//账户id

    public String getGmtCreateStr() {
        return DateUtil.convertDateToStr(gmtCreate, "yyyy-MM-dd HH:mm");
    }
}
