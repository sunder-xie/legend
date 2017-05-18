package com.tqmall.legend.entity.question;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class ExpertBonusRecord extends BaseEntity {

    private Long userId;//'用户
    private Long questionId;//'问题
    private Long answerId;//'回答
    private String bonusName;//奖金名称
    private BigDecimal bonusAmount;//奖金金额
    private Integer actionType;//'0:进账
    private Integer recordStatus;//'进账状态
    private Long withdrawId;//自关联提现记录id

}

