package com.tqmall.legend.entity.privilege;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by QXD on 2014/10/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Roles extends BaseEntity {
    private String name;
    private Long shopId;
    private Long parentId;
    private Integer levelId;
    private Integer pvgRoleId;
}
