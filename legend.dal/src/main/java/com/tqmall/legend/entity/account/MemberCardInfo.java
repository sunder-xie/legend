package com.tqmall.legend.entity.account;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wanghui on 6/2/16.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MemberCardInfo extends BaseEntity {

    public static final Integer COMPATIBLE_WITH_COUPON_NO = Integer.valueOf(0);
    public static final Integer COMPATIBLE_WITH_COUPON_YES = Integer.valueOf(1);
    public static final int ENABLED = 1;
    public static final int DISABLED = 2;

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
    private Integer discountType;//折扣类型:0.无折扣;1.全部工单折扣;2.服务折扣;3.配件折扣;4.多种类型 折扣（服务折扣类型、配件折扣类型）
    private BigDecimal orderDiscount;//工单折扣
    private Integer cardInfoStatus;//状态1启用2停用
    private List<CardServiceRel> cardServiceRels;
    private List<CardGoodsRel> cardGoodsRels;

    private Integer grantedCount;//已发放数量
    private String gmtCreateStr;//创建日期



    private Integer size = 0;//总共数量

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
        switch (discountType.intValue()) {
            case 0:
                resetGoodDiscount();
                resetGoodDiscountType();
                resetOrderDiscount();
                resetServiceDiscount();
                resetServiceDiscountType();
                break;
            case 1:
                resetGoodDiscount();
                resetGoodDiscountType();
                resetServiceDiscountType();
                resetServiceDiscount();
                break;
            case 2:
                resetGoodDiscount();
                resetGoodDiscountType();
                resetOrderDiscount();
                break;
            case 3:
                resetServiceDiscount();
                resetServiceDiscountType();
                resetOrderDiscount();
                break;
            default:
                break;

        }
    }

    public void setServiceDiscountType(Integer serviceDiscountType) {
        this.serviceDiscountType = serviceDiscountType;
        if (Integer.valueOf(2).equals(serviceDiscountType)) {
            resetServiceDiscount();
        }
    }

    public void setGoodDiscountType(Integer goodDiscountType) {
        this.goodDiscountType = goodDiscountType;
        if (Integer.valueOf(2).equals(goodDiscountType)) {
            resetGoodDiscount();
        }
    }

    private void resetServiceDiscount() {
        this.serviceDiscount = null;
    }

    private void resetServiceDiscountType() {
        this.serviceDiscountType = 0;
    }

    private void resetGoodDiscount() {
        this.goodDiscount = null;
    }

    private void resetGoodDiscountType() {
        this.goodDiscountType = 0;
    }

    private void resetOrderDiscount() {
        this.orderDiscount = BigDecimal.ZERO;
    }

    /**
     * 是否可与其他优惠券共同使用
     * @return
     */
    public boolean canCompatibleWithCoupon(){
        return COMPATIBLE_WITH_COUPON_YES.equals(compatibleWithCoupon);
    }

    public String getDiscountDescription() {
        StringBuilder discountDescript = new StringBuilder();
        switch (discountType) {
            case 0: discountDescript.append("无折扣");
                break;
            case 1: discountDescript.append("全部工单")
                    .append(orderDiscount)
                    .append("折");
                break;
            case 2:
                if (serviceDiscountType.equals(1)) {
                    discountDescript.append("全部服务").append(serviceDiscount).append("折");
                }
                if (serviceDiscountType.equals(2)) {
                    setServiceDescript(discountDescript);
                }
                break;
            case 3:
                if (goodDiscountType.equals(1)) {
                    discountDescript.append("全部配件").append(goodDiscount).append("折");
                }
                if (goodDiscountType.equals(2)) {
                    setGoodsDescript(discountDescript);
                }
                break;
            case 4:
                if (serviceDiscountType.equals(1)) {
                    discountDescript.append("全部服务").append(serviceDiscount).append("折");
                }
                if (serviceDiscountType.equals(2)) {
                    setServiceDescript(discountDescript);
                }
                discountDescript.append("\n");
                if (goodDiscountType.equals(1)) {
                    discountDescript.append("全部配件").append(goodDiscount).append("折");
                }
                if (goodDiscountType.equals(2)) {
                    setGoodsDescript(discountDescript);
                }
                break;
            default:
                discountDescript.append("未知");
                break;
        }

        return discountDescript.toString();
    }

    private void setGoodsDescript(StringBuilder discountDescript) {
        discountDescript.append("配件: ");
        if (cardGoodsRels != null) {
            for (CardGoodsRel cardGoodsRel : cardGoodsRels) {
                discountDescript.append(cardGoodsRel.getGoodsCatName())
                        .append(cardGoodsRel.getDiscount())
                        .append("折").append("、");
            }
            discountDescript.deleteCharAt(discountDescript.length()-1);
        }
    }

    private void setServiceDescript(StringBuilder discountDescript) {
        discountDescript.append("服务: ");
        if (cardServiceRels != null) {
            for (CardServiceRel cardServiceRel : cardServiceRels) {
                discountDescript.append(cardServiceRel.getServiceCatName())
                        .append(cardServiceRel.getDiscount())
                        .append("折").append("、");
            }
            discountDescript.deleteCharAt(discountDescript.length()-1);
        }
    }

    public String getLimitDescription() {
        StringBuilder sb = new StringBuilder();
        if (Integer.valueOf(0).equals(this.compatibleWithCoupon)) {
            sb.append("不允许与优惠券共同使用");
        } else {
            sb.append("允许与其他优惠券共同使用");
        }
//        sb.append(';');
//        if (Integer.valueOf(0).equals(this.generalUse)) {
//            sb.append("不允许其他人非该账户下的车辆使用");
//        } else {
//            sb.append("允许其他人非该账户下的车辆使用");
//        }
        return sb.toString();
    }

    public String getGmtCreateStr(){
        if(null!= gmtCreate){
            return DateUtil.convertDateToYMD(gmtCreate);
        }else{
            return "";
        }
    }
}