package com.tqmall.legend.object.result.pvg;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xiangdong.qu on 17/2/14 16:50.
 */
@Data
public class RoleInfoRespDTO implements Serializable {
    // 角色ID
    private Long roleId;
    // 角色名称
    private String roleName;
}
