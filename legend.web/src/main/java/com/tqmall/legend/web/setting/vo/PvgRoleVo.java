package com.tqmall.legend.web.setting.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/1/10.
 */
@Getter
@Setter
public class PvgRoleVo {
    private Long id;//APP角色id
    private String roleName;//角色名称
    private boolean exist;//员工是否是此角色，true是，false不是
}
