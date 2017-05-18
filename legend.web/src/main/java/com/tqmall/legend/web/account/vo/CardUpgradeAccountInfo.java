package com.tqmall.legend.web.account.vo;

import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wanghui on 04/01/2017.
 */
@Data
public class CardUpgradeAccountInfo {

    private Long cardId;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 会员卡
     */
    private String cardType;
    /**
     * 会员卡号
     */
    private String cardNumber;
    /**
     * 会员卡余额
     */
    private BigDecimal cardBalance;
    /**
     * 累计充值金额
     */
    private BigDecimal totalChargeAmount;
    /**
     * 过期时间
     */
    private Date expireTime;

    public String getExpireTimeStr() {
        return DateFormatUtils.toYMD(getExpireTime());
    }
    /**
     * 可升级的会员卡类型
     */
    private List<CardUpgradeCardInfo> cardInfoList;
}
