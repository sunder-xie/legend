package com.tqmall.legend.biz.privilege;

import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.pojo.ShopManagerCom;

import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/30.
 */
public interface RolesService {
    /**
     * 根据shop_id查询店铺的角色
     *
     * @param shopId 店铺id
     * @return 角色列表
     */
    List<RolesL> selectRolesByShopId(long shopId);

    /**
     * 根据shop_id查询店铺的角色
     *
     * @param shopId 店铺id
     * @return 树状列表
     */
    RolesL selectRolesByShopIdL(long shopId);

    /**
     * 获取角色id下的员工列表
     * @param shopId 门店id
     * @param roleId 角色id
     * @return
     */
    List<ShopManagerCom> findByShopIdAndRoleId(Long shopId,Long roleId);

    Roles selectById(Object id);

    /**
     * update roles
     *
     * @param roles
     * @return
     */
    int update(Roles roles);

    /**
     * save roles
     *
     * @param roles
     * @return
     */
    int save(Roles roles);


    /**
     * 通用查询
     * @param searchMap
     * @return
     */
    List<Roles> select(Map searchMap);

    /**
     * 查询
     * @param searchMap
     * @return
     */
    Integer selectCount(Map searchMap);

    /**
     * 更加岗位id删除
     * @param id
     * @return
     */
    Integer deleteById(Long id);
}
