package com.tqmall.legend.entity.pvg;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PvgOrgTemplate extends BaseEntity {

    private String name;
    private Integer level;
    private Long parentId;
    private Long pvgRoleId;
    private Integer type;

}

