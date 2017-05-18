package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yanxinyin on 16/7/11.
 */
@Setter
@Getter
public class LineProcessManagerVO {
    private Long id;
    private Long lineProcessId;//生产线工序关联表id
    private Long managerId;//员工id
    private String managerName;//员工名称
    private Long teamId;//班组id
    private String teamName;//班组名称
    private String isDeleted;
}
