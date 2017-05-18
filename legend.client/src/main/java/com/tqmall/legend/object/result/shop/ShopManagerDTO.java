package com.tqmall.legend.object.result.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiangDong.qu on 16/4/13.
 */
@Data
@EqualsAndHashCode
@ToString
public class ShopManagerDTO implements Serializable {
    private Long id;
    private String isDeleted;
    private Date gmtCreate;
    private Long creator;
    private Date gmtModified;
    private Long modifier;
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
    private Integer education;
    private String graduateSchool;
}
