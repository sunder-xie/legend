package com.tqmall.legend.web.account.vo;

import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.util.Date;

/**
 * Created by wanghui on 04/01/2017.
 */
@Data
public class CardUpgradeCardInfo {
    /**
     * 会员卡类型id
     */
    private Long cardInfoId;
    /**
     * 会员卡类型名称
     */
    private String cardInfoName;
    /**
     * 会员卡过期时间
     */
    private Date expireTime;

    public String getExpireTimeStr() {
        return DateFormatUtils.toYMD(getExpireTime());
    }
}
