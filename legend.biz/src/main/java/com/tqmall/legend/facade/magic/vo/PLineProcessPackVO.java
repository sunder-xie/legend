package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by haojiahong on 16/7/14.
 */
@Data
public class PLineProcessPackVO {
    private Long lineId;//生产线id
    private String lineName;//生产线name
    private List<PLineProcessVO> pLineProcessVOList;//生产线工序关系集合
}
