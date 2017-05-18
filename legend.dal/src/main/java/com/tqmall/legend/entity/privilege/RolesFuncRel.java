package com.tqmall.legend.entity.privilege;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by QXD on 2014/10/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class RolesFuncRel extends BaseEntity {
    private Long rolesId;
    private Long funcId;
    private Long shopId;
}
