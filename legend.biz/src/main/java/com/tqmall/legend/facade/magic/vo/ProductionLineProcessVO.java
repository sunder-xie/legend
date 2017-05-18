package com.tqmall.legend.facade.magic.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by yanxinyin on 16/7/9.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class ProductionLineProcessVO {
    private Long id;//id
    private Long shopId;//所属门店id
    private Long lineId;//生产线id
    private String lineName;//生产线名称
    private String type;//生产线类型：1：快修线2：事故线3：快喷线
    private String remark;//生产线类型：1：快修线2：事故线3：快喷线
    private Boolean isReScheduling;
    private List<ProcessVO> processVOList;
}
