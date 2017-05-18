package com.tqmall.legend.object.param.security;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lilige on 16/9/8.
 */
@Data
public class ManagerDeviceDTO implements Serializable {
    private static final long serialVersionUID = -7361801178921695628L;
    //员工头像
    private String userPhotoUrl;
    //姓名
    private String name;
    //角色名称
    private String roleName;
    //设备绑定状态
    private Integer deviceStatus;

    private Long shopId;

    private Long userId;

}
