package com.tqmall.legend.web.vo.cardcoupon;

import lombok.Data;

/**
 * Created by twg on 16/7/25.
 */
@Data
public class CardCouponSearchVO {
    private String sTime;//办理开始查询时间
    private String eTime;//办理结束查询时间
    private String cardNumber;//会员卡号
    private String license;//车牌
    private String mobile;//车主电话
    private Long cardCouponTypeId;//卡、券类型id
    private Integer consumeTypeId;//交易类型
    private Integer suiteId;//优惠券套餐id
    private Integer tradeType;//tab标签 1 优惠券 2 计次卡 3 会员卡
    private Integer isExcel;//是否为excel导出 0不是  1是

}
