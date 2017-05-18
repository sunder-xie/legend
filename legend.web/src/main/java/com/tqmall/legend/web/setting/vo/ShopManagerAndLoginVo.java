package com.tqmall.legend.web.setting.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by twg on 17/1/9.
 */
@Getter
@Setter
public class ShopManagerAndLoginVo {
    private Long shopId;
    private Long userId;//当前登录用户的id
    private String name;    //用户姓名
    private Long roleId;// 岗位id
    private String mobile;
    private String account; //账户名称
    private Long managerId;   //manager表的id
    private Integer isAdmin;  //1,0
    private String password;
    private String rolesName;  //角色名称
    private String pvgRoleIds;   //APP 角色ID，逗号隔开
    private String newPassword; //密码更新时的新密码
    private Long managerLoginId;   //manager_login表的id
    private String identityCard;


    //--钣喷属性--
    private Long extId;
    private Long teamId;        //班组id   *
    private String teamName;    //*
    private Long processId;     //工序id *
    private String processName; //*
    private Integer workStatus; //0表示不在岗，1表示在岗*

    private String startTime; //登陆开始时间
    private String endTime;//登陆结束时间



}
