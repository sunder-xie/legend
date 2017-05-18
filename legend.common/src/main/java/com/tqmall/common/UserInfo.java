package com.tqmall.common;

import lombok.Data;

/**
 * Created by litan on 14-11-5.
 */
@Data
public class UserInfo {

    /**
     * 用戶角色
     * session未设置
     */
    private String userRole;

    /**
     * 用户账户
     * session未设置
     */
    private String account;

    /**
     * 名称
     */
    private String name;

    /**
     * 用戶权限
     */
    private String userRoleFunc;

    /**
     * 用戶所属门店ID
     */
    private Long shopId;

    /**
     * 用戶ID
     */
    private Long userId;

    /**
     * 用戶是否是管理员
     */
    private Integer userIsAdmin;

    /**
     * 角色id
     */
    private Long rolesId;

    /**
     * 门店版本，6档口版,9云修版
     */
    private Integer level;

    /**
     * 用户账号登录表ID
     * session未设置
     */
    private Long managerLoginId;

    /**
     * uc的shopId
     */
    private String userGlobalId;
}
