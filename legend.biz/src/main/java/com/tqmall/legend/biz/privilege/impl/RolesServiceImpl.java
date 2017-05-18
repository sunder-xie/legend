package com.tqmall.legend.biz.privilege.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.legend.biz.privilege.RolesService;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.dao.privilege.RolesDao;
import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.pojo.ShopManagerCom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/30.
 */
@Service
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesDao rolesDao;

    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopManagerLoginService shopManagerLoginService;

    @Override
    public Roles selectById(Object id) {
        return rolesDao.selectById(id);
    }

    @Override
    @Transactional
    public int update(Roles roles) {
        return rolesDao.updateById(roles);
    }

    @Override
    @Transactional
    public int save(Roles roles) {
        return rolesDao.insert(roles);
    }

    @Override
    public List<Roles> select(Map searchMap) {
        return rolesDao.select(searchMap);
    }

    @Override
    public Integer selectCount(Map searchMap) {
        return rolesDao.selectCount(searchMap);
    }

    @Override
    public Integer deleteById(Long id) {
        return rolesDao.deleteById(id);
    }

    /**
     * @param shopId 店铺id
     * @return 角色列表
     * 根据shop_id查询店铺的角色 以角色level_id排序
     */
    @Override
    public List<RolesL> selectRolesByShopId(long shopId) {
        List<RolesL> shopRolesList = rolesDao.selectRolesByShopId(shopId);
        return shopRolesList;
    }

    /**
     * @param shopId 店铺id
     * @return 树状结构
     * 根据shop_id查询店铺的角色
     */
    @Override
    public RolesL selectRolesByShopIdL(long shopId) {
        List<RolesL> shopRolesList = selectRolesByShopId(shopId);
        if (CollectionUtils.isEmpty(shopRolesList)) {
            return null;
        }
        return sortShopRolesList(shopRolesList);
    }

    /**
     * 将全部角色列表转化为一棵角色树
     *
     * @param shopRolesList 角色列表
     * @return RolesL 角色树根节点
     */
    private RolesL sortShopRolesList(List<RolesL> shopRolesList) {
        /*根节点，第一层*/
        RolesL root = null;
        for (RolesL rolesl : shopRolesList) {
            if (rolesl.getParentId().compareTo(Long.valueOf(Constants.ZERO_FLAG)) == 0
                    && rolesl.getLevelId() == Constants.ZERO_FLAG) {
                root = rolesl;
                break;
            }
        }
        if (null == root) {
            return null;
        }
        root = getRolesLForTree(root, shopRolesList);
        return root;
    }


    /**
     * @迭代生成 树
     * 用于获取所有的角色列表
     */
    private RolesL getRolesLForTree(RolesL roles, List<RolesL> rolesList) {
        List<RolesL> rolesListForRolesL = new ArrayList<RolesL>();
        for (RolesL rolesl : rolesList) {
            if (rolesl.getParentId().compareTo(roles.getId()) == 0) {
                RolesL result = getRolesLForTree(rolesl, rolesList);
                rolesListForRolesL.add(result);
            }
        }
        roles.setData(rolesListForRolesL);
        return roles;
    }

    @Override
    public List<ShopManagerCom> findByShopIdAndRoleId(Long shopId, Long roleId) {
        List<ShopManager> shopManagers = shopManagerService.findByShopIdAndRoleId(shopId, roleId);
        List<Long> managerIds = Lists.transform(shopManagers, new Function<ShopManager, Long>() {
            @Override
            public Long apply(ShopManager input) {
                return input.getId();
            }
        });
        if(CollectionUtils.isEmpty(managerIds)){
            return Lists.newArrayList();
        }
        List<ShopManagerLogin> shopManagerLogins = shopManagerLoginService.findByManagerIds(shopId, managerIds);
        Map<Long,ShopManagerLogin> shopManagerLoginMap = Maps.uniqueIndex(shopManagerLogins, new Function<ShopManagerLogin, Long>() {
            @Override
            public Long apply(ShopManagerLogin input) {
                return input.getManagerId();
            }
        });

        List<ShopManagerCom> shopManagerComList = Lists.newArrayList();
        for (ShopManager shopManager : shopManagers) {
            if(shopManagerLoginMap.containsKey(shopManager.getId())){
                ShopManagerCom shopManagerCom = new ShopManagerCom();
                shopManagerCom.setNameReg(shopManager.getName());
                shopManagerCom.setMobileReg(shopManager.getMobile());
                shopManagerCom.setRolesReg(shopManager.getRoleId());
                shopManagerCom.setShopReg(shopManager.getShopId());
                shopManagerCom.setStatusReg(shopManager.getStatus());
                ShopManagerLogin shopManagerLogin = shopManagerLoginMap.get(shopManager.getId());
                shopManagerCom.setAccountIdReg(shopManagerLogin.getManagerId());
                shopManagerCom.setAccountReg(shopManagerLogin.getAccount());
                shopManagerCom.setIsAdminReg(shopManager.getIsAdmin());
                shopManagerCom.setIdentityCard(shopManager.getIdentityCard());
                shopManagerComList.add(shopManagerCom);
            }
        }
        return shopManagerComList;
    }
}
