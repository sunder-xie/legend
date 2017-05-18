package com.tqmall.legend.enums.privilege;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * APP角色枚举
 */
public enum PvgRoleEnum {
    GLY(1, "管理员", false),
    DZ(2, "店长", false),
    KF(3, "客服", false),
    CG(4, "仓管", false),
    JS(5, "技师", false),
    CW(6, "财务", false);

    private final Integer id;//APP角色id
    private final String roleName;//角色名称
    private boolean exist;//员工是否是此角色，true是，false不是

    PvgRoleEnum(Integer id, String roleName, boolean exist) {
        this.id = id;
        this.roleName = roleName;
        this.exist = exist;
    }

    public Integer getId() {
        return this.id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public static List<PvgRoleEnum> getAppRoleList(Long pvgRoleId) {
        List<PvgRoleEnum> pvgRoleEnumList = Lists.newArrayList();
        for (PvgRoleEnum pvgRoleEnum : values()) {
            if (pvgRoleEnum.getRoleName().equals(PvgRoleEnum.GLY.getRoleName())) {
                continue;
            }
            if (pvgRoleId != null && pvgRoleEnum.getId() == pvgRoleId.intValue()) {
                pvgRoleEnum.setExist(true);
            } else {
                pvgRoleEnum.setExist(false);
            }
            pvgRoleEnumList.add(pvgRoleEnum);
        }
        return pvgRoleEnumList;
    }

    /**
     * 获取app角色
     *
     * @param pvgUerOrgMap 已存在的员工app角色id
     * @return
     */
    public static List<PvgRoleEnum> getAppRoleList(Map<Long, Long> pvgUerOrgMap) {
        List<PvgRoleEnum> pvgRoleEnumList = Lists.newArrayList();
        for (PvgRoleEnum pvgRoleEnum : values()) {
            if (pvgRoleEnum.getRoleName().equals(PvgRoleEnum.GLY.getRoleName())) {
                continue;
            }
            Long id = pvgRoleEnum.getId().longValue();
            if (pvgUerOrgMap != null && pvgUerOrgMap.containsKey(id)) {
                pvgRoleEnum.setExist(true);
            } else {
                pvgRoleEnum.setExist(false);
            }
            pvgRoleEnumList.add(pvgRoleEnum);
        }
        return pvgRoleEnumList;
    }

    public boolean getExist() {
        return this.exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}