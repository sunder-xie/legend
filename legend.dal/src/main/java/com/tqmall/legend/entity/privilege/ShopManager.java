package com.tqmall.legend.entity.privilege;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by QXD on 2014/10/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopManager extends BaseEntity {

    private String name;
    private String mobile;
    private Long roleId;
    private Long shopId;
    private Integer status;
    private Integer isAdmin;
    private String identifyingCode;
    private Date sendCodeTime;
    private String nickName;
    private Integer gender;
    private String focusCar;
    private String userPhotoUrl;
    private String identityCard;
    /** 学历 */
    private Integer education;
    /** 毕业学校 */
    private String graduateSchool;

    /*展示用 角色名*/
    private String rolesName;

    private String startTime; //登陆开始时间
    private String endTime;//登陆结束时间
    private Long userId;

    @Override
    public String toString() {
        return "ShopManager{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", roleId=" + roleId +
                ", shopId=" + shopId +
                ", status=" + status +
                ", isAdmin=" + isAdmin +
                ", identifyingCode='" + identifyingCode + '\'' +
                ", sendCodeTime=" + sendCodeTime +
                ", nickName='" + nickName + '\'' +
                ", gender=" + gender +
                ", focusCar='" + focusCar + '\'' +
                ", userPhotoUrl='" + userPhotoUrl + '\'' +
                ", identityCard='" + identityCard + '\'' +
                "} " + super.toString();
    }
}
