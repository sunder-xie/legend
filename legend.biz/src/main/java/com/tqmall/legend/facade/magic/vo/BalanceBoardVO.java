package com.tqmall.legend.facade.magic.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by yanxinyin on 16/7/14.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BalanceBoardVO {
    private List<LineBalanceVO> lineBalanceVOs;
    private String LineName;

}
