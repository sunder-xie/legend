package com.tqmall.legend.object.result.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/4/10.
 */
@Data
@EqualsAndHashCode
@ToString
public class RoleInfoDTO implements Serializable {
    private static final long serialVersionUID = -1956058714447314789L;
    // 角色ID
    private Long roleId;
    // 角色名称
    private String roleName;
}
