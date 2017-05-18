package com.tqmall.legend.object.param.zhidao;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 16/3/30.
 */
@Data
public class RpcBonusRecordParam implements Serializable{

    private static final long serialVersionUID = 6296551562145202822L;
    private Integer id;
    private Long answerId;//回答id
    private Long questionId;//问题id
    private String bonusName;//奖金名称
    private Long userId;//用户id
    private BigDecimal bonusAmount;//奖金金额

}
