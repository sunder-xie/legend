package com.tqmall.legend.entity.privilege;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by QXD on 2014/11/9.
 * 带list的Func
 * 用于功能菜单
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class FuncF {
    private Long id;
    private Long rolesId;  //当前角色
    private String name;
    private String value;
    private Long parentId;
    private Integer type;
    private Long managerId; //用户id
    private Long shopId;
    private Integer sortId;
    private Integer shopLevel;//门店版本，6档口版,9云修版

    private List<FuncF> funcFList;  //用于2级功能列表
}
