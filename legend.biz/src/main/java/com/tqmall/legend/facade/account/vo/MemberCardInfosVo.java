package com.tqmall.legend.facade.account.vo;

import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.entity.account.CardServiceRel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tanghao on 16/6/8.
 */
@Data
public class MemberCardInfosVo {

    private Long shopId;//门店id
    private String typeName="";//会员卡类型名
    private String cardInfoExplain="";//描述
    private Integer serviceDiscountType;//服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
    private BigDecimal serviceDiscount=BigDecimal.ZERO;//服务折扣
    private Integer goodDiscountType;//配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
    private BigDecimal goodDiscount=BigDecimal.ZERO;//配件折扣
    private Long effectivePeriodDays;//有效期
    private Integer compatibleWithCoupon;//0:不允许与优惠券共同使用;1:允许与其他优惠券共同使用
    private Integer generalUse;//0:不允许其他人非该账户下的车辆使用;1.允许其他人非该账户下的车辆使用
    private BigDecimal initBalance=BigDecimal.ZERO;//初始余额
    private BigDecimal salePrice=BigDecimal.ZERO;//售卖金额
    private Integer discountType;//折扣类型:0.无折扣;1.全部工单折扣;2.全部服务折扣;3.全部配件折扣;4.多种类型 折扣（服务折扣类型、配件折扣类型）
    private BigDecimal orderDiscount=BigDecimal.ZERO;//工单折扣
    private boolean expired;//是否过期


    private List<CardServiceRel> cardServiceList;
    private List<CardGoodsRel> cardGoodsList;
    private String cardNum="";//会员卡号
    private String outTimeDate="";//过期时间
    private Integer usedNum;//使用次数
    private String buyTime="";//购买时间
    private String publisherName="";//操作人
    private String discountDescript;




}
