package com.tqmall.legend.facade.magic.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by yanxinyin on 16/7/19.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class LineBalanceVO {
    private Long lineType;
    private String lineName;
    private Long processId;
    private String processName;
    private Long managerId;//员工id
    private String managerName;//员工名称
    private Long teamId;//班组id
    private String teamName;//班组名称
    private List<CarVO> carVOList;

}
