package com.tqmall.legend.facade.privilege.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.util.BdUtil;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.sys.WhiteAddress;
import com.tqmall.legend.facade.privilege.PvgRoleFacade;
import com.tqmall.legend.facade.privilege.RolesFacade;
import com.tqmall.legend.facade.privilege.ShopUserInfoFacade;
import com.tqmall.legend.facade.privilege.WhiteAddressFacade;
import com.tqmall.legend.facade.privilege.bo.UserInfoQueryBo;
import com.tqmall.legend.facade.privilege.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/1/5.
 */
@Service
public class ShopUserInfoFacadeImpl implements ShopUserInfoFacade {
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopManagerLoginService shopManagerLoginService;
    @Autowired
    private RolesFacade rolesFacade;
    @Autowired
    private PvgRoleFacade pvgRoleFacade;
    @Autowired
    private WhiteAddressFacade whiteAddressFacade;
    @Autowired
    private ShopService shopService;

    /**
     * 获取门店员工列表
     *
     * @param userInfoQueryBo
     * @return
     */
    @Override
    public DefaultPage<UserInfoVo> getUserInfoPage(UserInfoQueryBo userInfoQueryBo) {
        List<UserInfoVo> userInfoVoList = Lists.newArrayList();
        int page = userInfoQueryBo.getOffset();
        int size = userInfoQueryBo.getLimit();
        PageRequest pageRequest = new PageRequest(page, size);
        Map<String, Object> searchParams = BdUtil.beanToMap(userInfoQueryBo);
        int total = shopManagerService.selectCount(searchParams);
        List<ShopManager> shopManagerList = shopManagerService.select(searchParams);
        if (CollectionUtils.isEmpty(shopManagerList)) {
            DefaultPage<UserInfoVo> userInfoVoPage = new DefaultPage<>(userInfoVoList, pageRequest, total);
            return userInfoVoPage;
        }
        //组装Vo对象
        buildUserInfoVoList(userInfoQueryBo, userInfoVoList, shopManagerList);
        DefaultPage<UserInfoVo> userInfoVoPage = new DefaultPage<>(userInfoVoList, pageRequest, total);
        return userInfoVoPage;
    }

    @Override
    public Map<Long, ShopManagerLogin> getShopManagerLoginMapByShopIdAndUserIds(Long shopId, List<Long> userIds) {
        Map<Long, ShopManagerLogin> shopManagerLoginMap = Maps.newHashMap();
        if(CollectionUtils.isEmpty(userIds)){
            return shopManagerLoginMap;
        }
        List<ShopManagerLogin> shopManagerLoginList = shopManagerLoginService.findByManagerIds(shopId, userIds);
        for(ShopManagerLogin shopManagerLogin : shopManagerLoginList){
            shopManagerLoginMap.put(shopManagerLogin.getManagerId(),shopManagerLogin);
        }
        return shopManagerLoginMap;
    }

    @Override
    public boolean isExistShopManagerByShopIdAndRoleId(Long shopId, Long roleId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("roleId", roleId);
        Integer selectCount = shopManagerService.selectCount(searchMap);
        if (selectCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCanAddUser(Long shopId) {
        Shop shop = shopService.selectById(shopId);
        Integer accountNum = shop.getAccountNum();
        if (accountNum == null || accountNum <= 0) {
            return true;
        }
        //查询门店账号子账号数量
        List<ShopManager> shopManagerList = shopManagerService.selectByShopId(shopId);
        Integer accountExistNum = shopManagerList.size();
        if (accountExistNum > accountNum) {
            return false;
        }
        return true;
    }

    /**
     * 组装Vo对象
     *
     * @param userInfoQueryBo
     * @param userInfoVoList
     * @param shopManagerList
     */
    private void buildUserInfoVoList(UserInfoQueryBo userInfoQueryBo, List<UserInfoVo> userInfoVoList, List<ShopManager> shopManagerList) {
        //查询门店角色
        Long shopId = userInfoQueryBo.getShopId();
        Map<Long, RolesL> rolesLMap = rolesFacade.getRolesLMapByShopId(shopId);
        //设置基本信息
        List<Long> userIds = Lists.newArrayList();
        for (ShopManager shopManager : shopManagerList) {
            UserInfoVo userInfoVo = new UserInfoVo();
            Long id = shopManager.getId();
            Long roleId = shopManager.getRoleId();
            userInfoVo.setId(id);
            userInfoVo.setName(shopManager.getName());
            userInfoVo.setIsAdmin(shopManager.getIsAdmin());
            userInfoVo.setMobile(shopManager.getMobile());
            userInfoVo.setRoleId(roleId);
            if (rolesLMap.containsKey(roleId)) {
                RolesL rolesL = rolesLMap.get(roleId);
                userInfoVo.setRolesName(rolesL.getName());
            }
            userInfoVoList.add(userInfoVo);
            userIds.add(id);
        }
        //查询门店岗位列表
        Map<Long, List<String>> rolesNameListMap = pvgRoleFacade.getRolesNameListMapByShopIdAndUserIds(shopId, userIds);
        //查询员工白名单信息
        Map<Long, WhiteAddress> whiteAddressMap = whiteAddressFacade.getWhiteAddressMapByUserIds(userIds);
        //查询员工登录信息
        Map<Long, ShopManagerLogin> shopManagerLoginMap = getShopManagerLoginMapByShopIdAndUserIds(shopId,userIds);
        for (UserInfoVo userInfoVo : userInfoVoList) {
            Long id = userInfoVo.getId();
            if (rolesNameListMap.containsKey(id)) {
                List<String> rolesNameList = rolesNameListMap.get(id);
                userInfoVo.setAppRoleList(rolesNameList);
            }
            if (whiteAddressMap.containsKey(id)) {
                WhiteAddress whiteAddress = whiteAddressMap.get(id);
                userInfoVo.setLoginBeginTime(whiteAddress.getLoginBeginTime());
                userInfoVo.setLoginEndTime(whiteAddress.getLoginEndTime());
            }
            if(shopManagerLoginMap.containsKey(id)){
                ShopManagerLogin shopManagerLogin = shopManagerLoginMap.get(id);
                userInfoVo.setAccount(shopManagerLogin.getAccount());
            }
        }
    }
}
