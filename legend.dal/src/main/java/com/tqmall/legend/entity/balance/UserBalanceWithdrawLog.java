package com.tqmall.legend.entity.balance;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xiangDong.qu on 15/12/22.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserBalanceWithdrawLog extends BaseEntity {

    private Long balanceLogId;//提现记录id(legend_user_balance_log表id)
    private Integer handleStatus;//'操作状态
    private String handleStatusName;//提现状态名称
    private String handleComment;//提现状态变更备注

}


