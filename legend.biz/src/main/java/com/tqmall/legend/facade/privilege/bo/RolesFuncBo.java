package com.tqmall.legend.facade.privilege.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 2017/01/09.
 * 更新岗位权限功能
 */
@Getter
@Setter
public class RolesFuncBo {
    private Long userId;    //用户id
    private Long shopId;    //门店id
    private Long rolesId; //岗位id
    private String funcIdsStr;  //权限ids,用，隔开
    private Long parentId;  //父id
}
