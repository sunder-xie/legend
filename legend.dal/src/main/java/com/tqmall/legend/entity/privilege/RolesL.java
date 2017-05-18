package com.tqmall.legend.entity.privilege;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QXD on 2014/11/6.
 * 添加角色的List列表
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class RolesL {
    private Long id;
    private String name;
    private Long shopId;
    private Long parentId;
    private Integer levelId;
    private Long pvgRoleId;
    private List<RolesL> data = new ArrayList<RolesL>();
}
