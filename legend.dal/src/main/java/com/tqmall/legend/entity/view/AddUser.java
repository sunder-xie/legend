package com.tqmall.legend.entity.view;

import lombok.Data;

/**
 * Created by QXD on 2014/10/31.
 */
@Data
public class AddUser {
    //添加用户的信息
    private String preUserAccount;
    private String userAccount;
    private String userName;
    private String userPassword;
    private Long userRoleId;
    private String userMobile;
    private String pvgRoleIds;


    //--钣喷中心-
    private Long extId;
    private Long teamId;        //班组id   *
    private String teamName;    //*
    private Long processId;     //工序id *
    private String processName; //*
    private Integer workStatus; //0表示不在岗，1表示在岗*

    private String startTime;//开始时间
    private String endTime;//结束时间

    private Long shopId;
    private Long userId;
    private String addAcount;

}
