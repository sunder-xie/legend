package com.tqmall.legend.facade.discount.bo;

import com.tqmall.legend.enums.account.CardDiscountTypeEnum;
import com.tqmall.legend.enums.account.CardGoodsDiscountTypeEnum;
import com.tqmall.legend.enums.account.CardServiceDiscountTypeEnum;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Author 辉辉大侠
 * @Date:10:10 AM 02/03/2017
 */
@Data
public class AccountCardDiscountBo {
    public AccountCardDiscountBo() {
        this.selected = false;
        this.available = true;
    }

    private Long cardId;
    private String cardNumber;
    /**
     * 折扣信息描述
     */
    private String discountDesc;
    /**
     * 会员余额
     */
    private BigDecimal balance;
    /**
     * 会员卡类型名
     */
    private String cardName;
    private Long cardTypeId;
    /**
     * 会员卡是否使用
     */
    private boolean selected;
    /**
     * 会员卡是否可用
     */
    private boolean available;
    /**
     * 当会员卡不可用时,具体的提示信息
     */
    private String message;
    /**
     * 会员卡计算出来的优惠金额
     */
    private BigDecimal discount;
    /**
     * 会员卡优惠金额(计算出来之后修改的金额)
     */
    private BigDecimal finalDiscount;
    /**
     * 会员卡失效时间
     */
    private Date expireDate;
    /**
     * 会员卡优惠类型
     */
    private CardDiscountTypeEnum cardDiscountType;
    /**
     * 会员卡服务折扣类型
     */
    private CardServiceDiscountTypeEnum cardServiceDiscountType;
    /**
     * 会员卡配件折扣类型
     */
    private CardGoodsDiscountTypeEnum cardGoodsDiscountType;
    /**
     * 整单折扣
     */
    private BigDecimal orderDiscountRate;
    /**
     * 全部服务折扣
     */
    private BigDecimal serviceDiscountRate;
    /**
     * 全部配件折扣
     */
    private BigDecimal goodsDiscountRate;
    /**
     * 部分服务类别折扣
     */
    private Map<Long, BigDecimal> serviceCatDiscountRateMap;
    /**
     * 部分配件类别折扣(标准类别)
     */
    private Map<Long, BigDecimal> goodsStdCatDiscountRateMap;
    /**
     * 部分配件类别折扣(自定义类别)
     */
    private Map<Long, BigDecimal> goodsCustomCatDiscountRateMap;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户手机号
     */
    private String mobile;
    private Long accountId;

    /**
     * 过期时间
     *
     * @return
     */
    public String getExpireDateStr() {
        return DateFormatUtils.toYMD(expireDate);
    }
}
