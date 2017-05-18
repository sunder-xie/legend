package com.tqmall.legend.object.param.service;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by twg on 16/1/11.
 * 员工与角色实体
 */
@Data
public class UserRoleInfoParam implements Serializable {
    private Long id;//员工id
    private String name;//员工姓名
    private String mobile;//手机号
    private Long roleId;//岗位id
    private Long shopId;//门店id
    private Integer status = Integer.valueOf(1);//状态
    private Integer isAdmin;//标记管理员
    private String identifyingCode;//登陆验证码
    private Date sendCodeTime;//验证码发送时间
    private String nickName;//昵称
    private Integer gender;//性别
    private String focusCar;//关注车型
    private String userPhotoUrl;//用户头像链接
    private String identityCard;//身份证号码
    private Integer education;//学历
    private String graduateSchool;//毕业学校
    private Integer seniority;//维修工龄: 1: 1年以下  2:2-3年中工 3:3-5年师傅  4: 5-8年 5: 8年以上
    private String adeptRepair;//擅长维修品牌
    private Integer techLevel;//技师等级：1-初级 2-中级 3-高级
    private Integer techStatus;//认证状态：0-未认证 1-认证中 2- 认证通过

}
