package com.tqmall.legend.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * role base info
 * <p/>
 * Created by dongc on 15/9/21.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class RoleInfoResp {

    // 角色ID
    private Long roleId;//legend_pvg_user_org的pvg_role_id
    // 角色名称
    private String roleName;
}
