package com.tqmall.legend.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色信息
 * <p/>
 * Created by dongc on 15/9/21.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PvgUserOrgResp {
    private Long userId;
    private Long orgId;
    private Long roleId;
    private String roleName;
    private Long shopId;
}
