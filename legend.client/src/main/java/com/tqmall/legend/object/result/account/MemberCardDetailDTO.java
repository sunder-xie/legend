package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by majian on 16/9/9.
 * 会员卡详情(微信)
 */
@Data
public class MemberCardDetailDTO implements Serializable {
    private String cardName;//会员卡名称
    private String cardNumber;//会员卡编号
    private Integer discountType;//折扣范围 0.无折扣;1.全部工单折扣;2.全部服务折扣;3.全部配件折扣;
    private BigDecimal orderDiscount;//工单折扣 discountType = 1 时可用
    private BigDecimal goodsDiscount;//服务折扣 discountType = 3 时可用
    private BigDecimal serviceDiscount;//配件折扣 discountType = 2 时可用
    private Date expireDate;//失效日期
    private BigDecimal usedAmount;//已消费金额
    private Integer usedCount;//已消费次数
    private BigDecimal depositAmount;//累计充值金额
    private Integer depositCount;//累计充值次数
    private BigDecimal balance;//余额
    private Long cardTypeId;//会员卡类型id
}
