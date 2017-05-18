package com.tqmall.legend.object.result.security;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 16/8/11.
 * 用户登录后返回的对象
 */
@Data
@ToString
public class UserInfoDTO implements Serializable {

    private static final long serialVersionUID = -2758499095393264161L;

    private String errorMsg;//错误信息

    /**
     * 门店信息
     */
    private boolean isTqmallVersion;//是否是档口版门店，true是，false不是
    private boolean isAgreeProtocol;//是否同意协议，true是，false不是
    private Long shopId;//门店id
    private String shopName;//门店名称
    private String userGlobalId;//uc的shopId
    private Integer shopStatus;//门店状态：1:开通,2:冻结,3:测试,4:试用
    private Integer shopLevel;//门店版本：6档口版,9云修版

    /**
     * 用户信息
     */
    private Long managerId;//用户id
    private String name;//用户姓名
    private Long roleId;//角色id
    private Integer isAdmin;//是否是管理员 1管理员0普通用户
    private String nickName;//昵称
    private String identityCard;//身份证号码

    /**
     * 登录信息
     */
    private String account;//登录账户
    private String password;//登录密码 MD5后的
    private Long shopManagerLoginId;//登录账户id
    private Long isFormalUser; // 是否为正式用户（1为临时用户，0或null为正式用户）

    /**
     * 角色列表
     */
    private List<PvgRoleDTO> pvgRoleList;//角色列表
}
