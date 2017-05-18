package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by shulin on 16/7/1.
 */
@Data
public class PaintSpeciesVO {
    private Long Id;
    private Long shopId;//门店id
    private Long paintLevelId;//面漆级别id
    private String name;//面漆种类名称
    private String paintLevelName;//面漆级别名称
    private BigDecimal price;//油漆单价
    private String remark;//备注
}
