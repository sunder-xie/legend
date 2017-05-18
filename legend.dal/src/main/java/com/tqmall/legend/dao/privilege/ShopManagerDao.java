package com.tqmall.legend.dao.privilege;

import com.tqmall.legend.api.entity.ShopManagerResp;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.pojo.ShopManagerCom;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/29.
 */
@MyBatisRepository
public interface ShopManagerDao extends BaseDao<ShopManager> {

    /**
     * 根据shop_id查询员工
     *
     * @param shopId 店铺id
     */
    public List<ShopManager> selectShopManagerByShopId(@Param("shopId") Long shopId);

    /**
     * 根据shopId和manager_id查询员工
     *
     * @param managerId 角色id
     * @param shopId    店铺id
     */
    public ShopManager selectByShopIdAndManagerId(@Param("shopId") Long shopId,
                                                  @Param("managerId") Long managerId);

    /**
     * 根据shopId和manager_id查询员工
     *
     * @param managerId 角色id
     * @param shopId    店铺id
     */
    @Deprecated
    public ShopManagerCom selectUserInfoByShopIdAndManagerId(@Param("managerId") Long managerId,
                                                             @Param("shopId") Long shopId);

    public List<ShopManager> selectAll();

    /**
     * 根据shopIds批量查询管理员信息
     *
     * @param shopIds
     * @return
     */

    List<ShopManager> selectAdminByShopIds(@Param("shopIds") List<Long> shopIds);

    List<ShopManager> selectByNameOrMobile(@Param("shopId")Long shopId,@Param("keyword")String keyword);

    /**
     * 根据ids获取所有包括删除的员工
     * @param ids
     * @return
     */
    List<ShopManager> selectByIdsWithDeleted(Long[] ids);

}
