package com.tqmall.legend.facade.privilege.vo;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zsy on 17/1/4.
 */
@Getter
@Setter
public class UserInfoVo {

    /**
     * legend_shop_manager字段
     */
    private Long id;
    private String name;//员工姓名
    private String mobile;//联系电话
    private Long roleId;//岗位id   关联legend_roles的id
    private String rolesName;//岗位名称   legend_roles的name字段
    private Integer isAdmin;//是否是管理员

    /**
     * legend_shop_manager_login字段
     */
    private String account;//登录账号

    /**
     * legend_white_address字段
     */
    private String loginBeginTime;//登陆开始时间
    private String loginEndTime;//登陆结束时间

    /**
     * legend_pvg_user_org字段
     */
    private String appRoles;//app角色（多条数据）
    private List<String> appRoleList;//app角色

    public String getAppRoles() {
        if (CollectionUtils.isEmpty(appRoleList)) {
            return "";
        }
        StringBuffer nameSb = new StringBuffer();
        for (String name : appRoleList) {
            nameSb.append(name);
            nameSb.append("/");
        }
        return nameSb.substring(0, nameSb.length() - 1);
    }
}
