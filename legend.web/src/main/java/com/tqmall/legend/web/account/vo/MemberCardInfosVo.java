package com.tqmall.legend.web.account.vo;

import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.entity.account.CardServiceRel;
import com.tqmall.legend.entity.account.MemberCard;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tanghao on 16/6/8.
 */
@Data
public class MemberCardInfosVo {

    private Long shopId;//门店id
    private String typeName;//会员卡类型名
    private String cardInfoExplain;//描述
    private Integer serviceDiscountType;//服务折扣类型:0.无折扣;1.全部服务享受折扣;2部分服务享受折扣
    private BigDecimal serviceDiscount;//服务折扣
    private Integer goodDiscountType;//配件折扣类型:0.无折扣;1.全部配件享用折扣;2.部分配件享受折扣
    private BigDecimal goodDiscount;//配件折扣
    private Long effectivePeriodDays;//有效期
    private Integer compatibleWithCoupon;//0:不允许与优惠券共同使用;1:允许与其他优惠券共同使用
    private Integer generalUse;//0:不允许其他人非该账户下的车辆使用;1.允许其他人非该账户下的车辆使用
    private BigDecimal initBalance;//初始余额
    private BigDecimal salePrice;//售卖金额

    private List<CardServiceRel> cardServiceList;
    private List<CardGoodsRel> cardGoodsList;
    private String cardNum;//会员卡号
    private String outTimeDate;//过期时间
    private Integer usedNum;//使用次数
    private String buyTime;//购买时间
    private String publisherName;//操作人



}
