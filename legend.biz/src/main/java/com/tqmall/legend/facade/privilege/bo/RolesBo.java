package com.tqmall.legend.facade.privilege.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/1/9.
 */
@Getter
@Setter
public class RolesBo {
    private Long id;  //岗位id
    private Long shopId;  //门店id
    private Long userId;//登录人id
    private Long parentId;//父id
    private String name;//岗位名称
    private Integer levelId;//岗位等级
    private Integer pvgRoleId;//岗位角色id  legend_pvg_role的id   1管理员2店长3客服4仓管5技师6财务
}
