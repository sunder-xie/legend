package com.tqmall.legend.biz.pvg;

import com.google.common.base.Optional;
import com.tqmall.legend.api.entity.PvgUserOrgResp;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.entity.pvg.PvgUserOrg;

import java.util.List;
import java.util.Map;

/**
 * 用户岗位关系表Service
 * <p/>
 * Created by dongc on 15/9/21.
 */
public interface IPvgUserOrgService {

    /**
     * 获取关联的角色列表
     *
     * @param managerId 用户ID
     * @param shopId    门店ID
     * @return Optional<List<RoleInfoResp>>
     */
    Optional<List<RoleInfoResp>> getReferRoleList(Integer managerId, Long shopId);

    /**
     * 获取所有角色
     *
     * @param shopId 门店ID
     * @return Optional<List<RoleInfoResp>>
     */
    Optional<List<PvgUserOrgResp>> getReferRoleList(Long shopId);

    /**
     * 插入用户岗位角色关系
     * @param pvgUserOrg
     * @return
     */
    public int insert(PvgUserOrg pvgUserOrg);

    /**
     * 通用查询
     * @param searchMap
     * @return
     */
    public List<PvgUserOrg> select(Map searchMap);


    /**
     * 通用删除
     * @param id
     * @return
     */
    public int delete(Long id);

    /**
     * 通用更新
     * @param pvgUserOrg
     * @return
     */
    public Integer update(PvgUserOrg pvgUserOrg);

    /**
     * 获取
     * @param shopId
     * @param userId
     * @return
     */
    List<PvgUserOrg> findByShopIdAndUserId(Long shopId,Long userId);

}
