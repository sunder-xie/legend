package com.tqmall.legend.entity.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.Date;

/**
 * 登录登出
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class LoginLogoutLog extends BaseEntity {

    private Date loginTime;
    private Date logoutTime;
    private Long managerLoginId;
    private String account;
    private Long shopId;
    private Integer refer;
    private String opUrl;
}