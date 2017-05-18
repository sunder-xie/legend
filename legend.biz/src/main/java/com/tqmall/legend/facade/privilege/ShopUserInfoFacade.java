package com.tqmall.legend.facade.privilege;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.facade.privilege.bo.UserInfoQueryBo;
import com.tqmall.legend.facade.privilege.vo.UserInfoVo;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 */
public interface ShopUserInfoFacade {

    /**
     * 获取员工列表
     * @param userInfoQueryBo
     * @return
     */
    DefaultPage<UserInfoVo> getUserInfoPage(UserInfoQueryBo userInfoQueryBo);

    /**
     * 根据用户ids获取登录信息
     *
     * @param userIds
     * @return
     */
    Map<Long, ShopManagerLogin> getShopManagerLoginMapByShopIdAndUserIds(Long shopId, List<Long> userIds);

    /**
     * 根据门店id和岗位id判断岗位下是否存在用户
     *
     * @param roleId
     * @return
     */
    boolean isExistShopManagerByShopIdAndRoleId(Long shopId, Long roleId);

    /**
     * 是否可以添加用户
     *
     * @param shopId
     * @return
     */
    boolean isCanAddUser(Long shopId);
}
