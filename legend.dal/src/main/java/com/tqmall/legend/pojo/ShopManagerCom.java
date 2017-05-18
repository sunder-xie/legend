package com.tqmall.legend.pojo;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by QXD on 2014/10/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopManagerCom extends BaseEntity {

    private String accountReg; //账户名称
    private String nameReg;    //用户姓名
    private String mobileReg;
    private String passwordReg;
    private String newPasswordReg; //密码更新时的新密码
    private String codeReg;
    private Long accountIdReg;   //account
    private Long accountLoginIdReg;   //account
    private Long rolesReg;
    private Long roleId;// 岗位id,设置改版后的
    private Long shopReg;
    private Integer statusReg;
    private Integer isAdminReg;  //1,0
    private String rolesNameReg;  //角色名称
    private String identityCard;

    private String pvgRoleIds;   //APP 角色ID


    //--钣喷属性--
    private Long extId;
    private Long teamId;        //班组id   *
    private String teamName;    //*
    private Long processId;     //工序id *
    private String processName; //*
    private Integer workStatus; //0表示不在岗，1表示在岗*

    private String startTime; //登陆开始时间
    private String endTime;//登陆结束时间

    private Long shopId;
    private Long managerId;
}
