package com.tqmall.legend.biz.account.vo;

import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanghao on 16/6/16.
 */
@Getter
@Setter
public class MemberCardVo {
    private Long accountId;
    private Long memberCardId;
    // 客户姓名
    private String customerName;
    // 联系电话
    private String customerMobile;
    private Long cardTypeId;
    // 会员卡类型
    private String cardTypeName;
    // 卡号
    private String cardNum;
    // 累计收款金额
    private BigDecimal totalPayAmount;
    // 累计充值金额
    private BigDecimal depositAmount;
    // 卡余额
    private BigDecimal balance;
    // 办卡日期
    private Date grantTime;
    // 过期日期
    private Date expireDate;

    public String getGrantTimeStr() {
        if (grantTime == null) {
            return "";
        }
        return DateFormatUtils.toYMD(grantTime);
    }

    public String getExpireDateStr() {
        if (expireDate == null) {
            return "";
        }
        return DateFormatUtils.toYMD(expireDate);
    }
}
