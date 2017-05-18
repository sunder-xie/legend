package com.tqmall.legend.dao.privilege;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.privilege.RolesFuncRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by QXD on 2014/10/28.
 */

@MyBatisRepository
public interface RolesFuncRelDao extends BaseDao<RolesFuncRel> {
    /**
     * 根据角色Id查询角色所拥有的功能Id
     *
     * @param shopId  店铺id
     * @param rolesId 角色Id
     */
    List<Long> selectFuncIdsByRolesId(@Param("shopId") Long shopId,@Param("rolesId") Long rolesId);
}
