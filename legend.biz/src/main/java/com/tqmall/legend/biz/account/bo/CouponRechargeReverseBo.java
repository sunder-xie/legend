package com.tqmall.legend.biz.account.bo;

import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.CouponServiceRel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by majian on 16/7/14.
 */
@Data
public class CouponRechargeReverseBo {
    private Long shopId;
    private Long userId;
    private String userName;//操作人姓名
    private AccountTradeFlow flow;//正流水
}
