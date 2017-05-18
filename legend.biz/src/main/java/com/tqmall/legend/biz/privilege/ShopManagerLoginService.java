package com.tqmall.legend.biz.privilege;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.view.AddUser;

import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/31.
 */
public interface ShopManagerLoginService {

    public Result updateById(ShopManagerLogin shopManagerLogin);

    public List<ShopManagerLogin> select(Map<String, Object> param);

    /**
     * 获取用户登录信息
     *
     * @param userId 员工ID
     * @param shopId 门店ID
     * @return
     */
    Optional<ShopManagerLogin> get(Long userId, Long shopId);

    ShopManagerLogin selectShopManagerByManagerId(Long managerId);

    /**
     * 根据门店和员工ids，获取信息
     * @param shopId
     * @param managerIds
     * @return
     */
    List<ShopManagerLogin> findByManagerIds(Long shopId,List<Long> managerIds);

    /**
     * 根据门店和员工id，获取信息
     * @param shopId
     * @param managerId
     * @return
     */
    ShopManagerLogin findByShopIdAndManagerId(Long shopId,Long managerId);

    ShopManagerLogin findByShopIdAndAccount(Long shopId,String account);

    void updateByManagerId(ShopManagerLogin shopManagerLogin);

}
