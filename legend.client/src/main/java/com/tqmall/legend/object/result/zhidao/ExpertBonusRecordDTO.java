package com.tqmall.legend.object.result.zhidao;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 16/3/30.
 */
@Data
public class ExpertBonusRecordDTO implements Serializable{
    private static final long serialVersionUID = 5860976121956980510L;
    private String createStr;//记录时间
    private String bonusName;//奖金名称
    private BigDecimal bonusAmount;//奖金金额
    private String questionContent;//问题内容
    private Integer actionType;
    private Integer recordStatus;
}