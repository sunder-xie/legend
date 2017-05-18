package com.tqmall.legend.facade.magic.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by yanxinyin on 16/7/19.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class LoadVO {
    private List<LoadPlateVO> loadPlateVO;
    private Integer carNumberQuick;//快修或者快喷
    private Integer carNumberSlow; //事故
    private Integer carNumberInterrupt;//中断
}
