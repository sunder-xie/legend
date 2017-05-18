package com.tqmall.legend.biz.pvg;

import com.tqmall.legend.entity.pvg.PvgRole;

import java.util.List;

/**
 * 角色Service
 * <p/>
 * Created by dongc on 15/9/21.
 */
public interface IPvgRoleService {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    PvgRole findPvgRoleById(Long id);

    /**
     * 获取所有角色list
     * @return
     */
    List<PvgRole> selectPvgRoleList();
}
