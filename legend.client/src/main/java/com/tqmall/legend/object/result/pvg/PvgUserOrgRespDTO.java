package com.tqmall.legend.object.result.pvg;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xiangdong.qu on 17/2/14 14:58.
 */
@Data
public class PvgUserOrgRespDTO implements Serializable {
    private Long userId;  //用户id
    private Long orgId;   //岗位id
    private Long roleId;  //角色id
    private String roleName; //角色名称
    private Long shopId;  //店铺id
}
