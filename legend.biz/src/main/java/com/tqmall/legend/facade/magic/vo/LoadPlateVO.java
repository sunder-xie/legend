package com.tqmall.legend.facade.magic.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by yanxinyin on 16/7/18.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class LoadPlateVO {
    private List<BalanceBoardVO> balanceBoardVOs;
    private PlateVO plateVOs;
    private Integer type;
    private String typeName;
    private Long shopId;

}
