package com.tqmall.legend.biz.sys.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.privilege.RolesDao;
import com.tqmall.legend.dao.privilege.ShopManagerDao;
import com.tqmall.legend.dao.privilege.ShopManagerLoginDao;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.pojo.ShopManagerCom;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserInfoServiceImpl extends BaseServiceImpl implements UserInfoService {

    private static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private ShopManagerLoginDao shopManagerLoginDao;

    @Autowired
    private ShopManagerDao shopManagerDao;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private RolesDao rolesDao;
    @Autowired
    private CacheComponent cacheComponent;
    /**
     * 用户登录（包含门店和门店子账户）
     *
     * @param userName 登录账号，可以是manager_login的account,可以是manager的mobile
     * @return
     */
    @Override
    public List<ShopManagerLogin> getLoginInfo(String userName) {
        List<ShopManagerLogin> shopManagerLoginList = Lists.newArrayList();
        if(StringUtils.isBlank(userName)){
            return shopManagerLoginList;
        }
        //查询manager_login
        Map<String,Object> searchManagerLoginMap = Maps.newHashMap();
        searchManagerLoginMap.put("account",userName);
        shopManagerLoginList = shopManagerLoginDao.select(searchManagerLoginMap);
        //设置shopManagerLogin
        List<Long> shopManagerIds = Lists.newArrayList();
        for(ShopManagerLogin shopManagerLogin : shopManagerLoginList){
            shopManagerIds.add(shopManagerLogin.getManagerId());
        }
        if(!CollectionUtils.isEmpty(shopManagerIds)){
            //查询manager,设置isAdmin
            Map<String,Object> searchManagerMap = Maps.newHashMap();
            searchManagerMap.put("ids",shopManagerIds);
            List<ShopManager> shopManagerList = shopManagerDao.select(searchManagerMap);
            Map<Long,ShopManager> shopManagerMap = Maps.newHashMap();
            for(ShopManager shopManager : shopManagerList){
                shopManagerMap.put(shopManager.getId(),shopManager);
            }
            for(ShopManagerLogin shopManagerLogin : shopManagerLoginList){
                Long managerId = shopManagerLogin.getManagerId();
                if(shopManagerMap.containsKey(managerId)){
                    ShopManager shopManager = shopManagerMap.get(managerId);
                    shopManagerLogin.setIsAdmin(shopManager.getIsAdmin());
                }
            }
        }
        //查询manager
        Map<String,Object> searchManagerMap = Maps.newHashMap();
        searchManagerMap.put("mobile",userName);
        List<ShopManager> shopManagerList = shopManagerDao.select(searchManagerMap);
        if(CollectionUtils.isEmpty(shopManagerList)){
           return shopManagerLoginList;
        }
        Map<Long,Object> existMap = Maps.newHashMap();
        for(ShopManagerLogin shopManagerLogin : shopManagerLoginList){
            existMap.put(shopManagerLogin.getManagerId(),null);
        }
        List<Long> managerIds = Lists.newArrayList();
        Map<Long,ShopManager> shopManagerMap = Maps.newHashMap();
        for(ShopManager shopManager : shopManagerList){
            Long id = shopManager.getId();
            if(!existMap.containsKey(id)){
                managerIds.add(id);
                shopManagerMap.put(id,shopManager);
            }
        }
        if(!CollectionUtils.isEmpty(managerIds)){
            Map<String,Object> searchOtherManagerLoginMap = Maps.newHashMap();
            searchOtherManagerLoginMap.put("managerIds",managerIds);
            List<ShopManagerLogin> otherShopManagerLoginList = shopManagerLoginDao.select(searchOtherManagerLoginMap);
            //设置isAdmin
            for(ShopManagerLogin shopManagerLogin : otherShopManagerLoginList){
                Long managerId = shopManagerLogin.getManagerId();
                if(shopManagerMap.containsKey(managerId)){
                    ShopManager shopManager = shopManagerMap.get(managerId);
                    shopManagerLogin.setIsAdmin(shopManager.getIsAdmin());
                }
            }
            shopManagerLoginList.addAll(otherShopManagerLoginList);
        }
        //设置信息
        return shopManagerLoginList;
    }

    @Override
    public List<ShopManagerLogin> getLoginInfo(String userName, String password) {
        List<ShopManagerLogin> shopManagerLoginList = getLoginInfo(userName);
        List<ShopManagerLogin> returnList = Lists.newArrayList();
        for(ShopManagerLogin shopManagerLogin : shopManagerLoginList){
            String checkPassword = shopManagerLogin.getPassword();
            if(checkPassword.equalsIgnoreCase(password)){
                returnList.add(shopManagerLogin);
            }
        }
        return returnList;
    }

    @Override
    public ShopManagerLogin getLoginByAccount(String account) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("account",account);
        List<ShopManagerLogin> shopManagerLoginList = shopManagerLoginDao.select(searchMap);
        if(CollectionUtils.isEmpty(shopManagerLoginList)){
            return null;
        }
        return shopManagerLoginList.get(0);
    }

    @Override
    public ShopManager getUserInfo(long managerId) {

        if(!StringUtils.isNumeric(managerId+""))
        {
            logger.error("用户ID空！");
            return null;
        }
        ShopManager userInfo = null;
        try {
            userInfo = shopManagerDao.selectById(managerId);
        }catch (Exception e) {
            logger.error("DB error", e);
        }
        if (null == userInfo) {
            logger.error("无此用户信息！用户ID为：" + managerId);
        }
        return userInfo;
    }

    /**
     * 更新用户信息
     *
     * @param shopManager
     * @return
     */
    @Override
    public Result updateUserInfo(ShopManager shopManager) {
        if(StringUtils.isNumeric(shopManager.getId()+""))
        {
            if(shopManagerDao.updateById(shopManager) > Constants.DB_DEFAULT_INT)
            {
                this.cacheComponent.reload(CacheKeyConstant.PICKING);
                return Result.wrapSuccessfulResult(true);
            }
        }
        return Result.wrapErrorResult("", "更新用户信息失败");
    }

    /**
     * 获取门店管理员的基本信息
     *
     * @param shopId
     * @return
     */
    @Override
    public ShopManager getShopAdminUserInfo(Long shopId) {

        List<ShopManager> list = null;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("shopId", shopId);
        parameters.put("isAdmin", 1);
        try {
            list = shopManagerDao.select(parameters);
        }catch (Exception e) {
            logger.error("DB error", e);
        }
        if(!CollectionUtils.isEmpty(list))
        {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据ID获取用户信息
     *
     * @param resultMap
     * @return
     */
    @Override
    public List<ShopManager> getUserInfo(Map<String, Object> resultMap) {

        List<ShopManager> list = null;
        try {
            list = shopManagerDao.select(resultMap);
        }catch (Exception e) {
            logger.error("DB error", e);
        }
        if (null == list) {
            logger.error("无此用户信息！用户ID为：" + list);
        }
        return list;
    }

    /**
     * 添加B门店账户
     *
     * @param com
     * @return
     */
    @Transactional
    @Override
    public Result addShopUser(ShopManagerCom com, Map<String, Object> resultMap) {
        if(StringUtils.isBlank(com.getNameReg()) || StringUtils.isBlank(com.getPasswordReg()) || StringUtils.isBlank(com.getMobileReg()))
        {
            return Result.wrapErrorResult("", "参数错误");
        }
        String mobile = com.getMobileReg();
        String name = "管理员";
        // 添加门店信息
        long shopId = insertShop(com, resultMap);

        if (Constants.FAIL_FLAG >= shopId)
        {
            logger.error("添加门店信息错误"+com.getMobileReg());
            return Result.wrapSuccessfulResult(false);
        }

        // 添加角色信息
        long roleId = insertRole(shopId);

        if (Constants.FAIL_FLAG >= roleId)
        {
            logger.error("添加角色信息错误"+com.getMobileReg());
            return Result.wrapSuccessfulResult(false);
        }

        // 添加账户基本信息
        long managerId = insertShopManger(shopId, mobile, name, roleId);

        if (Constants.FAIL_FLAG >= managerId)
        {
            logger.error("添加账户基本信息失败"+com.getMobileReg());
            return Result.wrapSuccessfulResult(false);
        }

        // 添加账户
        long loginId = insertShopLogin(managerId, com.getNameReg(), com.getPasswordReg(), shopId);

        if (Constants.FAIL_FLAG >= loginId)
        {
            logger.error("添加账户失败" + com.getMobileReg());
            return Result.wrapSuccessfulResult(false);
        }

        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 根据ID获取用户信息
     *
     * @param mobile
     * @return
     */
    @Override
    public ShopManagerLogin getUserInfoByMobile(String mobile) {

        ShopManagerLogin login = null;
        try
        {
            login = shopManagerLoginDao.selectByMobile(mobile);

        }catch (Exception e)
        {
            logger.error("DB error", e);
        }
        if (null == login)
        {
            logger.error("无此用户信息！手机为：" + mobile);
        }
        return login;

    }

    /**
     * 添加商户
     * @param name
     * @param passWord
     * @param managerId
     * @return
     */
    private long insertShopLogin(long managerId, String name, String passWord,long shopId)
    {
        ShopManagerLogin login = new ShopManagerLogin();
        login.setAccount(name);
        login.setManagerId(managerId);
        login.setPassword(MD5Util.MD5(passWord));
        login.setShopId(shopId);
        shopManagerLoginDao.insert(login);
        return login.getId();
    }

    /**
     * 添加角色
     * @param shopId
     * @return
     */
    private long insertRole(long shopId)
    {
        Roles roles = new Roles();
        roles.setName("管理员");
        roles.setParentId((long) Constants.DB_DEFAULT_INT);
        roles.setShopId(shopId);
        roles.setLevelId(Constants.DB_DEFAULT_INT);
        rolesDao.insert(roles);
        return roles.getId();
    }

    /**
     * 添加商户
     * @param com
     * @param resultMap
     * @return
     */
    private long insertShop(ShopManagerCom com, Map<String, Object> resultMap)
    {
        Shop shop = new Shop();
        Map<String, Object> map = (Map<String, Object>) resultMap.get("data");
        if (!CollectionUtils.isEmpty(map))
        {
            shop.setOpenTime(DateUtil.convertStringToDate(map.get("businessTimeBegin") == null ? "" : (String) map.get("businessTimeBegin")));
            shop.setCloseTime(DateUtil.convertStringToDate(map.get("businessTimeEnd") == null ? "" : (String) map.get("businessTimeEnd")));
            shop.setProvinceName(map.get("provinceName") == null ? "" : (String) map.get("provinceName"));
            shop.setCityName(map.get("cityName") == null ? "" : (String) map.get("cityName"));
            shop.setDistrictName(map.get("districtName") == null ? "" : (String) map.get("districtName"));
            shop.setStreetName(map.get("streetName") == null ? "" : (String) map.get("streetName"));
            shop.setProvince(map.get("province") == null ? 0 : (Long.valueOf((Integer)map.get("province"))));
            shop.setCity(map.get("city") == null ? 0 : (Long.valueOf((Integer)map.get("city"))));
            shop.setDistrict(map.get("district") == null ? 0 : (Long.valueOf((Integer)map.get("district"))));
            shop.setStreet(map.get("street") == null ? 0 : (Long.valueOf((Integer)map.get("street"))));
            shop.setAddress((String)map.get("address"));
            shop.setCrmCustomerId(map.get("customerId") == null ? 0 : (Long.valueOf((Integer)map.get("customerId"))));
            shop.setName((String)map.get("companyName"));
            shop.setContact((String)map.get("contactsName"));
        }
        shop.setAbbr(com.getNameReg());
        shop.setMobile(com.getMobileReg());
        shopDao.insert(shop);
        return shop.getId();
    }

    /**
     * 添加账户基本信息
     * @param id
     * @param mobile
     * @param contact
     * @return
     */
    private long insertShopManger(long id, String mobile, String contact, long roleId)
    {
        ShopManager shopManager = new ShopManager();
        shopManager.setShopId(id);
        shopManager.setMobile(mobile);
        shopManager.setName(contact);
        shopManager.setCreator(0l);
        shopManager.setModifier(0l);
        shopManager.setStatus(1);
        shopManager.setRoleId(roleId);
        shopManager.setIsAdmin(1);
        shopManagerDao.insert(shopManager);
        this.cacheComponent.reload(CacheKeyConstant.PICKING);
        return shopManager.getId();
    }
}
