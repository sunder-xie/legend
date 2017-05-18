package com.tqmall.legend.entity.pvg;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PvgUserOrg extends BaseEntity {

    private Long userId;
    private Long orgId;
    private Long pvgRoleId;
    private Long shopId;

}

