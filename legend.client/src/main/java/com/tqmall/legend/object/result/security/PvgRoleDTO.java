package com.tqmall.legend.object.result.security;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/8/12.
 */
@Data
@ToString
public class PvgRoleDTO implements Serializable {
    private Long roleId;//角色id
    private String roleName;//角色名称
}
