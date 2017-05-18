package com.tqmall.legend.entity.privilege;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by QXD on 2014/10/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopManagerLogin extends BaseEntity {

    private Long managerId;
    private String account;
    private String password;
    private Long shopId;
    private Long isFormalUser;
    private Integer isAdmin;
    //密码转小写的md5值
    private String lowerPassword;
}
