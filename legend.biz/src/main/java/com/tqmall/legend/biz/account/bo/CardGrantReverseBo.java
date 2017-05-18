package com.tqmall.legend.biz.account.bo;

import com.tqmall.legend.entity.account.AccountTradeFlow;
import lombok.Data;

/**
 * Created by majian on 16/7/18.
 */
@Data
public class CardGrantReverseBo {
    private Long shopId;
    private Long userId;
    private String userName;//操作人姓名
    private AccountTradeFlow flow;//正流水
}
