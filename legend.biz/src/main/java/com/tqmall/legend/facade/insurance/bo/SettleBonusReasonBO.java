package com.tqmall.legend.facade.insurance.bo;

import lombok.Data;

/**
 * Created by zxg on 17/2/8.
 * 15:14
 * no bug,以后改代码的哥们，祝你好运~！！
 * 奖励金原因列表
 */
@Data
public class SettleBonusReasonBO {
    // 原因的action，唯一标识
    private String reasonAction;
    // 原因的名称
    private String reasonName;
}
