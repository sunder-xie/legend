package com.tqmall.legend.facade.magic.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 生产线工序关系包装对应技师
 * Created by haojiahong on 16/7/14.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class PLineProcessVO {
    private Long id;//id
    private Long shopId;//所属门店id
    private Long lineId;//生产线id
    private String lineName;//生产线名称
    private Long processSort;//排序号
    private Long processId;//工序id
    private String processName;//工序名称
    private String workTime;//施工时间
    private List<LineProcessManagerVO> lineProcessManagerVOList;//生产线工序对应技师

}
