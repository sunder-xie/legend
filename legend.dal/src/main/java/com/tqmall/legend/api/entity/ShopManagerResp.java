package com.tqmall.legend.api.entity;

import lombok.Data;

/**
 * 管理员基本信息实体
 * <p/>
 * Created by dongc on 15/9/21.
 */
@Data
public class ShopManagerResp {

    // TODO 字段为空是，不暴露NULL
    private String name;
    private String mobile;
    private Long roleId;
    private Long shopId;
    private Integer status;
    private Integer isAdmin;
    private String identifyingCode;
    private String nickName;
    private Integer gender;
    private String focusCar;
    private String userPhotoUrl;
    private String identityCard;
    private Integer education;
    private String graduateSchool;

    // 岗位基本信息
    private String postName;
    // 店铺名称
    private String shopName;
    // 店铺地址
    private String shopAddress;
    //商家状态
    private Integer shopStatus;
    // 关联角色list
    private Object roleList;
    //门店安全等级
    private String saveLevel;
    //签约安心类型
    private String axInsuranceModel; //0:1,2,3 1:1, 2:2,3 模式

}
