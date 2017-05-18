package com.tqmall.legend.biz.privilege.vo;

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
    private Long managerLoginId;   //manager_login表的id
    private String identityCard;
    private String startTime; //登陆开始时间
    private String endTime;//登陆结束时间

    //--钣喷属性--
    private Long extId;         //员工扩展表id
    private Long teamId;        //班组id
    private String teamName;    //班组项目
    private Long processId;        //工序id
    private String processName;    //工序名称
    private Integer workStatus;    //是否在岗，0否1是
}
