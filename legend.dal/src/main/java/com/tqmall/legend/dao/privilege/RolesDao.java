package com.tqmall.legend.dao.privilege;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.RolesL;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by QXD on 2014/10/29.
 */
@MyBatisRepository
public interface RolesDao extends BaseDao<Roles> {

    /**
     * 根据shop_id查询店铺的角色
     *
     * @param shopId 店铺id
     */
    public List<RolesL> selectRolesByShopId(Long shopId);

    /**
     * 根据shop_id,parent_id查询店铺的角色
     *
     * @param rolesL 传入的参数
     */
    public List<RolesL> selectRolesByShopIdAndParentIdL(RolesL rolesL);

    /**
     * 根据parent_id,shop_id,roleName查重
     *
     * @param roles 传入的参数
     */
    public Roles selectRolesByShopIdAndParentIdAndRoleName(Roles roles);


}
